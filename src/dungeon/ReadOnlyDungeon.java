package dungeon;

import java.util.List;
import java.util.Map;

import location.Caves;
import player.Player;

/**
 * ReadOnly Dungeon model for GUI based game.
 */
public interface ReadOnlyDungeon {
  /**
   * Get stats of the player.
   *
   * @return string containing stats of the player
   */
  String getPlayerStats();

  /**
   * Get directions from the given cave id.
   *
   * @param caveId  cave id of the current cave
   * @return        List of possible directions from the given cave id
   */
  List<String> getDirections(int caveId);

  /**
   * Calculate smell in a cave.
   * @return smell value
   */
  int calcSmell();

  /**
   * Checks for the treasure in the current cave position.
   *
   * @return list of treasure in the current cave position
   */
  Map<String, Integer> checkTreasure();

  /**
   * Get current player position in the dungeon.
   * @return cave id of the player
   */
  int getCurrentPlayerPosition();

  /**
   * Get the start cave id.
   * @return start cave id
   */
  int getStartCave();

  /**
   * Get the end cave id.
   * @return end cave id
   */
  int getEndCave();

  /**
   * List of possible directions from player's current location.
   * @param cave  Cave in which player is
   * @return list of possible directions to move
   */
  String getPossibleDirections(Caves cave);

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
   * Total number of monsters in the dungeon.
   *
   * @return monster count
   */
  int getMonsterCount();

  /**
   * List of locations visited by the player.
   * @return list of visited locations.
   */
  List<Integer> visitedLocations();

}
