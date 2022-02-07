package location;

import java.util.Map;

import monster.IMonster;

/**
 * Cave Interface that contains methods associated with the cave.
 */
public interface ICaves {

  /**
   * Get id of the current cave.
   *
   * @return cave id.
   */
  int getCaveId();

  /**
   * Check whether cave is tunnel or not.
   *
   * @return true if cave is tunnel or false
   */
  boolean isTunnel();

  /**
   * Returns treasure list of current cave.
   *
   * @return list of treasure
   */
  Map<String, Integer> getTreasureList();

  /**
   * Set treasure in a particular cave.
   *
   * @param treasure type of treasure
   */
  void setTreasureList(String treasure);

  void updateTreasureList(Map<String, Integer> treasureList);

  void setMonster(IMonster monster);

  IMonster getMonster();

  void setArrows(int count);

  int getArrows();
}
