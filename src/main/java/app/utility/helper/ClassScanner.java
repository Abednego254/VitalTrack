package app.utility.helper;

import org.reflections.Reflections;
import jakarta.persistence.Entity;
import java.util.Set;

public class ClassScanner {

    public static Set<Class<?>> scanForEntities(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    public static Set<Class<?>> scanForAction(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(app.framework.Action.class);
    }
}