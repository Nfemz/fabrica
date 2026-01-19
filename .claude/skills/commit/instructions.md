Base directory for this skill: /home/nfemz/fabrica

Git commit and push changes to the repository.

## Steps

1. **Check git status** to see what files have changed

2. **Check git diff --stat** to understand the scope of changes

3. **Stage all changes** with `git add -A`

4. **Generate a commit message** based on the changes:
   - First line: short summary (50 chars max)
   - Blank line
   - Bullet points explaining key changes
   - Always end with: `Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>`

5. **Commit** using heredoc format:
   ```bash
   git commit -m "$(cat <<'EOF'
   Short summary here

   - Bullet point 1
   - Bullet point 2

   Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
   EOF
   )"
   ```

6. **Push** to the remote repository

7. **Report** the commit hash and summary

## Options

If the user provides a message with the command (e.g., `/commit fix typo`), use that as the commit summary instead of generating one.

## Safety

- Never use `--force` or `--amend` unless explicitly requested
- Never skip hooks unless explicitly requested
- If there are no changes to commit, report that and exit
