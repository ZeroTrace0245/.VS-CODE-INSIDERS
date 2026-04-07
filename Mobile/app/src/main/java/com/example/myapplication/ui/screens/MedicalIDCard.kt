package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.HealthRecord

@Composable
fun MedicalIDCard(
    record: HealthRecord,
    backgroundColor: Color = Color(0xFF1E3C72)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(backgroundColor, backgroundColor.copy(alpha = 0.8f))))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("MediPlus ID", color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                    Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Color(0xFF40C4FF))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = record.personalInfo.name.uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Text("BLOOD TYPE", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text(record.bloodType, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("MOBILE", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text(record.personalInfo.mobileNumber, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                    Box(
                        modifier = Modifier
                            .size(36.dp, 26.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFD700).copy(alpha = 0.8f))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "VALID THROUGH: 12/28",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
