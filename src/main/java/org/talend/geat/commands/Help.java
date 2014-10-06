package org.talend.geat.commands;

import java.io.IOException;
import java.io.Writer;

import org.talend.geat.SanityCheck.CheckLevel;

/**
 * Displays all commands name and description.
 */
public class Help extends Command {

    protected Help() {
        super();
    }

    @Override
    public CommandNames getNames() {
        return new CommandNames("help");
    }

    @Override
    public CheckLevel getCheckLevel() {
        return CheckLevel.NONE;
    }

    public void execute(Writer writer) throws IOException {
        writer.write("Available commands are:");
        for (Command command : CommandsRegistry.INSTANCE.orderedCommands) {
            String desc = " - " + command.getNames().getMainName();
            if (!command.getNames().getAlternateNames().isEmpty()) {
                desc += " (or " + command.getNames().getAlternateNames()
                        + ")";
            }
            desc += " - " + command.getDescription();

            writer.write(desc);
        }
    }

    public String getUsage() {
        return "";
    }

    public String getDescription() {
        return "Displays this help";
    }

}
