package propostas.microservice.jaegar;

import feign.Client;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Client.class)
@ConditionalOnBean(Tracer.class)
@AutoConfigureAfter(TracerAutoConfiguration.class)
@AutoConfigureBefore(name = "org.springframework.cloud.openfeign.FeignAutoConfiguration")
@ConditionalOnProperty(name = "opentracing.spring.cloud.feign.enabled", havingValue = "true", matchIfMissing = true)
public class FeignTracingAutoConfiguration {


}
