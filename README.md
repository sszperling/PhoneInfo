# PhoneInfo - An experimental Android project

The idea behind PhoneInfo is to provide a simple playground for new or diverse technologies.

The app itself is simple: it displays some basic information about your device,
plus a list of every package installed in it.

*You're currently in the `java` branch.* This branch was implemented with Android Studio 4.0.1. 
All classes provided are written in Java.

## Notes about relevant changes

- Previous versions used `AsyncTask` to perform IO tasks. That class has since been deprecated,
and has been replaced with `java.util.concurrency`.
