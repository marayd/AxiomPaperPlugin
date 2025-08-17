plugins {
    `java-library`
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper) // Adds runServer and runMojangMappedServer tasks for testing

    // Shades and relocates dependencies into our plugin jar
    alias(libs.plugins.shadow)
}

group = "com.moulberry.axiom"
version = "4.0.5+1.21.6"
description = "Serverside component for Axiom on Paper"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 21
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.viaversion.com")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        mavenContent { snapshotsOnly() }
    }
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://maven.playpro.com")
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    paperweight.paperDevBundle("1.21.6-R0.1-SNAPSHOT")

    // Reflection Remapper (use stable release, not timestamped snapshot)
    implementation("xyz.jpenilla:reflection-remapper:0.1.2")

    implementation(libs.cloud.paper)

    // Zstd Compression Library
    implementation(libs.zstd.jni)

    // ViaVersion support
    compileOnly(libs.viaversion.api)

    // WorldGuard support
    compileOnly(libs.worldguard.bukkit)

    // PlotSquared support
    implementation(platform(libs.bom.newest))
    compileOnly(libs.plotsquared.core)
    compileOnly(libs.plotsquared.bukkit) { isTransitive = false }

    // CoreProtect support
    compileOnly(libs.coreprotect)

    // Placeholder API support
    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(21) // Target bytecode + APIs
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        // helper function to relocate a package into our package
        fun reloc(pkg: String) = relocate(pkg, "com.moulberry.axiom.dependency.$pkg")

        reloc("xyz.jpenilla.reflectionremapper")
    }
}
