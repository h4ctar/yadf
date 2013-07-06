package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.map.MapArea;
import simulation.map.MapIndex;

/**
 * Interface for graphic objects to implement.
 */
public interface IGraphicObject {

    /**
     * Render the graphic object to the graphics.
     * @param graphics the graphics
     * @param viewArea the view area
     */
    void render(Graphics graphics, MapArea viewArea);

    /**
     * Does the graphic object contain a map index.
     * @param mapIndex the map index
     * @return true if it contains the map index
     */
    boolean containsIndex(MapIndex mapIndex);
}
