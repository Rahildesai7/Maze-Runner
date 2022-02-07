package controller;

import java.io.IOException;

import dungeon.IDungeon;

/**
 * AbstractController implements Dungeon Controller Interface
 * Extended by Dungeon console controller and Dungeon Graphical Interface.
 */
public class AbstractController implements IDungeonController {

  @Override
  public void playGame() {
    //implement play game for console controller.
  }

  @Override
  public void playGame(IDungeon dungeon) throws IOException {
    //implement play game for graphical controller.
  }

  @Override
  public void handleMoveClick(String direction) {
    //implement move for graphical controller.
  }

  @Override
  public int handleShootClick(int distance, String direction) {
    return 0;
  }

  @Override
  public void handlePickupClick(String treasure) {
    //implement pickup for graphical controller.
  }
}
