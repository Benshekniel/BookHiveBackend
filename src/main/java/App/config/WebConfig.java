package App.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Optional: Keep local filesystem for fallback
        registry.addResourceHandler("/Users/**")
                .addResourceLocations("file:/path/to/bookhive-static/Users/")
                .setCachePeriod(0);
        registry.addResourceHandler("/Users/**")
                .addResourceLocations("classpath:/static/Users/")
                .setCachePeriod(3600);
    }
}

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Map /users/** to static/users/ and all its subfolders
//        registry.addResourceHandler("/Users/**")
//                .addResourceLocations("classpath:/static/Users/");
//    }
//}
