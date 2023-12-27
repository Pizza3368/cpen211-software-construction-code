package ca.ubc.ece.cpen221.worlds.commands;

import ca.ubc.ece.cpen221.worlds.World;

/**
 * A WaitCommand is a {@link Command} which represents doing nothing
 */
public final class WaitCommand implements Command {

    @Override
    public void execute(World w) {
        // Do nothing.
    }

}
