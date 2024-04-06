package com.bivashy.auth.asset.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarResourcesTest extends ResourcesTest {
    public JarResourcesTest() throws MalformedURLException {
        super(new URLClassLoader(new URL[]{new URL("jar", "", "test/test.jar!/")}));
    }
}
