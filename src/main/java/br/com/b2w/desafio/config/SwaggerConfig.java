package br.com.b2w.desafio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe responsável por configurar o swagger afim de documentar os serviços Rest expostos.
 * 
 * @author Bruno Galvao<brunogalvaods@gmail.com>
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
	/**
	 * Método reponsável por configurar o pacote onde estão localizados os serviços Rest.
	 * 
	 * @return objeto com as configurações do swagger. 
	 */
	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.b2w.desafio.api"))
                .build()
                .apiInfo(metaData());
    }
    
	/**
	 * Método reponsável por customizar as informações que irão aparecer na página inicial do swagger.
	 * 
	 * @return objeto com as informações customizadas.
	 */
	@SuppressWarnings("deprecation")
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("Desafio B2W REST API",
				"Desafio B2W REST API", "1.0",
				"Termos de serviço",
				"Bruno Galvão",
				"OLX License Version 1.0",
				"http://localhost:8080/licenses/LICENSE-1.0");
        return apiInfo;
    }
    
}