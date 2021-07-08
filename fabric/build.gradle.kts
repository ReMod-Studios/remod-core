import groovy.util.Node

plugins {
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

val shadowCommon = configurations.register("shadowCommon")

architectury {
    platformSetupLoomIde()
    fabric()
}

repositories {
    mavenCentral()
}

dependencies {
    val floader_version: String by rootProject
    val fapi_version: String by rootProject
    val archapi_version: String by rootProject
    val flk_version: String by rootProject
    val kotlin_version: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$floader_version")
    modApi("net.fabricmc.fabric-api:fabric-api:$fapi_version")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:$archapi_version")
    modImplementation("net.fabricmc:fabric-language-kotlin:$flk_version+kotlin.$kotlin_version")
    implementation(project(path = ":common")) {
        isTransitive = false
    }
    "developmentFabric"(project(path = ":common")) {
        isTransitive = false
    }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        configurations = listOf(shadowCommon.get())
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set("fabric")
    }

    jar {
        archiveClassifier.set("dev")
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

publishing {
    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        maven {
            name = "OSSRH"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USER") ?: return@credentials
                password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
            }
        }
    }

    publications {
        register<MavenPublication>("mavenFabric") {
            val maven_group: String by rootProject
            val archives_base_name: String by rootProject
            val mod_version: String by rootProject

            groupId = maven_group
            artifactId = "$archives_base_name-${project.name}"
            version = mod_version

            pom {
                suppressAllPomMetadataWarnings()

                name.set("ReMod Core Fabric")
                description.set("Common code across all Java Edition mods from ReMod Studios | Fabric version")
                url.set("https://github.com/ReMod-Studios/remod-core")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        name.set("leocth")
                        url.set("https://github.com/LeoCTH")
                        email.set("leocth31@gmail.com")
                        organization.set("ReMod Studios")
                        organizationUrl.set("https://github.com/ReMod-Studios")
                    }
                    developer {
                        name.set("ADudeCalledLeo")
                        url.set("https://github.com/Leo40Git")
                        organization.set("ReMod Studios")
                        organizationUrl.set("https://github.com/ReMod-Studios")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ReMod-Studios/remod-core.git")
                    developerConnection.set("scm:git:ssh://github.com:ReMod-Studios/remod-core.git")
                    url.set("https://github.com/ReMod-Studios/remod-core/tree/master")
                }

                withXml {
                    // this removes all dependencies
                    // dependencies are apparently added after we remove everything,
                    //  but these don't include "fake" dependencies (remapped dependencies and our common code)
                    val root = asNode()
                    val depsList = root["dependencies"] as groovy.util.NodeList
                    if (depsList.isNotEmpty())
                        root.remove(depsList.first() as Node)
                }
            }
            from(components["java"])
            // add all the jars that should be included when publishing to maven

            artifact(tasks.remapJar) {
                classifier = null
            }
            artifact(tasks.sourcesJar) {
                classifier = "sources"
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenFabric"])
}

