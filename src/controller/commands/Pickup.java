package controller.commands;

import controller.CommandController;
import controller.IDungeonController;
import view.IDungeonView;
import view.TreasureDialog;

/**
 * Pickup class to handle treasure and arrow pickups commands from the view.
 */
public class Pickup implements CommandController {

  @Override
  public void run(IDungeonController listener, IDungeonView view) {
    new TreasureDialog(listener, view);
  }
}
