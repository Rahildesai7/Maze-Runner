package controller;

import view.IDungeonView;

/**
 * Command Control interface that contains method to handle GUI based game.
 */
public interface CommandController {

  void run(IDungeonController listener, IDungeonView view);
}
