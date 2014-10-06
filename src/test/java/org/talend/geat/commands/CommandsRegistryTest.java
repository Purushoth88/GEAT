package org.talend.geat.commands;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CommandsRegistryTest {

    @Test
    public void testGetCommand() {
        Command command = CommandsRegistry.INSTANCE.getCommand("feature-start");
        Assert.assertEquals(new FeatureStart(), command);
    }

    @Test
    public void testGetCommandClassName() {
        Command command = CommandsRegistry.INSTANCE.getCommand(FeatureStart.class.getCanonicalName());
        Assert.assertEquals(new FeatureStart(), command);
    }

    @Test
    public void testGetCommandAlternateNames() {
        Command command = CommandsRegistry.INSTANCE.getCommand("fs");
        Assert.assertEquals(new FeatureStart(), command);
    }

    @Test
    public void testCommandOrder() {
        List<Command> orderedCommands = CommandsRegistry.INSTANCE.orderedCommands;
        Assert.assertEquals(new Help(), orderedCommands.get(0));
        Assert.assertEquals(new Version(), orderedCommands.get(1));
    }

}
