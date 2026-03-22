rootProject.name = "AiCodeTest"

include(
    "apps:app",
    "modules:common",
    "modules:menu",
    "modules:auth",
    "modules:user"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
