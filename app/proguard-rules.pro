# Håll Question-modellen intakt för Gson-deserialisering
-keep class com.korkort.cquiz.Question { *; }

# Bevara TypeToken-subklasser så att Gson kan deserialisera generiska typer
-keep class * extends com.google.gson.reflect.TypeToken

# Gson kräver att fält-/klassnamn bevaras för reflection
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
