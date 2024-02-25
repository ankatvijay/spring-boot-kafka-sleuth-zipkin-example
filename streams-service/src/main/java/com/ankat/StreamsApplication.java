package com.ankat;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@EnableKafkaStreams
@SpringBootApplication
public class StreamsApplication {


    public static void main(String[] args) {
        SpringApplication.run(StreamsApplication.class,args);
        //SpringApplication application = new SpringApplication(StreamsApplication.class);
        //application.setBannerMode(Banner.Mode.OFF);
        //System.exit(SpringApplication.exit(application.run(args)));
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }

    /*
    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 42;
    }
    */
}
