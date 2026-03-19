package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthProfileEditScreen(
    initialRecord: HealthRecord = HealthRecord(),
    onSave: (HealthRecord) -> Unit,
    onBack: () -> Unit
) {
    var profileName by remember { mutableStateOf(initialRecord.profileName) }
    
    // Personal Info
    var name by remember { mutableStateOf(initialRecord.personalInfo.name) }
    var age by remember { mutableStateOf(initialRecord.personalInfo.age.toString()) }
    var dateOfBirth by remember { mutableStateOf(initialRecord.personalInfo.dateOfBirth) }
    var bloodType by remember { mutableStateOf(initialRecord.bloodType) }
    
    // List based info (comma separated for editing simplicity)
    var allergies by remember { mutableStateOf(initialRecord.allergies.joinToString(", ")) }
    var medicalConditions by remember { mutableStateOf(initialRecord.medicalConditions.joinToString(", ")) }
    var medicalHistory by remember { mutableStateOf(initialRecord.medicalHistory.joinToString(", ")) }
    
    // Emergency Contact
    var ecName by remember { mutableStateOf(initialRecord.emergencyContact?.name ?: "") }
    var ecRelationship by remember { mutableStateOf(initialRecord.emergencyContact?.relationship ?: "") }
    var ecPhone by remember { mutableStateOf(initialRecord.emergencyContact?.phone ?: "") }
    
    // Insurance
    var insProvider by remember { mutableStateOf(initialRecord.insuranceInfo?.provider ?: "") }
    var insPolicy by remember { mutableStateOf(initialRecord.insuranceInfo?.policyNumber ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialRecord.id == 0) "Create Profile" else "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val updatedRecord = initialRecord.copy(
                            profileName = profileName,
                            personalInfo = PersonalInfo(
                                name = name, 
                                age = age.toIntOrNull() ?: 0,
                                dateOfBirth = dateOfBirth
                            ),
                            bloodType = bloodType,
                            allergies = allergies.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            medicalConditions = medicalConditions.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            medicalHistory = medicalHistory.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            emergencyContact = if (ecName.isNotEmpty()) EmergencyContact(ecName, ecRelationship, ecPhone) else null,
                            insuranceInfo = if (insProvider.isNotEmpty()) InsuranceInfo(insProvider, insPolicy) else null,
                            lastUpdated = System.currentTimeMillis()
                        )
                        onSave(updatedRecord)
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileSectionHeader("Profile Settings")
            OutlinedTextField(
                value = profileName,
                onValueChange = { profileName = it },
                label = { Text("Profile Name (e.g., My Health)") },
                modifier = Modifier.fillMaxWidth()
            )

            ProfileSectionHeader("Personal Information")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = bloodType,
                    onValueChange = { bloodType = it },
                    label = { Text("Blood Type") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth()
            )

            ProfileSectionHeader("Medical Lists (comma separated)")
            OutlinedTextField(
                value = allergies,
                onValueChange = { allergies = it },
                label = { Text("Allergies") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = medicalConditions,
                onValueChange = { medicalConditions = it },
                label = { Text("Medical Conditions") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                label = { Text("Medical History") },
                modifier = Modifier.fillMaxWidth()
            )

            ProfileSectionHeader("Emergency Contact")
            OutlinedTextField(
                value = ecName,
                onValueChange = { ecName = it },
                label = { Text("Contact Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ecRelationship,
                onValueChange = { ecRelationship = it },
                label = { Text("Relationship") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ecPhone,
                onValueChange = { ecPhone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            ProfileSectionHeader("Insurance Info")
            OutlinedTextField(
                value = insProvider,
                onValueChange = { insProvider = it },
                label = { Text("Insurance Provider") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = insPolicy,
                onValueChange = { insPolicy = it },
                label = { Text("Policy Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}
