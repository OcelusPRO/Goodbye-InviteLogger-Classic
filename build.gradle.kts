import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar


plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

var botVersion = "1.0.1"
var mainClassName : String = "fr.ftnl.MainKt"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.18") { exclude("opus-java") }
    implementation("com.github.minndevelopment:jda-ktx:081a177")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.7.0")
    
    implementation("org.slf4j", "slf4j-api", "1.7.2")
    implementation("ch.qos.logback", "logback-classic", "1.2.9")
    implementation("ch.qos.logback", "logback-core", "1.2.9")
}

tasks.test {
    useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}


tasks.withType<ShadowJar> {
    archiveBaseName.set("GoodbyeInviteLoggerClassic")
    archiveClassifier.set("min")
    minimize()
    archiveVersion.set(botVersion)
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Title"] = "GoodbyeInviteLoggerClassic"
        attributes["Implementation-Version"] = botVersion
        attributes["Main-Class"] = mainClassName
    }
}