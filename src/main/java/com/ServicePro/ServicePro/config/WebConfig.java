package com.ServicePro.ServicePro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {

	// Método que configura usuários e escopo de atuação no sistema teste
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder)throws Exception{
		
		builder
		.inMemoryAuthentication()
		.withUser("ruan").password("{noop}ruan").roles("USER")
		.and().withUser("root").password("{noop}root").roles("ADMIN");
		
	}
	
	// Método que configura quais seções do site podem ser acessadas com e sem login
	protected void configure(HttpSecurity http) throws Exception{
		
		http
		.authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/requerimentos").authenticated()
				.antMatchers("/requerimentosSALA").authenticated()
				.antMatchers("/requerimentosProj").authenticated()
		.antMatchers("/cadastrarFuncionario").authenticated()
		.antMatchers("/funcionarios").authenticated()
		
		.antMatchers("/**").permitAll().anyRequest()
		.authenticated().and().formLogin().permitAll()
		.and().csrf().disable();
	}

	
}