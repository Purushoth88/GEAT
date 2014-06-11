package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.talend.geat.exception.IllegalCommandArgumentException;

public interface CredentialsProviderBuilder {

    public void install() throws IllegalCommandArgumentException;

    public CredentialsProvider build() throws IllegalCommandArgumentException;

}
