package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val title: String = "",
    val content: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val tags: List<String> = emptyList(),
    val prompt: String? = null // Short reflective question
)

@Serializable
enum class Mood {
    HAPPY, GOOD, NEUTRAL, SAD, VERY_SAD
}

@Serializable
data class GuidedSession(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int, // in minutes
    val category: SessionCategory,
    val audioUrl: String? = null,
    val animationUrl: String? = null,
    val difficulty: Difficulty = Difficulty.BEGINNER
)

@Serializable
enum class SessionCategory {
    MEDITATION, BREATHING, RELAXATION, MOTIVATION, SLEEP
}

@Serializable
enum class Difficulty {
    BEGINNER, INTERMEDIATE, ADVANCED
}

@Serializable
@Entity(tableName = "wellness_metrics")
data class WellnessMetric(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val stressLevel: Int = 5, // 1-10
    val sleepHours: Double = 0.0,
    val exerciseMinutes: Int = 0,
    val waterIntakeL: Double = 0.0,
    val notes: String = ""
)

@Serializable
data class CalmingVisual(
    val id: Int,
    val name: String,
    val description: String,
    val visualUrl: String, // can be a local drawable ID or a URL
    val type: VisualType = VisualType.IMAGE,
    val category: String = "nature"
)

@Serializable
enum class VisualType {
    IMAGE, ANIMATION, GRADIENT
}

@Serializable
data class MotivationalQuote(
    val text: String,
    val author: String? = null
)
