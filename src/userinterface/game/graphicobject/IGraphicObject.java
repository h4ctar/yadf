package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.map.MapArea;

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
}
