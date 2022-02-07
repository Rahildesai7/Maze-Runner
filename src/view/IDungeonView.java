package view;

import controller.IDungeonController;

/**
 * Dungeon view interface contains methods to handle methods of view.
 */
public interface IDungeonView {

  /**
   * Adds Mouse key listener to the view.
   * @param listener  Controller interface
   */
  void addClickListener(IDungeonController listener);

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Adds key binding to the view.
   * @param listener Controller interface
   */
  void bindKeys(IDungeonController listener);

  /**
   * Close the current view.
   */
  void close();
}
