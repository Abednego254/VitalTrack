package app.utility.helper;

import app.framework.DbTable;
import app.framework.PageMenuItem;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class ClassScanner {

    @PostConstruct
    public void initialize(){
        System.out.println("==========The class scanner has been " +
            "initialised....");
    }

    public Set<Class<?>> scanForDbTables(String basePackage) {

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(DbTable.class);

        return new HashSet<>(annotatedClasses);
    }

    public Set<Class<?>> scanForMenuItem(String basePackage) {

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> annotatedClasses =
                reflections.getTypesAnnotatedWith(PageMenuItem.class);

        return new HashSet<>(annotatedClasses);
    }

    @PreDestroy
    public void destroy(){
        System.out.println("////////////...Instance clean up....");
    }
}