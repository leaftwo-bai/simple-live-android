# Add project specific ProGuard rules here.

# Keep ALL app classes to prevent reflection issues
# This is aggressive but ensures no ClassCastException
-keep class com.xycz.simplelive.** { *; }
-keep class com.xycz.simplelive.core.** { *; }
-keepclassmembers class com.xycz.simplelive.** { *; }
-keepclassmembers class com.xycz.simplelive.core.** { *; }

# Keep data classes used with Room and Retrofit
-keep class com.xycz.simplelive.data.** { *; }
-keep class com.xycz.simplelive.domain.model.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keepclasseswithmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-keep interface com.google.android.exoplayer2.** { *; }

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.xycz.simplelive.**$$serializer { *; }
-keepclassmembers class com.xycz.simplelive.** {
    *** Companion;
}
-keepclasseswithmembers class com.xycz.simplelive.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep generic signatures for proper type inference
-keepattributes Signature
-keepattributes *Annotation*

# Keep ViewModel constructors for Hilt
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep DataStore Preferences
-keep class androidx.datastore.** { *; }
-keepclassmembers class * {
    @androidx.datastore.core.Serializer *;
}