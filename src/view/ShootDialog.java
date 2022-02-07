package view;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.Timer;

import controller.IDungeonController;

/**
 * ShootDialog to take inputs for distance and direction for shooting arrow.
 */
public class ShootDialog extends JDialog implements ActionListener {
  private final IDungeonController listener;
  private final JSpinner spinner;
  private final IDungeonView view;

  /**
   * Constructs shootDialog object.
   * @param listener  controller object
   * @param view  view object
   */
  public ShootDialog(IDungeonController listener, IDungeonView view) {
    this.listener = listener;
    this.view = view;

    this.setLayout(new BorderLayout());
    this.setBounds(100, 100, 250, 200);

    JPanel jPanel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    constraints.insets = new Insets(3, 3, 3, 3);

    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 0;
    jPanel.add(new JLabel("Enter Distance"), constraints);
    SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(
            1, 1, 5, 1
    );
    spinner = new JSpinner(spinnerNumberModel);

    constraints.gridx = 1;
    jPanel.add(spinner, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    jPanel.add(new JLabel("Choose direction"), constraints);

    JRadioButton btnEast = new JRadioButton("EAST");
    JRadioButton btnWest = new JRadioButton("WEST");
    JRadioButton btnNorth = new JRadioButton("NORTH");
    JRadioButton btnSouth = new JRadioButton("SOUTH");

    btnSouth.addActionListener(this);
    btnEast.addActionListener(this);
    btnNorth.addActionListener(this);
    btnWest.addActionListener(this);

    ButtonGroup group = new ButtonGroup();
    group.add(btnEast);
    group.add(btnWest);
    group.add(btnSouth);
    group.add(btnNorth);

    constraints.gridy = 2;
    jPanel.add(btnEast, constraints);

    constraints.gridy = 3;
    jPanel.add(btnWest, constraints);

    constraints.gridy = 4;
    jPanel.add(btnNorth, constraints);

    constraints.gridy = 5;
    jPanel.add(btnSouth, constraints);

    this.add(jPanel);

    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String direction = ((JRadioButton) e.getSource()).getText();
    int damage = listener.handleShootClick((Integer) spinner.getValue(), direction);
    this.dispose();
    JDialog dialog;
    if (damage == 100) {
      dialog = new JDialog();
      dialog.add(new JLabel("You hear a great howl in the distance"));

    } else if (damage == 50) {
      dialog = new JDialog();
      dialog.add(new JLabel("You hear a howl in the distance"));
    } else {
      dialog = new JDialog();
      dialog.add(new JLabel("You shoot an arrow into darkness"));
    }
    dialog.setBounds(100, 100, 230, 100);
    dialog.setVisible(true);
    Timer timer = new Timer(2000, e1 -> dialog.dispose());
    timer.start();
    timer.setRepeats(false);
    view.refresh();
  }
}
