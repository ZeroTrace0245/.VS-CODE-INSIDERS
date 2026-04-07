package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val doctorName: String,
    val location: String = "",
    val dateTime: Long, // Timestamp
    val notes: String = "",
    val isCompleted: Boolean = false
)
