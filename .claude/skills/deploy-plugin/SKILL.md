---
name: deploy-plugin
description: Builds the Fabrica plugin and deploys it to the Hytale mods folder. Use when deploying, building for release, or updating the mod installation.
---

Build the Fabrica plugin and deploy it to the Hytale mods folder.

## Steps

1. **Ask user for version bump type** using AskUserQuestion:
   - Question: "What type of version bump?"
   - Options: "Patch" (0.0.X), "Minor" (0.X.0), "Major" (X.0.0)

2. **Read current version** from `gradle.properties` (the `version=X.Y.Z` line)

3. **Increment version** based on user choice:
   - Patch: increment Z (e.g., 0.0.2 → 0.0.3)
   - Minor: increment Y, reset Z to 0 (e.g., 0.0.2 → 0.1.0)
   - Major: increment X, reset Y and Z to 0 (e.g., 0.0.2 → 1.0.0)

4. **Update gradle.properties** with the new version

5. **Run `./gradlew build`** to build the plugin

6. **Find the base JAR file** (not -javadoc or -sources) in `build/libs/`

7. **Copy it** to `/mnt/c/Users/femia/AppData/Roaming/Hytale/UserData/Mods/`

8. **Remove the old version** JAR from the mods folder (e.g., delete `Fabrica-0.0.2.jar` when deploying `Fabrica-0.0.3.jar`)

9. **Report success** with:
   - Previous version → New version
   - Deployed filename
   - Deployment location
