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
import java.lang.reflect.Parameter;
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
            Object appComponent;
            int parameterCount = appComponentMethod.getParameterCount();
            if (parameterCount > 0) {
                List<Object> arguments = new ArrayList<>();
                Parameter[] parameters = appComponentMethod.getParameters();
                for (Parameter parameter : parameters) {
                    getComponentByTypeName(parameter.getType().getName()).ifPresent(arguments::add);
                }
                appComponent = appComponentMethod.invoke(configObject, arguments.toArray());
            } else {
                appComponent = appComponentMethod.invoke(configObject);
            }
            appComponents.add(appComponent);
            appComponentsByName.put(getAppComponentName(appComponentMethod), appComponent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("error during execution of appComponentMethod - {}", appComponentMethod.getName());
            throw new RuntimeException(e);
        }
    }

    private Optional<Object> getComponentByTypeName(String typeName) {
        return appComponents.stream()
                .filter(component -> containsClassOrInterfaceWithName(typeName, component))
                .findFirst();
    }

    private boolean containsClassOrInterfaceWithName(String typeName, Object component) {
        Class<?> componentClass = component.getClass();
        Class<?>[] interfaces = componentClass.getInterfaces();
        return componentClass.getName().equals(typeName) || Arrays.stream(interfaces)
                .map(Class::getName)
                .anyMatch(interfaceName -> interfaceName.equals(typeName));
    }

    private String getAppComponentName(Method appComponentMethod) {
        return appComponentMethod.getAnnotation(AppComponent.class).name();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        // find in the list of objects, object with the same class or implemented interface
        return (C) getComponentByTypeName(componentClass.getName()).orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        // find in componentMap
        return (C) appComponentsByName.get(componentName);
    }
}
