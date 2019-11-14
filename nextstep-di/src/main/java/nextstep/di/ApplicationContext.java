package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(Class<?>... classes) {
        Object[] basePackages = Arrays.stream(classes)
                .map(this::getBasePackages)
                .toArray();

        beanFactory = new BeanFactory(Arrays.asList(
                new ConfigurationScanner(basePackages),
                new ComponentScanner(basePackages)));
        beanFactory.initialize();
    }

    private Object[] getBasePackages(Class<?> clazz) {
        ComponentScan scan = clazz.getAnnotation(ComponentScan.class);
        Object[] basePackages = scan.basePackages();
;
        return basePackages.length == 0 ? new Object[]{getPackageName(clazz)} : basePackages;
    }

    private String getPackageName(Class<?> root) {
        return root.getPackage().getName();
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Set<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }
}
