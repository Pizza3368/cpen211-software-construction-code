package ca.ubc.ece.cpen221.worlds;

import ca.ubc.ece.cpen221.worlds.items.Item;
import ca.ubc.ece.cpen221.worlds.items.animals.ArenaAnimal;

import java.util.Set;

/**
 * Subset of the world visible to {@link ArenaAnimal}s.
 */
public interface ArenaWorld {

    /**
     * Returns items within the view range of <code>animal</code>, which is
     * defined by {@link ArenaAnimal#getViewRange()}.
     *
     * @param animal the ArenaAnimal whose location is used
     * @return a set of items visible to <code>animal</code>
     * @see World#searchSurroundings(Location, int)
     */
    Set<Item> searchSurroundings(ArenaAnimal animal);

    /**
     * @return an int representing the number of columns this World has
     */
    int getWidth();

    /**
     * @return an int representing the number of rows this World has
     */
    int getHeight();

}
