package com.example.myapplication

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.*
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.navigation.BottomNavBar
import com.example.myapplication.util.NfcHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingNdefMessage = mutableStateOf<NdefMessage?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        
        val database = AppDatabase.getInstance(applicationContext)
        val firebaseSync = FirebaseSyncRepository()
        val settingsRepository = SettingsRepository(applicationContext)
        val aiService = AiService { 
            settingsRepository.githubToken.first()?.takeIf { it.isNotBlank() } 
                ?: settingsRepository.getFallbackToken() 
        }
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bypassLogin = false // Set to true to skip all screens and use mock data
                    
                    var currentAppStep by remember { 
                        mutableStateOf(if (bypassLogin) "main" else "login") 
                    }
                    var userName by remember { 
                        mutableStateOf<String?>(if (bypassLogin) "John Doe" else null) 
                    }
                    var userProfile by remember { 
                        mutableStateOf<HealthRecord?>(if (bypassLogin) MockData.getMockProfile() else null) 
                    }
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(bypassLogin) {
                        if (bypassLogin) {
                            val mockApps = MockData.getMockAppointments()
                            mockApps.forEach { 
                                database.appointmentDao().insertAppointment(it)
                            }
                        }
                    }

                    Crossfade(
                        targetState = currentAppStep,
                        animationSpec = tween(durationMillis = 800),
                        label = "main_navigation"
                    ) { step ->
                        when (step) {
                            "login" -> {
                                LoginScreen(
                                    onLoginComplete = { name ->
                                        userName = name
                                        currentAppStep = "registration"
                                    },
                                    onDebugLogin = {
                                        val mock = MockData.getMockProfile()
                                        userName = mock.personalInfo.name
                                        userProfile = mock
                                        currentAppStep = "main"
                                    }
                                )
                            }
                            "registration" -> {
                                RegistrationScreen(
                                    onRegistrationComplete = { record ->
                                        userProfile = record
                                        scope.launch {
                                            database.healthRecordDao().insertHealthRecord(record)
                                        }
                                        currentAppStep = "main"
                                    },
                                    onViewPolicies = { currentAppStep = "policies" }
                                )
                            }
                            "policies" -> {
                                PoliciesScreen(onBack = { currentAppStep = "registration" })
                            }
                            "main" -> {
                                var showCameraScan by remember { mutableStateOf(false) }
                                
                                if (showCameraScan) {
                                    CameraScanScreen(
                                        aiService = aiService,
                                        onBack = { showCameraScan = false }
                                    )
                                } else {
                                    HealthWellnessApp(
                                        database = database, 
                                        firebaseSync = firebaseSync, 
                                        userName = userName ?: "User", 
                                        userProfile = userProfile,
                                        aiService = aiService,
                                        settingsRepository = settingsRepository,
                                        onWriteNfc = { record ->
                                            if (nfcAdapter == null) {
                                                Toast.makeText(this@MainActivity, "NFC not supported", Toast.LENGTH_SHORT).show()
                                            } else if (!nfcAdapter!!.isEnabled) {
                                                Toast.makeText(this@MainActivity, "Please enable NFC", Toast.LENGTH_SHORT).show()
                                            } else {
                                                pendingNdefMessage.value = NfcHandler.createNdefMessage(record)
                                                Toast.makeText(this@MainActivity, "Hold your phone near an NFC tag", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        onLaunchCamera = { showCameraScan = true },
                                        onProfileUpdated = { userProfile = it }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    @Suppress("DEPRECATION")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val message = pendingNdefMessage.value
            
            if (tag != null && message != null) {
                NfcHandler.writeTag(tag, message).fold(
                    onSuccess = {
                        Toast.makeText(this, "Successfully written to NFC tag!", Toast.LENGTH_SHORT).show()
                        pendingNdefMessage.value = null
                    },
                    onFailure = {
                        Toast.makeText(this, "Failed to write: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun HealthWellnessApp(
    database: AppDatabase, 
    firebaseSync: FirebaseSyncRepository, 
    userName: String,
    userProfile: HealthRecord?,
    aiService: AiService,
    settingsRepository: SettingsRepository,
    onWriteNfc: (HealthRecord) -> Unit,
    onLaunchCamera: () -> Unit,
    onProfileUpdated: (HealthRecord) -> Unit
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    var editingRecord by remember { mutableStateOf<HealthRecord?>(null) }
    var showChatOverlay by remember { mutableStateOf(false) }
    var showEmergencyOverlay by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    val appointments by database.appointmentDao().getUpcomingAppointments(System.currentTimeMillis()).collectAsState(initial = emptyList())
    val alwaysShowNav by settingsRepository.alwaysShowNav.collectAsState(initial = true)
    var isNavVisibleManually by remember { mutableStateOf(false) }
    
    val isNavVisible = alwaysShowNav || isNavVisibleManually

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(alwaysShowNav) {
                if (!alwaysShowNav) {
                    awaitEachGesture {
                        val down = awaitFirstDown(pass = PointerEventPass.Initial)
                        val isNearBottom = down.position.y > size.height * 0.8f
                        var dragAmount = 0f
                        do {
                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                            event.changes.forEach { change ->
                                if (change.id == down.id) {
                                    val delta = change.position.y - change.previousPosition.y
                                    dragAmount += delta

                                    if (dragAmount < -40f && isNearBottom) {
                                        isNavVisibleManually = true
                                    } else if (dragAmount > 40f && isNavVisibleManually) {
                                        isNavVisibleManually = false
                                    }
                                }
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                AnimatedVisibility(
                    visible = isNavVisible && editingRecord == null && !showChatOverlay && !showEmergencyOverlay,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    BottomNavBar(
                        currentScreen = currentScreen,
                        onScreenChange = { 
                            currentScreen = it
                            editingRecord = null
                            if (!alwaysShowNav) isNavVisibleManually = false
                        }
                    )
                }
            }
        ) { innerPadding ->
            if (editingRecord != null) {
                HealthProfileEditScreen(
                    initialRecord = editingRecord!!,
                    onSave = { record ->
                        scope.launch {
                            if (record.id == 0) {
                                database.healthRecordDao().insertHealthRecord(record)
                            } else {
                                database.healthRecordDao().updateHealthRecord(record)
                            }
                            firebaseSync.syncHealthRecord(record)
                            onProfileUpdated(record)
                            editingRecord = null
                        }
                    },
                    onBack = { editingRecord = null }
                )
            } else if (showChatOverlay) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ChatScreen(
                        aiService = aiService,
                        settingsRepository = settingsRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                    IconButton(
                        onClick = { showChatOverlay = false },
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close Chat")
                    }
                }
            } else if (showEmergencyOverlay && userProfile != null) {
                EmergencyDetailsScreen(
                    record = userProfile,
                    onEdit = { 
                        editingRecord = userProfile
                        showEmergencyOverlay = false 
                    },
                    onBack = { showEmergencyOverlay = false }
                )
            } else {
                when (currentScreen) {
                    0 -> HomeScreen(
                        modifier = Modifier.padding(innerPadding), 
                        userName = userName,
                        userProfile = userProfile,
                        aiService = aiService,
                        onExportToNfc = onWriteNfc,
                        onLaunchCamera = onLaunchCamera,
                        onOpenAiChat = { showChatOverlay = true },
                        onOpenEmergency = { showEmergencyOverlay = true },
                        upcomingAppointments = appointments,
                        settingsRepository = settingsRepository
                    )
                    1 -> CalendarScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                    2 -> HealthRecordsScreen(
                        modifier = Modifier.padding(innerPadding),
                        onEditProfile = { editingRecord = it },
                        onCreateProfile = { editingRecord = HealthRecord() }
                    )
                    3 -> MeditationScreen(modifier = Modifier.padding(innerPadding))
                    4 -> JournalScreen(modifier = Modifier.padding(innerPadding))
                    5 -> SettingsScreen(
                        settingsRepository = settingsRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
