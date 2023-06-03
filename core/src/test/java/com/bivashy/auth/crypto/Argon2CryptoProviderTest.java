package com.bivashy.auth.crypto;

import me.mastercapexd.auth.crypto.Argon2CryptoProvider;

public class Argon2CryptoProviderTest extends CryptoProviderTest {
    public Argon2CryptoProviderTest() {
        super(new Argon2CryptoProvider(),
                new String[]{"$argon2id$v=19$m=15360,t=2,p=1$HF+N3NmpNyQ5ZbinSyllrrNAqr/NO+CKgoesWTstpCtqBodFCp7kKcPMqUkw9klVzAMHiuwDoHe4hPO+EMjdrg$Lr" +
                        "+3aaZqoIpc5sv42aosHOUKKT1PVJWoBfqCN8x+Ht4", "$argon2id$v=19$m=15360,t=2," +
                        "p=1$6KN5ypKz0DRdqMSkHrkJaSVlGW5zOovYWLodZT8YZ3gVHXBJdZ6nQTzc6FyZpDUt6KdhW9cxMURXo9sZ+AmcsQ$GhIuSWXMuDaeVjl9tsZLFf0GTHppEAAL2nIfUUG9T" +
                        "/M", "$argon2id$v=19$m=15360,t=2,p=1$N5iXnDrwztcNRLSrDHxBfzW5TJ9rCzt2TvVq65VEzrOzAYDdcsOHvnK7+hMe0kUbtZ4lxCXD4B6KhYG" +
                        "+SRmoNw$wAlBoPFGrFFQmgvrAatJiaPHURvE1xBwCbDu/i8cjBE", "$argon2id$v=19$m=15360,t=2," +
                        "p=1$1pCzpJANzmRDmh5ow3QTb7gjOMwhuZEDrr5mGZ2VEZcjVvQyh5UwJawQ8EKuML6Q6KPfULDdjDxkLul6980Yyg$fFaTco4hv" +
                        "+a8rYQQczCb6xK8Kb1H2pp8LDdlchemO1g"});
    }
}
