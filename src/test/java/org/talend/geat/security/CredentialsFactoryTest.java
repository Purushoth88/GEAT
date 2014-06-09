package org.talend.geat.security;

import java.net.URISyntaxException;

import org.eclipse.jgit.transport.URIish;
import org.junit.Assert;
import org.junit.Test;

public class CredentialsFactoryTest {

    @Test
    public void testIsHttps() throws URISyntaxException {
        Assert.assertFalse(CredentialsFactory.isHttps(new URIish("git@github.com:smallet/GEAT.git")));
        Assert.assertTrue(CredentialsFactory.isHttps(new URIish("https://github.com/smallet/testing.git")));
    }

}
