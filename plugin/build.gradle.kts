plugins {
  id("java")
  kotlin("jvm") version "2.1.20"
  id("org.jetbrains.intellij.platform.module")
}

repositories {
  mavenCentral()

  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  implementation(project(":server"))

  intellijPlatform {
    intellijIdeaCommunity("2024.3.5")
  }
}