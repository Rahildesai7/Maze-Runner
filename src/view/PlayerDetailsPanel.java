package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

import javax.swing.JPanel;

import dungeon.ReadOnlyDungeon;

/**
 * PlayerDetailsPanel handle player details in the view.
 */
public class PlayerDetailsPanel extends JPanel {

  private final ReadOnlyDungeon model;

  /**
   * Constructs PlayerDetailsPanel object.
   * @param model Readonly model
   */
  public PlayerDetailsPanel(ReadOnlyDungeon model) {
    this.model = model;
    this.setVisible(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setFont(new Font("Serif", Font.BOLD, 14));
    g2d.drawString("Player Details", 75, 20);

    g2d.setFont(new Font("Serif", Font.PLAIN, 13));
    String[] directions = model.getPlayerStats().split("\\n");

    g2d.drawString(model.playerDesc().split("\\.")[0] + "", 10, 38);
    g2d.drawString(model.playerDesc().split("\\.")[1] + "", 10, 53);

    int y = 88;
    for (int i = 0; i < directions.length; i++) {
      g2d.drawString(directions[i] + "", 10, y + i * 15);
    }
  }
}
