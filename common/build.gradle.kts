repositories {
    mavenCentral()
}

dependencies {
    val floader_version: String by rootProject
    val archapi_version: String by rootProject

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:$floader_version")
    // Remove the next line if you don't want to depend on the API
    modApi("me.shedaniel:architectury:$archapi_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

architectury {
    common()
}