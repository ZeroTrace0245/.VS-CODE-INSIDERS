# Health & Wellbeing App - Quick Reference

## 🚀 Quick Start

```bash
# 1. Sync Gradle (Android Studio does this automatically)
./gradlew build

# 2. Run the app
./gradlew installDebug

# 3. Optional: Generate Manim animations
pip install manim
manim -pql wellness_animations.py
```

## 📱 App Screens

| Screen | Icon | Features |
|--------|------|----------|
| **Home** | 🏠 | Dashboard with wellness tips, streaks, quick access |
| **Health** | 🏥 | Emergency records, blood type, allergies, medications |
| **Meditate** | 💚 | Breathing exercises, guided sessions, meditation player |
| **Journal** | 📓 | Mood tracking, journaling, entry management |

## 🗂️ File Structure

```
app/src/main/java/com/example/myapplication/
├── data/                    # Database & models
│   ├── HealthRecord.kt     # Health data model
│   ├── MentalWellness.kt   # Wellness data model
│   ├── Dao.kt              # Database operations
│   ├── AppDatabase.kt      # Room setup
│   └── Converters.kt       # Type conversion
├── ui/
│   ├── screens/            # Composable screens
│   │   ├── HomeScreen.kt
│   │   ├── HealthRecordsScreen.kt
│   │   ├── MeditationScreen.kt
│   │   └── JournalScreen.kt
│   ├── navigation/
│   │   └── BottomNavBar.kt # Navigation
│   └── theme/
│       ├── Color.kt        # Colors & gradients
│       ├── Theme.kt        # Design system
│       └── Type.kt         # Fonts
└── MainActivity.kt         # App entry point
```

## 🎨 Color Palette

```kotlin
// Primary gradient (purple to blue)
val primaryGradient = listOf(Color(0xFF667eea), Color(0xFF764ba2))

// Calm gradient (blues to teal)
val calmGradient = listOf(Color(0xFF81C3D7), Color(0xFF7ECBC9), Color(0xFFA2D5C6))

// Danger gradient (red)
val dangerGradient = listOf(Color(0xFFe74c3c), Color(0xFFC0392b))
```

## 🔄 Data Flow

```
User Interaction
    ↓
Composable Screen
    ↓
ViewModel/Flow
    ↓
Room Database
    ↓
Local Device Storage
```

## 💾 Database Schema

### health_records
```
- id (Primary Key)
- bloodType
- allergies (JSON)
- medications (JSON)
- medicalConditions (JSON)
- emergencyContact (JSON)
- insuranceInfo (JSON)
- lastUpdated
```

### journal_entries
```
- id (Primary Key)
- date
- title
- content
- mood
- tags (JSON)
```

### wellness_metrics
```
- id (Primary Key)
- date
- stressLevel (1-10)
- sleepHours
- exerciseMinutes
- waterIntakeL
- notes
```

## 🎬 Manim Animations

Location: `wellness_animations.py`

### Animations Available:
1. **BreathingCircleAnimation** - 4-7-8 breathing
2. **HeartbeatAnimation** - Heart pulse
3. **WaterDropAnimation** - Hydration
4. **CalmWaves** - Relaxation waves
5. **ProgressRing** - Progress 0-100%

### Quick Render:
```bash
manim -pql wellness_animations.py BreathingCircleAnimation
```

## 🎭 Animation Types Used

### Compose Animations
- **slideInVertically**: Bottom sheet, cards entering
- **fadeIn**: Content fading in
- **expandVertically**: Expandable sections
- **scale**: Breathing circle, mood selector
- **animateColor**: Color transitions
- **infiniteRepeatable**: Continuous breathing pulse

### Custom Animations
- Animated breathing circle with scale + color
- Journal mood selector with scale + fade
- Card entrance animations on Home screen

## 🔐 Key Features

### Emergency Health Records
✅ Offline-first architecture
✅ Quick access design
✅ Color-coded sections
✅ Expandable detailed info

### Mental Resilience
✅ 4-7-8 breathing with animation
✅ 4 guided meditation sessions
✅ Journal with mood tracking
✅ Wellness dashboard

### Storage
✅ Room database (local SQLite)
✅ No internet required
✅ DataStore for preferences
✅ Automatic data sync

## 🛠️ Dependencies Summary

```gradle
// Compose & Material
androidx.compose.* (2024.09.00)
androidx.material3.*

// Local Storage
androidx.room.* (2.6.1)
androidx.datastore.* (1.0.0)

// Utilities
io.coil-kt.coil-compose (2.5.0)
com.airbnb.android.lottie-compose (6.1.0)
org.jetbrains.kotlinx.kotlinx-serialization-json (1.6.0)
```

## 📝 Common Tasks

### Adding a New Screen
1. Create `NewScreen.kt` in `ui/screens/`
2. Add to BottomNavBar navigation
3. Update MainActivity screen selection

### Adding to Health Records
1. Modify `HealthRecord.kt` data class
2. Update Converters if complex type
3. Update HealthRecordsScreen UI

### Adding Journal Entry
1. Insert via `journalEntryDao.insertJournalEntry(entry)`
2. Read via `journalEntryDao.getAllJournalEntries()`
3. UI automatically updates via Flow

### Adding Wellness Metric
1. Use `wellnessMetricDao.insertMetric(metric)`
2. Query with date range: `getMetricsInDateRange(start, end)`

## 🚨 Troubleshooting

| Issue | Solution |
|-------|----------|
| Gradle sync fails | `./gradlew clean`, restart Studio |
| Database errors | Clear app data, rebuild |
| Animation stutters | Run on real device, not emulator |
| Compose errors | Check @Composable functions are marked |
| Room errors | Ensure all entities in AppDatabase |

## 📊 Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Build variant
./gradlew assembleDebug
```

## 🎯 Next Development Ideas

- 🔔 Medication reminders with notifications
- 📈 Health metrics charting
- 🌍 Cloud sync (Firebase)
- 🎤 Audio recording for sessions
- 🔐 Biometric authentication
- 🎮 Gamification (achievements)
- 📍 Emergency SOS location sharing

## 📚 Resources

- [Jetpack Compose Docs](https://developer.android.com/develop/ui/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)
- [Manim Docs](https://docs.manim.community/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**Need help? Check README.md or MANIM_SETUP.md for detailed guides!**
