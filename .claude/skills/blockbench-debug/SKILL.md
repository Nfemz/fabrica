---
name: blockbench-debug
description: Check Blockbench for model errors and warnings. Reports issues and safely fixes non-destructive ones. Use when models have issues or before exporting.
---

# Blockbench Debug - Find and Fix Model Issues

Diagnose and fix errors/warnings in the currently open Blockbench model.

## CRITICAL: Destructive Fixes

**DO NOT auto-click fix buttons blindly!** Some fixes have destructive side effects:

| Fix Button | Side Effect | Safe? |
|------------|-------------|-------|
| "Fix UV Size" | **RESETS ALL TEXTURE ASSIGNMENTS** - all cubes become same texture | **NO** |
| "Remove Unused" | Removes animation data | Usually OK |
| Other fixes | Varies | Check first |

## Workflow

### Step 1: Run Validation (Report Only)

Use `risky_eval` to query the Blockbench Validator:

```javascript
Validator.validate();
JSON.stringify({
  errors: Validator.errors.map(e => ({
    message: e.message,
    fixButtons: e.buttons ? e.buttons.map(b => b.name) : []
  })),
  warnings: Validator.warnings.map(w => ({
    message: w.message,
    fixButtons: w.buttons ? w.buttons.map(b => b.name) : []
  }))
})
```

### Step 2: Report Issues to User

Display ALL issues found. For each issue:
- Show the error/warning message
- Indicate if it has a fix button
- **Flag destructive fixes** (especially "Fix UV Size")

### Step 3: Handle Each Issue Type

#### UV Size Mismatch (DESTRUCTIVE - DO NOT AUTO-FIX)

**Error**: "texture X has a resolution (AxB) that does not match its UV size (CxD)"

**Why the fix button is bad**: It resets ALL texture assignments to the first texture.

**Correct manual fix**:
1. Note the texture resolution (e.g., 16x16)
2. Go to File > Project in Blockbench
3. Set UV Width and UV Height to match (e.g., both to 16)
4. This preserves texture assignments

**Or via risky_eval** (safe approach):
```javascript
Project.texture_width = 16;
Project.texture_height = 16;
Validator.validate();
JSON.stringify({errors: Validator.errors.length})
```

#### Zero-Width UV Faces (Safe to fix)

Can be auto-fixed - usually just removes degenerate faces.

#### Unused Animators (Usually safe)

Can be auto-fixed if user confirms they don't need the animation data.

#### Node Count Exceeded (Manual only)

No auto-fix available. User must simplify the model.

### Step 4: Apply ONLY Safe Fixes

Only auto-fix issues that are confirmed safe:

```javascript
Validator.warnings
  .filter(w => !w.message.includes('UV size'))
  .forEach(w => w.buttons?.forEach(b => b.click?.()));
"Applied safe fixes only"
```

### Step 5: Re-Validate and Report

```javascript
Validator.validate();
JSON.stringify({
  remainingErrors: Validator.errors.length,
  remainingWarnings: Validator.warnings.length,
  details: Validator.errors.concat(Validator.warnings).map(i => i.message)
})
```

## Safe One-Liner (Report Only)

For checking without fixing:

```javascript
Validator.validate();
JSON.stringify({
  errors: Validator.errors.map(e => e.message),
  warnings: Validator.warnings.map(w => w.message),
  total: Validator.errors.length + Validator.warnings.length
})
```

## Fix UV Size Mismatch Safely

If UV size errors exist, fix them WITHOUT destroying texture assignments:

```javascript
Project.texture_width = 16;
Project.texture_height = 16;
Validator.validate();
JSON.stringify({errors: Validator.errors.length, warnings: Validator.warnings.length})
```

Adjust 16 to match your actual texture resolution (check with `list_textures`).

## Common Issues Reference

| Issue | Auto-Fix Safe? | Manual Solution |
|-------|----------------|-----------------|
| UV size mismatch | **NO** | Set Project UV size to match texture |
| Too many nodes | N/A | Simplify model geometry |
| Zero-width faces | Yes | Auto-fix removes them |
| Unused animators | Usually | Auto-fix removes references |
| Invalid rotation | No | Use 22.5° increments |
| Missing texture | No | Create or reassign texture |

## When to Use This Skill

- Before exporting a model
- When something looks wrong
- As a pre-flight check

**Always report issues first, then discuss fixes with user before applying!**
