package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import controller.IDungeonController;

/**
 * Treasure Dialog to support pickup of treasure and arrows.
 */
public class TreasureDialog extends JDialog implements ActionListener {

  private final IDungeonController listener;
  private final IDungeonView view;

  /**
   * Constructs treasure dialog object.
   * @param listener  Dungeon controller
   * @param view  Dungeon view
   */
  public TreasureDialog(IDungeonController listener, IDungeonView view) {
    this.listener = listener;
    this.view = view;

    this.setBounds(100, 100, 200, 200);
    this.setLayout(new GridLayout(5, 1));
    this.add(new JLabel("What would you like to pick?"));

    ButtonGroup buttonGroup = new ButtonGroup();

    JRadioButton btnSapphire = new JRadioButton("Sapphire", false);
    JRadioButton btnDiamond = new JRadioButton("Diamond", false);
    JRadioButton btnRubies = new JRadioButton("Rubies", false);
    JRadioButton btnArrows = new JRadioButton("Arrows", false);

    btnDiamond.addActionListener(this);
    btnRubies.addActionListener(this);
    btnSapphire.addActionListener(this);
    btnArrows.addActionListener(this);

    buttonGroup.add(btnSapphire);
    buttonGroup.add(btnDiamond);
    buttonGroup.add(btnRubies);
    buttonGroup.add(btnArrows);

    this.add(btnSapphire);
    this.add(btnDiamond);
    this.add(btnRubies);
    this.add(btnArrows);

    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String treasure = ((JRadioButton) e.getSource()).getText();
    listener.handlePickupClick(treasure);
    view.refresh();
    this.dispose();
  }
}
