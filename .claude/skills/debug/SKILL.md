# Debug Skill

Analyze recent Hytale client logs for errors and warnings.

## Usage

```
/debug [lines]
```

- `lines` (optional): Number of error/warning lines to show (default: 50)

## Steps

1. **Find the most recent client log** in `/mnt/c/Users/femia/AppData/Roaming/Hytale/UserData/Logs/`
   - Files are named like `2026-01-17_19-53-57_client.log`
   - Sort by modification time, pick the newest

2. **Search for errors and warnings** using grep:
   ```bash
   grep -i -E "(error|warning|fail|missing|severe|texture|block|recipe|asset)" "<log_file>" | tail -<lines>
   ```

3. **Report findings** grouped by category:
   - **Asset Errors**: Missing textures, failed to load assets, validation failures
   - **Recipe Errors**: Input validation, missing items
   - **UI Errors**: CustomUI loading failures, property resolution errors
   - **Other Warnings**: Any other warnings or errors

4. **Suggest fixes** based on common patterns:
   | Error Pattern | Likely Cause | Suggested Fix |
   |---------------|--------------|---------------|
   | "Input: Can't be null!" | Wrong recipe format | Convert to Input array format |
   | "Failed to validate asset" | Missing required field | Check asset JSON structure |
   | "Missing model for item" | Item lacks model/icon | Add icon to Icons/ItemsGenerated/ |
   | "Failed to load CustomUI" | Invalid .ui syntax | Use CSS-like syntax, not JSON |
   | Missing texture | Referenced texture doesn't exist | Create texture in Common/BlockTextures/ |

5. **Show log file info**:
   - Full path to log file analyzed
   - Log file timestamp
   - Total errors/warnings found vs lines shown
