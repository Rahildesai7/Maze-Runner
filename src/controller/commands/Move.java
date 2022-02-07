package controller.commands;

import controller.CommandController;
import controller.IDungeonController;
import view.IDungeonView;

/**
 * Move class to handle moves from key stroke.
 */
public class Move implements CommandController {

  private final String direction;

  /**
   * Constructs Move Object.
   * @param direction direction for the move
   */
  public Move(String direction) {
    this.direction = direction;
  }

  @Override
  public void run(IDungeonController listener, IDungeonView view) {
    listener.handleMoveClick(direction);
    view.refresh();
  }
}
