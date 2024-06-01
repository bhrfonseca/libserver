package br.pucpr.libserver.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name="LibServer" ,
        type=SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
class SecurityConfig (val jwtTokenFilter: JwtTokenFilter) {
    @Bean
    fun mvc (introspector: HandlerMappingIntrospector ) =
            MvcRequestMatcher.Builder(introspector)

    @Bean
    fun filterChain(security: HttpSecurity, mvc: MvcRequestMatcher.Builder): DefaultSecurityFilterChain =
        security.cors( Customizer.withDefaults() )
                .csrf { it.disable() }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .exceptionHandling { it.authenticationEntryPoint { _, res, ex ->
                    res.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            if (ex.message.isNullOrEmpty()) "UNAUTHORIZED" else ex.message
                    )
                } }
                .headers { it.frameOptions { fo -> fo.disable() } }
                .authorizeHttpRequests { requests ->
                    requests
                            .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET)).permitAll()
                            .requestMatchers(mvc.pattern(HttpMethod.POST, "/book")).permitAll()
                            .requestMatchers(mvc.pattern(HttpMethod.POST, "/book/login")).permitAll()
                            //.requestMatchers(mvc.pattern(HttpMethod.DELETE, "/book")).hasRole("ADMIN")
                            .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/book/**")).hasRole("ADMIN")
                            .anyRequest().authenticated()
                }
                .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
                .build()

    @Bean
    fun corsFilter() = CorsConfiguration().apply {
        addAllowedHeader("*")
        addAllowedOrigin("*")
        addAllowedMethod("*")
    }.let {
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", it)
        }
    }.let { CorsFilter(it) }

}
