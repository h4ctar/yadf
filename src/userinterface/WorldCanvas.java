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
package userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import logger.Logger;
import simulation.Player;
import simulation.Region;
import simulation.Tree;
import simulation.character.Animal;
import simulation.character.Dwarf;
import simulation.character.Goblin;
import simulation.farm.Farm;
import simulation.farm.FarmPlot;
import simulation.item.Item;
import simulation.job.JobManager;
import simulation.job.designation.AbstractDesignation;
import simulation.labor.LaborType;
import simulation.map.BlockType;
import simulation.map.IMapListener;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.room.Room;
import simulation.stock.Stockpile;
import simulation.workshop.Workshop;

/**
 * The Class WorldCanvas.
 */
public class WorldCanvas extends JComponent implements IMapListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The canvas width. */
    private int canvasWidth;

    /** The canvas height. */
    private int canvasHeight;

    /** The view position. */
    private MapIndex viewPosition = new MapIndex();

    /** The view size. */
    private MapIndex viewSize = new MapIndex();

    /** The selection. */
    private MapArea selection;

    /** The selection valid. */
    private boolean selectionValid;

    /** The player. */
    private Player player;

    /** The region. */
    private Region region;

    /** The colour of designations. */
    private static final Color DESIGNATION_COLOUR = new Color(0.8f, 0.5f, 0.5f, 0.8f);

    /** The colour of the mouse. */
    private static final Color MOUSE_COLOUR = new Color(0.5f, 0.5f, 0.8f, 0.8f);

    /** The colour of a valid selection. */
    private static final Color VALID_SELECTION_COLOUR = new Color(0.5f, 0.8f, 0.5f, 0.8f);

    /** The colour of an invalid selection. */
    private static final Color INVALID_SELECTION_COLOUR = new Color(0.8f, 0.5f, 0.5f, 0.8f);

    /** The background image. */
    private BufferedImage backgroundImage = null;

    /** Does the background need to be redrawn. */
    private boolean drawBackgroundRequired;

    /**
     * Instantiates a new world canvas.
     */
    public WorldCanvas() {
        setIgnoreRepaint(true);
        setDoubleBuffered(true);
    }

    /**
     * Draws a designation.
     * 
     * @param g the g
     */
    public void drawDesignations(final Graphics g) {
        JobManager jobManager = player.getJobManager();
        AbstractDesignation[] designations = jobManager.getDesignations();

        for (AbstractDesignation designation : designations) {
            List<MapIndex> mapIndicies = new ArrayList<>(designation.getMapIndicies());
            for (MapIndex mapIndex : mapIndicies) {
                if (mapIndex.z == viewPosition.z) {
                    g.setColor(DESIGNATION_COLOUR);
                    g.fillRect((mapIndex.x - viewPosition.x) * SpriteManager.SPRITE_SIZE,
                            (mapIndex.y - viewPosition.y) * SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE);
                }
            }
        }
    }

    /**
     * Draw mouse.
     * 
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
     * 
     * @param g the g
     */
    public void drawSelection(final Graphics g) {
        if (selectionValid) {
            g.setColor(VALID_SELECTION_COLOUR);
        } else {
            g.setColor(INVALID_SELECTION_COLOUR);
        }

        g.fillRect((selection.pos.x - viewPosition.x) * SpriteManager.SPRITE_SIZE, (selection.pos.y - viewPosition.y)
                * SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE * selection.width, SpriteManager.SPRITE_SIZE
                * selection.height);
    }

    /**
     * Gets the mouse index.
     * 
     * @param x the x
     * @param y the y
     * @return the mouse index
     */
    public MapIndex getMouseIndex(final int x, final int y) {
        return viewPosition.add(x / SpriteManager.SPRITE_SIZE, y / SpriteManager.SPRITE_SIZE, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFocusTraversable() {
        return true;
    }

    @Override
    public void mapChanged() {
        drawBackgroundRequired = true;
    }

    /**
     * Move view.
     * 
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public void moveView(final int x, final int y, final int z) {
        viewPosition = viewPosition.add(x, y, z);
        drawBackgroundRequired = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(final Graphics g) {
        if (region == null) {
            return;
        }

        if (drawBackgroundRequired) {
            drawBlocks();
        }

        g.setColor(Color.BLACK);
        g.drawImage(backgroundImage, 0, 0, null);

        drawTrees(g);
        drawStockpiles(g);
        drawDesignations(g);
        drawFarms(g);
        drawRooms(g);
        drawWorkshops(g);
        drawItems(g);
        drawAnimals(g);
        drawDwarfs(g);
        drawGoblins(g);

        if (selection != null) {
            drawSelection(g);
        } else {
            drawMouse(g);
        }
    }

    /**
     * Sets the player.
     * 
     * @param playerTmp the player
     */
    public void setPlayer(final Player playerTmp) {
        player = playerTmp;
    }

    /**
     * Sets the region.
     * 
     * @param regionTmp the new region
     */
    public void setRegion(final Region regionTmp) {
        region = regionTmp;
        region.getMap().addListener(this);
        setSize(getSize());
    }

    /**
     * Sets the selection.
     * 
     * @param selectionTmp the selection
     * @param selectionValidTmp the selection valid
     */
    public void setSelection(final MapArea selectionTmp, final boolean selectionValidTmp) {
        selection = selectionTmp;
        selectionValid = selectionValidTmp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(final Dimension d) {
        canvasWidth = d.width;
        canvasHeight = d.height;

        if (canvasWidth <= 0) {
            canvasWidth = 1;
        }

        if (canvasHeight <= 0) {
            canvasHeight = 1;
        }

        viewSize = new MapIndex(canvasWidth / SpriteManager.SPRITE_SIZE + 1, canvasHeight / SpriteManager.SPRITE_SIZE
                + 1, 0);

        if (region != null) {
            MapIndex mapSize = region.getMap().getMapSize();
            viewPosition = new MapIndex((mapSize.x - viewSize.x) / 2, (mapSize.y - viewSize.y) / 2, 0);
            viewPosition.z = region.getMap().getHeight(mapSize.x / 2, mapSize.y / 2);
        }

        super.setSize(d);
        backgroundImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        drawBackgroundRequired = true;
    }

    /**
     * Block sprite.
     * 
     * @param block the block
     * @return the sprite
     */
    // TODO: what the hell is this?
    private Sprite blockSprite(final BlockType block) {
        if (block.toString().startsWith("MINE_")) {
            return SpriteManager.getInstance().getBlockSprite(
                    BlockType.valueOf(block.toString().substring(5)).ordinal());
        }

        return SpriteManager.getInstance().getBlockSprite(block.ordinal());
    }

    /**
     * Draw animals.
     * 
     * @param g the g
     */
    private void drawAnimals(final Graphics g) {
        Sprite animalSprite = SpriteManager.getInstance().getItemSprite(48);
        for (Animal animal : region.getAnimals()) {
            MapIndex position = animal.getPosition();
            if (position.z == viewPosition.z) {
                int x = (position.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                int y = (position.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                if (x >= 0 && x < canvasWidth && y >= 0 && y < canvasHeight) {
                    animalSprite.draw(g, x, y);
                }
            }
        }
    }

    /**
     * Draw blocks.
     */
    private void drawBlocks() {
        if (region == null) {
            // TODO: remove this check
            return;
        }

        Logger.getInstance().log(this, "Draw blocks");
        RegionMap map = region.getMap();
        Sprite tile;
        Graphics g = backgroundImage.getGraphics();

        for (int x = 0; x < viewSize.x; x++) {
            for (int y = 0; y < viewSize.y; y++) {
                BlockType block = map.getBlock(viewPosition.add(x, y, -1));
                BlockType blockBelow = map.getBlock(viewPosition.add(x, y, -2));
                BlockType blockAbove = map.getBlock(viewPosition.add(x, y, 0));

                if (blockBelow != BlockType.RAMP && blockBelow != BlockType.STAIR) {
                    tile = blockSprite(blockBelow);
                    tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                }
                if (block != BlockType.RAMP && block != BlockType.STAIR) {
                    g.setColor(new Color(0.5f, 0.5f, 0.7f, 0.5f));
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }

                tile = blockSprite(block);
                tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                if (block == BlockType.RAMP || block == BlockType.STAIR) {
                    g.setColor(new Color(0.5f, 0.5f, 0.7f, 0.5f));
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }

                if (blockAbove == BlockType.RAMP || blockAbove == BlockType.STAIR) {
                    tile = blockSprite(blockAbove);
                    tile.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                } else if (!blockAbove.isStandIn) {
                    g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
                    g.fillRect(x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE,
                            SpriteManager.SPRITE_SIZE, SpriteManager.SPRITE_SIZE);
                }
            }
        }
        drawBackgroundRequired = false;
    }

    /**
     * Draw dwarfs.
     * 
     * @param g the g
     */
    private void drawDwarfs(final Graphics g) {
        List<Player> players = region.getPlayers();
        for (Player playerTmp : players) {
            List<Dwarf> dwarfs = playerTmp.getDwarfs();
            for (Dwarf dwarf : dwarfs) {
                MapIndex position = dwarf.getPosition();
                if (position.z == viewPosition.z) {
                    int x = (position.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                    int y = (position.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                    if (x >= 0 && x < canvasWidth && y >= 0 && y < canvasHeight) {
                        Sprite dwarfSprite;
                        if (dwarf.isDead()) {
                            dwarfSprite = SpriteManager.getInstance().getItemSprite(44);
                        } else {
                            LaborType profession = dwarf.getSkill().getProfession();

                            if (profession == null) {
                                dwarfSprite = SpriteManager.getInstance().getItemSprite(32);
                            } else {
                                dwarfSprite = SpriteManager.getInstance().getItemSprite(profession.sprite);
                            }
                        }
                        dwarfSprite.draw(g, x, y);
                    }
                }
            }
        }
    }

    /**
     * Draw farms.
     * 
     * @param g the g
     */
    private void drawFarms(final Graphics g) {
        Sprite tillSprite = SpriteManager.getInstance().getItemSprite(64);
        Sprite plantSprite = SpriteManager.getInstance().getItemSprite(65);
        Sprite growSprite = SpriteManager.getInstance().getItemSprite(66);
        Sprite harvestSprite = SpriteManager.getInstance().getItemSprite(67);
        g.setColor(new Color(0.4f, 0.1f, 0.0f, 0.8f));
        List<Player> players = region.getPlayers();
        for (Player player : players) {
            List<Farm> farms = player.getFarms();
            for (Farm farm : farms) {
                MapArea area = farm.getArea();

                if (viewPosition.z != area.pos.z) {
                    continue;
                }

                List<FarmPlot> farmPlots = farm.getPlots();
                for (FarmPlot farmPlot : farmPlots) {
                    MapIndex pos = farmPlot.getPosition();
                    int x = (pos.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                    int y = (pos.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;

                    if (farmPlot.getState() == FarmPlot.State.TILL) {
                        tillSprite.draw(g, x, y);
                    } else if (farmPlot.getState() == FarmPlot.State.PLANT) {
                        plantSprite.draw(g, x, y);
                    } else if (farmPlot.getState() == FarmPlot.State.GROW) {
                        growSprite.draw(g, x, y);
                    } else if (farmPlot.getState() == FarmPlot.State.HARVEST) {
                        harvestSprite.draw(g, x, y);
                    }
                }
            }
        }
    }

    /**
     * Draw goblins.
     * 
     * @param g the g
     */
    private void drawGoblins(final Graphics g) {
        Sprite goblinSprite = SpriteManager.getInstance().getItemSprite(80);
        List<Goblin> goblins = region.getGoblins();
        for (Goblin goblin : goblins) {
            MapIndex position = goblin.getPosition();
            if (position.z == viewPosition.z) {
                int x = (position.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                int y = (position.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                if (x >= 0 && x < canvasWidth && y >= 0 && y < canvasHeight) {
                    goblinSprite.draw(g, x, y);
                }
            }
        }
    }

    /**
     * Draw items.
     * 
     * @param g the g
     */
    private void drawItems(final Graphics g) {
        List<Player> players = region.getPlayers();
        for (Player player : players) {
            List<Item> items = player.getStockManager().getItems();
            for (Item item : items) {
                MapIndex position = item.getPosition();
                if (position.z == viewPosition.z) {
                    int x = (position.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                    int y = (position.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                    if (x >= 0 && x < canvasWidth && y >= 0 && y < canvasHeight) {
                        Sprite itemSprite = SpriteManager.getInstance().getItemSprite(item.getType().sprite);
                        itemSprite.draw(g, x, y);
                    }
                }
            }
        }
    }

    /**
     * Draw rooms.
     * 
     * @param g the g
     */
    private void drawRooms(final Graphics g) {
        List<Player> players = region.getPlayers();
        for (Player player : players) {
            List<Room> rooms = player.getRooms();
            for (Room room : rooms) {
                MapArea area = room.getArea();
                if (viewPosition.z != area.pos.z) {
                    continue;
                }
                int x = (area.pos.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                int y = (area.pos.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                g.setColor(new Color(0.7f, 0.7f, 0.7f, 0.8f));
                g.fillRect(x, y, area.width * SpriteManager.SPRITE_SIZE, area.height * SpriteManager.SPRITE_SIZE);
            }
        }
    }

    /**
     * Draw stockpiles.
     * 
     * @param g the g
     */
    private void drawStockpiles(final Graphics g) {
        List<Player> players = region.getPlayers();
        for (Player player : players) {
            List<Stockpile> stockpiles = player.getStockManager().getStockpiles();
            for (Stockpile stockpile : stockpiles) {
                MapArea area = stockpile.getArea();

                if (viewPosition.z != area.pos.z) {
                    continue;
                }

                int x = (area.pos.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                int y = (area.pos.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;

                g.setColor(new Color(0.7f, 0.6f, 0.5f, 0.8f));
                g.fillRect(x, y, area.width * SpriteManager.SPRITE_SIZE, area.height * SpriteManager.SPRITE_SIZE);

                List<Item> items = stockpile.getItems();
                for (Item item : items) {
                    MapIndex position = item.getPosition();
                    if (position.z == viewPosition.z) {
                        int x2 = (position.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                        int y2 = (position.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                        if (x2 >= 0 && x2 < canvasWidth && y2 >= 0 && y2 < canvasHeight) {
                            Sprite itemSprite = SpriteManager.getInstance().getItemSprite(item.getType().sprite);
                            itemSprite.draw(g, x2, y2);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw the trees.
     * @param g
     */
    private void drawTrees(final Graphics g) {
        Sprite treeSprite = SpriteManager.getInstance().getItemSprite(255);
        List<Tree> trees = region.getTrees();
        for (Tree tree : trees) {
            MapIndex position = tree.getPosition();
            if (position.z == viewPosition.z) {
                int x = position.x - viewPosition.x;
                int y = position.y - viewPosition.y;
                if (x >= 0 && x < viewSize.x && y >= 0 && y < viewSize.y) {
                    treeSprite.draw(g, x * SpriteManager.SPRITE_SIZE, y * SpriteManager.SPRITE_SIZE);
                }
            }
        }
    }

    /**
     * Draw workshops.
     * 
     * @param g the g
     */
    private void drawWorkshops(final Graphics g) {
        List<Player> players = region.getPlayers();
        for (Player player : players) {
            List<Workshop> workshops = player.getWorkshops();
            for (Workshop workshop : workshops) {
                MapArea area = new MapArea(workshop.getPosition(), 3, 3);
                if (viewPosition.z != area.pos.z) {
                    continue;
                }
                int x = (area.pos.x - viewPosition.x) * SpriteManager.SPRITE_SIZE;
                int y = (area.pos.y - viewPosition.y) * SpriteManager.SPRITE_SIZE;
                Sprite workshopSprite = SpriteManager.getInstance().getWorkshopSprite(workshop.getType().sprite);
                workshopSprite.draw(g, x, y);
            }
        }
    }
}
