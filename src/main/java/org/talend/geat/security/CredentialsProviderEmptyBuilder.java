package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.talend.geat.exception.IllegalCommandArgumentException;


public class CredentialsProviderEmptyBuilder implements CredentialsProviderBuilder {

    public void install() throws IllegalCommandArgumentException {
    }

    public CredentialsProvider build() throws IllegalCommandArgumentException {
        return null;
    }

}
