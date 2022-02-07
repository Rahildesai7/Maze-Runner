import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import directions.Direction;
import dungeon.Dungeon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import location.Caves;
import org.junit.Before;
import org.junit.Test;
import player.IPlayer;
import treasure.Diamonds;
import treasure.Treasure;

/**
 * ModelTest to test dungeon model.
 */
public class ModelTest {

  Random random = new Random(3);
  Dungeon dungeon;
  Dungeon dungeonWithInterconnectivity;

  /**
   * Setup method to initialize dungeon objects.
   */
  @Before
  public void setup() {
    dungeon = new Dungeon(5, 5, 0, false, 100, 20, random);
    dungeonWithInterconnectivity = new Dungeon(16, 16, 10, true, 25, 30, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSetup() {
    assertEquals("Number of rows should be greater than 3",
            new Dungeon(0, 4, 0, false, 25, 10, random));

    assertEquals("Number of columns should be greater than 3",
            new Dungeon(4, 0, 0, false, 25, 10, random));

    assertEquals("Interconnectivity cannot be less than 0",
            new Dungeon(4, 0, -10, false, 25, 10,  random));

    assertEquals("Treasure amount should be between 0 and 100",
            new Dungeon(4, 4, 0, false, 101, 10, random));
  }

  @Test
  public void checkNeighbourList() {
    assertEquals(dungeon.getRows() * dungeon.getColumns(), dungeon.getNeighbourList().size());

    assertEquals(dungeonWithInterconnectivity.getRows() * dungeonWithInterconnectivity.getColumns(),
            dungeonWithInterconnectivity.getNeighbourList().size());


  }

  @Test
  public void testCaveConnection() {
    List<Caves> cavesList = dungeon.getCavesList();
    Vector<Boolean> visited = new Vector<>();
    for (int i = 0; i < cavesList.size(); i++) {
      visited.add(false);
    }

    Stack<Integer> stack = new Stack<>();
    stack.push(0);

    Map<Integer, Map<Direction, Integer>> map = dungeon.getNeighbourList();

    while (!stack.empty()) {
      int s = stack.peek();
      stack.pop();

      if (!visited.get(s)) {
        visited.set(s, true);
      }

      Map<Direction, Integer> currentCaveMap = map.get(s);

      //Test for tunnel
      if (currentCaveMap.size() == 2) {
        assertTrue(cavesList.get(s).isTunnel());

        // Test for treasure in tunnel
        assertTrue(cavesList.get(s).getTreasureList().size() == 0);
      } else {
        assertFalse(currentCaveMap.size() < 1);
        assertFalse(currentCaveMap.size() > 4);
        assertFalse(cavesList.get(s).isTunnel());
      }
      List<Integer> neighbours = new ArrayList<>();
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        neighbours.add(entry.getValue());
      }

      Iterator<Integer> itr = neighbours.iterator();
      while (itr.hasNext()) {
        int v = itr.next();
        if (!visited.get(v)) {
          stack.push(v);
        }
      }
    }
    
    //Each node is visited, i.e. there exists a path from every cave to every other cave.
    assertTrue(visited.size() == cavesList.size());
    assertTrue(stack.isEmpty());
  }

  @Test
  public void testEndCaveConnection() {
    List<Caves> cavesList = dungeon.getCavesList();
    Vector<Boolean> visited = new Vector<>();
    Caves lastCave = null;
    for (int i = 0; i < cavesList.size(); i++) {
      visited.add(false);
    }

    Stack<Integer> stack = new Stack<>();
    stack.push(0);

    Map<Integer, Map<Direction, Integer>> map = dungeon.getNeighbourList();

    while (!stack.empty()) {
      int s = stack.peek();
      stack.pop();

      if (!visited.get(s)) {
        visited.set(s, true);
      }

      Map<Direction, Integer> currentCaveMap = map.get(s);

      if (cavesList.get(s).getCaveId() == dungeon.getEndCave()) {
        lastCave = cavesList.get(s);
        break;
      }
      
      List<Integer> neighbours = new ArrayList<>();
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        neighbours.add(entry.getValue());
      }

      Iterator<Integer> itr = neighbours.iterator();
      while (itr.hasNext()) {
        int v = itr.next();
        if (!visited.get(v)) {
          stack.push(v);
        }
      }
    }
    assertTrue(lastCave.getCaveId() == dungeon.getEndCave());
  }

  @Test
  public void testConnectivityForWrappingAndInterconnectivity() {
    List<Caves> cavesList = dungeonWithInterconnectivity.getCavesList();
    Vector<Boolean> visited = new Vector<>();
    for (int i = 0; i < cavesList.size(); i++) {
      visited.add(false);
    }

    Stack<Integer> stack = new Stack<>();
    stack.push(0);

    Map<Integer, Map<Direction, Integer>> map = dungeonWithInterconnectivity.getNeighbourList();

    while (!stack.empty()) {
      int s = stack.peek();
      stack.pop();

      if (!visited.get(s)) {
        visited.set(s, true);
      }

      Map<Direction, Integer> currentCaveMap = map.get(s);

      //Test for tunnel
      if (currentCaveMap.size() == 2) {
        assertTrue(cavesList.get(s).isTunnel());

        // Test for treasure in tunnel
        assertTrue(cavesList.get(s).getTreasureList().size() == 0);
      } else {
        assertFalse(currentCaveMap.size() < 1);
        assertFalse(currentCaveMap.size() > 4);
        assertFalse(cavesList.get(s).isTunnel());
      }
      List<Integer> neighbours = new ArrayList<>();
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        neighbours.add(entry.getValue());
      }

      Iterator<Integer> itr = neighbours.iterator();
      while (itr.hasNext()) {
        int v = itr.next();
        if (!visited.get(v)) {
          stack.push(v);
        }
      }
    }

    //Each node is visited, i.e. there exists a path from every cave to every other cave.
    assertTrue(visited.size() == cavesList.size());
    assertTrue(stack.isEmpty());

  }

  @Test
  public void testMove() {

    //Player should start from the start cave.
    assertTrue(dungeon.getCurrentPlayerPosition() == dungeon.getStartCave());

    //Player movement test.
    List<String> directions = dungeon.getDirections(dungeon.getCurrentPlayerPosition());
    String direction = directions.get(0);
    int currentLocation = dungeon.getCurrentPlayerPosition();
    int location = dungeon.movePlayer(direction);

    if (direction.equals("NORTH")) {
      if (location < currentLocation) {
        assertTrue(location - currentLocation == -dungeon.getColumns());
      } else {
        assertTrue(location - currentLocation
                == dungeon.getRows() * dungeon.getColumns() - dungeon.getColumns());
      }

    } else if (direction.equals("SOUTH")) {
      if (location > currentLocation) {
        assertTrue(location - currentLocation == dungeon.getColumns());
      } else {
        assertTrue(location - currentLocation
                == dungeon.getColumns() - dungeon.getRows() * dungeon.getColumns());
      }

    } else if (direction.equals("EAST")) {
      if (location > currentLocation) {
        assertTrue(location - currentLocation == 1);
      } else {
        assertTrue(location - currentLocation == 1 - dungeon.getColumns());
      }

    } else if (direction.equals("WEST")) {
      if (location < currentLocation) {
        assertTrue(location - currentLocation == -1);
      } else {
        assertTrue(location - currentLocation == dungeon.getColumns() - 1);
      }
    }
  }

  @Test
  public void testTreasureAllocation() {
    List<Caves> cavesList = dungeon.getCavesList();
    int count = 0;
    int caveCount = 0;
    for (Caves cave : cavesList) {
      if (!cave.isTunnel()) {
        caveCount++;
      }
      if (cave.getTreasureList().size() > 0) {
        count++;
      }
    }
    assertEquals(count, (caveCount * dungeon.getTreasureAmount()) / 100);
  }


  @Test
  public void testInterconnectivity() {
    List<int[]> mst = dungeonWithInterconnectivity.getMst();
    assertEquals(mst.size(),
            dungeonWithInterconnectivity.getRows() * dungeonWithInterconnectivity.getColumns()
                    + dungeonWithInterconnectivity.getInterconnectivity() - 1);

    List<int[]> mstWithInterZero = dungeon.getMst();
    assertEquals(mstWithInterZero.size(), dungeon.getRows() * dungeon.getColumns() - 1);
  }


  @Test
  public void testWrappingTrue() {
    assertEquals(wrappingHelper(dungeonWithInterconnectivity),
                dungeonWithInterconnectivity.isWrapping());
  }

  @Test
  public void testWrappingFalse() {
    assertEquals(wrappingHelper(dungeon), dungeon.isWrapping());
  }


  private boolean wrappingHelper(Dungeon dungeon) {
    boolean wrapping = false;
    int count = 0;
    Map<Integer, Map<Direction, Integer>> map = dungeon.getNeighbourList();
    int rows = dungeon.getRows();
    int columns = dungeon.getColumns();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (i == 0 || j == 0) {
          int cave = i * rows + j;
          Map<Direction, Integer> caveMap = map.get(cave);
          if (caveMap.containsKey(Direction.NORTH)) {
            if (caveMap.get(Direction.NORTH) - cave > 0) {
              count++;
            }
          }
          if (caveMap.containsKey(Direction.SOUTH)) {
            if (caveMap.get(Direction.SOUTH) - cave < 0) {
              count++;
            }
          }
          if (caveMap.containsKey(Direction.EAST)) {
            if (caveMap.get(Direction.EAST) - cave < 0) {
              count++;
            }
          }
          if (caveMap.containsKey(Direction.WEST)) {
            if (caveMap.get(Direction.WEST) - cave > 0) {
              count++;
            }
          }
        }
      }
    }
    if (count > 0) {
      wrapping = true;
    }
    return wrapping;
  }

  @Test
  public void testTreasureCollection() {
    IPlayer player = dungeon.getPlayer();
    Caves dummy = new Caves(100, false);
    Treasure treasure = new Diamonds("Diamonds", 30);
    dummy.setTreasureList(treasure.getName());
    assertTrue(dummy.getTreasureList().containsKey("Diamonds"));
    assertEquals("{}", player.getCollectedTreasure().toString());

    player.setCurrentPos(dummy);
    dungeon.collectTreasure("Diamonds");
    assertTrue(player.getCollectedTreasure().containsKey("Diamonds"));
    assertEquals("{}", dummy.getTreasureList().toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    //print();
    int startCave = dungeon.getStartCave();
    int columns = dungeon.getColumns();
    List<Caves> cavesList = dungeon.getCavesList();
    Map<Integer, Map<Direction, Integer>> map = dungeon.getNeighbourList();
    Map<Direction, Integer> currentMap = map.get(startCave);
    if (currentMap.containsKey(Direction.NORTH)) {
      assertEquals(-columns, dungeon.movePlayer("NORTH") - startCave);
    } else {
      assertEquals("Invalid move", dungeon.movePlayer("north") - startCave);
    }
    dungeon.getPlayer().setCurrentPos(cavesList.get(startCave));
    if (currentMap.containsKey(Direction.SOUTH)) {
      assertEquals(columns, dungeon.movePlayer("SOUTH") - startCave);
    } else {
      assertEquals("Invalid move", dungeon.movePlayer("south") - startCave);
    }
    dungeon.getPlayer().setCurrentPos(cavesList.get(startCave));
    if (currentMap.containsKey(Direction.EAST)) {
      assertEquals(1, dungeon.movePlayer("EAST") - startCave);
    } else {
      assertEquals("Invalid move", dungeon.movePlayer("east") - startCave);
    }
    dungeon.getPlayer().setCurrentPos(cavesList.get(startCave));
    if (currentMap.containsKey(Direction.WEST)) {
      assertEquals(-1, dungeon.movePlayer("WEST") - startCave);
    } else {
      assertEquals("Invalid move", dungeon.movePlayer("west") - startCave);
    }
  }

  @Test
  public void distanceStartEnd() {
    int count = 4;
    int startCave = dungeon.getStartCave();
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(startCave);
    List<Integer> visited = new ArrayList<>();
    while (queue.size() > 0) {
      if (count == 0) {
        break;
      }
      int currCave = queue.peek();
      queue.poll();
      visited.add(currCave);
      Map<Direction, Integer> currentCaveMap = dungeon.getNeighbourList().get(currCave);
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        if (!visited.contains(entry.getValue())) {
          queue.offer(entry.getValue());
        }
      }
      count--;
    }
    assertFalse(visited.contains(dungeon.getEndCave()));
  }

  @Test
  public void distStartEndWithWrapping() {
    int count = 4;
    int startCave = dungeonWithInterconnectivity.getStartCave();
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(startCave);
    List<Integer> visited = new ArrayList<>();
    while (queue.size() > 0) {
      if (count == 0) {
        break;
      }
      int currCave = queue.peek();
      queue.poll();
      visited.add(currCave);
      Map<Direction, Integer> currentCaveMap
              = dungeonWithInterconnectivity.getNeighbourList().get(currCave);
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        if (!visited.contains(entry.getValue())) {
          queue.offer(entry.getValue());
        }
      }
      count--;
    }
    assertFalse(visited.contains(dungeonWithInterconnectivity.getEndCave()));
  }


  @Test
  public void testArrowsCount() {
    List<Caves> cavesList = dungeon.getCavesList();
    int count = 0;
    for (Caves cave : cavesList) {
      if (cave.isTunnel()) {
        assertTrue(cave.getArrows() > 0);
      }
      if (cave.getArrows() > 0) {
        count++;
      }
    }
    assertEquals(count, (cavesList.size() * dungeon.getTreasureAmount()) / 100);
  }

  @Test
  public void testMonsterCount() {
    List<Caves> cavesList = dungeon.getCavesList();
    int count = 0;
    int caveCount = 0;
    for (Caves cave : cavesList) {
      if (cave.getCaveId() == dungeon.getStartCave()) {
        assertTrue(cave.getMonster() == null);
      }
      if (cave.getCaveId() == dungeon.getEndCave()) {
        assertTrue(cave.getMonster() != null);
      }
      if (cave.isTunnel()) {
        assertTrue(cave.getMonster() == null);
      } else {
        caveCount++;
      }
      if (cave.getMonster() != null) {
        count++;
      }
    }

    if (dungeon.getMonsterCount() > caveCount - 1) {
      assertEquals(count, caveCount - 2);
    }

    List<Caves> cavesList1 = dungeonWithInterconnectivity.getCavesList();
    int count1 = 0;
    int caveCount1 = 0;
    for (Caves cave : cavesList1) {
      if (cave.getCaveId() == dungeonWithInterconnectivity.getStartCave()) {
        assertTrue(cave.getMonster() == null);
      }
      if (cave.getCaveId() == dungeonWithInterconnectivity.getEndCave()) {
        assertTrue(cave.getMonster() != null);
      }
      if (cave.isTunnel()) {
        //System.out.println(cave.getCaveId());
        assertTrue(cave.getMonster() == null);
      } else {
        caveCount1++;
      }
      if (cave.getMonster() != null) {
        count1++;
      }
    }
    if (dungeonWithInterconnectivity.getMonsterCount() > caveCount1 - 1) {
      assertEquals(count1, caveCount1 - 1);
    }
  }

  @Test
  public void testPlayerShouldStartWith3Arrows() {
    assertEquals(3, dungeon.getPlayer().getArrows());
  }

  @Test
  public void testArrowCollection() {
    dungeon.collectArrows();
    assertEquals(4, dungeon.getPlayer().getArrows());
  }

  @Test
  public void testSmell() {
    List<Caves> cavesList = dungeon.getCavesList();
    int count = 2;
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(dungeon.getStartCave());
    List<Integer> visited = new ArrayList<>();
    while (queue.size() > 0) {
      if (count == 0) {
        break;
      }
      int currCave = queue.peek();
      queue.poll();
      visited.add(currCave);
      Map<Direction, Integer> currentCaveMap = dungeon.getNeighbourList().get(currCave);
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        if (!visited.contains(entry.getValue())) {
          queue.offer(entry.getValue());
          if (cavesList.get(entry.getValue()).getMonster() != null) {
            assertTrue(cavesList.get(entry.getValue()).getMonster().getHealth() == 100);
          }
        }
      }
      count--;
    }
  }

  @Test
  public void testShootArrow() {
    Map<Direction, Integer> currentCaveMap = dungeon.getNeighbourList().get(dungeon.getStartCave());
    int nextCave = currentCaveMap.get(Direction.EAST);
    assertEquals(100, dungeon.getCavesList().get(nextCave).getMonster().getHealth());
    dungeon.shootArrow(1, "EAST");
    assertEquals(2, dungeon.getPlayer().getArrows());
    assertEquals(50, dungeon.getCavesList().get(nextCave).getMonster().getHealth());
    dungeon.shootArrow(1, "EAST");
    assertEquals(1, dungeon.getPlayer().getArrows());
    assertEquals(null, dungeon.getCavesList().get(nextCave).getMonster());
  }

  @Test
  public void testArrowMissedMonster() {
    Map<Direction, Integer> currentCaveMap = dungeon.getNeighbourList().get(dungeon.getStartCave());
    dungeon.shootArrow(3, "EAST");
    int nextCave = currentCaveMap.get(Direction.EAST);
    assertEquals(100, dungeon.getCavesList().get(nextCave).getMonster().getHealth());
  }

  @Test
  public void testArrowDirections() {
    dungeon.movePlayer("EAST");
    dungeon.shootArrow(5, "NORTH");
    List<Integer> directions = new ArrayList<>();
    directions.add(16);
    directions.add(11);
    directions.add(6);
    assertEquals(directions, dungeon.getArrowLocation());
  }

  @Test
  public void testArrowDirectionsWithWrapping() {
    dungeonWithInterconnectivity.shootArrow(10, "WEST");
    List<Integer> directions = new ArrayList<>();
    directions.add(67);
    directions.add(66);
    directions.add(50);
    directions.add(34);
    directions.add(33);
    directions.add(32);
    assertEquals(directions, dungeonWithInterconnectivity.getArrowLocation());
  }

  @Test

  public void testPlayerDesc() {
    assertEquals("Player1 is currently at 15 with 3 arrows.\n"
            + "Player1 has not collected any treasure.", dungeon.playerDesc());
  }

  @Test
  public void testPlayerStats() {
    assertTrue(dungeon.getPlayerStats().contains("EAST"));
    assertTrue(dungeon.getPlayerStats().contains("SOUTH"));
  }

  @Test
  public void testVisitedLocations() {
    dungeon.movePlayer("SOUTH");
    assertEquals("[15, 20]", dungeon.visitedLocations().toString());
  }

}
