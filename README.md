This is RecipeBox, a Kotlin Multiplatform recipe browser for Android, iOS, and Desktop.

* `/shared` — business logic only (Ktor, Room, Koin, ViewModels). No UI.
* `/androidApp` — native Jetpack Compose (Material 3).
* `/desktopApp` — Compose Desktop with a NavigationRail.
* `/iosApp` — native SwiftUI. Open the Xcode project and run from there.

### Running the apps

- Android: `./gradlew :androidApp:assembleDebug`
- Desktop: `./gradlew :desktopApp:run`
- iOS: open `/iosApp` in Xcode (needs a configured Apple developer team)
