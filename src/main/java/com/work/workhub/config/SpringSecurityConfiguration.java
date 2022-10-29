package com.work.workhub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.work.workhub.admin.member.model.service.AdminMemberService;
import com.work.workhub.handler.LoginFailHandler;
import com.work.workhub.member.member.service.MemberService;



/* 스프링 시큐리티 설정 활성화 + bean 등록 가능 */
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final AdminMemberService adminMemberService;
	private final PasswordEncoder passwordEncoder;
	private final MemberService memberService;
	
   
	@Autowired
	public SpringSecurityConfiguration(AdminMemberService adminMemberService, MemberService memberService, PasswordEncoder passwordEncoder) {
		this.adminMemberService = adminMemberService;
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}

	
	/* 암호화에 사용할 객체 BCryptPasswordEncoder bean 등록 - ContextConfiguration */

	/* 정적 리소스는 권한이 없어도 요청 가능하게 무시할 경로를 작성한다 */
	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/vendors/**", "/uploadFiles/**");
		web.ignoring().antMatchers("/assets/*/**", "/uploadFiles/**");
	}
	

	/* HTTP 요청에 대한 설정 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/* fileUpload를 위한 EncodingFilter 적용 */
		/* 참고 - http://millky.com/@origoni/post/1057 */
		
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		
		filter.setEncoding("UTF-8");
		
		filter.setForceEncoding(true);

		http.addFilterBefore(filter,CsrfFilter.class);

	    
		
		http
			/* CSRF(Cross-Site Request Forgery)
			 * 웹 애플리케이션 취약점 중 하나로 사용자의 의지와 무관하게 공격자가 의도한 행위(등록/수정/삭제)를 
			 * 특정 웹 사이트에 요청하도록 만드는 공격 
			 * 
			 * CSRF 공격 방어를 위해 referrer 검증 -> 요청 도메인이 일치하는지 검증
			 * Spring Security CSRF Token 사용 
			 * -> 임의의 토큰 발급 후 자원에 대한 변경 요청일 경우 Token 확인하여 클라이언트가 정상적인 요청을 보낸 것인지 확인
			 * 만약 CSRF Token이 존재하지 않거나 기존의 Token과 일치하지 않는 경우 4XX 상태 코드를 반환
			 * */
			.csrf().disable()	/* csrf는 기본적으로 활성화 되어 있으므로 비활성화 처리 */
			/* 요청에 대한 권한 체크 */
			.authorizeHttpRequests()
//				.antMatchers("/approval/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/approval/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/message/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/message/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/reserve/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/reserve/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/calendar/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/calendar/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/post/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/calendar/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/survey/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/calendar/**").hasRole("ROLE_MEMBER")
//				.antMatchers("/admin/**").authenticated()
//				.antMatchers(HttpMethod.GET, "/admin/**").hasRole("ROLE_ADMIN")
				/*
				 * hasRole("ROLE_MEMBER") 사용 안함 : ROLE_ADMIN인 경우에는 접근이 불가능해지므로 / authenticated()만 적용하여 ROLE 무관하게 로그인한 경우 접근 가능하도록 함
				 * hasRole("ROLE_ADMIN")만 사용 : ROLE_ADMIN이면 접근 / ROLE_MEMBER인 경우에는 접근 불가능해야 하므로
				 */
				.antMatchers("/", "/member/login").permitAll()
				.antMatchers("/admin/**").authenticated()
				.antMatchers(HttpMethod.GET, "/admin/**").hasRole("ROLE_ADMIN")
				.antMatchers("/asset/**").authenticated()
				.antMatchers(HttpMethod.GET, "/asset/**").hasRole("ROLE_ADMIN")
				.antMatchers("/post/**").authenticated()
				.antMatchers(HttpMethod.GET, "/post/**").hasRole("ROLE_ADMIN")
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/")
				.loginProcessingUrl("/member/login")
				.defaultSuccessUrl("/myPage/myPage", true)
				.failureHandler(loginFailHandler())
			.and()
				.logout()
				/* 로그아웃 주소 */
				.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				/* JSESSIONID 쿠키 삭제 */
				.deleteCookies("JSESSIONID")
				/* 세션 만료 */
				.invalidateHttpSession(true)
				/* 성공 시 랜딩 페이지 */
				.logoutSuccessUrl("/")
			.and()
				.sessionManagement()
				.maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/")
			.and()
				.and().rememberMe()
				.alwaysRemember(true)
				.tokenValiditySeconds(1800);
			
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
	}
	
	/* 로그인 실패 핸들러 bean 등록 */
	@Bean
	public LoginFailHandler loginFailHandler() {
		
		return new LoginFailHandler();
		
	}
	
	
	
	
}

