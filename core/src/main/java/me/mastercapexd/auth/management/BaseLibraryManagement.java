package me.mastercapexd.auth.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyResolutionException;

import com.bivashy.auth.api.management.LibraryManagement;
import com.google.gson.Gson;

import me.mastercapexd.auth.util.MavenArtifactUtil;
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
            .relocate("com{}squareup{}okio", "com{}bivashy{}auth{}lib{}com{}squareup{}okio")
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
            getLibraries(library, exclusion).forEach(dependency -> loadLibrary(dependency, exclusion, false));
        return this;
    }

    @Override
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    private List<Library> getLibraries(Library library, Collection<Library> exclusion) {
        try {
            RemoteRepository[] repositories = Stream.of(library.getRepositories(), libraryManager.getRepositories())
                    .flatMap(Collection::stream)
                    .map(repository -> MavenArtifactUtil.newDefaultRepository(repository, repository))
                    .toArray(RemoteRepository[]::new);

            return MavenArtifactUtil.findCompileDependencies(library.getGroupId(), library.getArtifactId(), library.getVersion(), repositories)
                    .stream()
                    .filter(ArtifactResult::isResolved)
                    .map(ArtifactResult::getArtifact)
                    .map(artifact -> {
                        Builder libraryDependencyBuilder = Library.builder()
                                .groupId(artifact.getGroupId())
                                .artifactId(artifact.getArtifactId())
                                .version(artifact.getVersion());
                        library.getRelocations().forEach(libraryDependencyBuilder::relocate);
                        return libraryDependencyBuilder.build();
                    })
                    .filter(transitiveLibrary -> exclusion.stream()
                            .noneMatch(excludedLibrary -> excludedLibrary.getGroupId().equals(transitiveLibrary.getGroupId()) &&
                                    excludedLibrary.getArtifactId().equals(transitiveLibrary.getArtifactId())))
                    .collect(Collectors.toList());
        } catch (DependencyResolutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
