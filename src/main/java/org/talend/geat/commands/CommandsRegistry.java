package org.talend.geat.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsRegistry {

    public static final CommandsRegistry INSTANCE        = new CommandsRegistry();

    private Map<String, Command>         commands        = new HashMap<String, Command>();

    protected List<Command>              orderedCommands = new ArrayList<Command>();

    private CommandsRegistry() {
        registerCommands();
    }

    public Command getCommand(String key) {
        if (commands.containsKey(key)) {
            try {
                return commands.get(key).getClass().newInstance();
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    private void registerCommands() {
        registerCommand(new Help());
        registerCommand(new Version());
        registerCommand(new FeatureStart());
        registerCommand(new FeatureFinish());
        registerCommand(new FeaturePush());
        registerCommand(new FeaturePull());
        registerCommand(new BugfixStart());
        registerCommand(new BugfixFinish());
    }

    private void registerCommand(Command command) {
        commands.put(command.getNames().getMainName(), command);
        commands.put(command.getClass().getCanonicalName(), command);
        for (String name : command.getNames().getAlternateNames()) {
            commands.put(name, command);
        }
        orderedCommands.add(command);
    }

    protected Map<String, Command> getCommands() {
        return commands;
    }

}
