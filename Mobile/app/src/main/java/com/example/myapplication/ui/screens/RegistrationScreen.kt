package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.EmergencyContact
import com.example.myapplication.data.HealthRecord
import com.example.myapplication.data.PersonalInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegistrationComplete: (HealthRecord) -> Unit,
    onViewPolicies: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    
    // Form States
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var medicalCondition by remember { mutableStateOf("") }
    var emergencyName by remember { mutableStateOf("") }
    var emergencyPhone by remember { mutableStateOf("") }
    var emergencyName2 by remember { mutableStateOf("") }
    var emergencyPhone2 by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }
    
    // Error States
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var mobileError by remember { mutableStateOf<String?>(null) }
    var bloodError by remember { mutableStateOf<String?>(null) }
    var conditionError by remember { mutableStateOf<String?>(null) }
    var emergencyError by remember { mutableStateOf<String?>(null) }

    var showProfileCard by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (showProfileCard) {
            val record = HealthRecord(
                profileName = "Primary Profile",
                personalInfo = PersonalInfo(name = username, mobileNumber = mobileNumber),
                bloodType = bloodType,
                medicalConditions = listOf(medicalCondition),
                emergencyContact = EmergencyContact(name = emergencyName, phone = emergencyPhone, relationship = "Primary")
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Registration Successful!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    MedicalIDCard(record)
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = { onRegistrationComplete(record) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Continue to App")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Create Profile",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Step $step of 3",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                when (step) {
                    1 -> {
                        RegistrationStep1(
                            username = username,
                            onUsernameChange = { username = it; usernameError = null },
                            usernameError = usernameError,
                            password = password,
                            onPasswordChange = { password = it; passwordError = null },
                            passwordError = passwordError,
                            agreedToTerms = agreedToTerms,
                            onAgreedChange = { agreedToTerms = it },
                            onViewPolicies = onViewPolicies
                        )
                    }
                    2 -> {
                        RegistrationStep2(
                            mobile = mobileNumber,
                            onMobileChange = { mobileNumber = it; mobileError = null },
                            mobileError = mobileError,
                            bloodType = bloodType,
                            onBloodChange = { bloodType = it; bloodError = null },
                            bloodError = bloodError,
                            condition = medicalCondition,
                            onConditionChange = { medicalCondition = it; conditionError = null },
                            conditionError = conditionError
                        )
                    }
                    3 -> {
                        RegistrationStep3(
                            eName = emergencyName,
                            onENameChange = { emergencyName = it; emergencyError = null },
                            ePhone = emergencyPhone,
                            onEPhoneChange = { emergencyPhone = it; emergencyError = null },
                            eName2 = emergencyName2,
                            onENameChange2 = { emergencyName2 = it },
                            ePhone2 = emergencyPhone2,
                            onEPhoneChange2 = { emergencyPhone2 = it },
                            emergencyError = emergencyError
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step-- },
                            modifier = Modifier.weight(1f).height(56.dp).padding(end = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Back")
                        }
                    }
                    
                    Button(
                        onClick = {
                            if (validateStep(step, username, password, mobileNumber, bloodType, medicalCondition, emergencyName, emergencyPhone, agreedToTerms, 
                                { usernameError = it }, { passwordError = it }, { mobileError = it }, { bloodError = it }, { conditionError = it }, { emergencyError = it })) {
                                if (step < 3) step++ else showProfileCard = true
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp).padding(start = if (step > 1) 8.dp else 0.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (step == 3) "Finish" else "Next")
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationStep1(
    username: String, onUsernameChange: (String) -> Unit, usernameError: String?,
    password: String, onPasswordChange: (String) -> Unit, passwordError: String?,
    agreedToTerms: Boolean, onAgreedChange: (Boolean) -> Unit, onViewPolicies: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = usernameError != null,
            supportingText = { usernameError?.let { Text(it) } }
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password (min 6 chars)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it) } }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreedToTerms, onCheckedChange = onAgreedChange)
            Text("I agree to the ")
            Text(
                text = "Terms & Policies",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onViewPolicies() },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationStep2(
    mobile: String, onMobileChange: (String) -> Unit, mobileError: String?,
    bloodType: String, onBloodChange: (String) -> Unit, bloodError: String?,
    condition: String, onConditionChange: (String) -> Unit, conditionError: String?
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = mobile,
            onValueChange = onMobileChange,
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            isError = mobileError != null,
            supportingText = { mobileError?.let { Text(it) } }
        )
        
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = bloodType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Blood Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                isError = bloodError != null,
                supportingText = { bloodError?.let { Text(it) } }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onBloodChange(type)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = condition,
            onValueChange = onConditionChange,
            label = { Text("Medical Condition (e.g. Diabetes)") },
            modifier = Modifier.fillMaxWidth(),
            isError = conditionError != null,
            supportingText = { conditionError?.let { Text(it) } }
        )
    }
}

@Composable
fun RegistrationStep3(
    eName: String, onENameChange: (String) -> Unit,
    ePhone: String, onEPhoneChange: (String) -> Unit,
    eName2: String, onENameChange2: (String) -> Unit,
    ePhone2: String, onEPhoneChange2: (String) -> Unit,
    emergencyError: String?
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Primary Emergency Contact", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = eName,
            onValueChange = onENameChange,
            label = { Text("Contact Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ePhone,
            onValueChange = onEPhoneChange,
            label = { Text("Contact Phone") },
            modifier = Modifier.fillMaxWidth(),
            isError = emergencyError != null,
            supportingText = { emergencyError?.let { Text(it) } }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        Text("Secondary Contact (Optional)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        OutlinedTextField(
            value = eName2,
            onValueChange = onENameChange2,
            label = { Text("Contact Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ePhone2,
            onValueChange = onEPhoneChange2,
            label = { Text("Contact Phone") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun validateStep(
    step: Int, u: String, p: String, m: String, b: String, c: String, en: String, ep: String, tc: Boolean,
    setU: (String?) -> Unit, setP: (String?) -> Unit, setM: (String?) -> Unit, setB: (String?) -> Unit, setC: (String?) -> Unit, setE: (String?) -> Unit
): Boolean {
    when (step) {
        1 -> {
            var valid = true
            if (u.length < 3) { setU("Username too short"); valid = false }
            if (p.length < 6) { setP("Password must be at least 6 characters"); valid = false }
            if (!tc) { setU("You must agree to terms"); valid = false }
            return valid
        }
        2 -> {
            var valid = true
            if (m.length < 10) { setM("Invalid mobile number"); valid = false }
            if (b.isEmpty()) { setB("Blood type required"); valid = false }
            if (c.isEmpty()) { setC("Provide at least one condition"); valid = false }
            return valid
        }
        3 -> {
            if (en.isEmpty() || ep.isEmpty()) { setE("Primary emergency contact required"); return false }
            return true
        }
    }
    return false
}
