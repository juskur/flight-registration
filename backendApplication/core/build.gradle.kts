plugins {
    id("java")
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.google.guava:guava")
    implementation("org.mapstruct:mapstruct")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor")

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
