plugins {
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

allprojects {
    group = "me.akymaky"
    version = "1.0-SNAPSHOT"
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.5")
        pluginModule(implementation(project(":plugin")))
        pluginModule(implementation(project(":server")))
    }
}