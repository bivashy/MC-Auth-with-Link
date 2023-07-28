package me.mastercapexd.auth.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bivashy.auth.api.management.LibraryManagement;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.byteflux.libby.Library;
import net.byteflux.libby.Library.Builder;
import net.byteflux.libby.LibraryManager;

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
    public LibraryManagement loadLibrary(Library library, Collection<Library> exclusion, boolean loadDependencies) {
        libraryManager.loadLibrary(library);
        if (loadDependencies)
            getLibraries(library, exclusion).forEach(dependency -> loadLibrary(dependency, exclusion, true));
        return this;
    }

    @Override
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    private List<Library> getLibraries(Library library, Collection<Library> exclusion) {
        List<Library> libraries = new ArrayList<>();
        for (String libraryUrl : libraryManager.resolveLibrary(library)) {
            try {
                URL jsonUrl = new URL(libraryUrl.replace(".jar", ".module"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(jsonUrl.openStream()));
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                JsonArray variants = json.getAsJsonArray("variants");

                for (JsonElement variantElement : variants) {
                    JsonObject variant = variantElement.getAsJsonObject();
                    if (!variant.get("name").getAsString().equals("runtimeElements"))
                        continue;
                    JsonArray dependencies = variant.getAsJsonArray("dependencies");
                    for (JsonElement dependencyElement : dependencies) {
                        JsonObject dependency = dependencyElement.getAsJsonObject();
                        String groupId = dependency.get("group").getAsString();
                        String artifactId = dependency.get("module").getAsString();
                        String version = dependency.get("version").getAsJsonObject().get("requires").getAsString();
                        if (exclusion.stream()
                                .anyMatch(
                                        excludedLibrary -> excludedLibrary.getGroupId().equals(groupId) && excludedLibrary.getArtifactId().equals(artifactId)))
                            continue;
                        // TODO: Find another way to resolve dependency type (pom, jar)
                        if (dependency.has("attributes"))
                            continue;
                        Builder libraryDependencyBuilder = Library.builder().groupId(groupId).artifactId(artifactId).version(version);
                        library.getRelocations().forEach(libraryDependencyBuilder::relocate);
                        Library libraryDependency = libraryDependencyBuilder.build();
                        libraries.add(libraryDependency);
                    }
                }
            } catch(IOException ignored) {
            }
        }
        return libraries;
    }
}
