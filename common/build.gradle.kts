repositories {
    mavenCentral()
}

dependencies {
    val fabric_loader_version: String by rootProject
    val architectury_version: String by rootProject

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    // Remove the next line if you don't want to depend on the API
    modApi("me.shedaniel:architectury:$architectury_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

architectury {
    common()
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
        maybeCreate<MavenPublication>("mavenCommon") {
            val maven_group: String by rootProject
            val archives_base_name: String by rootProject
            val mod_version: String by rootProject

            groupId = maven_group
            artifactId = archives_base_name
            version = mod_version

            pom {
                name.set("ReMod Core")
                description.set("A shared library across all of our mods")
                url.set("https://github.com/ReMod-Studios/remod-core")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
            }
            // add all the jars that should be included when publishing to maven
            artifact(tasks.remapJar)
            artifact(tasks.sourcesJar) {
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenCommon"])
}