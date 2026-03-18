rootProject.name = "AiCodeTest"

include(
    "apps:app",
    "modules:common",
    "modules:menu"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
