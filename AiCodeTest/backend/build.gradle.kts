plugins {
    java
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.dit"
version = "1.0.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        // 공통 의존성
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

// 앱 프로젝트 설정
project(":apps:app") {
    apply(plugin = "org.springframework.boot")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.1.jre11")
        
        // MyBatis
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
        
        // 모듈 의존성
        implementation(project(":modules:common"))
        implementation(project(":modules:menu"))

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

// 공통 모듈
project(":modules:common") {
    dependencies {
        compileOnly("org.springframework.boot:spring-boot-starter")
    }
}

// 메뉴 도메인 모듈
project(":modules:menu") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation(project(":modules:common"))
    }
}
