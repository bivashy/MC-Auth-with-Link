package me.mastercapexd.auth.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alessiodp.libby.Library;
import com.alessiodp.libby.LibraryManager;
import com.bivashy.auth.api.management.LibraryManagement;

public class BaseLibraryManagement implements LibraryManagement {

    private static final String JDA_VERSION = "5.0.0-beta.20";
    public static final Library JDA_LIBRARY = Library.builder()
            .groupId("net{}dv8tion")
            .artifactId("JDA")
            .version(JDA_VERSION)
            .relocate("net{}dv8tion", "com{}bivashy{}auth{}lib{}net{}dv8tion")
            .relocate("com{}iwebpp", "com{}bivashy{}auth{}lib{}com{}iwebpp")
            .relocate("org{}apache{}commons", "com{}bivashy{}auth{}lib{}org{}apache{}commons")
            .relocate("com{}neovisionaries{}ws", "com{}bivashy{}auth{}lib{}com{}neovisionaries{}ws")
            .relocate("com{}fasterxml{}jackson", "com{}bivashy{}auth{}lib{}com{}fasterxml{}jackson")
            .relocate("org{}slf4j", "com{}bivashy{}auth{}lib{}org{}slf4j")
            .relocate("gnu{}trove", "com{}bivashy{}auth{}gnu{}trove")
            .relocate("okhttp3", "com{}bivashy{}auth{}lib{}okhttp3")
            .relocate("com{}squareup{}okio", "com{}bivashy{}auth{}lib{}com{}squareup{}okio")
            .resolveTransitiveDependencies(true)
            .excludeTransitiveDependency("club{}minnced", "opus-java")
            .build();
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
    public LibraryManagement loadLibrary(Library library) {
        libraryManager.loadLibrary(library);
        return this;
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
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

}
