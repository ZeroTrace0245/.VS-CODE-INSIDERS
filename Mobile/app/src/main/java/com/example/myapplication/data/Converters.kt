package com.example.myapplication.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class Converters {
    @TypeConverter
    fun fromMedicationList(medications: List<Medication>): String {
        return Json.encodeToString(medications)
    }

    @TypeConverter
    fun toMedicationList(data: String): List<Medication> {
        return if (data.isEmpty()) emptyList() else Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromEmergencyContact(contact: EmergencyContact?): String {
        return if (contact == null) "" else Json.encodeToString(contact)
    }

    @TypeConverter
    fun toEmergencyContact(data: String): EmergencyContact? {
        return if (data.isEmpty()) null else Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromInsuranceInfo(info: InsuranceInfo?): String {
        return if (info == null) "" else Json.encodeToString(info)
    }

    @TypeConverter
    fun toInsuranceInfo(data: String): InsuranceInfo? {
        return if (data.isEmpty()) null else Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromMood(mood: Mood): String {
        return mood.name
    }

    @TypeConverter
    fun toMood(data: String): Mood {
        return Mood.valueOf(data)
    }

    @TypeConverter
    fun fromPersonalInfo(info: PersonalInfo): String {
        return Json.encodeToString(info)
    }

    @TypeConverter
    fun toPersonalInfo(data: String): PersonalInfo {
        return if (data.isEmpty()) PersonalInfo() else Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromDoctorContactList(contacts: List<DoctorContact>): String {
        return Json.encodeToString(contacts)
    }

    @TypeConverter
    fun toDoctorContactList(data: String): List<DoctorContact> {
        return if (data.isEmpty()) emptyList() else Json.decodeFromString(data)
    }
}
