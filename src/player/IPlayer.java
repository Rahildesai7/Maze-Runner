package player;

import java.util.Map;
import location.Caves;

/**
 * Player Interface that contains methods associated with the player.
 */
public interface IPlayer {

  /**
   * Returns the name of the player.
   *
   * @return name of the player
   */
  String getName();

  /**
   * Returns the current position(cave) of the player.
   *
   * @return current cave id of the player
   */
  Caves getCurrentPos();

  /**
   * Returns the list of collected treasure by the player during game.
   *
   * @return list of treasure
   */
  Map<String, Integer> getCollectedTreasure();

  /**
   * Sets the updated position of the player.
   *
   * @param currentPos updated cave id
   */
  void setCurrentPos(Caves currentPos);

  /**
   * Updates the treasure list of the player.
   *
   * @param collectedTreasure Collected treasure from the cave
   * @param amount  amount of treasure collected
   */
  void setCollectedTreasure(String collectedTreasure, int amount);

  /**
   * Returns number of arrows collected by the player.
   *
   * @return arrow count
   */
  int getArrows();

  /**
   * Set the arrow count of the player.
   *
   * @param arrows updated arrow count
   */
  void setArrows(int arrows);

  boolean getIsAlive();

  void setIsAlive(boolean bool);
}
