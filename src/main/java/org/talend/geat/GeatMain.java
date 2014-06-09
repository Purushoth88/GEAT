package org.talend.geat;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.talend.geat.commands.Command;
import org.talend.geat.commands.CommandsRegistry;
import org.talend.geat.commands.Help;
import org.talend.geat.commands.Version;
import org.talend.geat.exception.IncorrectRepositoryStateException;
import org.talend.geat.exception.InterruptedCommandException;
import org.talend.geat.security.CredentialsManager;

public class GeatMain {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("dev")) {
            args = new String[] { "help" };
            args = new String[] { "feature-start", "tagada" };
            args = new String[] { "feature-finish", "config" };
            System.setProperty("user.dir", "/tmp/test");
        }

        try {
            if (GitUtils.hasRemote("origin", Git.open(new File(System.getProperty("user.dir"))).getRepository())) {
                initSsh();
            }
        } catch (IOException e) {
            // Should not occurs (check above in SanityCheck)
        }

        if (args.length < 1) {
            usage();
        }

        Command command = CommandsRegistry.INSTANCE.getCommand(args[0]);
        if (command == null) {
            usage();
        }

        try {
            command.parseArgs(args).run();
        } catch (IncorrectRepositoryStateException e) {
            System.out.println(e.getDetails());
        } catch (InterruptedCommandException e) {
            System.out.println(e.getDetails());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("");
    }

    private static void usage() {
        try {
            CommandsRegistry.INSTANCE.getCommand(Version.NAME).run();
            System.out.println("\n");
            CommandsRegistry.INSTANCE.getCommand(Help.NAME).run();
        } catch (Exception e) {
            // Should not occurs
        }
        System.out.println("");
        System.exit(1);
    }

    private static void initSsh() {
        CredentialsManager.init();
    }

}
