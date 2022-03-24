# Using :buildSrc Kotlin Extension Properties From Groovy Build Scripts
This tutorial describes how to work with Kotlin extension functions/properties within Groovy Gradle build scripts.

To explore how the `:buildSrc` extensions are used, you can modify the following properties located within `gradle.properties`
- `VERSION_NAME`
- `LOG_ANALYTICS`
Once you've modified a property value, redeploy the project to see the updated value reflected in the app.

Each of those Gradle properties are accessed from with `app/build.gradle` through the usage of Kotlin extension functions/properties which are defined within the `:buildSrc` Gradle project.

Because `app/build.gradle` is written in Groovy, the Kotlin extensions can't be accessed using the typical Kotlin syntax.
Instead, the must be used as they would be from Java, using the generated class with static method.
In addition, while Android Studio may autocomplete the method name, the build will not compile unless you explicitly import the generated class containing the extensions.