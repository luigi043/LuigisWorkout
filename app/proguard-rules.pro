# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Room entities
-keep class com.luigisworkout.data.database.entities.** { *; }
-keep class com.luigisworkout.data.** { *; }

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep Gson (used for recipe serialisation if needed)
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# Keep WorkManager workers
-keep class com.luigisworkout.notifications.** { *; }

# General Android rules
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
