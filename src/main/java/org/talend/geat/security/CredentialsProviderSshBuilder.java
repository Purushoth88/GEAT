package org.talend.geat.security;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.CredentialsProviderUserInfo;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.URIish;
import org.talend.geat.GitConfiguration;
import org.talend.geat.InputsUtils;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class CredentialsProviderSshBuilder implements CredentialsProviderBuilder {

    public void install() {
        final CredentialsProvider credentialsProvider = build();

        JschConfigSessionFactory sessionFactory = new JschConfigSessionFactory() {

            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                UserInfo userInfo = new CredentialsProviderUserInfo(session, credentialsProvider);
                session.setUserInfo(userInfo);
            }
        };
        SshSessionFactory.setInstance(sessionFactory);
    }

    public CredentialsProvider build() {
        final String sshPassphrase = findSshPassphrase();
        if (sshPassphrase == null) {
            System.out.println("WARN: SSH not set.");
            return null;
        }

        CredentialsProvider provider = new CredentialsProvider() {

            @Override
            public boolean isInteractive() {
                return false;
            }

            @Override
            public boolean supports(CredentialItem... items) {
                return true;
            }

            @Override
            public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
                for (CredentialItem item : items) {
                    ((CredentialItem.StringType) item).setValue(sshPassphrase);
                }
                return true;
            }
        };
        return provider;
    }

    private String findSshPassphrase() {
        String sshPassphrase = GitConfiguration.getInstance().get("sshpassphrase");
        if (sshPassphrase == null) {
            sshPassphrase = InputsUtils.askUser("SSH passphrase, leave empty to skip", null);
            if (InputsUtils.askUserAsBoolean("Do you want to save this passphrase in your local gitconfig file")) {
                GitConfiguration.getInstance().set(GitConfiguration.CONFIG_PREFIX, "sshpassphrase", sshPassphrase);
            }
        }

        return sshPassphrase;
    }

}
