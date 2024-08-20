package com.math.cleanarchex.infra.driven.blockchainProvider.chain.config;

import com.math.cleanarchex.infra.driven.blockchainProvider.chain.settings.NodeSettings;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

@Setter
public class BlockchainServiceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
        final NodeSettings nodeSettings = getNodeSettings();

        nodeSettings.getNodes().forEach((name, node) ->
                getNodeBeanRegistratioStrategy(nodeSettings).register(node, registry));
    }

    protected NodeBeanRegistrationStrategy getNodeBeanRegistratioStrategy(NodeSettings nodeSettings) {
        return new NodeBeanRegistrationStrategy(nodeSettings, new OkHttpClient());

    }

    protected NodeSettings getNodeSettings() {
        return new NodeSettings(environment);
    }
}
