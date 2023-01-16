package th.co.truecorp.catalogue.prepaid;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@SpringBootApplication
public class PrepaidCatalogueApplication {
    
//        @Override
//        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//            return application.sources(PrepaidCatalogueApplication.class);
//        }

	public static void main(String[] args) {
		SpringApplication.run(PrepaidCatalogueApplication.class, args);
	}
        
        @Bean
        public WebClient createWebClient() throws SSLException {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
            return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
        }
}
