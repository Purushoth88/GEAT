package org.talend.geat.commands;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.RefSpec;
import org.talend.geat.Configuration;
import org.talend.geat.GitConfiguration;
import org.talend.geat.GitUtils;
import org.talend.geat.InputsUtils;
import org.talend.geat.MyGit;
import org.talend.geat.SanityCheck;
import org.talend.geat.SanityCheck.CheckLevel;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;

import com.google.common.base.Strings;

/**
 * Command that starts a feature. Means:
 * <ul>
 * <li>fetch develop branch</li>
 * <li>create a local branch based on develop branch</li>
 * <li>checkout new feature branch</li>
 * </ul>
 */
public class FeatureStart extends Command {

    protected static final String NAME = "feature-start";

    protected String              featureName;

    protected FeatureStart() {
        super();
    }

    @Override
    public CommandNames getNames() {
        return new CommandNames(NAME, "fs");
    }

    protected Command innerParseArgs(String[] args) throws IllegalCommandArgumentException {
        if (args.length != 2) {
            throw IllegalCommandArgumentException.build(this);
        }
        featureName = args[1];

        return this;
    }

    public String getDescription() {
        return "Create a branch to work on a new feature";
    }

    public String getUsage() {
        return "<feature-name>";
    }

    @Override
    public CheckLevel getCheckLevel() {
        return CheckLevel.GIT_REPO_ONLY;
    }

    public void execute(Writer writer) throws IncorrectRepositoryStateException, IOException, GitAPIException {
        try {
            SanityCheck.check(CheckLevel.NO_UNCOMMITTED_CHANGES);
        } catch (IncorrectRepositoryStateException e) {
            if (!InputsUtils.askUserAsBoolean(e.getDetails() + "\n\nProceed anyway")) {
                return;
            }
        }

        MyGit repo = MyGit.open();
        String featureBranchName = GitConfiguration.getInstance().get("featurePrefix") + "/" + featureName;
        boolean hasRemote = GitUtils.hasRemote("origin", repo.getRepository());

        // Test if such a branch exists locally:
        if (GitUtils.hasLocalBranch(repo.getRepository(), featureBranchName)) {
            throw new IncorrectRepositoryStateException("A local branch named '" + featureBranchName
                    + "' already exist.");
        }

        if (hasRemote) {
            // Test if branch exist remotely:
            if (GitUtils.hasRemoteBranch(repo.getRepository(), featureBranchName)) {
                IncorrectRepositoryStateException irse = new IncorrectRepositoryStateException(
                        "A remote branch named '" + featureBranchName + "' already exist.");
                irse.addLine("To checkout this branch locally, use:");
                irse.addLine("");
                irse.addLine(Strings.repeat(" ", Configuration.INSTANCE.getAsInt("geat.indentForCommandTemplates"))
                        + "git fetch && git checkout " + featureBranchName);
                throw irse;
            }

            // git checkout master
            repo.checkout().setName(GitConfiguration.getInstance().get("featureStartPoint")).call();

            // git pull --rebase origin
            // 1. git fetch
            repo.fetch()
                    .setRefSpecs(
                            new RefSpec("refs/heads/" + GitConfiguration.getInstance().get("featureStartPoint")
                                    + ":refs/remotes/origin/" + GitConfiguration.getInstance().get("featureStartPoint")))
                    .setRemote("origin").call();
            // 2. git merge ff
            Ref refOriginMaster = repo.getRepository().getRef("origin/master");
            repo.merge().setFastForward(FastForwardMode.FF_ONLY).include(refOriginMaster).call();
        }

        repo.checkout().setCreateBranch(true).setStartPoint(GitConfiguration.getInstance().get("featureStartPoint"))
                .setName(featureBranchName).call();

        writer.write("Summary of actions:");
        writer.write(" - A new branch '" + featureBranchName + "' was created, based on '"
                + GitConfiguration.getInstance().get("featureStartPoint") + "'");
        writer.write(" - You are now on branch '" + featureBranchName + "'");
        writer.write("");
        writer.write("Now, start committing on your feature. When done, use:");
        writer.write("");
        writer.write(Strings.repeat(" ", Configuration.INSTANCE.getAsInt("geat.indentForCommandTemplates")) + "geat "
                + FeatureFinish.NAME + " " + featureName + " <policy>");
        writer.write("");
        writer.write("To share this branch, use:");
        writer.write("");
        writer.write(Strings.repeat(" ", Configuration.INSTANCE.getAsInt("geat.indentForCommandTemplates")) + "geat "
                + FeaturePush.NAME + " " + featureName);
    }

}
