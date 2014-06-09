package org.talend.geat.security;


public class CredentialsManager {

    public static void init() {
        CredentialsFactory.getCredentialsProviderBuilder().install();
    }
}
