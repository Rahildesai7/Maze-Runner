package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import controller.IDungeonController;
import dungeon.ReadOnlyDungeon;

/**
 * MouseAdapterView that handles the clicks of the user to move the player.
 */
public class MouseAdapterView extends MouseAdapter {

  private final IDungeonController listener;
  private final IDungeonView view;
  private final ReadOnlyDungeon model;

  /**
   * Constructs MouseAdapterView object.
   * @param listener  Dungeon controller
   * @param view  Dungeon view
   * @param model Readonly dungeon model
   */
  public MouseAdapterView(IDungeonController listener, IDungeonView view, ReadOnlyDungeon model) {
    this.listener = listener;
    this.view = view;
    this.model = model;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    int x = e.getX() / 64;
    int y = e.getY() / 64;
    int location = model.getColumns() * y + x;

    movePlayer(model.getCurrentPlayerPosition() - location);
  }

  private void movePlayer(int location) {
    int col = model.getColumns();
    if (location == 1) {
      listener.handleMoveClick("WEST");
      view.refresh();
    } else if (location == -1) {
      listener.handleMoveClick("EAST");
      view.refresh();
    } else if (location == col) {
      listener.handleMoveClick("NORTH");
      view.refresh();
    } else if (location == -1 * col) {
      listener.handleMoveClick("SOUTH");
      view.refresh();
    }
  }
}
