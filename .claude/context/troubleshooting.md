# Troubleshooting Guide

## Build Fails

| Error | Solution |
|-------|----------|
| "Could not find HytaleServer.jar" | Check `hytale_home` in `gradle.properties` points to correct Hytale installation |
| "Java 25 required" | Install Java 25 SDK, ensure `JAVA_HOME` is set |
| Compilation errors | Check import statements match Hytale API packages |

## Server Won't Start

| Issue | Solution |
|-------|----------|
| "Port already in use" | Stop other servers, or change port in server config |
| "Assets not found" | Verify `--assets` path in run configuration |
| Authentication errors | Run `auth login device` in server console |

## Plugin Not Loading

| Issue | Solution |
|-------|----------|
| Plugin not detected | Check `manifest.json` is in JAR at root level |
| "Main class not found" | Verify `Main` in `manifest.json` matches fully qualified class name |
| Version mismatch | Ensure `ServerVersion` in manifest is `*` or matches server version |

## In-Game Issues

| Issue | Solution |
|-------|----------|
| Command not found | Check command registered in `setup()`, try `/help` |
| Permission denied | Set `setPermissionGroup(GameMode.Adventure)` for all players |
| Changes not appearing | Rebuild with `./gradlew build`, restart server, reconnect |

## Asset/Recipe Errors

Check client logs at: `%APPDATA%/Hytale/UserData/Logs/`

| Error | Cause | Solution |
|-------|-------|----------|
| "Input: Can't be null!" | Using Minecraft-style Pattern/Key recipe format | Convert to Hytale Input array format (see asset-formats.md) |
| "Failed to validate asset" | Missing required field in recipe | Ensure Input, PrimaryOutput, Output, BenchRequirement are present |
| Recipe not appearing in-game | Wrong BenchRequirement | Check bench Type and Id match an existing bench |
| Block has no texture | Missing texture file | Ensure all textures referenced in BlockType.Textures exist in Common/BlockTextures/ |
| Block top/bottom missing texture | Wrong face names | Use `Up`/`Down` not `Top`/`Bottom` in Textures object |
| Block can't be placed | Missing required fields | Ensure `PlayerAnimationsId: "Block"` at item level and `HitboxType: "Full"` in BlockType |
| Block has no "Press F" prompt | Missing IsUsable flag | Add `"Flags": { "IsUsable": true }` in BlockType |
| No block preview in hand | Missing PlayerAnimationsId | Add `"PlayerAnimationsId": "Block"` at the item level (not inside BlockType) |
| "Failed to load CustomUI documents" | Invalid .ui syntax or wrong file extension | Ensure files use CSS-like syntax (not JSON), use only `.ui` extension |
| "Could not resolve expression for property Alignment" | Using `Alignment: Left` in .ui | Use `HorizontalAlignment: Left` instead |
| "Could not find document X for Custom UI Append command" | Wrong path format in `append()` | Use just filename: `commands.append("MyUI.ui")`, not full path |
| "Value cannot be null. (Parameter 'element')" | Element selector not found | Verify element ID exists in .ui file, use `#ElementId.Property` format |
| "CustomUI Set command couldn't set value" | Wrong property name in selector | For Label text, use `.TextSpans` not `.Text`: `#Label.TextSpans` |
| "Index was outside the bounds of the array" | Using two-arg `append(selector, path)` incorrectly | Use single-arg `append(filename)` instead |

## Debugging Tips

### Enable Diagnostic Mode
In Hytale client settings (General tab), enable **Diagnostic Mode** for detailed error messages with line numbers.

### Check Client Logs
```powershell
# Windows path
%APPDATA%\Hytale\UserData\Logs\

# From WSL
/mnt/c/Users/<username>/AppData/Roaming/Hytale/UserData/Logs/
```

Look for lines containing `ERROR`, `WARN`, `SEVERE`, or `Failed`.

### Extract Vanilla Assets for Reference
```powershell
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::OpenRead('C:\Users\<user>\AppData\Roaming\Hytale\install\release\package\game\latest\Assets.zip')

# List files matching pattern
$zip.Entries | Where-Object { $_.FullName -like '*Recipe*' }

# Extract specific file
$entry = $zip.Entries | Where-Object { $_.FullName -eq 'Server/Item/Items/Bench/Bench_WorkBench.json' }
$reader = [System.IO.StreamReader]::new($entry.Open())
$reader.ReadToEnd()
```

## WSL-Specific Notes

Since Hytale is installed on Windows but development is in WSL:
- The `hytale_home` path uses `/mnt/c/...` to access Windows files
- Server runs in WSL, client connects from Windows
- File changes in WSL are immediately visible to the Windows filesystem
- Client logs are at `/mnt/c/Users/<username>/AppData/Roaming/Hytale/UserData/Logs/`

### WSL2 IP Changes on Reboot

WSL2 assigns a new IP to the Windows host on each reboot. This breaks MCP connections to Windows apps like Blockbench.

**To fix Blockbench MCP connection after reboot:**

1. Get the current Windows host IP:
   ```bash
   cat /etc/resolv.conf | grep nameserver | awk '{print $2}'
   ```

2. Update `~/.claude.json` - find the `blockbench` MCP server config and replace the old IP with the new one:
   ```json
   "args": ["mcp-remote", "http://<NEW_IP>:3000/bb-mcp"]
   ```

3. Restart Claude Code

**Quick one-liner to update the IP:**
```bash
NEW_IP=$(ip route show | grep -i default | awk '{print $3}') && sed -i "s|http://[0-9.]*:3000/bb-mcp|http://$NEW_IP:3000/bb-mcp|g" ~/.claude.json && echo "Updated to $NEW_IP"
```
