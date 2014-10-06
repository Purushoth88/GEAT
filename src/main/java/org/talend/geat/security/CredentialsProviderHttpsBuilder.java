package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.talend.geat.GitConfiguration;
import org.talend.geat.InputsUtils;
import org.talend.geat.MyGit;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;


public class CredentialsProviderHttpsBuilder implements CredentialsProviderBuilder {

    private static String password;

    public void install() throws IllegalCommandArgumentException, IncorrectRepositoryStateException {
        MyGit.credentialsProvider = build();
    }

    public CredentialsProvider build() throws IllegalCommandArgumentException, IncorrectRepositoryStateException {
        String username = GitConfiguration.getInstance().get("user.email");
        if (password == null || password.length() == 0) {
            password = findPassword(username);
        }
        if (password == null) {
            throw new IllegalCommandArgumentException("Password cannot be null");
        }

        return new UsernamePasswordCredentialsProvider(username, password);
    }

    private String findPassword(String username) throws IncorrectRepositoryStateException {
        String toReturn = GitConfiguration.getInstance().get("httpspwd");
        if (toReturn == null) {
            toReturn = InputsUtils.askUser("HTTPS password for [" + username + "]", null);
            if (toReturn != null
                    && InputsUtils.askUserAsBoolean("Do you want to save this password in your local gitconfig file")) {
                GitConfiguration.getInstance().set(GitConfiguration.CONFIG_PREFIX, "httpspwd", toReturn);
            }
        }

        return toReturn;
    }

}
