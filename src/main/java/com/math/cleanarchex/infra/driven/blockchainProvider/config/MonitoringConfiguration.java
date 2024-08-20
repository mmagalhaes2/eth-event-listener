package com.math.cleanarchex.infra.driven.blockchainProvider.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.EventeumValueMonitor;
import com.math.cleanarchex.infra.driven.blockchainProvider.monitoring.MicrometerValueMonitor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MonitoringConfiguration {

    public static class PrometheusConfiguration {

        @Bean
        @ConditionalOnProperty(name = "management.endpoint.metrics.enabled", havingValue = "true")
        public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(Environment environment) {
            return registry -> registry.config().commonTags("application", "Eventeum", "environment", getProfileName(environment));
        }

        @Bean
        @ConditionalOnProperty(name = "management.endpoint.metrics.enabled", havingValue = "true")
        public EventeumValueMonitor eventeumValueMonitor(MeterRegistry meterRegistry) {
            return new MicrometerValueMonitor(meterRegistry);
        }

        @Bean
        @ConditionalOnProperty(name = "management.endpoint.prometheus.enabled", havingValue = "true")
        public PrometheusMeterRegistry.Config configurePrometheus(MeterRegistry meterRegistry) {
            return meterRegistry.config().namingConvention(new CustomNamingConvention());
        }

        private String getProfileName(Environment environment) {
            if (environment.getActiveProfiles() == null || environment.getActiveProfiles().length == 0) {
                return "Default";
            }

            return environment.getActiveProfiles()[0];
        }

    }

    @ConditionalOnProperty(value = "management.endpoint.metrics.enabled", havingValue = "false", matchIfMissing = true)
    public static class DoNothingMonitoringConfiguration {

        @Bean
        public EventeumValueMonitor eventeumValueMonitor() {
            return new EventeumValueMonitor() {

                @Override
                public <T extends Number> T monitor(String name, String node, T number) {
                    return number;
                }
            };
        }
    }
}
