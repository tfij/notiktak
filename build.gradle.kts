import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("groovy")
	kotlin("jvm") version "1.5.20"
	kotlin("plugin.spring") version "1.5.20"
	kotlin("plugin.jpa") version "1.5.20"
}

group = "pl.tfij"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

val versions = mapOf(
	"axon" to "4.5.2",
	"spock" to "2.0-groovy-3.0"
)

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.axonframework:axon-spring-boot-starter:${versions["axon"]}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.spockframework:spock-core:${versions["spock"]}")
	testImplementation("org.spockframework:spock-spring:${versions["spock"]}")
	testImplementation("org.axonframework:axon-test:${versions["axon"]}")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
