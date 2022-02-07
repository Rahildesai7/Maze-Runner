package controller;

import dungeon.IDungeon;
import view.IDungeonView;

/**
 * DungeonGraphicalController handles GUI based game.
 */
public class DungeonGraphicalController extends AbstractController {

  IDungeonView view;
  IDungeon model;

  /**
   * Constructs DungeonGraphicalController object.
   * @param view main view
   * @param model dungeon model
   */
  public DungeonGraphicalController(IDungeonView view, IDungeon model) {
    this.view = view;
    this.model = model;
  }

  @Override
  public void playGame() {
    this.view.makeVisible();
    this.view.bindKeys(this);
    this.view.addClickListener(this);
  }

  @Override
  public void handleMoveClick(String direction) {
    try {
      model.movePlayer(direction);
    } catch (IllegalArgumentException | NullPointerException | IllegalStateException iae) {
      //catch empty
    }

  }

  @Override
  public int handleShootClick(int distance, String direction) {
    try {
      return model.shootArrow(distance, direction);
    } catch (IllegalArgumentException iae) {
      // do nothing
    }
    return -1;
  }

  @Override
  public void handlePickupClick(String treasure) {
    if (treasure.equals("Arrows")) {
      model.collectArrows();
    } else {
      model.collectTreasure(treasure);
    }
  }
}
