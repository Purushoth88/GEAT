package org.talend.geat.commands;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.talend.geat.SanityCheck;
import org.talend.geat.SanityCheck.CheckLevel;
import org.talend.geat.exception.IllegalCommandArgumentException;
import org.talend.geat.exception.IncorrectRepositoryStateException;
import org.talend.geat.exception.InterruptedCommandException;
import org.talend.geat.io.AutoFlushLineWriter;

/**
 * Common superclass of all commands in the package.
 * 
 * Thanks to callable field, this class assures that a command instance can be run only once.
 */
public abstract class Command {

    private boolean callable = true;

    private Writer  writer   = new AutoFlushLineWriter(new OutputStreamWriter(System.out));

    public Command() {
        super();
    }

    /**
     * Returns names that this command will be associated with in registry. At least one name is required, few are
     * allowed for shortcuts.
     */
    public abstract CommandNames getNames();

    /**
     * Used only to prints help.
     */
    public abstract String getUsage();

    /**
     * Used only to prints help.
     */
    public abstract String getDescription();

    public abstract CheckLevel getCheckLevel();

    /**
     * This is the method to implement to define the behaviour of your command.
     * 
     * @param writer
     *            writer used to write the command std output. will not be closed by this method.
     * @throws IncorrectRepositoryStateException
     *             if the repository state is not compatible with this command. If this exception is thrown, the
     *             repository is let as it was before (no changes).
     * @throws IOException
     * @throws GitAPIException
     * @throws InterruptedCommandException
     *             if the command failed in the middle of its execution. If this exception is thrown, the repository has
     *             changed, and the command needs to be re-launch to complete.
     */
    protected abstract void execute(Writer writer) throws IncorrectRepositoryStateException, IOException,
            GitAPIException, InterruptedCommandException;

    public final Command parseArgs(String[] args) throws IllegalCommandArgumentException,
            IncorrectRepositoryStateException {
        SanityCheck.check(getCheckLevel());
        return innerParseArgs(args);
    }

    protected Command innerParseArgs(String[] args) throws IllegalCommandArgumentException,
            IncorrectRepositoryStateException {
        return this;
    }

    /**
     * This is the way to run a command execution.
     */
    public void run() throws IncorrectRepositoryStateException, IOException, GitAPIException,
            InterruptedCommandException {
        checkCallable();

        SanityCheck.check(getCheckLevel());

        callable = false;

        execute(this.writer);
    }

    protected void checkCallable() {
        if (!callable) {
            throw new IllegalStateException("Command was called in the wrong state");
        }
    }

    public Writer getWriter() {
        return writer;
    }

    public Command setWriter(Writer writer) {
        this.writer = writer;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) {
            return false;
        }
        Command other = (Command) obj;
        if (callable != other.callable) {
            return false;
        }
        return true;
    }

}
