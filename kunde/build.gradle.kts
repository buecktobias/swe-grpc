@file:Suppress(
    "MaxLineLength",
    "StringLiteralDuplication",
    "MissingPackageDeclaration",
    "SpellCheckingInspection",
    "GrazieInspection",
)
import java.nio.file.Paths

val javaLanguageVersion = JavaVersion.VERSION_23.majorVersion
val javaLanguageVersionBuildpacks = JavaVersion.VERSION_23.majorVersion
val javaVersion = libs.versions.javaVersion.get()


val imagePath = "juergenzimmermann"

val useDevTools = true
val activeProfiles = "dev,http"

plugins {
    java
    idea
    checkstyle
    `project-report`

    //region grpc
    alias(libs.plugins.protobuf)
    //endregion

    alias(libs.plugins.spring.boot)


    alias(libs.plugins.spotbugs)

    alias(libs.plugins.test.logger)
}

defaultTasks = mutableListOf("compileTestJava")
group = "com.acme"
version = "2024.10.1"
val imageTag = version


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaLanguageVersion)
    }
}

repositories {
    mavenCentral()

    maven("https://repo.spring.io/milestone")
}

configurations.checkstyle {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
        select("com.google.guava:guava:0")
    }
}

dependencies {
    implementation(platform(libs.slf4j.bom))
    implementation(platform(libs.netty.bom))
    implementation(platform(libs.reactor.bom))
    implementation(platform(libs.spring.framework.bom))
    implementation(platform(libs.logback.parent))

    testImplementation(platform(libs.mockito.bom))
    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.allure.bom))

    implementation(platform(libs.spring.boot.parent))
    implementation(platform(libs.springdoc.openapi))

    annotationProcessor(libs.hibernate.processor)
    annotationProcessor(libs.mapstruct.processor)

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(libs.mapstruct)

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

    // region grpc
    implementation(libs.protobuf.java)
    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    // endregion
    runtimeOnly(libs.jansi)

    compileOnly(libs.spotbugs.annotations)
    testCompileOnly(libs.spotbugs.annotations)
    testImplementation(libs.modernizer)

    if (useDevTools) {
        developmentOnly(libs.spring.boot.devtools)
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.hamcrest", module = "hamcrest")
        exclude(group = "org.skyscreamer", module = "jsonassert")
        exclude(group = "org.xmlunit", module = "xmlunit-core")
        exclude(group = "org.awaitility", module = "awaitility")
    }
    testImplementation(libs.junit.platform.suite.api)
    testRuntimeOnly(libs.junit.platform.suite.engine)
    testImplementation(libs.mockito.inline)

    // TODO: Add this to libs.versions.toml
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    constraints {
        implementation(libs.bundles.tomcat)
        implementation(libs.jakarta.validation)
        implementation(libs.hibernate.validator)
        implementation(libs.spring.hateoas)
    }
}

//region grpc
protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
    plugins {
        create("grpc") {
            artifact = libs.grpc.protoc.plugin.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}
// endregion

tasks.named<JavaCompile>("compileJava") {
    with(options) {
        isDeprecation = true
        with(compilerArgs) {

            add("-Xlint:all,-serial,-processing,-preview")
        }
    }
}

tasks.named<JavaCompile>("compileTestJava") {
    with(options) {
        isDeprecation = true
        with(compilerArgs) {
            add("-Xlint:all,-serial,-processing,-preview")
        }
    }
}

tasks.named("bootBuildImage", org.springframework.boot.gradle.tasks.bundling.BootBuildImage::class.java) {
    createdDate = "now"

    imageName = "$imagePath/${project.name}:$imageTag"

    environment =
        mapOf(
            "BP_JVM_VERSION" to javaLanguageVersionBuildpacks,
            "BPL_JVM_THREAD_COUNT" to "4",
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to " ",
        )

    imageName = "${imageName.get()}-bellsoft"
    println("Buildpacks: JVM durch   B e l l s o f t   L i b e r i c a   (default)")
}

tasks.named("bootRun", org.springframework.boot.gradle.tasks.run.BootRun::class.java) {
    systemProperty("spring.profiles.active", activeProfiles)
    systemProperty("logging.file.name", "./build/log/application.log")
    systemProperty("server.tomcat.basedir", "build/tomcat")
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeTags = setOf("integration", "unit")
    }

    systemProperty("spring.profiles.active", activeProfiles)
    systemProperty("junit.platform.output.capture.stdout", true)
    systemProperty("junit.platform.output.capture.stderr", true)

    systemProperty("server.tomcat.basedir", "build/tomcat")

    testLogging.showStandardStreams = true
}


checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    isIgnoreFailures = false
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = true
        html.required = true
    }
}

spotbugs {
    toolVersion = libs.versions.spotbugs.get()
}
tasks.named("spotbugsMain", com.github.spotbugs.snom.SpotBugsTask::class.java) {
    reportLevel = com.github.spotbugs.snom.Confidence.LOW
    reports.create("html") { required = true }
    val excludePath = Paths.get("config", "spotbugs", "exclude.xml")
    excludeFilter = file(excludePath)
}


idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
        sourceDirs.add(file("generated/"))
        generatedSourceDirs.add(file("generated/"))
    }
}
