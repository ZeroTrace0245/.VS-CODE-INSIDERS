package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.SettingsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val aiEnabled by settingsRepository.aiEnabled.collectAsState(initial = true)
    val alwaysShowNav by settingsRepository.alwaysShowNav.collectAsState(initial = true)
    val cardColorLong by settingsRepository.cardColor.collectAsState(initial = null)
    val githubToken by settingsRepository.githubToken.collectAsState(initial = null)
    
    var showTokenDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFF1E3C72), // Default Blue
        Color(0xFF1B5E20), // Green
        Color(0xFFB71C1C), // Red
        Color(0xFF4A148C), // Purple
        Color(0xFFE65100), // Orange
        Color(0xFF263238), // Grey/Dark
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) }
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // AI Settings
            SettingsSection(title = "AI Features", icon = Icons.Default.AutoAwesome) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Enable MediPlus AI", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Dashboard tips and healthy living chat",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = aiEnabled,
                        onCheckedChange = { scope.launch { settingsRepository.setAiEnabled(it) } }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { showTokenDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (githubToken.isNullOrBlank()) "Set GitHub Token" else "Update GitHub Token")
                }
            }

            // Customization Settings
            SettingsSection(title = "Customization", icon = Icons.Default.ColorLens) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Always Show Navigation Bar", fontWeight = FontWeight.SemiBold)
                        Text(
                            "If disabled, swipe up from bottom to show",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = alwaysShowNav,
                        onCheckedChange = { scope.launch { settingsRepository.setAlwaysShowNav(it) } }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Medical ID Card Color", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(colors) { color ->
                        val isSelected = cardColorLong == color.toArgb().toLong() || (cardColorLong == null && color == colors[0])
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch { settingsRepository.saveCardColor(color.toArgb().toLong()) }
                                }
                        )
                    }
                }
            }

            // Privacy Settings
            SettingsSection(title = "Privacy", icon = Icons.Default.Shield) {
                Text(
                    "Permissions",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "You can manage camera and NFC permissions in your system settings. MediPlus only uses these when you explicitly trigger a scan or export.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }

        if (showTokenDialog) {
            TokenDialog(
                onDismiss = { showTokenDialog = false },
                onSave = { newToken ->
                    scope.launch {
                        settingsRepository.saveGithubToken(newToken)
                        showTokenDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}
