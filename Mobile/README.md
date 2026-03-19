# 🧘 Health & Wellbeing Mobile App

A comprehensive Android mobile application for emergency health records management and mental resilience with sleek animations and offline support.

## Features

### 1. **Emergency Health Records** 🏥
- **Offline-First**: All critical medical info stored locally for quick access without internet
- **Blood Type Display**: Quick reference for blood type (O+, A, B, AB)
- **Allergies Management**: Track severe allergies with potential reactions
- **Current Medications**: Store medication names, dosages, and frequencies
- **Medical Conditions**: Log existing health conditions
- **Emergency Contact**: Store primary emergency contact information
- **Insurance Details**: Keep insurance provider and policy information

### 2. **Mental Resilience Companion** 🧠
- **4-7-8 Breathing Exercise**: Animated breathing guide for stress relief
- **Guided Sessions**:
  - Morning Meditation (10 min)
  - Stress Relief (8 min)
  - Sleep Guide (15 min)
  - Gratitude Practice (5 min)
- **Journal with Mood Tracking**: Write entries and track your emotional state
- **Wellness Dashboard**: Track streaks and daily wellness metrics
- **Calming Visuals**: Smooth animations for meditation and relaxation

## Tech Stack

- **Platform**: Android 36+
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Database**: Room (SQLite) for offline storage
- **Architecture**: MVVM with Flow
- **Data Serialization**: Kotlinx Serialization
- **Image Loading**: Coil
- **Animations**: Jetpack Compose animations + Optional Manim visualizations

## Project Structure

```
app/src/main/java/com/example/myapplication/
├── data/                          # Data layer
│   ├── HealthRecord.kt           # Health record models
│   ├── MentalWellness.kt         # Wellness models
│   ├── Dao.kt                    # Room database DAOs
│   ├── AppDatabase.kt            # Database setup
│   └── Converters.kt             # Type converters
├── ui/
│   ├── screens/                  # Composable screens
│   │   ├── HomeScreen.kt         # Dashboard
│   │   ├── HealthRecordsScreen.kt # Health info
│   │   ├── MeditationScreen.kt   # Meditation & breathing
│   │   └── JournalScreen.kt      # Journal & mood tracking
│   ├── navigation/
│   │   └── BottomNavBar.kt       # Navigation bar
│   └── theme/
│       ├── Color.kt              # Color scheme & gradients
│       ├── Theme.kt              # Theme configuration
│       └── Type.kt               # Typography
└── MainActivity.kt               # Entry point
```

## Getting Started

### Prerequisites
- Android Studio Giraffe or later
- Gradle 8.0+
- Kotlin 2.2.10+
- Minimum SDK: Android 12 (API 36)

### Installation

1. **Clone/Open the project** in Android Studio
   ```bash
   cd Mobile
   ```

2. **Sync Gradle files**
   - Android Studio will automatically download dependencies
   - Dependencies include: Compose, Room, DataStore, Coil, Lottie

3. **Build the app**
   ```bash
   ./gradlew build
   ```

4. **Run on emulator or device**
   ```bash
   ./gradlew installDebug
   ```

## Features in Detail

### Emergency Health Records
- **Quick Access**: View critical medical info on one screen
- **Expandable Sections**: Click to reveal detailed information
- **Offline Support**: Data syncs to device immediately
- **Color-Coded Cards**: Different sections use distinct colors for easy scanning

### Meditation & Breathing
- **Animated Circle Breathing**: Visual guide for 4-7-8 breathing
- **Multiple Sessions**: 4 different guided meditation sessions
- **Progress Tracking**: Monitor session duration
- **Calm Gradient Background**: Blue-to-teal gradient for relaxation

### Journal with Mood Tracking
- **Rich Text Entries**: Write detailed thoughts and feelings
- **Mood Emojis**: Track your emotional state with 5 moods (😊 to 😢)
- **Date-Sorted**: View entries by most recent first
- **Offline Storage**: All entries saved locally in Room database
- **Edit Capability**: Update previous entries

### Dashboard Home
- **Welcome Message**: Personalized greeting
- **Quick Access Card**: Fast navigation to health records
- **Daily Wellness Tips**: Get actionable health suggestions
- **Streak Tracking**: Monitor meditation, journaling, and hydration streaks

## Animation Features

### Native Compose Animations
- **Bottom Navigation**: Smooth transitions between screens
- **Card Slidein**: Content slides in with fade effects
- **Breathing Circle**: Continuous pulse animation
- **Mood Selector**: Scale and fade when selecting moods
- **Progress Indicator**: Animated progress bar in meditation

### Optional Manim Visualizations
If you want to generate animated visualizations with Python, use the included Manim script.

## Manim Animations (Optional)

### Setup Manim
```bash
pip install manim
```

### Generate Animations
From the project root:
```bash
# Render all animations
manim -pql wellness_animations.py

# Render specific animation
manim -pql wellness_animations.py BreathingCircleAnimation

# High quality render
manim -pxh wellness_animations.py
```

### Available Animations
1. **BreathingCircleAnimation**: 4-7-8 breathing exercise visual
2. **HeartbeatAnimation**: Pulsing heart for health monitoring
3. **WaterDropAnimation**: Hydration reminder animation
4. **CalmWaves**: Wave animation for relaxation
5. **ProgressRing**: Session progress visualization

### Using Manim Videos in the App
You can integrate generated MP4 videos:
1. Render animation: `manim -pql wellness_animations.py BreathingCircleAnimation`
2. Move MP4 to `app/src/main/res/raw/`
3. Load with VideoView or ExoPlayer in Compose

## Data Models

### HealthRecord
```kotlin
data class HealthRecord(
    val bloodType: String,
    val allergies: List<String>,
    val medications: List<Medication>,
    val medicalConditions: List<String>,
    val emergencyContact: EmergencyContact?,
    val insuranceInfo: InsuranceInfo?
)
```

### JournalEntry
```kotlin
data class JournalEntry(
    val date: Long,
    val title: String,
    val content: String,
    val mood: Mood,
    val tags: List<String>
)
```

### WellnessMetric
```kotlin
data class WellnessMetric(
    val date: Long,
    val stressLevel: Int,
    val sleepHours: Double,
    val exerciseMinutes: Int,
    val waterIntakeL: Double
)
```

## UI/UX Highlights

- **Color Gradients**: Beautiful gradients for visual appeal
  - Primary: Purple to Blue
  - Calm: Light blue to teal
  - Danger: Red gradient for emergency info
- **Smooth Animations**: All transitions are animated
- **Material Design 3**: Modern card-based UI
- **Dark Mode Support**: Automatic light/dark theme switching
- **Accessibility**: Proper contrast ratios and semantic labels

## Future Enhancements

- 🔔 Notifications for medication reminders
- 📊 Health metrics dashboard with charts
- 🔐 Biometric authentication for sensitive data
- 🌍 Cloud sync with Firebase
- 🎤 Audio recordings for guided sessions
- 🎮 Gamification with achievements
- 📍 Location-based emergency services
- 🤖 AI wellness recommendations

## Contributing

Feel free to extend this app with:
- Additional meditation sessions
- More health tracking metrics
- Integration with wearable devices
- Backend synchronization
- Advanced animations

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Troubleshooting

### Gradle Sync Issues
- Clear cache: `./gradlew clean`
- Update Android Studio to latest version

### Animation Stuttering
- Run on a real device (emulator can be slow)
- Reduce animation duration for testing

### Room Database Errors
- Ensure all @Entity classes are registered in AppDatabase
- @TypeConverters must be specified for complex types

## License

This project is open source and available under the MIT License.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review the inline code comments
3. Check Android documentation for Compose/Room

---

**Made with ❤️ for health and wellbeing**
