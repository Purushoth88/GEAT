package org.talend.geat.commands;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.talend.geat.GitConfiguration;
import org.talend.geat.GitUtils;
import org.talend.geat.MyGit;
import org.talend.geat.SanityCheck.CheckLevel;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;
import org.talend.geat.exception.InterruptedCommandException;

/**
 * Command that finish a feature. Means:
 * <ul>
 * <li>fetch feature branch</li>
 * <li>fetch develop branch</li>
 * <li>merge feature branch into develop branch (differents policy can be applied here)</li>
 * <li>delete feature branch</li>
 * </ul>
 */
public class FeatureFinish extends Command {

    protected static final String NAME = "feature-finish";

    protected String              featureName;

    protected MergePolicy         mergePolicy;

    protected FeatureFinish() {
        super();
    }

    @Override
    public CommandNames getNames() {
        return new CommandNames(NAME, "ff");
    }

    public String getDescription() {
        return "Merge and close a feature branch when work is finished";
    }

    public String getUsage() {
        String usage = "<feature-name> [policy (squash|rebase)";
        try {
            usage += ", default=" + GitConfiguration.getInstance().get("finishmergemode") + "]";
        } catch (IncorrectRepositoryStateException e) {
            // May occurs if out of a GIT repo
        }
        return usage;
    }

    protected Command innerParseArgs(String[] args) throws IllegalCommandArgumentException,
            IncorrectRepositoryStateException {
        if (args.length < 2) {
            throw IllegalCommandArgumentException.build(this);
        }
        featureName = args[1];

        if (args.length >= 3) {
            mergePolicy = parseMergePolicy(args[2]);
        } else {
            mergePolicy = MergePolicy.valueOf(GitConfiguration.getInstance().get("finishmergemode").toUpperCase());
        }

        return this;
    }

    protected MergePolicy parseMergePolicy(String arg) throws IllegalCommandArgumentException {
        try {
            return MergePolicy.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();

            sb.append("Unknown merge policy '" + arg + "'");
            sb.append("Availables merge policy are:");
            for (MergePolicy current : MergePolicy.values()) {
                sb.append(" - " + current.name().toLowerCase());
            }
            throw new IllegalCommandArgumentException(sb.toString());
        }
    }

    @Override
    public CheckLevel getCheckLevel() {
        return CheckLevel.NO_UNCOMMITTED_CHANGES;
    }

    public void execute(Writer writer) throws IncorrectRepositoryStateException, IOException, GitAPIException,
            InterruptedCommandException {
        MyGit repo = MyGit.open();

        GitUtils.merge(writer, repo, featureName, GitConfiguration.getInstance().get("featurePrefix"), GitConfiguration
                .getInstance().get("featureStartPoint"), "feature", mergePolicy, getNames().getMainName());
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Enum<MergePolicy> getMergePolicy() {
        return mergePolicy;
    }

    public void setMergePolicy(MergePolicy mergePolicy) {
        this.mergePolicy = mergePolicy;
    }

}
