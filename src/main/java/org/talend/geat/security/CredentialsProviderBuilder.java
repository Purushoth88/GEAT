package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;

public interface CredentialsProviderBuilder {

    public void install();

    public CredentialsProvider build();

}
