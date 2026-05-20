package com.app.quantitymeasurement.config;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementCacheRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
public class RepositoryConfig {

    @Bean(name = "uc16Repository")
    @ConditionalOnMissingBean
    public QuantityMeasurementRepository uc16Repository() {
        return QuantityMeasurementCacheRepository.getInstance();
    }

    @Bean
    @Primary
    public QuantityMeasurementRepository uc17Repository(
            QuantityMeasurementRepository jpaRepo) {
        return jpaRepo;
    }
}