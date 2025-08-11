package mg.apprologic.apprologic.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernateModule() {
        return new Hibernate6Module();
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                )
                .modulesToInstall(
                        new Hibernate6Module()
                                .configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false)
                                .configure(Hibernate6Module.Feature.REPLACE_PERSISTENT_COLLECTIONS, true)
                )
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .timeZone(TimeZone.getDefault());
    }
}
