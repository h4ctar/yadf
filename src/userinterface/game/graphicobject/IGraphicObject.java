package userinterface.game.graphicobject;

import java.awt.Graphics;

import simulation.map.MapIndex;

/**
 * Interface for graphic objects to implement.
 */
public interface IGraphicObject {

    /**
     * Render the graphic object to the graphics.
     * @param graphics the graphics
     * @param viewPosition the position of the view
     * @param viewSize the size of the view
     */
    void render(Graphics graphics, MapIndex viewPosition, MapIndex viewSize);
}
