package org.github.udaracamarasinghe.simpledbeditorwebui.configs;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * 
 * @author Udara Amarasinghe
 */
@Configuration
@ComponentScan(basePackages = "lk.dialog.ccs.crmbackwebcom.configctrl")
@PropertySource("classpath:/application.properties")
public class ModuleConfigs {

	// @Override
	// public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void addCorsMappings(CorsRegistry arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void addFormatters(FormatterRegistry arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void addInterceptors(InterceptorRegistry arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// System.out.println("Kllllll");
	// registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(3600).resourceChain(true)
	// .addResolver(new GzipResourceResolver()).addResolver(new
	// PathResourceResolver());
	// }
	//
	// @Override
	// public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler>
	// arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void addViewControllers(ViewControllerRegistry arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureDefaultServletHandling(DefaultServletHandlerConfigurer
	// arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver>
	// arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configurePathMatch(PathMatchConfigurer arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void configureViewResolvers(ViewResolverRegistry arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver>
	// arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public MessageCodesResolver getMessageCodesResolver() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Validator getValidator() {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
