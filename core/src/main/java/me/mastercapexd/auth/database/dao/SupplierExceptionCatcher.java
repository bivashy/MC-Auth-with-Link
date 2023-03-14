package me.mastercapexd.auth.database.dao;

public class SupplierExceptionCatcher {
    public <T, V extends Throwable> T execute(SupplierWithException<T, V> supplierWithException, T def) {
        try {
            return supplierWithException.get();
        } catch(Throwable e) {
            processException(e);
            return def;
        }
    }

    public <T, V extends Throwable> T execute(SupplierWithException<T, V> supplierWithException) {
        return execute(supplierWithException, null);
    }

    public void processException(Throwable throwable) {
        throwable.printStackTrace();
    }

    public interface SupplierWithException<T, V extends Throwable> {
        T get() throws V;
    }
}
