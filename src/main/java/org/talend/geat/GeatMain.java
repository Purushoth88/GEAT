package org.talend.geat;

import org.talend.geat.commands.Command;
import org.talend.geat.commands.CommandsRegistry;
import org.talend.geat.commands.Help;
import org.talend.geat.commands.Version;
import org.talend.geat.exception.IncorrectRepositoryStateException;
import org.talend.geat.exception.InterruptedCommandException;

public class GeatMain {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("dev")) {
            args = new String[] { "feature-finish", "config" };
            args = new String[] { "feature-start", "tagada" };
            args = new String[] { "version" };
            System.setProperty("user.dir", "/tmp/tuj-config");
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

}
