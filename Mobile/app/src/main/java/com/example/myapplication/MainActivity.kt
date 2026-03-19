package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.FirebaseSyncRepository
import com.example.myapplication.data.HealthRecord
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.navigation.BottomNavBar
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize resources before setContent to avoid potential initialization hangs in composition
        val database = AppDatabase.getInstance(applicationContext)
        val firebaseSync = FirebaseSyncRepository()
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HealthWellnessApp(database, firebaseSync)
                }
            }
        }
    }
}

@Composable
fun HealthWellnessApp(database: AppDatabase, firebaseSync: FirebaseSyncRepository) {
    var currentScreen by remember { mutableIntStateOf(0) }
    var editingRecord by remember { mutableStateOf<HealthRecord?>(null) }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background, // Explicitly set background
        bottomBar = {
            if (editingRecord == null) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onScreenChange = { 
                        currentScreen = it
                        editingRecord = null
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
                        editingRecord = null
                    }
                },
                onBack = { editingRecord = null }
            )
        } else {
            when (currentScreen) {
                0 -> HomeScreen(modifier = Modifier.padding(innerPadding))
                1 -> HealthRecordsScreen(
                    modifier = Modifier.padding(innerPadding),
                    onEditProfile = { editingRecord = it },
                    onCreateProfile = { editingRecord = HealthRecord() }
                )
                2 -> MeditationScreen(modifier = Modifier.padding(innerPadding))
                3 -> JournalScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
