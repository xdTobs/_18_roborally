package dk.dtu.compute.se.pisd.roborally;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@EnableWebMvc
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customGsonHttpMessageConverter());
    }

    private GsonHttpMessageConverter customGsonHttpMessageConverter() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(IFieldAction.class, new Adapter<IFieldAction>())
                .create();

        GsonHttpMessageConverter gsonMessageConverter = new GsonHttpMessageConverter();
        gsonMessageConverter.setGson(gson);
        return gsonMessageConverter;
    }
}