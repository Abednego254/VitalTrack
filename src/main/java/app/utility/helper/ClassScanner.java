package app.utility.helper;

import app.framework.DbTable;
import app.framework.PageMenuItem;
import org.reflections.Reflections;

import java.util.Set;

public class ClassScanner {

    public static Set<Class<?>> scanForDbTables(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(DbTable.class);
    }

    public static Set<Class<?>> scanForMenuItem(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(PageMenuItem.class);
    }

    public static Set<Class<?>> scanForAction(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(app.framework.Action.class);
    }
}