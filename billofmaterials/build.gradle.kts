plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:2.6.15"))
    constraints {
        api("org.springdoc:springdoc-openapi-ui:1.6.15")
        api("org.projectlombok:lombok:1.18.20")
        api("com.google.guava:guava:32.0.1-jre")
        api("org.mapstruct:mapstruct:1.5.5.Final")
        api("org.mapstruct:mapstruct-processor:1.5.5.Final")
        api("org.junit.jupiter:junit-jupiter-api:5.9.2")
        api("org.junit.jupiter:junit-jupiter-engine:5.9.2")
        api("com.h2database:h2:2.2.224")
    }
}
