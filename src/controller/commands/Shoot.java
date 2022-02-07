package controller.commands;

import controller.CommandController;
import controller.IDungeonController;
import view.IDungeonView;
import view.ShootDialog;

/**
 * Shoot class to handle shoot command from view.
 */
public class Shoot implements CommandController {

  @Override
  public void run(IDungeonController listener, IDungeonView view) {
    new ShootDialog(listener, view);
  }
}
