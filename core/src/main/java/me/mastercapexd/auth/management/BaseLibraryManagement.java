package me.mastercapexd.auth.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bivashy.auth.api.management.LibraryManagement;
import com.google.gson.Gson;

import net.byteflux.libby.ExcludedLibrary;
import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.TransitiveLibraryManager;

public class BaseLibraryManagement implements LibraryManagement {

    private static final Gson GSON = new Gson();
    private static final String JDA_VERSION = "5.0.0-beta.11";
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
            .build();
    private final List<String> customRepositories = new ArrayList<>();
    private final List<Library> customLibraries = new ArrayList<>();
    private final TransitiveLibraryManager libraryManager;

    public BaseLibraryManagement(LibraryManager libraryManager) {
        this.libraryManager = TransitiveLibraryManager.wrap(libraryManager);
    }

    @Override
    public void loadLibraries() {
        customRepositories.forEach(libraryManager::addRepository);

        libraryManager.addMavenCentral();
        libraryManager.addJitPack();

        Collection<Library> libraries = new ArrayList<>(customLibraries);

        libraries.forEach(libraryManager::loadLibraryTransitively);
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
    public LibraryManagement loadLibraryTransitively(Library library, ExcludedLibrary... excludedLibraries) {
        libraryManager.loadLibraryTransitively(library, excludedLibraries);
        return this;
    }

    @Override
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

}
