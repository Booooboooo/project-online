package com.nais.kid.keeper.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nais.kid.keeper.domain.User;
import com.nais.kid.utils.AuthorityContext;
import com.nais.kid.utils.SessionUtils;




@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter{
	
	public final static String SESSION_KEY = "tag";
	
	@Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**").addResourceLocations("classpath:/templates/image/");
		registry.addResourceHandler("/images/home/**").addResourceLocations("classpath:/templates/images/home/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/templates/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/templates/js/");
		registry.addResourceHandler("/temp/**").addResourceLocations("classpath:/templates/temp/");

		super.addResourceHandlers(registry);
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		MappingFastJsonHttpMessageConverter converter = new MappingFastJsonHttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_HTML));
		SerializerFeature[] sfarray = {SerializerFeature.WriteMapNullValue, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteDateUseDateFormat};
		converter.setSerializerFeature(sfarray);
		converter.setCharset("UTF-8");
		converters.add(converter);
		
	};
	
	
	private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
        		HttpSession session = request.getSession();
//            String s = (String)session.getAttribute(SESSION_KEY);
       
            String s = "admin";
            String username = "admin";

            if(!StringUtils.isEmpty(s)) {
//            	username = SessionUtils.Base64Decode(s);
            	User user = new User();
            	user.setUsername(username);
            	user.setId(100L);
        	    AuthorityContext.setUser(user);
        	    return true;
            }
            
            String url = "/login";
            response.sendRedirect(url);
            return false;
        }
   
        @Override
	    	public void postHandle(
	    			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	    			throws Exception {
	    				
	    	}
	    	
	    	
	    	@Override
	    	public void afterCompletion(
	    			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	    			throws Exception {
	    		
	    		AuthorityContext.clearUser();
	    	}
	}
	
	
	

}

