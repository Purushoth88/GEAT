package org.talend.geat.commands;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.geat.GitConfiguration;
import org.talend.geat.JUnitUtils;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;
import org.talend.geat.exception.InterruptedCommandException;

/**
 * Dedicated test class for bug #21.
 * 
 * This bug used to occurs, when geat was not called at the root of the git repo. Two cases:
 * 
 * - called in a sub-folder of the git repo (named case A bellow)
 * 
 * - called outside a git repo (named case B bellow)
 */
public abstract class Bug21Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        GitConfiguration.reset();
    }

    protected abstract Command initCommandInstance();

    @Test
    public void testIssue21CaseA() throws IllegalCommandArgumentException, GitAPIException, IOException,
            IncorrectRepositoryStateException, InterruptedCommandException {
        thrown.expect(IllegalCommandArgumentException.class);
        Git git = JUnitUtils.createTempRepo();
        JUnitUtils.createInitialCommit(git, "file1");

        File aFile = new File(git.getRepository().getDirectory().getAbsoluteFile().getParentFile(), "folder1");
        aFile.mkdir();
        git.add().addFilepattern("folder1").call();
        git.commit().setMessage("Initial commit (add " + "folder1" + ")").call();
        aFile = new File(aFile, "file2");
        aFile.createNewFile();
        git.add().addFilepattern("file2").call();
        git.commit().setMessage("Initial commit (add " + "file2" + ")").call();
        System.setProperty("user.dir", aFile.getParentFile().getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));

        Command command = initCommandInstance();
        command.parseArgs(new String[] { "--" });
    }

    @Test
    public void testIssue21CaseB() throws IllegalCommandArgumentException, GitAPIException, IOException,
            IncorrectRepositoryStateException, InterruptedCommandException {
        thrown.expect(IncorrectRepositoryStateException.class);
        Git git = JUnitUtils.createTempRepo();
        JUnitUtils.createInitialCommit(git, "file1");

        File aFile = new File(git.getRepository().getDirectory().getAbsoluteFile().getParentFile(), "folder1");
        aFile.mkdir();
        git.add().addFilepattern("folder1").call();
        git.commit().setMessage("Initial commit (add " + "folder1" + ")").call();
        aFile = new File(aFile, "file2");
        aFile.createNewFile();
        git.add().addFilepattern("file2").call();
        git.commit().setMessage("Initial commit (add " + "file2" + ")").call();
        System.setProperty("user.dir", aFile.getParentFile().getParentFile().getParentFile().getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));

        Command command = initCommandInstance();
        command.parseArgs(new String[] { "--" });
    }

}
