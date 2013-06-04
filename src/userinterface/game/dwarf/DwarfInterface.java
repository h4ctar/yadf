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
package userinterface.game.dwarf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import simulation.character.Dwarf;
import simulation.character.ICharacterListener;
import simulation.character.component.IComponentListener;
import simulation.item.Item;
import simulation.labor.LaborType;
import userinterface.game.WorldCanvas;
import userinterface.misc.ImagePanel;
import userinterface.misc.SpriteManager;

/**
 * The Class DwarfInterface.
 */
public class DwarfInterface extends JInternalFrame implements ICharacterListener, IComponentListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 6213975713592520095L;

    /** The size in pixels of the dwarf image. */
    private static final int IMAGE_SIZE = 200;

    /** The name text field. */
    private JTextField nameTextField;

    /** The profession text field. */
    private JTextField professionTextField;

    /** The alive text field. */
    private JTextField deadTextField;

    /** The item hauling text field. */
    private JTextField itemHaulingTextField;

    /** The tool holding text field. */
    private JTextField toolHoldingTextField;

    /** The health text field. */
    private JTextField healthTextField;

    /** The hunger text field. */
    private JTextField hungerTextField;

    /** The thirst text field. */
    private JTextField thirstTextField;

    /** The dwarf. */
    private Dwarf dwarf;

    /** The lbl lock. */
    private JLabel lockLabel;

    /** The lock text field. */
    private JTextField lockTextField;

    /** The lbl new label. */
    private JLabel imageLabel;
    private JPanel panel_1;
    private JButton btnZoom;
    private JButton btnFollow;

    /**
     * Create the frame.
     */
    public DwarfInterface() {
        setupLayout();
    }

    @Override
    public void charactedChanged() {
        update();
    }

    /**
     * Update.
     */
    @Override
    public void componentChanged() {
        update();
    }

    /**
     * Sets the dwarf.
     * @param dwarfTmp the dwarf
     * @param worldCanvas the world canvas
     */
    public void setDwarf(final Dwarf dwarfTmp, final WorldCanvas worldCanvas) {
        dwarf = dwarfTmp;
        dwarf.addListener(this);
        dwarf.getSkill().addListener(this);
        dwarf.getHealth().addListener(this);
        dwarf.getInventory().addListener(this);
        dwarf.getEatDrink().addListener(this);
        update();
    }

    /**
     * Update.
     */
    public void update() {
        nameTextField.setText(dwarf.getName());
        LaborType profession = dwarf.getSkill().getProfession();
        professionTextField.setText(profession.professionName);
        deadTextField.setText(Boolean.toString(dwarf.isDead()));
        Item haulItem = dwarf.getInventory().getHaulItem();
        itemHaulingTextField.setText(haulItem == null ? "Not hauling" : haulItem.getType().name);
        Item tool = dwarf.getInventory().getToolHolding();
        toolHoldingTextField.setText(tool == null ? "No tool" : tool.getType().name);
        healthTextField.setText(Integer.toString(dwarf.getHealth().getHealth()));
        hungerTextField.setText(Integer.toString(dwarf.getEatDrink().getHunger()));
        thirstTextField.setText(Integer.toString(dwarf.getEatDrink().getThirst()));
        lockTextField.setText(Boolean.toString(dwarf.isLock()));
        Image dwarfImage = SpriteManager.getInstance().getItemSprite(profession.sprite).getImage();
        dwarfImage = dwarfImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_FAST);
        imageLabel.setIcon(new ImageIcon(dwarfImage));
    }

    /**
     * Setup the layout.
     */
    // CHECKSTYLE:OFF
    private void setupLayout() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setClosable(true);
        setTitle("Dwarf interface");
        setBounds(100, 100, 608, 433);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel panel = new ImagePanel();
        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 252, 0, 0, 0 };
        panelLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        panelLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        panelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(panelLayout);
        getContentPane().add(panel, BorderLayout.CENTER);

        imageLabel = new JLabel("");
        imageLabel.setVerticalAlignment(SwingConstants.TOP);
        GridBagConstraints imageLabelConstraints = new GridBagConstraints();
        imageLabelConstraints.gridheight = 9;
        imageLabelConstraints.insets = new Insets(0, 0, 5, 5);
        imageLabelConstraints.gridx = 0;
        imageLabelConstraints.gridy = 0;
        panel.add(imageLabel, imageLabelConstraints);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        GridBagConstraints nameLabeConstraints = new GridBagConstraints();
        nameLabeConstraints.insets = new Insets(0, 0, 5, 5);
        nameLabeConstraints.anchor = GridBagConstraints.EAST;
        nameLabeConstraints.gridx = 1;
        nameLabeConstraints.gridy = 0;
        panel.add(nameLabel, nameLabeConstraints);

        nameTextField = new JTextField();
        nameTextField.setEditable(false);
        GridBagConstraints nameTextFielConstraints = new GridBagConstraints();
        nameTextFielConstraints.insets = new Insets(0, 0, 5, 0);
        nameTextFielConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameTextFielConstraints.gridx = 2;
        nameTextFielConstraints.gridy = 0;
        nameTextField.setColumns(10);
        panel.add(nameTextField, nameTextFielConstraints);

        JLabel professionLabel = new JLabel("Profession:");
        professionLabel.setForeground(Color.WHITE);
        GridBagConstraints professionLabelGridBagConstrints = new GridBagConstraints();
        professionLabelGridBagConstrints.anchor = GridBagConstraints.EAST;
        professionLabelGridBagConstrints.insets = new Insets(0, 0, 5, 5);
        professionLabelGridBagConstrints.gridx = 1;
        professionLabelGridBagConstrints.gridy = 1;
        panel.add(professionLabel, professionLabelGridBagConstrints);

        professionTextField = new JTextField();
        professionTextField.setEditable(false);
        GridBagConstraints professionTextFielConstraints = new GridBagConstraints();
        professionTextFielConstraints.insets = new Insets(0, 0, 5, 0);
        professionTextFielConstraints.fill = GridBagConstraints.HORIZONTAL;
        professionTextFielConstraints.gridx = 2;
        professionTextFielConstraints.gridy = 1;
        professionTextField.setColumns(10);
        panel.add(professionTextField, professionTextFielConstraints);

        JLabel deadLabel = new JLabel("Dead:");
        deadLabel.setForeground(Color.WHITE);
        GridBagConstraints deadLabeConstraints = new GridBagConstraints();
        deadLabeConstraints.anchor = GridBagConstraints.EAST;
        deadLabeConstraints.insets = new Insets(0, 0, 5, 5);
        deadLabeConstraints.gridx = 1;
        deadLabeConstraints.gridy = 2;
        panel.add(deadLabel, deadLabeConstraints);

        deadTextField = new JTextField();
        deadTextField.setEditable(false);
        GridBagConstraints deadTextFielConstraints = new GridBagConstraints();
        deadTextFielConstraints.insets = new Insets(0, 0, 5, 0);
        deadTextFielConstraints.fill = GridBagConstraints.HORIZONTAL;
        deadTextFielConstraints.gridx = 2;
        deadTextFielConstraints.gridy = 2;
        panel.add(deadTextField, deadTextFielConstraints);
        deadTextField.setColumns(10);

        JLabel itemHaulingLabel = new JLabel("Item Hauling:");
        itemHaulingLabel.setForeground(Color.WHITE);
        GridBagConstraints itemHaulingLabeConstraints = new GridBagConstraints();
        itemHaulingLabeConstraints.anchor = GridBagConstraints.EAST;
        itemHaulingLabeConstraints.insets = new Insets(0, 0, 5, 5);
        itemHaulingLabeConstraints.gridx = 1;
        itemHaulingLabeConstraints.gridy = 3;
        panel.add(itemHaulingLabel, itemHaulingLabeConstraints);

        itemHaulingTextField = new JTextField();
        itemHaulingTextField.setEditable(false);
        GridBagConstraints itemHaulingTextFielConstraints = new GridBagConstraints();
        itemHaulingTextFielConstraints.insets = new Insets(0, 0, 5, 0);
        itemHaulingTextFielConstraints.fill = GridBagConstraints.HORIZONTAL;
        itemHaulingTextFielConstraints.gridx = 2;
        itemHaulingTextFielConstraints.gridy = 3;
        panel.add(itemHaulingTextField, itemHaulingTextFielConstraints);
        itemHaulingTextField.setColumns(10);

        JLabel toolHoldingLabel = new JLabel("Tool holding:");
        toolHoldingLabel.setForeground(Color.WHITE);
        GridBagConstraints toolHoldingConstraints = new GridBagConstraints();
        toolHoldingConstraints.anchor = GridBagConstraints.EAST;
        toolHoldingConstraints.insets = new Insets(0, 0, 5, 5);
        toolHoldingConstraints.gridx = 1;
        toolHoldingConstraints.gridy = 4;
        panel.add(toolHoldingLabel, toolHoldingConstraints);

        toolHoldingTextField = new JTextField();
        toolHoldingTextField.setEditable(false);
        GridBagConstraints toolHoldingTextFieldConstraints = new GridBagConstraints();
        toolHoldingTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
        toolHoldingTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        toolHoldingTextFieldConstraints.gridx = 2;
        toolHoldingTextFieldConstraints.gridy = 4;
        panel.add(toolHoldingTextField, toolHoldingTextFieldConstraints);
        toolHoldingTextField.setColumns(10);

        JLabel healthLabel = new JLabel("Health:");
        healthLabel.setForeground(Color.WHITE);
        GridBagConstraints healthLabelConstraints = new GridBagConstraints();
        healthLabelConstraints.anchor = GridBagConstraints.EAST;
        healthLabelConstraints.insets = new Insets(0, 0, 5, 5);
        healthLabelConstraints.gridx = 1;
        healthLabelConstraints.gridy = 5;
        panel.add(healthLabel, healthLabelConstraints);

        healthTextField = new JTextField();
        healthTextField.setEditable(false);
        GridBagConstraints healthTextFieldConstraints = new GridBagConstraints();
        healthTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
        healthTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        healthTextFieldConstraints.gridx = 2;
        healthTextFieldConstraints.gridy = 5;
        panel.add(healthTextField, healthTextFieldConstraints);
        healthTextField.setColumns(10);

        JLabel hungerLabel = new JLabel("Hunger:");
        hungerLabel.setForeground(Color.WHITE);
        GridBagConstraints hungerLabelConstraints = new GridBagConstraints();
        hungerLabelConstraints.anchor = GridBagConstraints.EAST;
        hungerLabelConstraints.insets = new Insets(0, 0, 5, 5);
        hungerLabelConstraints.gridx = 1;
        hungerLabelConstraints.gridy = 6;
        panel.add(hungerLabel, hungerLabelConstraints);

        hungerTextField = new JTextField();
        hungerTextField.setEditable(false);
        GridBagConstraints hungerTextFieldConstraints = new GridBagConstraints();
        hungerTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
        hungerTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        hungerTextFieldConstraints.gridx = 2;
        hungerTextFieldConstraints.gridy = 6;
        panel.add(hungerTextField, hungerTextFieldConstraints);
        hungerTextField.setColumns(10);

        JLabel thirstLabel = new JLabel("Thirst:");
        thirstLabel.setForeground(Color.WHITE);
        GridBagConstraints thirstLabelConstraints = new GridBagConstraints();
        thirstLabelConstraints.anchor = GridBagConstraints.EAST;
        thirstLabelConstraints.insets = new Insets(0, 0, 5, 5);
        thirstLabelConstraints.gridx = 1;
        thirstLabelConstraints.gridy = 7;
        panel.add(thirstLabel, thirstLabelConstraints);

        thirstTextField = new JTextField();
        thirstTextField.setEditable(false);
        GridBagConstraints thirstTextFieldConstraints = new GridBagConstraints();
        thirstTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
        thirstTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        thirstTextFieldConstraints.gridx = 2;
        thirstTextFieldConstraints.gridy = 7;
        panel.add(thirstTextField, thirstTextFieldConstraints);
        thirstTextField.setColumns(10);

        lockLabel = new JLabel("Lock:");
        lockLabel.setForeground(Color.WHITE);
        GridBagConstraints lockLabelConstraints = new GridBagConstraints();
        lockLabelConstraints.anchor = GridBagConstraints.EAST;
        lockLabelConstraints.insets = new Insets(0, 0, 5, 5);
        lockLabelConstraints.gridx = 1;
        lockLabelConstraints.gridy = 8;
        panel.add(lockLabel, lockLabelConstraints);

        lockTextField = new JTextField();
        lockTextField.setEditable(false);
        GridBagConstraints lockTextFieldConstraints = new GridBagConstraints();
        lockTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
        lockTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        lockTextFieldConstraints.gridx = 2;
        lockTextFieldConstraints.gridy = 8;
        panel.add(lockTextField, lockTextFieldConstraints);
        lockTextField.setColumns(10);

        panel_1 = new JPanel();
        panel_1.setOpaque(false);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridwidth = 3;
        gbc_panel_1.insets = new Insets(0, 0, 0, 5);
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 9;
        panel.add(panel_1, gbc_panel_1);

        btnZoom = new JButton("Zoom");
        panel_1.add(btnZoom);

        btnFollow = new JButton("Follow");
        panel_1.add(btnFollow);
        // CHECKSTYLE:ON
    }
}
