package controller;

import java.io.IOException;
import dungeon.IDungeon;

/**
 * Dungeon Controller Interface that provides methods to invoke the Dungeon model methods.
 */
public interface IDungeonController {

  void playGame();

  void playGame(IDungeon dungeon) throws IOException;

  void handleMoveClick(String direction);

  int handleShootClick(int distance, String direction);

  void handlePickupClick(String treasure);
}
