package org.talend.geat.security;

import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;

public class CredentialsManager {

    public static void init() throws IllegalCommandArgumentException, IncorrectRepositoryStateException {
        CredentialsFactory.getCredentialsProviderBuilder().install();
    }
}
