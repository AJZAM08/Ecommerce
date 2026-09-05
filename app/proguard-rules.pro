# ======================================================================
# Katalog-Minyak Security & Obfuscation ProGuard Rules
# ======================================================================

# 1. Optimasi & Proteksi Kode Umum
-repackageclasses ''
-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# 2. Proteksi Model Data Room
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <methods>;
}
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# 3. Proteksi Domain Entities
-keep class com.ecommerce.domain.entity.** { *; }
-keep class com.ecommerce.data.local.entity.** { *; }

# 4. Proteksi Coil Image Loader
-keep class coil.** { *; }
-dontwarn coil.**

# 5. Proteksi Kotlin Coroutines & Flow
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**