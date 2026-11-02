# VoiceControl (starter)

Milestone 1: Accessibility Service + foreground service + typed test commands.

## Run
1. Open this folder in Android Studio (Hedgehog or newer). Let it sync Gradle.
2. Enable USB debugging on the Realme 7 and run the app.
3. Tap "Enable accessibility service" -> find VoiceControl -> turn on.
4. Type a command (e.g. `open chrome`), press Run, switch to another screen within 3 seconds.

## Commands
back, home, recents, notifications, scroll down, scroll up,
open <app>, tap <text>, type <text>

## Realme UI background settings
Settings > Apps > App management > VoiceControl > Battery usage -> Allow background activity,
enable Auto-start, and lock the app in the recents screen.

## Build the APK on GitHub
1. Create a new GitHub repo and push this folder to it (the `main` branch).
2. Open the repo's **Actions** tab. "Build APK" runs automatically on every push,
   or press **Run workflow** to start it manually.
3. When it finishes (about 3-5 minutes), open the run and download **VoiceControl-debug-apk**
   from the Artifacts section. Unzip it and install `app-debug.apk` on the phone.
