package dungeon;

import java.util.List;
import java.util.Map;
import location.Caves;
import player.Player;

/**
 * Dungeon interface containing methods required for the dungeon.
 */
public interface IDungeon extends ReadOnlyDungeon {

  /**
   * Treasure allocation for the dungeon.
   *
   * @param amount percent of caves of the dungeon in which treasure is to be allocated.
   */
  void allocateTreasure(int amount);

  /**
   * Get stats of the player.
   *
   * @return string containing stats of the player
   */
  String getPlayerStats();

  /**
   * Move player in the given direction.
   *
   * @param direction direction of the move
   * @return  cave id of the player after the move
   */
  int movePlayer(String direction);

  /**
   * Get directions from the given cave id.
   *
   * @param caveId  cave id of the current cave
   * @return        List of possible directions from the given cave id
   */
  List<String> getDirections(int caveId);

  /**
   * Calculate smell at player's current location.
   * @return amount of smell
   */
  int calcSmell();

  /**
   * Checks for the treasure in the current cave position.
   *
   * @return list of treasure in the current cave position
   */
  Map<String, Integer> checkTreasure();

  /**
   * Collect treasure for player.
   * @param treasure treasure
   * @return boolean value
   */
  boolean collectTreasure(String treasure);

  /**
   * Collect arrows for player.
   */
  void collectArrows();

  /**
   * Shoot arrows for player.
   * @param distance distance
   * @param direction direction
   * @return damage created by the arrow
   */
  int shootArrow(int distance, String direction);

  /**
   * List of locations visited by player.
   * @return list of locations
   */
  List<Integer> visitedLocations();

  /**
   * Get player's current position.
   * @return  cave id of player's current location
   */
  int getCurrentPlayerPosition();

  /**
   * Start cave id.
   * @return cave id of starting cave
   */
  int getStartCave();

  /**
   * End cave id.
   * @return cave id of ending cave
   */
  int getEndCave();

  /**
   * Print the dungeon with caves and tunnel.
   *
   * @return string formatted dungeon
   */
  String printDungeon();

  /**
   * List of possible directions from player's current location.
   * @param cave  Cave in which player is
   * @return list of possible directions to move
   */
  String getPossibleDirections(Caves cave);

  /**
   * Prints treasure collected by player.
   *
   * @return string formatted output of list of treasure collected
   */
  String playerTreasure();

  /**
   * Player Description.
   * @return description of player in string format
   */
  String playerDesc();

  /**
   * Get rows of the dungeon.
   *
   * @return rows
   */
  int getRows();

  /**
   * Get columns of the dungeon.
   *
   * @return columns
   */
  int getColumns();

  /**
   * Get list of caves of the dungeon.
   *
   * @return list of caves
   */
  List<Caves> getCavesList();

  /**
   * Get player of the dungeon.
   *
   * @return player
   */
  Player getPlayer();

  /**
   * Paths of the dungeon.
   *
   * @return list of paths
   */
  List<int[]> getMst();

  /**
   * Get interconnectivity of the dungeon.
   *
   * @return degree of interconnectivity
   */
  int getInterconnectivity();

  /**
   * Returns whether dungeon has wrapping or not.
   *
   * @return boolean isWrapping
   */
  boolean isWrapping();

  /**
   * Percent of caves having treasure.
   *
   * @return percent of caves having treasure
   */
  int getTreasureAmount();

  /**
   * Total monsters in the dungeon.
   * @return monster count
   */
  int getMonsterCount();

  /**
   * List of locations visited by arrow.
   * @return list of locations
   */
  List<Integer> getArrowLocation();
}
