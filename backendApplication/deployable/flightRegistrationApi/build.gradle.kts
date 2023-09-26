plugins {
    id("java")
    id("org.springframework.boot")
}

dependencies {
    //All versions are listed in the bill of materials
    compileOnly(platform(project(":billofmaterials")))
    runtimeOnly(platform(project(":billofmaterials")))
    annotationProcessor(platform(project(":billofmaterials")))
    testCompileOnly(platform(project(":billofmaterials")))
    testImplementation(platform(project(":billofmaterials")))
    testRuntimeOnly(platform(project(":billofmaterials")))

    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.google.guava:guava")
    implementation("org.mapstruct:mapstruct")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    implementation(project(":backendApplication:core"))
    implementation(project(":backendApplication:presentation:rest"))

    testCompileOnly("org.projectlombok:lombok")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("com.h2database:h2")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.create<Exec>("dockerBuild") {
    group = "docker"
    description = "Build and tag docker image"
    dependsOn("bootJar")
    mustRunAfter("bootJar")
    val args = mutableListOf<String>()
    args.add("docker")
    args.add("image")
    args.add("build")
    args.add("-t")
    args.add("edu/flightregistration:latest")
    args.add("-f")
    args.add("Dockerfile")
    args.add(".")
    commandLine = args
}

tasks.create<Exec>("dockerRun") {
    group = "docker"
    description = "Run docker image build by command dockerBuild"
    mustRunAfter("dockerBuild")
    val args = mutableListOf<String>()
    args.add("docker")
    args.add("run")
    args.add("-d")
    args.add("--name")
    args.add("flightregistration")
    args.add("-p")
    args.add("8080:8080")
    args.add("edu/flightregistration:latest")
    commandLine = args
}

tasks.create<Exec>("dockerStop") {
    group = "docker"
    description = "Stop docker container created by dockerRun command"
    val args = mutableListOf<String>()
    args.add("docker")
    args.add("stop")
    args.add("flightregistration")
    commandLine = args
}

tasks.create<Exec>("dockerRemoveContainer") {
    group = "docker"
    description = "Remove docker container created by dockerRun command"
    mustRunAfter("dockerStop")
    val args = mutableListOf<String>()
    args.add("docker")
    args.add("rm")
    args.add("flightregistration")
    commandLine = args
}

tasks.create("dockerStopAndRemoveContainer") {
    group = "docker"
    description = "Stop and remove docker container created by dockerRun command"
    dependsOn("dockerStop", "dockerRemoveContainer")
}

tasks.create("buildAndRun") {
    group = "all-in-one"
    description = "Build and run solution"
    dependsOn("bootJar", "dockerBuild", "dockerRun")
}
