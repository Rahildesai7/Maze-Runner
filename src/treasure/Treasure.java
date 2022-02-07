package treasure;

/**
 * Abstract class Treasure containing methods required by the treasure.
 */
public abstract class Treasure {

  private final String name;
  private final int points;

  /**
   * Constructs treasure object with name and points.
   *
   * @param name   name of treasure
   * @param points points of treasure
   */
  public Treasure(String name, int points) {
    this.name = name;
    this.points = points;
  }

  public String getName() {
    return name;
  }

  public int getPoints() {
    return points;
  }

}
