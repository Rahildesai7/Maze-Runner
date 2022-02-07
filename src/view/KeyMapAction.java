package view;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import controller.CommandController;
import controller.IDungeonController;
import controller.commands.Move;
import controller.commands.Pickup;
import controller.commands.Shoot;

/**
 * KeyMapAction to handle key strokes for move, pickup, shoot commands.
 */
public class KeyMapAction extends AbstractAction {

  enum Action {
    PICKUP, SHOOT, MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT
  }

  private final Map<Action, CommandController> commands;
  private final Action action;
  private final IDungeonView view;
  private final IDungeonController listener;

  /**
   * Constructs KeyMapAction object.
   * @param action  action performed
   * @param view    Dungeon view
   * @param listener  Graphical Controller
   */
  public KeyMapAction(
          Action action,
          IDungeonView view,
          IDungeonController listener) {

    this.action = action;
    this.view = view;
    this.listener = listener;

    commands = new HashMap<>();

    commands.put(Action.MOVE_UP, new Move("NORTH"));
    commands.put(Action.MOVE_DOWN, new Move("SOUTH"));
    commands.put(Action.MOVE_LEFT, new Move("WEST"));
    commands.put(Action.MOVE_RIGHT, new Move("EAST"));
    commands.put(Action.PICKUP, new Pickup());
    commands.put(Action.SHOOT, new Shoot());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    commands.get(action).run(listener, view);
  }

}
