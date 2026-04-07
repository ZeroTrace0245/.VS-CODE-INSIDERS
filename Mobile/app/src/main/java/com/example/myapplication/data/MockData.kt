package com.example.myapplication.data

object MockData {
    fun getMockProfile(): HealthRecord {
        return HealthRecord(
            id = 1,
            profileName = "Demo Profile",
            personalInfo = PersonalInfo(
                name = "John Doe",
                age = 28,
                dateOfBirth = "1995-05-15",
                mobileNumber = "+1 (555) 012-3456"
            ),
            bloodType = "O+",
            allergies = listOf("Peanuts", "Penicillin"),
            medicalConditions = listOf("Type 1 Diabetes", "Asthma"),
            emergencyContact = EmergencyContact(
                name = "Jane Doe",
                relationship = "Spouse",
                phone = "+1 (555) 987-6543"
            ),
            insuranceInfo = InsuranceInfo(
                provider = "MediShield Global",
                policyNumber = "MS-882-9910",
                memberId = "ID-4421"
            )
        )
    }

    fun getMockAppointments(): List<Appointment> {
        val now = System.currentTimeMillis()
        val oneHour = 3600000L
        val oneDay = 86400000L
        
        return listOf(
            Appointment(
                id = 1,
                title = "Cardiology Checkup",
                doctorName = "Dr. Smith",
                location = "MediPlus Center, Floor 3",
                dateTime = now + (oneDay * 2) + (oneHour * 2), // 2 days from now
                notes = "Bring previous test results"
            ),
            Appointment(
                id = 2,
                title = "Routine Eye Exam",
                doctorName = "Dr. Lee",
                location = "Vision Clinic, Suite 204",
                dateTime = now + (oneDay * 5), // 5 days from now
                notes = "Fast for 4 hours before"
            )
        )
    }
}
