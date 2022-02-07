package view;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Random;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;

import controller.DungeonGraphicalController;
import controller.IDungeonController;
import dungeon.Dungeon;
import dungeon.IDungeon;

/**
 * DungeonCreation view to change game configuration.
 */
public class DungeonCreationView extends JFrame implements IDungeonView, ActionListener {

  private IDungeon model;

  private final JSpinner txtRow;
  private final JSpinner txtCol;
  private final JSpinner txtInter;
  private final JSpinner txtEquipment;
  private final JSpinner txtMonster;
  private final JRadioButton btnFalse;

  /**
   * constructs DungeonCreationView object.
   * @param model dungeon model
   */
  public DungeonCreationView(IDungeon model) {
    super("Dungeon game");

    this.model = model;

    this.setLayout(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.insets = new Insets(5, 25, 5, 0);
    this.add(new JLabel("Welcome to the Dungeon game!!"), constraints);

    constraints.insets = new Insets(5, 5, 5, 2);
    constraints.gridx = 0;
    constraints.gridy = 1;
    this.add(new JLabel("Rows"), constraints);

    constraints.gridx = 1;
    SpinnerNumberModel rowSpinner = new SpinnerNumberModel(model.getRows(), 5, 100, 1);
    txtRow = new JSpinner(rowSpinner);
    this.add(txtRow, constraints);

    constraints.gridx = 0;
    constraints.gridy = 2;
    this.add(new JLabel("Columns"), constraints);

    constraints.gridx = 1;
    SpinnerNumberModel colSpinner = new SpinnerNumberModel(model.getColumns(), 5, 100, 1);
    txtCol = new JSpinner(colSpinner);
    this.add(txtCol, constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    this.add(new JLabel("Wrapping"), constraints);

    JRadioButton btnTrue = new JRadioButton("true");
    btnFalse = new JRadioButton("false");
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(btnFalse);
    buttonGroup.add(btnTrue);

    if (model.isWrapping()) {
      btnTrue.setSelected(true);
    } else {
      btnFalse.setSelected(true);
    }

    constraints.gridx = 1;
    this.add(btnTrue, constraints);

    constraints.gridx = 2;
    this.add(btnFalse, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    this.add(new JLabel("Interconnectivity"), constraints);

    constraints.gridx = 1;
    SpinnerNumberModel interSpin = new SpinnerNumberModel(model.getInterconnectivity(), 0,
            (Integer) txtRow.getValue() * (Integer) txtCol.getValue(), 1);
    txtInter = new JSpinner(interSpin);
    this.add(txtInter, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    this.add(new JLabel("Equipment Percent"), constraints);

    constraints.gridx = 1;
    SpinnerNumberModel equipSpin = new SpinnerNumberModel(model.getTreasureAmount(), 0, 100, 1);
    txtEquipment = new JSpinner(equipSpin);
    this.add(txtEquipment, constraints);


    constraints.gridx = 0;
    constraints.gridy = 6;
    this.add(new JLabel("Monster count"), constraints);

    constraints.gridx = 1;
    SpinnerNumberModel monsterSpinner = new SpinnerNumberModel(model.getMonsterCount(), 0, 10, 1);
    txtMonster = new JSpinner(monsterSpinner);
    this.add(txtMonster, constraints);

    constraints.gridx = 0;
    constraints.gridy = 7;
    JButton btnOk = new JButton("Start Game");
    btnOk.addActionListener(this);
    this.add(btnOk, constraints);

    this.setSize(400, 400);
  }

  @Override
  public void addClickListener(IDungeonController listener) {
    //do nothing.
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void bindKeys(IDungeonController listener) {
    // do nothing.
  }

  @Override
  public void close() {
    this.dispose();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      txtRow.commitEdit();
      txtCol.commitEdit();
      txtInter.commitEdit();
      txtEquipment.commitEdit();
      txtMonster.commitEdit();
    } catch (ParseException parseException) {
      parseException.printStackTrace();
    }
    int row = (Integer) txtRow.getValue();
    int col = (Integer) txtCol.getValue();
    int interconnectivity = (Integer) txtInter.getValue();
    int equipAmount = (Integer) txtEquipment.getValue();
    int monsterCount = (Integer) txtMonster.getValue();

    boolean wrapping;

    wrapping = !btnFalse.isSelected();

    Random random = new Random(1);

    model = new Dungeon(row, col, interconnectivity, wrapping, equipAmount, monsterCount, random);

    IDungeonView view = new DungeonView(model);

    IDungeonController controller = new DungeonGraphicalController(view, model);

    JDialog dialog = new JDialog();
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints dialogC = new GridBagConstraints();
    dialogC.fill = GridBagConstraints.HORIZONTAL;
    dialogC.gridx = 0;
    dialogC.gridy = 0;
    dialog.add(new JLabel("You are in cave: " + model.getStartCave()), dialogC);
    dialogC.gridy = 1;
    dialog.add(new JLabel("You need to reach: " + model.getEndCave()), dialogC);
    JButton btnContinue = new JButton("Continue");
    btnContinue.addActionListener(e1 -> {
      dialog.dispose();
      controller.playGame();
      close();
    });
    dialogC.gridy = 2;
    dialog.add(btnContinue, dialogC);
    dialog.setBounds(100, 100, 200, 200);
    dialog.setVisible(true);
  }

}
