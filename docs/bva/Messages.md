# Messages - BVA Analysis

`i18n.Messages` is a thin facade over a `ResourceBundle` that backs the locale (i18n)
feature. It has no numeric input boundaries; the meaningful partitions are the **active
locale** (the default English bundle vs. an alternative bundle) and the **locale-switch
transition** (default → alternative → back to default). A key lookup therefore resolves
against whichever bundle the most recent `setLocale` selected.

### Method under test: `String get(String key)`

Returns the label for `key` from the currently active bundle. The relevant partition is
the active locale rather than the key value (every key exists in every bundle).

- **TC1: active locale is the default (English)** ( :white_check_mark: )
    - **State of the system**: locale set to `Locale.ENGLISH`
    - **Expected output**: `get("ui.button.skip")` returns `"Skip"`; `get("ui.phase.attack")`
      returns `"Attack"` (English labels)

### Method under test: `void setLocale(Locale locale)`

Swaps the active bundle. Boundaries are the transitions between the default locale and an
alternative locale.

- **TC2: switch from default to alternative (Spanish)** ( :white_check_mark: )
    - **State of the system**: `setLocale(new Locale("es"))` called
    - **Expected output**: subsequent `get("ui.button.skip")` returns `"Omitir"` and
      `get("ui.phase.attack")` returns `"Ataque"` (Spanish labels)

- **TC3: switch alternative back to default (English)** ( :white_check_mark: )
    - **State of the system**: locale set to Spanish, then `setLocale(Locale.ENGLISH)` called
    - **Expected output**: `get("ui.button.skip")` returns `"Skip"` again (default restored)

### Method under test: `private Messages()`

Utility class; the constructor must not be publicly instantiable.

- **TC4: constructor is private and invocable via reflection** ( :white_check_mark: )
    - **State of the system**: reflective access to the declared no-arg constructor
    - **Expected output**: the constructor's modifier is `private`; invoking it via
      reflection succeeds (no exception)
