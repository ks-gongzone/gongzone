package com.gongzone.central.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${files.path}")
	private String fileRealPath;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("https://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedOriginPatterns("*")
				.allowedHeaders("*")
				.exposedHeaders("token-expired")
				.allowCredentials(true);
	}


	/**
	 * 주소요청에 따른 외부파일 경로 설정
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String rootPath = getOSFilePath();
		System.out.println(rootPath + fileRealPath + "attachement/");
		registry.addResourceHandler("/api/attachement/**")
				.addResourceLocations(rootPath + fileRealPath + "attachement/")
				.setCachePeriod(3600)
				.resourceChain(true)
				.addResolver(new PathResourceResolver());
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	public String getOSFilePath() {
		String rootPath = "file:///";
		String os = System.getProperty("os.name").toLowerCase();

		if(os.contains("win")) {
			rootPath = "file:///d:";
		}else if(os.contains("linux")) {
			rootPath = "file:///";
		}else if(os.contains("mac")){
			rootPath = "file:///Users/Shared";
		}
		return rootPath;
	}
}
