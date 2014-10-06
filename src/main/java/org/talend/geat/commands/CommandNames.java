package org.talend.geat.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage names of command.
 * 
 * Names are how command can be called in command line.
 * 
 * Each command have a main name, and optionnaly alternate names.
 */
public class CommandNames {

    private String       mainName;

    private List<String> alternateNames = new ArrayList<String>();

    public CommandNames(String mainName, String... alternates) {
        super();
        this.mainName = mainName;
        for (String alt : alternates) {
            alternateNames.add(alt);
        }
    }

    public List<String> getAlternateNames() {
        return alternateNames;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

}
