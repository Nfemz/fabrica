# AbstractCommand

**Package:** `com.hypixel.hytale.server.core.command.system`
**Type:** Abstract Class

## Overview

The foundational base class for all commands. Most plugins should extend `CommandBase` or `AbstractPlayerCommand` instead, which provide simpler APIs.

## Static Fields

```java
public static final HytaleLogger LOGGER
public static final String[] EMPTY_STRING_ARRAY
```

## Key Methods

### Argument Definition

#### withRequiredArg()
```java
public <D> RequiredArg<D> withRequiredArg(
    String name,
    String description,
    ArgumentType<D> type
)
```
Define a required argument.

#### withOptionalArg()
```java
public <D> OptionalArg<D> withOptionalArg(
    String name,
    String description,
    ArgumentType<D> type
)
```
Define an optional argument (returns null if not provided).

#### withDefaultArg()
```java
public <D> DefaultArg<D> withDefaultArg(
    String name,
    String description,
    ArgumentType<D> type,
    D defaultValue,
    String defaultDescription
)
```
Define an argument with a default value.

#### withFlagArg()
```java
public FlagArg withFlagArg(String name, String description)
```
Define a boolean flag argument (--flag).

#### withListRequiredArg() / withListOptionalArg() / withListDefaultArg()
Variants that accept multiple values as a list.

### Structure

#### addSubCommand()
```java
public void addSubCommand(AbstractCommand command)
```
Add a subcommand to this command.

#### addUsageVariant()
```java
public void addUsageVariant(AbstractCommand command)
```
Add an alternative usage pattern.

#### addAliases()
```java
public void addAliases(String... aliases)
```
Add command aliases.

### Permissions

#### requirePermission()
```java
public void requirePermission(String permission)
```
Require a specific permission to use this command.

#### hasPermission()
```java
public boolean hasPermission(CommandSender sender)
```
Check if a sender has permission.

#### getPermissionGroups()
```java
public List<String> getPermissionGroups()
```
Get permission groups for this command.

### Execution

#### acceptCall()
```java
public CompletableFuture<Void> acceptCall(
    CommandSender sender,
    ParserContext parserContext,
    ParseResult parseResult
)
```
Internal method called when command is executed. Override `executeSync` or `executeAsync` in subclasses instead.

### Metadata

#### getName()
```java
public String getName()
```
Get the command name.

#### getDescription()
```java
public String getDescription()
```
Get the command description.

#### getAliases()
```java
public Set<String> getAliases()
```
Get all aliases.

#### getFullyQualifiedName()
```java
public String getFullyQualifiedName()
```
Get the full command path (e.g., "parent subcommand").

#### getSubCommands()
```java
public Map<String, AbstractCommand> getSubCommands()
```
Get all subcommands.

#### getOwner()
```java
public CommandOwner getOwner()
```
Get the plugin that owns this command.

## Example: Direct Extension (Advanced)

```java
public class AdvancedCommand extends AbstractCommand {
    private final RequiredArg<String> targetArg;

    public AdvancedCommand() {
        // Constructor is protected in AbstractCommand
        // Use CommandBase instead for simpler API
        targetArg = withRequiredArg("target", "Target player", ArgTypes.STRING);
    }

    // Must implement execution logic
}
```

## Gotchas

1. **Use subclasses**: For most cases, extend `CommandBase`, `AbstractAsyncCommand`, or `AbstractPlayerCommand` instead.

2. **Argument order**: Define arguments in the order they should be parsed.

3. **Permission timing**: Set permissions in the constructor, before registration.

4. **Subcommand ownership**: Subcommands are automatically owned by the parent command's owner.

## Related

- [CommandBase](basecommands/CommandBase.md) - Simpler sync command base
- [AbstractPlayerCommand](basecommands/AbstractPlayerCommand.md) - Player commands with ECS access
- [RequiredArg](arguments/RequiredArg.md) - Required argument type
- [ArgTypes](arguments/ArgTypes.md) - Built-in argument types
