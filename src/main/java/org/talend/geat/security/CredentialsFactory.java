package org.talend.geat.security;

import org.eclipse.jgit.transport.URIish;
import org.talend.geat.GitUtils;


public class CredentialsFactory {

    public static CredentialsProviderBuilder getCredentialsProviderBuilder() {
        final URIish remoteUrl = GitUtils.getRemoteUrl("origin");
        if (!remoteUrl.isRemote()) {
            return new CredentialsProviderEmptyBuilder();
        }
        if (isHttps(remoteUrl)) {
            return new CredentialsProviderHttpsBuilder();
        } else {
            return new CredentialsProviderSshBuilder();
        }
    }

    /**
     * Checks if origin url is based on HTTPS (true) or SSH (false).
     * 
     * @return true if HTTPS, false if SSH
     */
    protected static boolean isHttps(URIish url) {
        return "https".equals(url.getScheme());
    }

}
