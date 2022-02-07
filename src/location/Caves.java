package location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import monster.IMonster;

/**
 * Caves class implements Caves interface.
 * Caves class is represented by id and a boolean value to check if it is a tunnel or not.
 */
public class Caves implements ICaves {
  private final int caveId;
  private final boolean isTunnel;
  private IMonster monster;
  private int arrows;

  private Map<String, Integer> treasureList = new HashMap<>();

  /**
   * Constructs a cave object with cave id and boolean isTunnel.
   *
   * @param caveId    id of the cave
   * @param isTunnel  boolean value to display whether cave is a tunnel or not
   */
  public Caves(int caveId, boolean isTunnel) {

    Objects.nonNull(caveId);
    Objects.nonNull(isTunnel);

    this.isTunnel = isTunnel;
    this.caveId = caveId;
  }

  public int getCaveId() {
    return caveId;
  }

  public boolean isTunnel() {
    return isTunnel;
  }

  public Map<String, Integer> getTreasureList() {
    return new HashMap<>(treasureList);
  }

  @Override
  public void setTreasureList(String treasure) {
    if (this.treasureList.containsKey(treasure)) {
      this.treasureList.put(treasure, treasureList.get(treasure) + 1);
    } else {
      this.treasureList.put(treasure, 1);
    }
  }

  @Override
  public void updateTreasureList(Map<String, Integer> treasureList) {
    this.treasureList = treasureList;
  }

  @Override
  public void setMonster(IMonster monster) {
    this.monster = monster;
  }

  @Override
  public IMonster getMonster() {
    return this.monster;
  }

  @Override
  public void setArrows(int count) {
    this.arrows += count;
  }

  @Override
  public int getArrows() {
    return arrows;
  }
}
