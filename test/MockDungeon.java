import java.util.List;
import java.util.Map;
import dungeon.IDungeon;
import location.Caves;
import player.Player;

/**
 * MockDungeon class to be used for sole controller testing.
 */
public class MockDungeon implements IDungeon {

  StringBuffer sb;

  public MockDungeon(StringBuffer log) {
    sb = log;
  }



  @Override
  public void allocateTreasure(int amount) {
    sb.append(amount).append(" amount received");
  }


  @Override
  public String getPlayerStats() {
    return null;
  }

  @Override
  public int movePlayer(String direction) {
    sb.append("in mock dungeon with direction: " + direction);
    return 0;
  }

  @Override
  public List<String> getDirections(int caveId) {
    return null;
  }


  @Override
  public int calcSmell() {
    return 0;
  }

  @Override
  public Map<String, Integer> checkTreasure() {
    return null;
  }

  @Override
  public boolean collectTreasure(String treasure) {
    sb.append("In mock dungeon: ").append(treasure);
    return false;
  }

  @Override
  public void collectArrows() {
    //Mock collectArrow function
  }

  @Override
  public int shootArrow(int distance, String direction) {
    sb.append("In mock dungeon: ").append(distance).append(" ").append(direction);
    return 0;
  }

  @Override
  public List<Integer> visitedLocations() {
    return null;
  }

  @Override
  public int getCurrentPlayerPosition() {
    return 0;
  }

  @Override
  public int getStartCave() {
    return 0;
  }

  @Override
  public int getEndCave() {
    return 0;
  }

  @Override
  public String printDungeon() {
    return null;
  }

  @Override
  public String getPossibleDirections(Caves cave) {
    return null;
  }


  @Override
  public String playerTreasure() {
    return null;
  }

  @Override
  public String playerDesc() {
    return null;
  }

  @Override
  public int getRows() {
    return 0;
  }

  @Override
  public int getColumns() {
    return 0;
  }

  @Override
  public List<Caves> getCavesList() {
    return null;
  }

  @Override
  public Player getPlayer() {
    return null;
  }

  @Override
  public List<int[]> getMst() {
    return null;
  }

  @Override
  public int getInterconnectivity() {
    return 0;
  }

  @Override
  public boolean isWrapping() {
    return false;
  }

  @Override
  public int getTreasureAmount() {
    return 0;
  }

  @Override
  public int getMonsterCount() {
    return 0;
  }

  @Override
  public List<Integer> getArrowLocation() {
    return null;
  }
}
