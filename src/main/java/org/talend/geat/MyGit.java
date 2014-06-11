package org.talend.geat;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.talend.geat.jgit.ListBranchCommand;

public class MyGit extends Git {

    public static CredentialsProvider credentialsProvider;

    public MyGit(Repository repo) {
        super(repo);
    }

    public static MyGit open() throws IOException {
        final Git open = Git.open(new File(System.getProperty("user.dir")));
        return new MyGit(open.getRepository());
    }

    public PushCommand push() {
        PushCommand command = super.push();
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }
        return command;
    }

    @Override
    public PullCommand pull() {
        PullCommand command = super.pull();
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }
        return command;
    }

    @Override
    public FetchCommand fetch() {
        FetchCommand command = super.fetch();
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }
        return command;
    }

    public ListBranchCommand branchList2() {
        ListBranchCommand command = new ListBranchCommand(getRepository());
        return command;
    }

    @Override
    public LsRemoteCommand lsRemote() {
        LsRemoteCommand command = super.lsRemote();
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }
        return command;
    }

}
