# PasswordVaultApp - MVC Refactor

This version keeps the same Android app but organizes it in a cleaner MVC-inspired structure:

- **Model**: `models/PasswordModel.java`
- **View**: XML layouts in `res/layout/` and `views/PasswordAdapter.java`
- **Controller**: Activities in `activities/`
- **Data access / repositories**: `repository/PasswordRepository.java`, `repository/SessionManager.java`, `database/DatabaseHelper.java`
- **Utilities**: `utils/PasswordGenerator.java`, `utils/SimpleCrypto.java`

## What was completed
- Fixed broken activity files
- Added real SQLite CRUD flow
- Connected Main/AddEdit/Detail screens
- Added search in Main screen
- Added master password setup/login flow
- Added logout and change master password placeholders in Settings
- Added generator return flow to Add/Edit screen
- Kept the modern XML UI screens

## Open in Android Studio
Open the project folder that contains:
- `app/`
- `build.gradle`
- `settings.gradle`
- `gradlew`
