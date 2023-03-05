package me.mastercapexd.auth.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class DriverUtil {
    private DriverUtil() {
    }

    public static boolean loadDriver(File driverPath, ClassLoader classLoader) {
        try {
            return loadDriver(driverPath.toURI().toURL(), classLoader);
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loadDriver(URL driverUrl, ClassLoader classLoader) {
        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{driverUrl}, classLoader);
            ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class, urlClassLoader);
            for (Driver driver : drivers) {
                Driver newDriver = (Driver) Class.forName(driver.getClass().getName(), true, urlClassLoader).getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DelegatingDriver(newDriver)); // register using the Delegating Driver
            }
            return true;
        } catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    static class DelegatingDriver implements Driver {
        private final Driver driver;

        public DelegatingDriver(Driver driver) {
            if (driver == null) {
                throw new IllegalArgumentException("Driver must not be null.");
            }
            this.driver = driver;
        }

        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }
}
