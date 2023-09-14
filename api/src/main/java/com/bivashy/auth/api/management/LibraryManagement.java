package com.bivashy.auth.api.management;

import java.util.Collection;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.transitive.ExcludedDependency;

public interface LibraryManagement {

    void loadLibraries();

    LibraryManagement addCustomRepository(String repository);

    LibraryManagement addCustomLibrary(Library library);

    LibraryManagement loadLibrary(Library library);

    @Deprecated
    default LibraryManagement loadLibrary(Library library, Collection<Library> exclusion) {
        return loadLibrary(library, exclusion, true);
    }

    @Deprecated
    default LibraryManagement loadLibrary(Library library, Collection<Library> exclusions, boolean loadDependencies) {
        Library.Builder libraryBuilder = Library.builder()
                .groupId(library.getGroupId())
                .artifactId(library.getArtifactId())
                .version(library.getVersion())
                .checksum(library.getChecksum())
                .isolatedLoad(library.isIsolatedLoad())
                .classifier(library.getClassifier())
                .resolveTransitiveDependencies(loadDependencies);
        library.getRepositories().forEach(libraryBuilder::repository);
        library.getRelocations().forEach(libraryBuilder::relocate);
        exclusions.stream()
                .map(exclusion -> new ExcludedDependency(exclusion.getGroupId(), exclusion.getArtifactId()))
                .forEach(libraryBuilder::excludeTransitiveDependency);
        loadLibrary(library);
        return this;
    }

    LibraryManager getLibraryManager();

}
