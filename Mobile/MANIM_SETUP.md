# Manim Animation Setup Guide

This guide helps you set up and use Manim animations with the Health & Wellbeing app.

## What is Manim?

Manim is a Python library for creating mathematical animations. We use it to generate smooth, professional animations for health visualizations (breathing, heartbeat, progress tracking, etc.).

## Installation

### Prerequisites
- Python 3.9+
- ffmpeg (for video encoding)
- pip (Python package manager)

### Step 1: Install Manim

```bash
pip install manim
```

Or for better performance with GPU support:
```bash
pip install manim[all]
```

### Step 2: Verify Installation

```bash
manim --version
```

### Step 3: Install FFmpeg (if not already installed)

**Windows:**
```bash
choco install ffmpeg
```
Or download from [ffmpeg.org](https://ffmpeg.org)

**macOS:**
```bash
brew install ffmpeg
```

**Linux:**
```bash
sudo apt-get install ffmpeg
```

## Running the Animations

### From the project root:

```bash
# Render single animation (fast, lower quality)
manim -pql wellness_animations.py BreathingCircleAnimation

# Render all animations in the file
manim -pql wellness_animations.py

# High quality render (takes longer)
manim -pqh wellness_animations.py

# Preview during rendering
manim -pqL wellness_animations.py --preview
```

### Output
- Rendered videos go to: `videos/`
- High quality: `videos/1440p60/`
- Low quality: `videos/480p15/`

## Available Animations

### 1. BreathingCircleAnimation
- **Purpose**: Visual guide for 4-7-8 breathing
- **Duration**: ~19 seconds (4s inhale + 7s hold + 8s exhale)
- **Use Case**: Meditation and stress relief
- **Command**: `manim -pql wellness_animations.py BreathingCircleAnimation`

### 2. HeartbeatAnimation
- **Purpose**: Heart health monitoring animation
- **Duration**: ~3 seconds (5 pulses)
- **Use Case**: Health records and vitality display
- **Command**: `manim -pql wellness_animations.py HeartbeatAnimation`

### 3. WaterDropAnimation
- **Purpose**: Hydration reminder
- **Duration**: ~12 seconds
- **Use Case**: Daily wellness tips
- **Command**: `manim -pql wellness_animations.py WaterDropAnimation`

### 4. CalmWaves
- **Purpose**: Relaxation and meditation atmosphere
- **Duration**: ~8 seconds
- **Use Case**: Background during meditation sessions
- **Command**: `manim -pql wellness_animations.py CalmWaves`

### 5. ProgressRing
- **Purpose**: Session progress tracking (0-100%)
- **Duration**: ~10 seconds
- **Use Case**: Show meditation session completion
- **Command**: `manim -pql wellness_animations.py ProgressRing`

## Integrating Videos into the Android App

### Method 1: Embed as Video Files

1. Render the animation:
   ```bash
   manim -pql wellness_animations.py BreathingCircleAnimation
   ```

2. Find the MP4 file in `videos/480p15/`

3. Copy to Android project:
   ```bash
   cp videos/480p15/wellness_animations/BreathingCircleAnimation.mp4 \
      app/src/main/res/raw/breathing_animation.mp4
   ```

4. Load in Compose using `VideoView` or `ExoPlayer`

### Method 2: Convert to GIF

For lighter file sizes:
```bash
manim -pql wellness_animations.py BreathingCircleAnimation
ffmpeg -i videos/480p15/wellness_animations/BreathingCircleAnimation.mp4 \
       -vf "fps=10,scale=320:-1:flags=lanczos" breathing.gif
```

Copy to `app/src/main/res/drawable/`

### Method 3: Custom Compose Integration

```kotlin
@Composable
fun AnimatedBreathingCircle() {
    val context = LocalContext.current
    
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(Uri.parse("android.resource://com.example.myapplication/raw/breathing_animation"))
                setMediaController(MediaController(context))
                start()
            }
        }
    )
}
```

## Customizing Animations

### Edit wellness_animations.py

You can modify:
- Colors: Change `color=BLUE_C` to any Manim color
- Speeds: Modify `run_time` parameter
- Sizes: Change `radius=2` or `scale()` values
- Text: Update `Text()` content

Example customization:
```python
class BreathingCircleAnimation(Scene):
    def construct(self):
        circle = Circle(radius=2, color=TEAL, fill_opacity=0.7)  # Change color
        # 3-4-5 breathing instead of 4-7-8
        self.play(circle.animate.scale(1.5), run_time=3)  # 3s instead of 4s
```

## Troubleshooting

### "manim: command not found"
- Ensure Python and pip are in PATH
- Reinstall: `pip install --upgrade manim`

### "ffmpeg not found"
- Install FFmpeg (see Installation step 3)
- Add to PATH if needed

### Slow rendering
- Use `-pql` flags (preview, quiet, low quality) for testing
- Use `-pqm` for medium quality (faster than high)
- Render on a machine with more CPU cores

### Memory issues with high quality
- Stick with `-pql` or `-pqm` for development
- Use `-pqh` only for final exports

### Videos too large
- Convert to lower frame rate: `-r 24`
- Reduce resolution: `-q l` (low quality)
- Convert to WebM for better compression

## Example: Creating a Custom Animation

```python
from manim import *

class CustomWellnessAnimation(Scene):
    def construct(self):
        # Your custom animation here
        title = Text("Your Animation", font_size=48)
        circle = Circle(radius=2, color=BLUE)
        
        self.add(title)
        self.play(Create(circle), run_time=2)
        self.wait(1)
        self.play(circle.animate.scale(2), run_time=2)

# Render with: manim -pql wellness_animations.py CustomWellnessAnimation
```

## Advanced Features

### Multiple Scenes
The wellness_animations.py file includes 5 different scenes. Render specific ones or all with:
```bash
manim -pql wellness_animations.py  # Renders all scenes
```

### Batch Processing
Create a script to render all and convert to GIF:
```bash
#!/bin/bash
for scene in BreathingCircleAnimation HeartbeatAnimation WaterDropAnimation; do
    manim -pql wellness_animations.py $scene
    ffmpeg -i videos/480p15/wellness_animations/${scene}.mp4 ${scene}.gif
done
```

## Performance Tips

1. **Use low quality (-q l) for development**
2. **Preview as you code (--preview flag)**
3. **Disable anti-aliasing for faster rendering**
4. **Use partial_movie_file_path for resuming**

## Resources

- [Manim Documentation](https://docs.manim.community/)
- [Manim Examples](https://github.com/3b1b/manim/tree/master/example_scenes)
- [Color Reference](https://docs.manim.community/en/stable/reference/manim.utils.color.XKCD_COLORS.html)

## Next Steps

1. Install Manim following steps above
2. Run: `manim -pql wellness_animations.py`
3. Find videos in `videos/480p15/`
4. Integrate into your Android app
5. Customize animations as needed

---

**Happy animating! 🎬**
