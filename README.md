<div align="center">

<img src="app/src/main/ic_launcher-playstore.png" alt="IPU One logo" width="96" height="96" />

# IPU One

**The unofficial student companion app for GGSIPU.**
Results, notices, and document collection — without ever touching the university's own portal.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-30-informational)
![Target SDK](https://img.shields.io/badge/targetSdk-36-informational)
![License](https://img.shields.io/badge/license-MIT-green)

</div>

---

## What it does

GGSIPU's official result portal is a captcha-gated relic. IPU One's [backend](../IPU%20One%20Backend) drives that portal server-side on the student's behalf, so this app gives students a clean, native way to:

- 🔑 **Sign in** with Email OTP or native Google Sign-In (Credential Manager) — no passwords stored on-device.
- 📊 **Import & view results** — CGPA, per-semester trend charts, and subject-level breakdowns, pulled straight from the university portal and parsed automatically.
- 📢 **Browse notices** — university and department announcements, auto-targeted to your program/institute/batch.
- 📄 **Submit documents** — marksheets, NOCs, and certificates sent directly to the student cell, no physical queue.
- 🧑‍🎓 **Track your profile** — enrollment, program, and institute details pulled from the official portal.

## Screenshots

<div align="center">
<sub><i>Coming soon</i></sub>
</div>

## Tech stack

| Concern | Choice |
|---|---|
| UI | [Jetpack Compose](https://developer.android.com/jetpack/compose) — fully declarative, Material 3 |
| Networking | [Retrofit2](https://square.github.io/retrofit/) + OkHttp3 |
| Auth | Email OTP & Google native sign-in via [Credential Manager](https://developer.android.com/identity/sign-in/credential-manager) |
| Local storage | Jetpack DataStore (`androidx.datastore`) |
| Charts | [Vico](https://patrykandpatrick.com/vico) for results analytics |
| Images | [Coil Compose](https://coil-kt.github.io/coil/compose/) |
| Navigation | Jetpack Navigation Compose |
| Language | Kotlin 2.2, Gradle Kotlin DSL |

## Architecture

Feature-centric MVVM. Each screen owns its `State` and `ViewModel`; shared building blocks live under `core/`.

```
app/src/main/java/com/ipu/ipuoneapp/
├── core/
│   ├── config/      → AppConfig.kt (API base URL, OAuth client id)
│   ├── network/     → Retrofit/OkHttp client, ApiService, AuthInterceptor
│   ├── ui/          → design-system components & theme (PrimaryButton, AppTextField, Header, Footer)
│   └── utils/       → TokenManager (DataStore-backed), extensions
├── data/model/      → DTOs mirroring the backend's JSON payloads (auth, result, notice, document, student)
├── features/
│   ├── auth/        → Email OTP + Google sign-in screens & view models
│   ├── home/         → Dashboard: student card, latest notices, result stats
│   ├── notices/      → Paginated, filterable announcements
│   ├── results/      → CGPA dashboard, semester trend chart, subject breakdown
│   ├── profile/      → Student profile
│   └── services/     → Document collection flow
└── navigation/       → AppNavGraph.kt, Routes.kt
```

Talks to the [IPU One backend](../IPU%20One%20Backend), which is the piece that actually impersonates a browser against the real GGSIPU result portal — this app never touches it directly.

## Getting started

### Prerequisites
- Android Studio (latest stable)
- JDK 11+
- An emulator or device on API 30+

### Setup

```bash
git clone <repository_url>
cd IPUOneApp
```

Open the project in Android Studio and let Gradle sync — it generates `local.properties` for you (SDK path). No API keys are required to build a debug APK.

By default the app points at the live backend:

```kotlin
// app/src/main/java/com/ipu/ipuoneapp/core/config/AppConfig.kt
const val BASE_URL = "http://tmsk864q5tetm736i55qlyi0.213.210.37.18.sslip.io/"
```

To run against a local backend instance from the emulator, change this to:

```kotlin
const val BASE_URL = "http://10.0.2.2:8080/"
```

### Run

Select a device (API 30+) and hit **Run 'app'** (`Shift+F10`), or from the CLI:

```bash
./gradlew assembleDebug   # build a debug APK
./gradlew installDebug    # build & install on a connected device/emulator
```

### Test & lint

```bash
./gradlew test                  # JVM unit tests
./gradlew connectedAndroidTest  # instrumented tests (needs a device/emulator)
./gradlew lint
```

### Release builds

Release builds are signed via `signingConfigs` in `app/build.gradle.kts`, which reads `KEYSTORE_PATH`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, and `KEY_PASSWORD` from `local.properties`. Neither the keystore nor `local.properties` are checked into version control — see [`.gitignore`](.gitignore).

## Development guidelines

- **State hoisting**: pass state down from ViewModels to stateless composables; keep composables free of business logic.
- **Design system first**: prefer `core/ui` components (`PrimaryButton`, `AppTextField`, `Header`, `Footer`) over raw Material components to keep the app on-brand.
- **DTOs mirror the backend**: when the backend's response shape changes, update the matching type under `data/model` rather than parsing ad hoc JSON in a ViewModel.

## Related projects

| Project | Description |
|---|---|
| [IPU One Backend](../IPU%20One%20Backend) | Spring Boot API that drives the GGSIPU result portal, parses results, and serves notices/documents |
| [IPU One Admin Portal](../ipu_one_admin_portal) | Next.js dashboard for managing notices, students, and submitted documents |
