---
name: blockbench-export
description: Export a finished Blockbench model and integrate it into the Fabrica mod. Saves model and textures to correct locations and updates block/item definitions.
---

# Export Blockbench Model to Mod

Take a finished model from Blockbench and integrate it into the Fabrica codebase.

## Prerequisites

- Blockbench is open with the finished model
- Model geometry is complete
- Textures are applied and look correct

## Steps

### Phase 1: Gather Information

1. **Get project info from Blockbench** using `mcp__blockbench__risky_eval`:
   ```javascript
   ({ name: Project.name, format: Project.format.id })
   ```

2. **Get texture list** using `mcp__blockbench__list_textures`

3. **Ask user for block/item name** using `AskUserQuestion`:
   - Question: "What should this block/item be named in the mod?"
   - Suggest: Convert project name to Item_Name format (e.g., `electric_generator` → `Electric_Generator`)
   - Options: Suggested name, or let user type custom name

4. **Ask if this is a new item or updating existing** using `AskUserQuestion`:
   - Options: "New block/item", "Update existing"

### Phase 2: Export Model (Human-in-the-Loop)

5. **Trigger export dialog** using `mcp__blockbench__trigger_action`:
   ```
   action: "export_blockymodel"
   confirmDialog: false
   ```

6. **Tell user to save the file**:
   ```
   HUMAN ACTION REQUIRED:

   A save dialog should be open in Blockbench.

   Save the file as: {item_name}.blockymodel
   Location: Your Documents folder is fine (I'll copy it to the right place)

   Reply "done" when you've saved it.
   ```

7. **Wait for user confirmation** before proceeding.

### Phase 3: Save Textures

8. **Check texture paths** using `mcp__blockbench__risky_eval`:
   ```javascript
   Texture.all.map(t => ({ name: t.name, path: t.path, saved: t.saved }))
   ```

9. **If textures are not saved**, trigger save:
   ```
   mcp__blockbench__trigger_action({ action: "save_textures", confirmDialog: false })
   ```

   Then tell user:
   ```
   HUMAN ACTION REQUIRED:

   Save each texture when prompted.
   Location: Your Documents folder is fine.

   Reply "done" when all textures are saved.
   ```

10. **Wait for user confirmation** before proceeding.

### Phase 4: Copy Files to Codebase

11. **Find the exported model file**:
    ```bash
    find ~/Documents -maxdepth 3 -name "*.blockymodel" -newer /tmp/skill_start -type f
    ```
    Or search for the expected filename.

12. **Create model directory**:
    ```
    src/main/resources/Common/Models/Blocks/{Item_Name}/
    ```

13. **Copy model file**:
    ```bash
    cp "{source_path}" "src/main/resources/Common/Models/Blocks/{Item_Name}/model.blockymodel"
    ```

14. **Copy texture files** to `src/main/resources/Common/BlockTextures/`:
    ```bash
    cp ~/Documents/{texture}.png src/main/resources/Common/BlockTextures/
    ```

### Phase 5: Update Block/Item Definition

15. **If new item**: Create `Server/Item/Items/{Item_Name}.json`:
    ```json
    {
      "TranslationProperties": {
        "Name": "server.{Item_Name}.name",
        "Description": "server.{Item_Name}.description"
      },
      "MaxStack": 1,
      "Icon": "Icons/ItemsGenerated/{Item_Name}.png",
      "Categories": ["Blocks.Machines"],
      "PlayerAnimationsId": "Block",
      "BlockType": {
        "Material": "Solid",
        "DrawType": "Model",
        "HitboxType": "Full",
        "BlockSoundSetId": "Metal",
        "ParticleColor": "#4a4a4a"
      }
    }
    ```

16. **If updating existing**: Change `DrawType` from `"Cube"` to `"Model"` and remove `Textures` array.

17. **Add translation** to `Server/Languages/en-US/server.lang`:
    ```
    {Item_Name}.name = {Display Name}
    {Item_Name}.description = {Description}
    ```

### Phase 6: Verify

18. **List created/modified files**:
    ```
    src/main/resources/
    ├── Common/
    │   ├── Models/Blocks/{Item_Name}/
    │   │   └── model.blockymodel
    │   └── BlockTextures/
    │       └── {textures}.png
    └── Server/
        └── Item/Items/{Item_Name}.json
    ```

19. **Report success** with:
    - Model location
    - Textures copied
    - JSON created/updated
    - Reminder: "Run `/deploy-plugin` to build and test"

## File Naming Conventions

| Source | Destination |
|--------|-------------|
| `my_model.blockymodel` | `Common/Models/Blocks/My_Model/model.blockymodel` |
| `steel.png` | `Common/BlockTextures/steel.png` |
| Project name | Converted to `Title_Case` for item name |

## Notes

- Model path is auto-detected by Hytale when `DrawType: "Model"` is set
- Textures referenced in the model must exist in `BlockTextures/`
- The item name in the JSON filename must match the model folder name
