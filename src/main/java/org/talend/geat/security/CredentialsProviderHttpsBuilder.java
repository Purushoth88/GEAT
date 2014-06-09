package org.talend.geat.security;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.talend.geat.GitConfiguration;
import org.talend.geat.InputsUtils;
import org.talend.geat.MyGit;


public class CredentialsProviderHttpsBuilder implements CredentialsProviderBuilder {

    public void install() {
        MyGit.credentialsProvider = build();
    }

    public CredentialsProvider build() {
        String username = GitConfiguration.getInstance().get("user.email");
        String password = findPassword(username);

        return new UsernamePasswordCredentialsProvider(username, password);
    }

    private String findPassword(String username) {
        String password = GitConfiguration.getInstance().get("httpspwd");
        if (password == null) {
            password = InputsUtils.askUser("HTTPS pasword for " + username + ", leave empty to skip", null);
            if (InputsUtils.askUserAsBoolean("Do you want to save this password in your local gitconfig file")) {
                GitConfiguration.getInstance().set(GitConfiguration.CONFIG_PREFIX, "httpspwd", password);
            }
        }

        return password;
    }

}
