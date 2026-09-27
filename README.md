# Alphabet Launcher

A minimal Android launcher-style app built with Kotlin and Jetpack Compose. Drag the A–Z bar to select a letter; the bar bends toward the finger and the screen shows installed launchable apps beginning with that letter.

## Features

- Live clock and date with a list of home apps
- A–Z alphabet bar with interactive curve animation
- Selected-letter bubble
- Installed launchable apps retrieved through `PackageManager`
- Apps filtered by starting letter and sorted alphabetically
- Empty state for letters with no matching apps
- Tap an app to launch it
- Cached app list for smooth interaction

## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the Gradle project.
4. Run the app on an Android device or emulator running Android 7.0 (API 24) or higher.


## Curve Animation

- **Visual:** Dragging along the A–Z bar bends it left toward screen center, with the letter under your finger moving furthest (25% screen width).
- **Math:** Surrounding letters shift progressively less using a smooth Gaussian bell curve (`shift = maxShift * e^-(d/σ)²`).
- **Release:** Lifting your finger animates a `curveAmount` factor from 1 to 0, smoothly snapping the bar back straight.
- **Performance:** Drawn directly on a Canvas during the draw phase, running at 60 FPS without triggering Compose recompositions.

## Libraries

| Library | Version  | Why |
|---|----------|---|
| `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.11.0` | Used to integrate ViewModels with Jetpack Compose. |

No external third-party library is used for the curve animation.

## AI Usage

Claude Code was used as a learning and development aid to understand and frame how the alphabet list interaction and curve animation logic worked. It was also used to clarify implementation approaches and documentation during development.
