package com.bivashy.auth.api.management;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

public interface LibraryManagement {
    void loadLibraries();

    LibraryManagement addCustomRepository(String repository);

    LibraryManagement addCustomLibrary(Library library);

    LibraryManagement loadLibrary(Library library);

    LibraryManager getLibraryManager();
}
