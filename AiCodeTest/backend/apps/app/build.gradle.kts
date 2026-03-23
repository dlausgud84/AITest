plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.1.jre11")

    // MyBatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")

    // 입력값 검증 (@Valid, @NotBlank, @Size, @Email)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // BCrypt 비밀번호 해싱 (Spring Security 전체 불필요, crypto 모듈만 사용)
    implementation("org.springframework.security:spring-security-crypto")

    // 모듈 의존성
    implementation(project(":modules:common"))
    implementation(project(":modules:menu"))
    implementation(project(":modules:auth"))
    implementation(project(":modules:user"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
