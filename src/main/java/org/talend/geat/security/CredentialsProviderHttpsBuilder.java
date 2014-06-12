package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.talend.geat.GitConfiguration;
import org.talend.geat.InputsUtils;
import org.talend.geat.MyGit;
import org.talend.geat.exception.IllegalCommandArgumentException;


public class CredentialsProviderHttpsBuilder implements CredentialsProviderBuilder {

    public void install() throws IllegalCommandArgumentException {
        MyGit.credentialsProvider = build();
    }

    public CredentialsProvider build() throws IllegalCommandArgumentException {
        String username = GitConfiguration.getInstance().get("user.email");
        String password = findPassword(username);
        if (password == null) {
            throw new IllegalCommandArgumentException("Password cannot be null");
        }

        return new UsernamePasswordCredentialsProvider(username, password);
    }

    private String findPassword(String username) {
        String password = GitConfiguration.getInstance().get("httpspwd");
        if (password == null) {
            password = InputsUtils.askUser("HTTPS password for [" + username + "]", null);
            if (password != null
                    && InputsUtils.askUserAsBoolean("Do you want to save this password in your local gitconfig file")) {
                GitConfiguration.getInstance().set(GitConfiguration.CONFIG_PREFIX, "httpspwd", password);
            }
        }

        return password;
    }

}
