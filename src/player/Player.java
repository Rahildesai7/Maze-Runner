package player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import location.Caves;

/**
 * Player class implements the Player Interface.
 * Player class to represent the player of the game. Player contains name, current position of the
 * cave and the collected treasure.
 */
public class Player implements IPlayer {

  private final String name;
  private Caves currentPos;
  private Map<String, Integer> collectedTreasure = new HashMap<>();
  private int arrows = 3;
  private boolean isAlive = true;

  /**
   * Constructs player with name and current position.
   *
   * @param name       name of the player
   * @param currentPos current position of the player
   */
  public Player(String name, Caves currentPos) {

    Objects.nonNull(name);
    Objects.nonNull(currentPos);

    this.name = name;
    this.currentPos = currentPos;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Caves getCurrentPos() {
    return currentPos;
  }

  @Override
  public Map<String, Integer> getCollectedTreasure() {
    return new HashMap<>(collectedTreasure);
  }

  @Override
  public void setCurrentPos(Caves currentPos) {
    this.currentPos = currentPos;
  }

  @Override
  public void setCollectedTreasure(String treasure, int amount) {
    if (this.collectedTreasure.containsKey(treasure)) {
      this.collectedTreasure.put(treasure, collectedTreasure.get(treasure) + amount);
    } else {
      this.collectedTreasure.put(treasure, amount);
    }
  }

  @Override
  public int getArrows() {
    return arrows;
  }

  @Override
  public void setArrows(int arrows) {
    this.arrows += arrows;
  }

  @Override
  public boolean getIsAlive() {
    return isAlive;
  }

  @Override
  public void setIsAlive(boolean bool) {
    this.isAlive = bool;
  }
}
