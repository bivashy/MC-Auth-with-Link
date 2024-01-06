package com.bivashy.auth.crypto;

import me.mastercapexd.auth.crypto.ScryptCryptoProvider;

public class ScryptCryptoProviderTest extends CryptoProviderTest {

    public ScryptCryptoProviderTest() {
        super(new ScryptCryptoProvider(), new String[]{
                "$100801$V/I0Y94IaF3NTPzquROFw2bvBCPr9pxFBQzeNSsdSLvSVRrB087dUb6JNrMBp5EQOK1/1/esyt3q73Z4yUm7/Q==$cEHD0aqYSCaplnWTCCgFsuCkDt4I8guRNYzmpHUohH23Zge/Rbo0Err6O/J1M6O6IB51MpF8yTwcx6pSXPS4FQ==",
                "$100801$sYf6oPoF4wHE9hYtr7McUcDZfzJh1abFGovQMafQC7XXdey/qVcu8n+p+rwa3hr5/mbUI+Kbx+rxT38y8FXy9A==$36jiGsm9D5o185VZJUyJ8YEm25DVFJgH8/75Lox0DJ0U5zVDUefulhaM9B7uTv7z0Szqwt5B/xf/egDFcI6b9Q==",
                "$100801$HtuKgUVIZJvQoPXIggIm1uL1JUFqHxRNygVYGFuUUjThB3bdH+suyd1CBSubGCoeXzQNFX3KdL+nPvAyBZu7xg==$xGDTQdeqtNcXblynU9spQjCe66ESM59SEWzY0yPGG1+e5mTKsxcHqAeSgrXlhu/iqbpty8IHWeVeSSELDFNctg==",
                "$100801$K6Iw3oVmk0QBTPLpOSfgW24bogEpp6LgNJpI3S8zFKiUNlOWINag+Sg8UUoWehlsOSQZyEE4ULHdapUDI+3GTg==$CQ7mIj7Lun6dzssI6iUkuupn+j7+8K7dOrtcp8SWS3sQpOz8Qqpqs5M5DNUf1B0EcU3DlurWFyG2Y/PXtyr/NA=="});
    }

}
