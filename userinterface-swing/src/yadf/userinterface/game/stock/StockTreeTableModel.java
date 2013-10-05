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
package yadf.userinterface.game.stock;

import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectManagerListener;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.item.ItemType;
import yadf.simulation.item.ItemTypeManager;

/**
 * The Class StockTreeTableModel.
 */
class StockTreeTableModel extends AbstractTreeTableModel implements IGameObjectManagerListener {

    /** The stock manager. */
    private final IStockManager stockManager;

    /** The root. */
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

    /**
     * Instantiates a new stock tree table model.
     * @param stockManagerTmp the stock manager
     */
    StockTreeTableModel(final IStockManager stockManagerTmp) {
        stockManager = stockManagerTmp;
        stockManager.addGameObjectManagerListener(this);

        List<String> categoryNames = ItemTypeManager.getInstance().getCategories();
        for (String categoryName : categoryNames) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(categoryName);
            root.add(categoryNode);

            Collection<ItemType> itemTypes = ItemTypeManager.getInstance().getItemTypesFromCategory(categoryName);
            for (ItemType itemType : itemTypes) {
                categoryNode.add(new DefaultMutableTreeNode(itemType.name));
            }
        }
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
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        switch (columnIndex) {
        case 0:
            return "Item type";
        case 1:
            return "Total";
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
        DefaultMutableTreeNode stockNode = (DefaultMutableTreeNode) node;
        if (ItemTypeManager.getInstance().getCategories().contains(stockNode.toString())) {
            switch (columnIndex) {
            case 0:
                return stockNode.toString();
            case 1:
                return Integer.valueOf(stockManager.getItemQuantity(stockNode.toString()));
            default:
                return null;
            }
        }
        switch (columnIndex) {
        case 0:
            return stockNode.toString();
        case 1:
            return Integer.valueOf(stockManager.getItemQuantity(ItemTypeManager.getInstance().getItemType(
                    stockNode.toString())));
        default:
            return null;
        }
    }

    @Override
    public void gameObjectAdded(final IGameObject gameObject, final int index) {
        if (gameObject instanceof Item) {
            Item item = (Item) gameObject;
            int categoryIndex = ItemTypeManager.getInstance().getCategories().indexOf(item.getType().category);
            int itemTypeIndex = ItemTypeManager.getInstance().getItemTypesFromCategory(item.getType().category)
                    .indexOf(item.getType());
            TreeNode categoryNode = root.getChildAt(categoryIndex);
            TreeNode itemTypeNode = categoryNode.getChildAt(itemTypeIndex);
            TreePath parentPath = new TreePath(new Object[] { root, categoryNode });
            modelSupport.fireChildChanged(parentPath, itemTypeIndex, itemTypeNode);
        }
    }

    @Override
    public void gameObjectRemoved(final IGameObject gameObject, final int index) {
        if (gameObject instanceof Item) {
            Item item = (Item) gameObject;
            int categoryIndex = ItemTypeManager.getInstance().getCategories().indexOf(item.getType().category);
            int itemTypeIndex = ItemTypeManager.getInstance().getItemTypesFromCategory(item.getType().category)
                    .indexOf(item.getType());
            TreeNode categoryNode = root.getChildAt(categoryIndex);
            TreeNode itemTypeNode = categoryNode.getChildAt(itemTypeIndex);
            TreePath parentPath = new TreePath(new Object[] { root, categoryNode });
            modelSupport.fireChildChanged(parentPath, itemTypeIndex, itemTypeNode);
        }
    }
}
