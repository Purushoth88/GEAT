package org.talend.geat.security;

import org.talend.geat.exception.IllegalCommandArgumentException;

public class CredentialsManager {

    public static boolean installed = false;

    public static void init() throws IllegalCommandArgumentException {
        if (!installed) {
            CredentialsFactory.getCredentialsProviderBuilder().install();
            installed = true;
        }
    }
}
