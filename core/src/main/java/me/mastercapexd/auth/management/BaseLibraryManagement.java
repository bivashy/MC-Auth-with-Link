package me.mastercapexd.auth.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bivashy.auth.api.management.LibraryManagement;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

public class BaseLibraryManagement implements LibraryManagement {
    private static final String JDA_VERSION = "5.0.0-beta.11";
    public static final Library JDA_LIBRARY = Library.builder().groupId("net{}dv8tion").artifactId("JDA").version(JDA_VERSION).relocate("", "").build();
    private final List<String> customRepositories = new ArrayList<>();
    private final List<Library> customLibraries = new ArrayList<>();
    private final LibraryManager libraryManager;

    public BaseLibraryManagement(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    @Override
    public void loadLibraries() {
        customRepositories.forEach(libraryManager::addRepository);

        libraryManager.addMavenCentral();
        libraryManager.addJitPack();

        Collection<Library> libraries = new ArrayList<>(customLibraries);

        libraries.forEach(libraryManager::loadLibrary);
    }

    @Override
    public LibraryManagement addCustomRepository(String repository) {
        customRepositories.add(repository);
        return this;
    }

    @Override
    public LibraryManagement addCustomLibrary(Library library) {
        customLibraries.add(library);
        return this;
    }

    @Override
    public LibraryManagement loadLibrary(Library library) {
        libraryManager.loadLibrary(library);
        return this;
    }

    @Override
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
