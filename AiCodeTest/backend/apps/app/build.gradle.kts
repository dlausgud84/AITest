plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.4.1.jre11")
    
    // MyBatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
    
    // 모듈 의존성
    implementation(project(":modules:common"))
    implementation(project(":modules:menu"))
    implementation(project(":modules:auth"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
