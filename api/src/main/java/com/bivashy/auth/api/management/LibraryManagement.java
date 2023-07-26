package com.bivashy.auth.api.management;

import java.util.Collection;
import java.util.Collections;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

public interface LibraryManagement {
    void loadLibraries();

    LibraryManagement addCustomRepository(String repository);

    LibraryManagement addCustomLibrary(Library library);

    default LibraryManagement loadLibrary(Library library) {
        return loadLibrary(library, Collections.emptyList(), true);
    }

    default LibraryManagement loadLibrary(Library library, Collection<Library> exclusion) {
        return loadLibrary(library, exclusion, true);
    }

    LibraryManagement loadLibrary(Library library, Collection<Library> exclusion, boolean loadDependencies);

    LibraryManager getLibraryManager();
}
