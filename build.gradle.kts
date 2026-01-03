plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.eastwoo.study"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    implementation("com.jsoniter:jsoniter:0.9.23")
    implementation("com.dslplatform:dsl-json:1.10.0")
    implementation("com.dslplatform:dsl-json-java8:1.10.0")  // runtime 지원
    implementation("org.json:json:20231013")

    jmh("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    jmh("com.jsoniter:jsoniter:0.9.23")
    jmh("com.dslplatform:dsl-json:1.10.0")
    jmh("com.dslplatform:dsl-json-java8:1.10.0")
    jmh("org.json:json:20231013")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

jmh {
    includes.set(listOf(".*JsonBenchmarkLarge.*"))
    resultFormat.set("JSON")
    fork.set(2)
    warmupIterations.set(5)
    iterations.set(10)
    timeUnit.set("ms")
}