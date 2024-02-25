package me.mastercapexd.auth.database;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.bivashy.auth.api.AuthPlugin;

import me.mastercapexd.auth.util.InheritableClassLoader;

public class IsolatedDatabaseHelperFactory {

    private final ClassLoader parentClassLoader;
    private final InheritableClassLoader classLoader;

    public IsolatedDatabaseHelperFactory(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
        this.classLoader = new InheritableClassLoader(parentClassLoader);
    }

    // TODO: Replace DatabaseHelper with interface to avoid ClassCastException
    public DatabaseHelper create(AuthPlugin authPlugin) {
        try {
            return tryToCreate(authPlugin);
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace(); // TODO: Replace with more robust logging
            return null;
        }
    }

    private DatabaseHelper tryToCreate(
            AuthPlugin authPlugin) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String className = DatabaseHelper.class.getName();
        String classPath = '/' + className.replace('.', '/') + ".class";
        Class<?> databaseHelperClass = classLoader.defineClass(className, parentClassLoader.getResourceAsStream(classPath));
        Constructor<?> databaseHelperConstructor = databaseHelperClass.getConstructor(AuthPlugin.class, ClassLoader.class);
        return (DatabaseHelper) databaseHelperConstructor.newInstance(authPlugin, classLoader);
    }

}
