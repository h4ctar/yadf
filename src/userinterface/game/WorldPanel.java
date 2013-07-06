/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package userinterface.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import logger.Logger;
import simulation.IGameObject;
import simulation.IGameObjectManagerListener;
import simulation.IPlayer;
import simulation.Region;
import simulation.Tree;
import simulation.character.IGameCharacter;
import simulation.character.component.ISkillComponent;
import simulation.farm.Farm;
import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.job.IJobManager;
import simulation.job.designation.AbstractDesignation;
import simulation.labor.LaborType;
import simulation.map.BlockType;
import simulation.map.IMapListener;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.room.Room;
import simulation.workshop.IWorkshop;
import userinterface.game.graphicobject.FarmGraphicObject;
import userinterface.game.graphicobject.IGraphicObject;
import userinterface.game.graphicobject.ItemGraphicObject;
import userinterface.game.graphicobject.RoomGraphicObject;
import userinterface.game.graphicobject.StockpileGraphicObject;
import userinterface.game.graphicobject.TreeGraphicObject;
import userinterface.game.graphicobject.WorkshopGraphicObject;
import userinterface.misc.Sprite;
import userinterface.misc.SpriteManager;

/**
 * The WorldCanvas.
 */
public class WorldPanel extends JPanel implements ComponentListener, IMapListener, IGameObjectManagerListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The colour of designations. */
    private static final Color DESIGNATION_COLOUR = new Color(0.8f, 0.5f, 0.5f, 0.4f);

    /** The colour of the mouse. */
    private static final Color MOUSE_COLOUR = new Color(0.5f, 0.5f, 0.8f, 0.8f);

    /** The colour of a valid selection. */
    private static final Color VALID_SELECTION_COLOUR = new Color(0.5f, 0.8f, 0.5f, 0.6f);

    /** The colour of an invalid selection. */
    private static final Color INVALID_SELECTION_COLOUR = new Color(0.8f, 0.5f, 0.5f, 0.6f);

    /** The colour of the atmosphere. */
    private static final Color ATMOSPHERE_COLOUR = new Color(0.5f, 0.5f, 0.7f, 0.5f);

    /** The colour of a block that is under ground. */
    private static final Color UNDER_GROUND_COLOUR = new Color(0.0f, 0.0f, 0.0f, 0.9f);

    /** The canvas width. */
    private int canvasWidth;

    /** The canvas height. */
    private int canvasHeight;

    /** The view area. */
    private final MapArea viewArea = new MapArea();

    /** The selection. */
    private MapArea selection;

    /** The selection valid. */
    private boolean selectionValid;

    /** The player. */
    private IPlayer player;

    /** The region. */
    private Region region;

    /** The background image. */
    private BufferedImage backgroundImage = null;

    /** Does the background need to be redrawn. */
    private boolean drawBackgroundRequired;

    /** All the graphic objects. */
    private final Map<Object, IGraphicObject> graphicObjects = new ConcurrentHashMap<>();

    /**
     * Instantiates a new world canvas.
     */
    public WorldPanel() {
        setIgnoreRepaint(true);
        setDoubleBuffered(true);
        addComponentListener(this);
    }

    /**
     * Setup the canvas.
     * @param playerTmp the current player (so we can only see the designations and jobs of this player)
     * @param regionTmp the new region
     */
    public void setup(final IPlayer playerTmp, final Region regionTmp) {
        player = playerTmp;
        region = regionTmp;
        region.getMap().addListener(this);
        region.getTreeManager().addGameObjectManagerListener(this);
        for (IPlayer player2 : region.getPlayers()) {
            player2.getStockManager().addGameObjectManagerListener(this);
            player2.getFarmManager().addGameObjectManagerListener(this);
            player2.getRoomManager().addGameObjectManagerListener(this);
            player2.getWorkshopManager().addGameObjectManagerListener(this);
        }
    }

    /**
     * Draws a designation.
     * @param g the g
     */
    public void drawDesignations(final Graphics g) {
        IJobManager jobManager = player.getJobManager();
        for (AbstractDesignation designation : jobManager.getDesignations()) {
            List<MapIndex> mapIndicies = new ArrayList<>(designation.getMapIndicies());
            for (MapIndex mapIndex : mapIndicies) {
                if (viewArea.containesIndex(mapIndex)) {
                    g.setColor(DESIGNATION_COLOUR);
                    g.fillRect((mapIndex.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE,
                            (mapIndex.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE);
                }
            }
        }
    }

    /**
     * Draw mouse.
     * @param g the g
     */
    public void drawMouse(final Graphics g) {
        Point point = this.getMousePosition();
        if (point != null) {
            int x = point.x / SpriteManager.SPRITE_SIZE;
            int y = point.y / SpriteManager.SPRITE_SIZE;
            g.setColor(MOUSE_COLOUR);
            g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE,
                    SpriteManager.SPRITE_SIZE);
        }
    }

    /**
     * Draw selection.
     * @param g the g
     */
    public void drawSelection(final Graphics g) {
        if (selectionValid) {
            g.setColor(VALID_SELECTION_COLOUR);
        } else {
            g.setColor(INVALID_SELECTION_COLOUR);
        }

        g.fillRect((selection.pos.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE, (selection.pos.y - viewArea.pos.y)
                * SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE * selection.width, SpriteManager.SPRITE_SIZE
                * selection.height);
    }

    /**
     * Gets the mouse index.
     * @param x the x
     * @param y the y
     * @return the mouse index
     */
    public MapIndex getMouseIndex(final int x, final int y) {
        return viewArea.pos.add(x / SpriteManager.SPRITE_SIZE, y / SpriteManager.SPRITE_SIZE, 0);
    }

    @Override
    public boolean isFocusTraversable() {
        return true;
    }

    @Override
    public void mapChanged(final MapIndex mapIndex) {
        drawBackgroundRequired = true;
    }

    /**
     * Move view.
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public void moveView(final int x, final int y, final int z) {
        viewArea.pos = viewArea.pos.add(x, y, z);
        drawBackgroundRequired = true;
    }

    /**
     * Move the view to center on a position.
     * @param position where to zoom to
     */
    public void zoomToPosition(final MapIndex position) {
        viewArea.pos.x = position.x - viewArea.width / 2;
        viewArea.pos.y = position.y - viewArea.height / 2;
        viewArea.pos.z = position.z;
        drawBackgroundRequired = true;
    }

    /**
     * Move the view to center on an area.
     * @param area where to zoom to
     */
    public void zoomToArea(final MapArea area) {
        viewArea.pos.x = area.pos.x + area.width / 2 - viewArea.width / 2;
        viewArea.pos.y = area.pos.y + area.height / 2 - viewArea.height / 2;
        viewArea.pos.z = area.pos.z;
        drawBackgroundRequired = true;
    }

    @Override
    public void paint(final Graphics graphics) {
        if (region == null) {
            return;
        }
        if (drawBackgroundRequired) {
            drawBlocks();
        }
        graphics.setColor(Color.BLACK);
        graphics.drawImage(backgroundImage, 0, 0, null);
        for (IGraphicObject graphicObject : graphicObjects.values()) {
            graphicObject.render(graphics, viewArea);
        }

        drawDesignations(graphics);
        drawDwarfs(graphics);

        if (selection != null) {
            drawSelection(graphics);
        } else {
            drawMouse(graphics);
        }
    }

    /**
     * Sets the selection.
     * @param selectionTmp the selection
     * @param selectionValidTmp the selection valid
     */
    public void setSelection(final MapArea selectionTmp, final boolean selectionValidTmp) {
        selection = selectionTmp;
        selectionValid = selectionValidTmp;
    }

    /**
     * Block sprite.
     * @param block the block
     * @return the sprite
     */
    private Sprite blockSprite(final BlockType block) {
        return SpriteManager.getInstance().getBlockSprite(block.sprite);
    }

    /**
     * Draw blocks.
     */
    private void drawBlocks() {
        if (region == null) {
            return;
        }

        Logger.getInstance().log(this, "Draw blocks");
        RegionMap map = region.getMap();
        Sprite tile;
        Graphics g = backgroundImage.getGraphics();
        for (int x = 0; x < viewArea.width; x++) {
            for (int y = 0; y < viewArea.height; y++) {
                BlockType block = map.getBlock(viewArea.pos.add(x, y, -1));
                BlockType blockBelow = map.getBlock(viewArea.pos.add(x, y, -2));
                BlockType blockAbove = map.getBlock(viewArea.pos.add(x, y, 0));

                if (blockBelow != BlockType.RAMP && blockBelow != BlockType.STAIR) {
                    tile = blockSprite(blockBelow);
                    tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                }
                if (block != BlockType.RAMP && block != BlockType.STAIR) {
                    g.setColor(ATMOSPHERE_COLOUR);
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }

                tile = blockSprite(block);
                tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                if (block == BlockType.RAMP || block == BlockType.STAIR) {
                    g.setColor(ATMOSPHERE_COLOUR);
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }

                if (blockAbove == BlockType.RAMP || blockAbove == BlockType.STAIR) {
                    tile = blockSprite(blockAbove);
                    tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                } else if (!blockAbove.isStandIn) {
                    g.setColor(UNDER_GROUND_COLOUR);
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }
            }
        }
        drawBackgroundRequired = false;
    }

    /**
     * Draw dwarfs.
     * @param g the graphics to draw on
     */
    private void drawDwarfs(final Graphics g) {
        Set<IPlayer> players = region.getPlayers();
        for (IPlayer playerTmp : players) {
            Set<IGameCharacter> dwarfs = playerTmp.getDwarfManager().getDwarfs();
            for (IGameCharacter dwarf : dwarfs) {
                MapIndex position = dwarf.getPosition();
                if (position.z == viewArea.pos.z) {
                    int x = (position.x - viewArea.pos.x) * SpriteManager.SPRITE_SIZE;
                    int y = (position.y - viewArea.pos.y) * SpriteManager.SPRITE_SIZE;
                    if (x >= 0 && x < canvasWidth && y >= 0 && y < canvasHeight) {
                        Sprite dwarfSprite;
                        if (dwarf.isDead()) {
                            dwarfSprite = SpriteManager.getInstance().getItemSprite(SpriteManager.DEAD_DWARF_SPRITE);
                        } else {
                            LaborType profession = dwarf.getComponent(ISkillComponent.class).getProfession();
                            dwarfSprite = SpriteManager.getInstance().getItemSprite(profession.sprite);
                        }
                        dwarfSprite.draw(g, x, y);
                    }
                }
            }
        }
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject) {
        assert !graphicObjects.containsKey(gameObject);
        if (gameObject instanceof Farm) {
            Farm farm = (Farm) gameObject;
            graphicObjects.put(farm, new FarmGraphicObject(farm));
        } else if (gameObject instanceof Stockpile) {
            Stockpile stockpile = (Stockpile) gameObject;
            graphicObjects.put(stockpile, new StockpileGraphicObject(stockpile));
        } else if (gameObject instanceof Item) {
            Item item = (Item) gameObject;
            graphicObjects.put(item, new ItemGraphicObject(item));
        } else if (gameObject instanceof Tree) {
            Tree tree = (Tree) gameObject;
            graphicObjects.put(tree, new TreeGraphicObject(tree));
        } else if (gameObject instanceof Room) {
            Room room = (Room) gameObject;
            graphicObjects.put(room, new RoomGraphicObject(room));
        } else if (gameObject instanceof IWorkshop) {
            IWorkshop workshop = (IWorkshop) gameObject;
            graphicObjects.put(workshop, new WorkshopGraphicObject(workshop));
        }
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject) {
        assert graphicObjects.containsKey(gameObject);
        graphicObjects.remove(gameObject);
    }

    @Override
    public void componentResized(final ComponentEvent e) {
        canvasWidth = getWidth();
        canvasHeight = getHeight();
        if (canvasWidth <= 0) {
            canvasWidth = 1;
        }
        if (canvasHeight <= 0) {
            canvasHeight = 1;
        }
        // To keep the view centered on the same location it is moved so the corner is where it use to be centered then
        // moved back or something, does that make any sense?
        viewArea.pos.x += viewArea.width / 2;
        viewArea.pos.y += viewArea.height / 2;
        viewArea.width = canvasWidth / SpriteManager.SPRITE_SIZE + 1;
        viewArea.height = canvasHeight / SpriteManager.SPRITE_SIZE + 1;
        viewArea.pos.x -= viewArea.width / 2;
        viewArea.pos.y -= viewArea.height / 2;
        backgroundImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        drawBackgroundRequired = true;
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
    }

    @Override
    public void componentShown(final ComponentEvent e) {
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
    }
}
