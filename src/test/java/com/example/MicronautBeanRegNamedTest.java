package com.example;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanRegistration;
import io.micronaut.inject.BeanIdentifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(rebuildContext = true)
class MicronautBeanRegNamedTest {

    @Inject
    private ApplicationContext applicationContext;

    @Test
    void testWorking() {
        var service = applicationContext.getBean(WorkingService.class);

        assertThat(service.getRegistrations())
            .map(BeanRegistration::getIdentifier)
            .map(BeanIdentifier::getName)
            .contains("first-bean", "second-bean");
    }

    @Test
    void testBroken() {
        var service = applicationContext.getBean(BrokenService.class);

        assertThat(service.getRegistrations())
            .map(BeanRegistration::getIdentifier)
            .map(BeanIdentifier::getName)
            .contains("first-bean", "second-bean");
    }

    interface SomeBean {

    }

    @Named("first-bean")
    @Singleton
    static class FirstBean implements SomeBean {}

    @Named("second-bean")
    @Singleton
    static class SecondBean implements SomeBean {}

    @Singleton
    static class WorkingService {
        private final Collection<BeanRegistration<SomeBean>> registrations;

        WorkingService(Collection<BeanRegistration<SomeBean>> registrations) {
            this.registrations = registrations;
        }

        public Collection<BeanRegistration<SomeBean>> getRegistrations() {
            return registrations;
        }
    }

    @Singleton
    static class BrokenService {
        private final Collection<BeanRegistration<SomeBean>> registrations;

        BrokenService(FirstBean firstBean, Collection<BeanRegistration<SomeBean>> registrations, SecondBean secondBean) {
            this.registrations = registrations;
        }

        public Collection<BeanRegistration<SomeBean>> getRegistrations() {
            return registrations;
        }
    }

}
