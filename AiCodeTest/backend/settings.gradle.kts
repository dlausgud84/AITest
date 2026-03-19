rootProject.name = "AiCodeTest"

include(
    "apps:app",
    "modules:common",
    "modules:menu",
    "modules:auth"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
