# IPU One Android Application

The IPU One Android app is the mobile client for the IPU One student portal. It allows students to log in, view notices, access their results, and submit necessary documents directly to the student cell.

## 📱 Features

*   **Authentication**: GitHub OAuth and Email OTP login flows.
*   **Notices**: View university and department announcements.
*   **Results Analytics**: Detailed result viewing with visual charts.
*   **Document Collection**: A portal for uploading required documents directly to the administration.

## 🛠 Tech Stack & Architecture

The application is built with modern Android development practices, emphasizing a clean, declarative UI and a robust modular architecture.

### Libraries & Tools
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for fully declarative UI.
*   **Networking**: [Retrofit2](https://square.github.io/retrofit/) & OkHttp3 for REST API communication.
*   **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/) for asynchronous image fetching.
*   **Local Storage**: Jetpack DataStore (`androidx.datastore`) for preference management.
*   **Charting**: [Vico](https://patrykandpatrick.com/vico) for interactive graphs and results analytics.
*   **Navigation**: Jetpack Navigation Compose.

### Package Architecture (MVVM)
The codebase is structured into a feature-centric modular pattern using the **Model-View-ViewModel (MVVM)** architecture:

*   `core`: Foundational, reusable elements.
    *   `config`: Centralized remote configurations (`AppConfig.kt`).
    *   `network`: Network interceptors and API client definitions.
    *   `ui`: Design system, themes, and reusable composite components.
    *   `utils`: Helper extensions.
*   `data`: Core data models and DTOs representing API payloads.
*   `features`: Independent functional modules containing their respective UI and ViewModels (e.g., `auth`, `home`, `notices`, `results`, `services`).
*   `navigation`: Routing definitions that tie the feature modules together.

## 🚀 Getting Started

### Prerequisites
*   Android Studio (Latest stable version recommended).
*   JDK 11+ (Targeting SDK 36, Min SDK 30).

### Configuration
The app relies on the `AppConfig.kt` file (located in `com.ipu.ipuoneapp.core.config`) to target the backend API.
The base URL is currently configured to point to the live cloud backend:
```kotlin
const val BASE_URL = "https://ipu-one-backend.onrender.com/"
```
If you wish to test against a local backend on an Android Emulator, change this to `http://10.0.2.2:8080/`.

### Building and Running
1. Clone the repository and open the `IPUOneApp` directory in Android Studio.
2. Let Gradle sync the project dependencies.
3. Select an Emulator (API 30+) or a physical device.
4. Click **Run 'app'** (`Shift + F10`).

## 🤝 Development Guidelines

*   **State Management**: Use State Hoisting in Compose. Pass state down from the ViewModels to stateless composables.
*   **Design System**: Prefer using components from the `core/ui` package (like `PrimaryButton`, `AppTextField`) instead of standard Material components to ensure brand consistency across the app.
