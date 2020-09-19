# PhoneInfo - An experimental Android project

The idea behind PhoneInfo is to provide a simple playground for new or diverse technologies.

The app itself is simple: it displays some basic information about your device,
plus a list of every package installed in it.

*You're currently in the `Litho` branch.* This branch was implemented with Android Studio 4.0.1, using the Litho library for all UI object.
All classes provided are written in Kotlin. No layout XML files are included.

## Notes about relevant changes

- Previous versions used `AsyncTask` to perform IO tasks. That class has since been deprecated,
and has been replaced with `lifecycleScope` and Kotlin's Coroutines API.
