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

## 2.5 IntelliJ JRE Config
- **Help** -> **Edit Custom VM Options**
- **Paste the following**:
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
- Run `./gradlew checkstyleMain checkstyleTest` — should pass

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
