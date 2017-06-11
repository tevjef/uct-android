# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Tevin\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-dontwarn android.support.v4.**
#-dontwarn android.support.v7.**
#-dontwarn me.zhanghai.android.**
#-dontwarn com.afollestad.materialdialogs.**
#-dontwarn dagger.android.**
#-dontwarn com.google.android.gms.**
#-dontwarn com.google.firebase.**
#-dontwarn io.fabric.sdk.**
#-dontwarn com.tevinjeffrey.rutgersct.ui.utils.**
#
#-dontwarn com.tevinjeffrey.rutgersct.**
#
##Otto
#
#-keepattributes *Annotation*
#-keepclassmembers class ** {
#    @com.squareup.otto.Subscribe public *;
#    @com.squareup.otto.Produce public *;
#}
#
#-dontwarn okio.**
#-dontwarn org.mockito.**
#-dontwarn sun.reflect.**
#-dontwarn android.test.**
#-dontwarn javax.annotation.**
#-dontwarn javax.inject.**
#-dontwarn sun.misc.Unsafe
#
#
#-allowaccessmodification
#-keepattributes *Annotation*
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
#-repackageclasses ''
#
###---------------Begin: proguard configuration for Gson  ----------
## Gson uses generic type information stored in a class file when working with fields. Proguard
## removes such information by default, so configure it to keep all of it.
#-keepattributes Signature
#
#-keep class com.crashlytics.** { *; }
#
#
#-dontwarn retrofit2.**
#-keepattributes Signature
#-keepattributes Exceptions
#
#-keepclasseswithmembers class * {
#    @retrofit2.http.* <methods>;
#}

-dontwarn retrofit2.**
-dontwarn retrofit2.Platform$Java8
-dontnote retrofit2.Platform
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
-dontnote retrofit2.Platform$IOS$MainThreadExecutor

-dontwarn me.zhanghai.android.**
-dontwarn com.afollestad.materialdialogs.**
-dontwarn dagger.android.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**
-dontwarn com.tevinjeffrey.rutgersct.ui.utils.**
-dontwarn okio.**
-dontwarn io.fabric.sdk.**

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*