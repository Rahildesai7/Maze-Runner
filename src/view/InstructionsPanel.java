package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Instruction panel that displays instructions to play the game.
 */
public class InstructionsPanel extends JPanel {
  /**
   * Constructs Instruction panel object.
   */
  public InstructionsPanel() {

    this.setLayout(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();

    constraints.fill = GridBagConstraints.BOTH;

    JPanel insPanel = new JPanel();
    insPanel.setLayout(new GridLayout(3, 1));
    insPanel.add(new JLabel("Press 'P' to pickup"));
    insPanel.add(new JLabel("Press 'S' to shoot"));
    insPanel.add(new JLabel("Press arrow keys/ click on the screen to move"));

    this.add(insPanel, constraints);
  }
}
