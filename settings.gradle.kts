rootProject.name = "koala"

include("plugin")
include("server")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}