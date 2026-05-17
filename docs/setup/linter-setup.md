# Risk Project — Linter Setup (one-time, after pulling main)

**Prerequisite:** make sure your default JDK is **11 or 17, not 25**. Gradle 8.10 can't run on JDK 25.

```sh
java -version   # should print 11.x or 17.x
```

If you're on 25, install Temurin 17 (Mac: `brew install --cask temurin@17`) and either set `JAVA_HOME` in `~/.zshrc` or use a JDK switcher like jenv.

**Verify the build works** before changing anything:

```sh
./gradlew build
```

Should end with `BUILD SUCCESSFUL`. If checkstyle fails out of the gate, stop and ping the channel — config drift.

---

## 1. Install the google-java-format plugin

- IntelliJ → **Settings** (`⌘,`) → **Plugins** → **Marketplace** tab
- Search `google-java-format` → **Install** → **Restart IDE**

## 2. Enable it

- **Settings** → **Other Settings** → **google-java-format Settings**
- Check **Enable google-java-format**
- Code style: **Default (Google Java Style)**

> Once enabled, the plugin **replaces what `Reformat Code` (`⌥⌘L`) does** — it doesn't add a new shortcut. So your existing keybinds keep working; they just use Google's formatting engine instead of IntelliJ's built-in one.

## 2.5 IntelliJ JRE Config

google-java-format uses internal JDK compiler APIs that are locked down by default in modern JDKs. Without these flags the plugin throws `module java.base does not export …` errors as soon as you try to format.

- **Help** → **Edit Custom VM Options**
- Paste the following at the bottom of the file, then restart IntelliJ:

```
--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
```

## 3. Fix IntelliJ's import layout (critical — without this, Optimize Imports fights Checkstyle forever)

- **Settings** → **Editor** → **Code Style** → **Java** → **Imports** tab
- **Class count to use import with `*`**: `9999`
- **Names count to use static import with `*`**: `9999`
- **Import Layout** panel (bottom): move `import static all other imports` **above** `import all other imports`. Final order:

  ```
  import static all other imports
  <blank line>
  import all other imports
  ```

## 4. Confirm the shortcuts

| Action | Mac | Win/Linux |
|---|---|---|
| Reformat Code | `⌥⌘L` | `Ctrl+Alt+L` |
| Optimize Imports | `⌃⌥O` | `Ctrl+Alt+O` |

(Standard IntelliJ defaults — only change them if you want to.)

## 5. Turn on auto-format on save (optional but recommended)

- **Settings** → **Tools** → **Actions on Save**
- Check **Reformat code** and **Optimize imports**

## 6. Sanity-check your setup

- Open any `.java` file, mess up the indentation or import order on purpose
- Hit `⌥⌘L` then `⌃⌥O`
- The file should snap to 2-space Google Style and imports should sort with statics on top
- Run `./gradlew checkstyleMain checkstyleTest` — should pass

## 7. (Optional) Bulk-format an entire branch

If you pull a branch that hasn't been formatted yet (or you want to clean up your own WIP), do it in one shot instead of file by file:

- In the **Project** tool window, right-click `src/` → **Reformat Code**
- In the dialog, check **Include subdirectories** and **Optimize imports** → **OK**

This is also how the existing repo was bulk-formatted the first time the linter was added.

---

## Day-to-day workflow

- Write code normally → hit `⌥⌘L` (or save, if auto-format on)
- `./gradlew build` before pushing — same checks CI runs
- If a chunk of code shouldn't be reformatted (rare):

  ```java
  // @formatter:off
  ... weird-but-intentional code ...
  // @formatter:on
  ```

## Troubleshooting

- **Gradle terminal sits forever after success:** you ran with `--scan`. Remove it from your run config.
- **`IllegalArgumentException: 25.0.2`:** your JDK is 25, see prerequisite.
- **Optimize Imports keeps putting static at the bottom:** you skipped step 3.
- **CI failing on something formatting can't fix** (e.g. `LineLength`, `NeedBraces`): fix manually — google-java-format does most things but won't shorten an over-long line.
