import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import controller.DungeonController;
import controller.DungeonGraphicalController;
import controller.IDungeonController;
import dungeon.Dungeon;
import dungeon.IDungeon;
import view.DungeonCreationView;
import view.IDungeonView;

/**
 * Driver class for Dungeon game.
 */
public class Driver {

  /**
   * Driver method to simulate Dungeon game by passing the dungeon model to the controller.
   *
   * @param args not used
   */
  public static void main(String[] args) {

    final Random random = new Random(1);
    if (args.length == 0) {
      IDungeon model = new Dungeon(6, 6, 6, true, 50, 5, random);
      IDungeonView view = new DungeonCreationView(model);
      IDungeonController controller = new DungeonGraphicalController(view, model);
      controller.playGame();
    } else {
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;

      int rows = Integer.parseInt(args[0]);
      int columns = Integer.parseInt(args[1]);
      int interconnectivity = Integer.parseInt(args[2]);
      boolean wrapping = Boolean.parseBoolean(args[3]);
      int treasureAmount = Integer.parseInt(args[4]);
      int monsterAmount = Integer.parseInt(args[5]);
      IDungeon dungeon = new Dungeon(
              rows, columns, interconnectivity, wrapping,
              treasureAmount, monsterAmount, random);

      try {
        IDungeonController controller = new DungeonController(input, output);
        controller.playGame(dungeon);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}
