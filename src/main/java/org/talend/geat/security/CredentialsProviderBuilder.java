package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;

public interface CredentialsProviderBuilder {

    public void install() throws IllegalCommandArgumentException, IncorrectRepositoryStateException;

    public CredentialsProvider build() throws IllegalCommandArgumentException, IncorrectRepositoryStateException;

}
