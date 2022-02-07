package monster;

/**
 * Monster class represents Monster of the dungeon
 * and implements methods of the IMonster Interface.
 */
public class Monster implements IMonster {

  private int health;

  public Monster(int health) {
    this.health = health;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void setHealth(int health) {
    this.health += health;
  }
}
