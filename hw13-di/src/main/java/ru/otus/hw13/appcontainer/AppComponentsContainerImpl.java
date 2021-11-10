package ru.otus.hw13.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.reflections.ReflectionUtils.getMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        processConfig(Set.of(configClasses));
    }

    public AppComponentsContainerImpl(String packageName) {
        Set<Class<?>> configClasses = getConfigClassesFromPackage(packageName);
        processConfig(configClasses);
    }

    private Set<Class<?>> getConfigClassesFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(), new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
    }

    private void processConfig(Set<Class<?>> configClasses) {
        Objects.requireNonNull(configClasses, "Set of config classes should not be null");
        configClasses.stream().sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(configClass -> {
                    checkConfigClass(configClass);
                    Object configObject = instantiateConfigClass(configClass);
                    Set<Method> appComponentMethods = getMethods(configClass, withAnnotation(AppComponent.class));
                    appComponentMethods.stream()
                            .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                            .forEach(appComponentMethod -> instantiateAppComponent(appComponentMethod, configObject));
                });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object instantiateConfigClass(Class<?> configClass) {
        try {
            Constructor<?> defaultConstructor = configClass.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            log.error("error while trying to create instance of config object");
            throw new RuntimeException(e);
        }
    }

    private void instantiateAppComponent(Method appComponentMethod, Object configObject) {
        try {
            Class<?>[] parameterTypes = appComponentMethod.getParameterTypes();
            Object[] components = getComponentsByTypes(parameterTypes);
            Object appComponent = appComponentMethod.invoke(configObject, components);
            addComponentIfAbsent(appComponentMethod, appComponent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("error during execution of appComponentMethod - {}", appComponentMethod.getName());
            throw new RuntimeException(e);
        }
    }

    private Object[] getComponentsByTypes(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(this::getComponentByType)
                .map(Optional::orElseThrow)
                .toArray();
    }

    private Optional<Object> getComponentByType(Class<?> parameterType) {
        return appComponents.stream()
                .filter(component -> parameterType.isAssignableFrom(component.getClass()))
                .findFirst();
    }

    private String getAppComponentName(Method appComponentMethod) {
        return appComponentMethod.getAnnotation(AppComponent.class).name();
    }

    private void addComponentIfAbsent(Method appComponentMethod, Object appComponent) {
        String appComponentName = getAppComponentName(appComponentMethod);
        if (!appComponentsByName.containsKey(appComponentName)) {
            appComponentsByName.put(appComponentName, appComponent);
            appComponents.add(appComponent);
        } else {
            throw new IllegalArgumentException(String.format("trying to add component with the same name - %s", appComponentMethod.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        // find in the list of objects, object with the same class or implemented interface
        return (C) getComponentByType(componentClass).orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        // find in componentMap
        return (C) appComponentsByName.get(componentName);
    }
}
