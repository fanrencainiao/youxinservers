package com.youxin.app;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import com.youxin.app.filter.AuthorizationFilterProperties;
import com.youxin.app.utils.applicationBean.AliPayConfig;
import com.youxin.app.utils.applicationBean.SmsConfig;
import com.youxin.app.utils.applicationBean.WxConfig;
import com.youxin.app.utils.applicationBean.YeePayConfig;



@Configuration
@SpringBootApplication(
	    scanBasePackages = {"com"}
	)
@EntityScan("com.youxin.app.entity")
@ComponentScan(basePackages ="com.youxin.app")
//@ComponentScan({ "com.youxin"})
@EnableConfigurationProperties(value = { AuthorizationFilterProperties.class, SmsConfig.class,AliPayConfig.class,WxConfig.class,YeePayConfig.class})
@ServletComponentScan
public class YouxinApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouxinApplication.class, args);
	}
	
	
	@Bean
	public RequestContextListener requestContextListener(){
		    return new RequestContextListener();
		} 

}
