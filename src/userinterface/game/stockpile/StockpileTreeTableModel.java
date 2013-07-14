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
package userinterface.game.stockpile;

import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import simulation.IGameObject;
import simulation.IGameObjectManagerListener;
import simulation.IPlayer;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.item.Stockpile;
import userinterface.game.labor.LaborNode;
import controller.AbstractController;
import controller.command.SetStockpileCommand;

/**
 * The Class StockpileTreeTableModel.
 */
class StockpileTreeTableModel extends AbstractTreeTableModel implements IGameObjectManagerListener {

    /** The stockpile. */
    private final Stockpile stockpile;

    /** The controller. */
    private final AbstractController controller;

    /** The player. */
    private final IPlayer player;

    /** The root. */
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

    /**
     * Instantiates a new stockpile tree table model.
     * @param stockpileTmp the stockpile
     * @param controllerTmp the controller
     * @param playerTmp the player
     */
    StockpileTreeTableModel(final Stockpile stockpileTmp, final AbstractController controllerTmp,
            final IPlayer playerTmp) {
        stockpile = stockpileTmp;
        controller = controllerTmp;
        player = playerTmp;

        List<String> categoryNames = ItemTypeManager.getInstance().getCategories();
        for (String categoryName : categoryNames) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(categoryName);
            root.add(categoryNode);

            Collection<ItemType> itemTypes = ItemTypeManager.getInstance().getItemTypesFromCategory(categoryName);
            for (ItemType itemType : itemTypes) {
                categoryNode.add(new DefaultMutableTreeNode(itemType.name));
            }
        }

        stockpile.addGameObjectManagerListener(this);
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        DefaultMutableTreeNode stockNode = (DefaultMutableTreeNode) parent;
        return stockNode.getChildAt(index);
    }

    @Override
    public int getChildCount(final Object parent) {
        DefaultMutableTreeNode stockNode = (DefaultMutableTreeNode) parent;
        return stockNode.getChildCount();
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }

        if (columnIndex == 1) {
            return Boolean.class;
        }

        return LaborNode.class;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        switch (columnIndex) {
        case 0:
            return "Item type";
        case 1:
            return "Accept";
        case 2:
            return "Count";
        default:
            return null;
        }
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        DefaultMutableTreeNode stockParentNode = (DefaultMutableTreeNode) parent;
        DefaultMutableTreeNode stockChildNode = (DefaultMutableTreeNode) child;
        return stockParentNode.getIndex(stockChildNode);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getValueAt(final Object node, final int columnIndex) {
        String name = ((DefaultMutableTreeNode) node).toString();

        if (name == null) {
            return null;
        }

        boolean isCategory = ItemTypeManager.getInstance().getCategories().contains(name);
        switch (columnIndex) {
        case 0:
            return name;
        case 1:
            boolean acceptsItemType;
            if (isCategory) {
                acceptsItemType = stockpile.hasAllItemTypesInCategory(name);
            } else {
                acceptsItemType = stockpile.isItemTypeAccepted(name);
            }
            return new Boolean(acceptsItemType);
        case 2:
            if (isCategory) {
                return new Integer(stockpile.getItemQuantity(name));
            }
            return new Integer(stockpile.getItemQuantity(ItemTypeManager.getInstance().getItemType(name)));
        default:
            return null;
        }
    }

    @Override
    public boolean isCellEditable(final Object node, final int columnIndex) {
        if (columnIndex == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(final Object value, final Object node, final int columnIndex) {
        String name = ((DefaultMutableTreeNode) node).toString();

        boolean isCategory = ItemTypeManager.getInstance().getCategories().contains(name);
        boolean checked = ((Boolean) value).booleanValue();

        if (isCategory) {
            for (ItemType itemType : ItemTypeManager.getInstance().getItemTypesFromCategory(name)) {
                controller.addCommand(new SetStockpileCommand(player, stockpile.getId(), itemType.name, checked));
            }
        } else {
            controller.addCommand(new SetStockpileCommand(player, stockpile.getId(), name, checked));
        }
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject, final int index) {
        assert gameObject instanceof Item;
        Item item = (Item) gameObject;
        int categoryIndex = ItemTypeManager.getInstance().getCategories().indexOf(item.getType().category);
        int itemTypeIndex = ItemTypeManager.getInstance().getItemTypesFromCategory(item.getType().category)
                .indexOf(item.getType());
        TreeNode categoryNode = root.getChildAt(categoryIndex);
        TreeNode itemTypeNode = categoryNode.getChildAt(itemTypeIndex);
        TreePath parentPath = new TreePath(new Object[] { root, categoryNode });
        modelSupport.fireChildChanged(parentPath, itemTypeIndex, itemTypeNode);
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject, final int index) {
        assert gameObject instanceof Item;
        // TODO Auto-generated method stub
    }
}
