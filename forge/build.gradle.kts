plugins {
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

val shadowCommon = configurations.register("shadowCommon")

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    useFabricMixin = true
}

repositories {
    mavenCentral()
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    val minecraft_version: String by rootProject
    val forge_version: String by rootProject
    val archapi_version: String by rootProject
    val kotlinforforge_version: String by rootProject

    forge("net.minecraftforge:forge:$minecraft_version-$forge_version")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-forge:$archapi_version")

    implementation("thedarkcolour:kotlinforforge:$kotlinforforge_version")

    implementation(project(path = ":common")) {
        isTransitive = false
    }
    "developmentForge"(project(path = ":common")) {
        isTransitive = false
    }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        exclude("fabric.mod.json")

        configurations = listOf(shadowCommon.get())
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set("forge")
    }

    jar {
        archiveClassifier.set("dev")
    }

    java {
        withSourcesJar()
        withJavadocJar()
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
        register<MavenPublication>("mavenForge") {
            val maven_group: String by rootProject
            val archives_base_name: String by rootProject
            val mod_version: String by rootProject

            groupId = maven_group
            artifactId = "$archives_base_name-${project.name}"
            version = mod_version

            pom {
                name.set("ReMod Core Forge")
                description.set("Common code across all Java Edition mods from ReMod Studios | Forge version")
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
                    fun child(parent: groovy.util.Node, name: String): groovy.util.Node? {
                        val raw = parent[name]
                        if (raw is groovy.util.Node)
                            return raw
                        else if (raw is groovy.util.NodeList) {
                            return if (raw.isEmpty())
                                null
                            else
                                raw.first() as groovy.util.Node
                        }
                        return null
                    }

                    // this removes all "fake" dependencies (remapped dependencies and our common code)
                    val root = asNode()
                    val deps = child(root, "dependencies")
                    if (deps != null) {
                        val toRemove = ArrayList<groovy.util.Node>()
                        for (depRaw in deps) {
                            val dep = depRaw as groovy.util.Node
                            val group = child(dep, "groupId")?.value() as String
                            val artifact = child(dep, "artifactId")?.value() as String
                            if (group.startsWith("net_fabricmc_yarn_") // remapped dependency
                                || group == project.group && artifact == "common") // our common code
                                toRemove.add(dep)
                        }
                        for (dep in toRemove)
                            deps.remove(dep)
                    }
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
    sign(publishing.publications["mavenForge"])
}