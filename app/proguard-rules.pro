# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Shivam Seth\AppData\Local\Android\sdk1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
#supportcrashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
#supportlibrary
-dontwarn android.support.**
#designlibrary
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#volley
-keep class org.apache.commons.logging.**
#otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
#facebook
-keep class com.facebook.** {
   *;
}
-keepattributes Signature
-keep class com.facebook.android.*
-keep class android.webkit.WebViewClient
-keep class * extends android.webkit.WebViewClient
-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}
-keep class com.facebook.** {
   *;
}
-keep class com.facebook.model.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
