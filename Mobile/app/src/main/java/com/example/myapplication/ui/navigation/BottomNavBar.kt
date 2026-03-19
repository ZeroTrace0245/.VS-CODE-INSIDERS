package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val index: Int
)

@Composable
fun BottomNavBar(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, 0),
        BottomNavItem("Health", Icons.Filled.MedicalServices, 1),
        BottomNavItem("Meditate", Icons.Filled.FavoriteBorder, 2),
        BottomNavItem("Journal", Icons.Filled.Notes, 3)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentScreen == item.index,
                onClick = { onScreenChange(item.index) }
            )
        }
    }
}
