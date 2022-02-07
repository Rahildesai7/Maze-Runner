package controller.commands;

import java.util.Random;

import dungeon.Dungeon;
import view.DungeonCreationView;

/**
 * New Game class to handle new game from menu bar.
 */
public class NewGame {

  /**
   * Method to setup new dungeon game.
   * @param random random seed
   */
  public void run(Random random) {
    DungeonCreationView view = new DungeonCreationView(new Dungeon(6, 6, 6, true, 50, 5, random));
    view.makeVisible();
  }
}
