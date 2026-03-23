dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")        // @JsonIgnore (Jackson)
    implementation("org.springframework.boot:spring-boot-starter-validation") // @NotBlank, @Size
    implementation("org.springframework.security:spring-security-crypto")     // BCryptPasswordEncoder
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    implementation(project(":modules:common"))
}
