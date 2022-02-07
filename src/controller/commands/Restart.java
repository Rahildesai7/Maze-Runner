package controller.commands;

import java.util.Random;

import controller.DungeonGraphicalController;
import controller.IDungeonController;
import dungeon.Dungeon;
import dungeon.IDungeon;
import dungeon.ReadOnlyDungeon;
import view.DungeonView;

/**
 * Restart class to handle restart game from menu bar.
 */
public class Restart {

  /**
   * Method to restart the dungeon game.
   * @param model ReadOnly Dungeon model
   */
  public void run(ReadOnlyDungeon model) {
    Random random = new Random(1);
    int rows = model.getRows();
    int col = model.getColumns();
    int inter = model.getInterconnectivity();
    boolean wrapping = model.isWrapping();
    int treasureAmount = model.getTreasureAmount();
    int monsterCount = model.getMonsterCount();
    IDungeon dungeon = new Dungeon(
            rows, col, inter, wrapping, treasureAmount, monsterCount, random);
    DungeonView newView = new DungeonView(dungeon);
    IDungeonController controller = new DungeonGraphicalController(newView, dungeon);
    controller.playGame();
  }
}
