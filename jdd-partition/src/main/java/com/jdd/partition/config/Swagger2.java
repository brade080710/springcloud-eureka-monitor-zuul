package com.jdd.partition.config;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



    /**
    * @ClassName: Swagger2
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author liujunwei
    * @date 2018年6月22日
    *
    */
    
@Configuration
@EnableSwagger2
@RefreshScope
public class Swagger2 {
	@Value("${jdd.swaggerShow}")
    private Boolean swaggerShow;
	private static final String splitor = ";";
	@Bean
    public Docket api() {
        List<Parameter> pars = new ArrayList<Parameter>();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
                .select()
                .apis(basePackage("com.jdd.partition.app.rest"+splitor+"com.jdd.partition.client.rest"+splitor+"com.jdd.partition.wx.rest"))
                .paths(PathSelectors.any())
                .build()
                .enable(swaggerShow);
        return docket;
    }
	private Predicate<RequestHandler> basePackage(final String basePackage) {
		return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
	}
	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("springboot利用swagger构建api文档")
				.description("简单优雅的restfun风格")
				.termsOfServiceUrl("http://localhost:8082/icbc-server/swagger-ui.html")
				.version("1.0")
				.build();
	}
}