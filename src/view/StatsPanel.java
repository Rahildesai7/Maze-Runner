package view;

import java.awt.GridLayout;
import javax.swing.JPanel;

import dungeon.ReadOnlyDungeon;

/**
 * StatsPanel to display stats of the player at each location.
 */
public class StatsPanel extends JPanel {

  private final CaveDetailsPanel caveDetails;
  private final PlayerDetailsPanel playerDetails;

  /**
   * Constructs StatePanel object.
   *
   * @param model ReadOnly Dungeon model.
   */
  public StatsPanel(ReadOnlyDungeon model) {
    caveDetails = new CaveDetailsPanel(model);
    playerDetails = new PlayerDetailsPanel(model);
    run();
  }

  /**
   * Run method to setup stats panel.
   */
  protected void run() {
    this.setLayout(new GridLayout(2, 1));
    this.add(caveDetails);
    this.add(playerDetails);
  }

}
