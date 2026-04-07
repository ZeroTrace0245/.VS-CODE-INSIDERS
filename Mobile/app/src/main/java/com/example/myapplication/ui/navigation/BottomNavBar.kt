package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, 0),
        BottomNavItem("Calendar", Icons.Filled.CalendarMonth, 1),
        BottomNavItem("Health", Icons.Filled.MedicalServices, 2),
        BottomNavItem("Meditate", Icons.Filled.FavoriteBorder, 3),
        BottomNavItem("Journal", Icons.AutoMirrored.Filled.Notes, 4),
        BottomNavItem("Settings", Icons.Filled.Settings, 5)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 9.sp, maxLines = 1) },
                selected = currentScreen == item.index,
                onClick = { onScreenChange(item.index) },
                alwaysShowLabel = true
            )
        }
    }
}
