package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "health_records")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileName: String = "My Profile",
    val personalInfo: PersonalInfo = PersonalInfo(),
    val bloodType: String = "",
    val allergies: List<String> = emptyList(),
    val medications: List<Medication> = emptyList(),
    val medicalConditions: List<String> = emptyList(),
    val medicalHistory: List<String> = emptyList(),
    val emergencyContact: EmergencyContact? = null,
    val insuranceInfo: InsuranceInfo? = null,
    val doctorContacts: List<DoctorContact> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
)

@Serializable
data class PersonalInfo(
    val name: String = "",
    val age: Int = 0,
    val dateOfBirth: String = ""
)

@Serializable
data class Medication(
    val name: String,
    val dosage: String,
    val instructions: String = "",
    val frequency: String = "",
    val prescribedBy: String = ""
)

@Serializable
data class EmergencyContact(
    val name: String,
    val relationship: String,
    val phone: String,
    val email: String? = null
)

@Serializable
data class InsuranceInfo(
    val provider: String = "",
    val policyNumber: String = "",
    val groupNumber: String = "",
    val memberId: String = ""
)

@Serializable
data class DoctorContact(
    val name: String,
    val specialization: String,
    val phone: String,
    val address: String = ""
)
