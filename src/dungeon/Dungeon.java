package dungeon;

import directions.Direction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import location.Caves;
import monster.IMonster;
import monster.Monster;
import player.IPlayer;
import player.Player;
import treasure.Diamonds;
import treasure.Rubies;
import treasure.Sapphire;
import treasure.Treasure;

/**
 * Dungeon class that implements the Dungeon Interface.
 * It is represented with rows, columns, degree of interconnectivity, wrapping, treasure amount.
 */
public class Dungeon implements IDungeon {

  private final int rows;
  private final int columns;
  private final int interconnectivity;
  private final boolean wrapping;
  private final int treasureAmount;
  private final int monsterCount;

  private Caves startCave;
  private Caves endCave;

  private IPlayer player;

  private final Random random;

  private final List<int[]> leftovers = new ArrayList<>();
  private final List<int[]> mst = new ArrayList<>();

  private List<Caves> cavesList;
  private final Map<Integer, Map<Direction, Integer>> neighbourList = new HashMap<>();

  private int[] parents;

  private List<Integer> arrowLocation;

  private final List<Integer> visitedLocations;


  /**
   * Constructs a dungeon object with rows, columns, interconnectivity,
   * wrapping, treasure amount and random object.
   *
   * @param rows              rows of the dungeon grid
   * @param columns           columns of the dungeon grid
   * @param interconnectivity degree of interconnectivity in the dungeon
   * @param wrapping          boolean wrapping for the dungeon
   * @param treasureAmount    amount of treasure to be assigned
   * @param random            random object of RandomInterface
   * @throws IllegalArgumentException if number of rows and columns is less than 3,
   *              interconnectivity is less than 0 or greater than
   *              rows * columns in case of wrapping,
   *              treasure amount is less than 0 or greater than 100.
   */
  public Dungeon(int rows, int columns,
                 int interconnectivity,
                 boolean wrapping,
                 int treasureAmount,
                 int monsterCount,
                 Random random) {

    Objects.requireNonNull(rows);
    Objects.requireNonNull(columns);
    Objects.requireNonNull(wrapping);
    Objects.requireNonNull(treasureAmount);
    Objects.requireNonNull(monsterCount);
    Objects.requireNonNull(random);

    if (rows <= 3) {
      throw new IllegalArgumentException("Number of rows should be greater than 3");
    }

    if (columns <= 3) {
      throw new IllegalArgumentException("Number of columns should be greater than 3");
    }

    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be less than 0");
    }

    if (wrapping && interconnectivity > (rows * columns) / 2) {
      throw new IllegalArgumentException("Interconnectivity for non-wrapping should be less than "
              + "number of rows * number of columns / 2");
    }

    if (treasureAmount < 0 || treasureAmount > 100) {
      throw new IllegalArgumentException("Treasure amount should be between 0 and 100");
    }

    if (monsterCount <  1 || monsterCount > rows * columns) {
      throw new IllegalArgumentException(
              String.format("monster.Monster count should be between 1 and %d", rows * columns));
    }

    this.rows = rows;
    this.columns = columns;
    this.interconnectivity = interconnectivity;
    this.wrapping = wrapping;
    this.treasureAmount = treasureAmount;
    this.monsterCount = monsterCount;
    this.random = random;

    this.visitedLocations = new ArrayList<>();

    createDungeon(rows, columns, interconnectivity, wrapping);

    setupGame();
    initializePlayer("Player1");
  }

  private void createDungeon(int rows, int columns, int interconnectivity, boolean isWrapping) {

    List<int[]> potentialPath = buildDungeonMap(rows, columns, isWrapping);


    createParentsUnion();

    kruskal(potentialPath, parents);

    if (interconnectivity > 0) {
      addInterconnectivity();
    }

    createNeighbourList();
    this.cavesList = generateCaves();
  }

  private void createParentsUnion() {
    parents = new int[rows * columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        parents[i * columns + j] = i * columns + j;
      }
    }
  }

  private List<int[]> buildDungeonMap(int rows, int columns, boolean isWrapping) {

    int[][] dungeonMatrix = new int[rows][columns];
    int count = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        dungeonMatrix[i][j] = count++;
      }
    }

    List<int[]> potentialPath = new ArrayList<>();

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {

        if (j + 1 < columns) {
          potentialPath.add(new int[]{dungeonMatrix[i][j], dungeonMatrix[i][j + 1]});
        }

        if (i + 1 < rows) {
          potentialPath.add(new int[]{dungeonMatrix[i][j], dungeonMatrix[i + 1][j]});
        }

        if (isWrapping) {
          if (j == 0) {
            potentialPath.add(new int[]{dungeonMatrix[i][j], dungeonMatrix[i][columns - 1]});
          }
          if (i == 0) {
            potentialPath.add(new int[]{dungeonMatrix[i][j], dungeonMatrix[rows - 1][j]});
          }
        }

      }
    }
    return potentialPath;
  }


  private void kruskal(List<int[]> potentialPath, int[] parents) {
    Set<Integer> visited = new HashSet<>();
    while (visited.size() < potentialPath.size()) {
      int r = random.nextInt(potentialPath.size());
      if (!visited.contains(r)) {
        int[] arr = potentialPath.get(r);
        if (find(arr[0], parents) == find(arr[1], parents)) {
          leftovers.add(new int[]{arr[0], arr[1]});
        } else {
          union(arr[0], arr[1], parents);
          mst.add(new int[]{arr[0], arr[1]});
        }
        visited.add(r);
      }
    }
  }




  private int find(int index, int[] parents) {
    if (parents[index] != index) {
      return find(parents[index], parents);
    }
    return parents[index];
  }

  private void union(int a, int b, int[] parents) {
    int parentA = find(a, parents);
    int parentB = find(b, parents);
    parents[parentA] = parentB;
  }


  private void addInterconnectivity() {
    int count = interconnectivity;
    while (count > 0) {
      int r = random.nextInt(interconnectivity);
      this.mst.add(leftovers.get(r));
      this.leftovers.remove(r);
      count--;
    }
  }


  //Create neighbour list
  private void createNeighbourList() {
    for (int[] path : mst) {
      if (!neighbourList.containsKey(path[0])) {
        Direction direction = checkDirection(path[0], path[1]);
        Map<Direction, Integer> map = new HashMap<>();
        map.put(direction, path[1]);
        neighbourList.put(path[0], map);
      } else {
        Map<Direction, Integer> map = neighbourList.get(path[0]);
        Direction direction = checkDirection(path[0], path[1]);
        map.put(direction, path[1]);
        neighbourList.put(path[0], map);
      }
    }

    for (int[] path : mst) {
      if (!neighbourList.containsKey(path[1])) {
        Direction direction = checkDirection(path[1], path[0]);
        Map<Direction, Integer> map = new HashMap<>();
        map.put(direction, path[0]);
        neighbourList.put(path[1], map);
      } else {
        Map<Direction, Integer> map = neighbourList.get(path[1]);
        Direction direction = checkDirection(path[1], path[0]);
        map.put(direction, path[0]);
        neighbourList.put(path[1], map);
      }
    }
  }

  public Map<Integer, Map<Direction, Integer>> getNeighbourList() {
    return new HashMap<>(neighbourList);
  }

  @Override
  public String getPossibleDirections(Caves cave) {
    Map<Direction, Integer> map = neighbourList.get(cave.getCaveId());
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Direction, Integer> entry : map.entrySet()) {
      if (entry.getKey().toString().equals(Direction.NORTH.toString())) {
        sb.append("N");
      } else if (entry.getKey().toString().equals(Direction.SOUTH.toString())) {
        sb.append("S");
      } else if (entry.getKey().toString().equals(Direction.EAST.toString())) {
        sb.append("E");
      } else {
        sb.append("W");
      }
    }

    return sb.toString();
  }

  //direction checking for neighbour list
  private Direction checkDirection(int src, int dest) {
    if (dest - src == 1 || dest - src == 1 - columns) {
      return Direction.EAST;
    }
    if (dest - src == -1 || dest - src == columns - 1) {
      return Direction.WEST;
    }
    if (dest - src == columns || dest - src == columns - rows * columns) {
      return Direction.SOUTH;
    }
    if (dest - src == -columns || dest - src == rows * columns - columns) {
      return Direction.NORTH;
    }
    return null;
  }


  //generate cave list
  private List<Caves> generateCaves() {
    List<Caves> cavesList = new ArrayList<>();
    for (Map.Entry<Integer, Map<Direction, Integer>> entry : neighbourList.entrySet()) {
      int key = entry.getKey();
      Map<Direction, Integer> value = entry.getValue();
      Caves cave = value.size() == 2 ? new Caves(key, true) : new Caves(key, false);
      cavesList.add(cave);
    }
    return cavesList;
  }

  //setup game utilities
  private void setupGame() {
    //System.out.println(cavesList.size());

    startCave = setStartCave();

    endCave = endCave(startCave);

    allocateTreasure(treasureAmount);

    spreadArrows(treasureAmount);

    spreadMonsters(monsterCount);
  }


  @Override
  public void allocateTreasure(int amount) {
    int numberOfCaves = (getCaveCount() * amount) / 100;
    List<Integer> caves = new ArrayList<>();
    while (numberOfCaves > 0) {
      int randomCave = random.nextInt(cavesList.size());
      //System.out.println(randomCave + " - " + caves.size());
      Caves cave = cavesList.get(randomCave);
      if (!cave.isTunnel()) {
        Treasure treasure;
        int randomTreasure = random.nextInt(3);
        if (randomTreasure == 0) {
          treasure = new Sapphire("Sapphire", 10);
        } else if (randomTreasure == 1) {
          treasure = new Rubies("Rubies", 20);
        } else {
          treasure = new Diamonds("Diamond", 30);
        }
        cave.setTreasureList(treasure.getName());
        if (!caves.contains(randomCave)) {
          caves.add(randomCave);
          numberOfCaves--;
        }
      }
    }
  }

  private int getCaveCount() {
    int count = 0;
    for (Caves cave : cavesList) {
      if (!cave.isTunnel()) {
        count++;
      }
    }
    return count;
  }


  private Caves setStartCave() {
    while (true) {
      int r = random.nextInt(cavesList.size());
      if (!cavesList.get(r).isTunnel()) {
        visitedLocations.add(cavesList.get(r).getCaveId());
        return cavesList.get(r);
      }
    }
  }

  private Caves endCave(Caves startCave) {
    int count = 5;
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(startCave.getCaveId());
    List<Integer> visited = new ArrayList<>();
    while (queue.size() > 0) {
      if (count == 0) {
        break;
      }
      int currCave = queue.peek();
      queue.poll();
      visited.add(currCave);
      Map<Direction, Integer> currentCaveMap = neighbourList.get(currCave);
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        if (!visited.contains(entry.getValue())) {
          queue.offer(entry.getValue());
        }
      }
      count--;
    }
    if (visited.size() == rows * columns) {
      int i = 1;
      while (true) {
        if (!cavesList.get(visited.size() - i).isTunnel()) {
          return cavesList.get(cavesList.size() - i);
        }
        i++;
      }
    } else {
      while (true) {
        int r = random.nextInt(cavesList.size());
        if (!visited.contains(r) && !cavesList.get(r).isTunnel()) {
          return cavesList.get(r);
        }
      }
    }
  }

  private void spreadMonsters(int count) {
    if (count > getCaveCount() - 1) {
      count = getCaveCount() - 1;
    }
    IMonster monster = new Monster(100);
    this.endCave.setMonster(monster);
    count--;
    HashSet<Integer> set = new HashSet<>();
    while (count > 0) {
      int randomCaves = random.nextInt(cavesList.size());
      Caves currCave = cavesList.get(randomCaves);
      if (set.contains(currCave.getCaveId())
              || currCave.getCaveId() == this.startCave.getCaveId()
              || currCave.isTunnel()) {
        continue;
      }
      set.add(currCave.getCaveId());
      currCave.setMonster(new Monster(100));
      count--;
    }

  }


  private void spreadArrows(int amount) {
    int numberOfCaves = (cavesList.size() * amount) / 100;
    List<Integer> visited = new ArrayList<>();
    while (numberOfCaves > 0) {
      int randomCave = random.nextInt(cavesList.size());
      if (!visited.contains(randomCave)) {
        Caves currCave = cavesList.get(randomCave);
        visited.add(randomCave);
        currCave.setArrows(1);
        numberOfCaves--;
      }
    }
  }

  private void initializePlayer(String playerName) {
    this.player = new Player(playerName, startCave);
  }

  @Override
  public String getPlayerStats() {
    Caves cave = player.getCurrentPos();
    StringBuilder stats = new StringBuilder();
    if (cave.getCaveId() != endCave.getCaveId()) {
      stats.append("From here the player can move : ");
      Map<Direction, Integer> neighbours = neighbourList.get(cave.getCaveId());
      for (Map.Entry<Direction, Integer> entry : neighbours.entrySet()) {
        stats.append("\n").append(entry.getKey().toString());
      }
    }
    return stats.toString();
  }

  @Override
  public int movePlayer(String direction) {
    int currentCaveId = player.getCurrentPos().getCaveId();
    if (checkMonster() == -100) {
      getPlayer().setIsAlive(false);
    }
    if (!getPlayer().getIsAlive()) {
      throw new IllegalStateException("Can't move, player dead");
    }

    Map<Direction, Integer> neighbours = neighbourList.get(currentCaveId);
    try {
      if (neighbours.containsKey(Direction.valueOf(direction))) {
        Caves cave = cavesList.get(neighbours.get((Direction.valueOf(direction))));
        player.setCurrentPos(cave);
        visitedLocations.add(player.getCurrentPos().getCaveId());
        return player.getCurrentPos().getCaveId();
      } else {
        throw new IllegalArgumentException("Invalid move");
      }
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

  private int checkMonster() {
    int currentCaveId = player.getCurrentPos().getCaveId();
    if (cavesList.get(currentCaveId).getMonster() != null) {
      IMonster monster = cavesList.get(currentCaveId).getMonster();
      if (monster.getHealth() == 100) {
        return -100;
      } else if (monster.getHealth() == 50) {
        int randomKill = random.nextInt(2);
        if (randomKill == 0) {
          return -100;
        }
      }
    }
    return 0;
  }

  @Override
  public int calcSmell() {
    Caves cave = player.getCurrentPos();
    int count = 2;
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(cave.getCaveId());
    List<Integer> visited = new ArrayList<>();
    while (queue.size() > 0) {
      if (count == 0) {
        break;
      }
      int currCave = queue.peek();
      queue.poll();
      visited.add(currCave);
      Map<Direction, Integer> currentCaveMap = neighbourList.get(currCave);
      for (Map.Entry<Direction, Integer> entry : currentCaveMap.entrySet()) {
        if (!visited.contains(entry.getValue())) {
          queue.offer(entry.getValue());
          if (cavesList.get(entry.getValue()).getMonster() != null && count == 2) {
            return 2;
          }
          if (cavesList.get(entry.getValue()).getMonster() != null && count == 1) {
            return 1;
          }
        }

      }
      count--;
    }
    return 0;
  }

  @Override
  public Map<String, Integer> checkTreasure() {
    Caves cave = player.getCurrentPos();
    Map<String, Integer> treasureList = cave.getTreasureList();
    return treasureList;
  }

  @Override
  public boolean collectTreasure(String treasure) {
    Caves cave = player.getCurrentPos();
    Map<String, Integer> treasureList = cave.getTreasureList();
    if (treasureList.containsKey(treasure)) {
      player.setCollectedTreasure(treasure, treasureList.get(treasure));
      treasureList.remove(treasure);
      cave.updateTreasureList(treasureList);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void collectArrows() {
    Caves cave = player.getCurrentPos();
    cave.setArrows(-1);
    player.setArrows(1);
  }

  @Override
  public int shootArrow(int distance, String direction) {

    if (player.getArrows() == 0) {
      throw new IllegalArgumentException("No arrows to shoot");
    }

    player.setArrows(-1);

    arrowLocation = new ArrayList<>();

    int currCaveId = createDamage(distance, direction, arrowLocation);

    int damage = 0;

    if (currCaveId == -1) {
      return damage;
    }
    if (cavesList.get(currCaveId).getMonster() != null) {
      IMonster monster = cavesList.get(currCaveId).getMonster();
      monster.setHealth(-50);
      if (monster.getHealth() <= 0) {
        cavesList.get(currCaveId).setMonster(null);
        damage = 100;
      } else {
        damage = 50;
      }

    }
    return damage;
  }

  private int createDamage(int distance, String direction, List<Integer> visited) {
    Caves currCave = player.getCurrentPos();
    int currCaveId = currCave.getCaveId();
    visited.add(currCaveId);
    Map<Direction, Integer> neighbours = neighbourList.get(currCaveId);

    while (distance > 0) {
      if (!neighbours.containsKey(Direction.valueOf(direction))) {
        return -1;
      }
      currCaveId = neighbours.get(Direction.valueOf(direction));
      if (visited.contains(currCaveId)) {
        return -1;
      }
      visited.add(currCaveId);
      neighbours = neighbourList.get(currCaveId);
      if (!cavesList.get(currCaveId).isTunnel()) {
        distance--;
        if (distance > 0 && !checkValidDirections(neighbourList, direction, currCave)) {
          return -1;
        }
      } else {
        if (!neighbours.containsKey(Direction.valueOf(direction))) {
          for (Map.Entry<Direction, Integer> map : neighbours.entrySet()) {
            if (!map.getKey().toString().equals(switchDirections(direction))) {
              direction = map.getKey().toString();
              break;
            }
          }
        }
      }

    }

    return currCaveId;
  }

  private static String switchDirections(String direction) {
    switch (direction) {
      case "EAST": return "WEST";
      case "WEST": return "EAST";
      case "NORTH": return "SOUTH";
      case "SOUTH": return "NORTH";
      default: return "";
    }
  }

  private static boolean checkValidDirections(Map<Integer, Map<Direction, Integer>> neighbourList,
                                              String direction, Caves currCave) {
    switch (direction) {
      case "EAST": return neighbourList.get(currCave.getCaveId()).containsKey(Direction.EAST);
      case "WEST": return neighbourList.get(currCave.getCaveId()).containsKey(Direction.WEST);
      case "NORTH": return neighbourList.get(currCave.getCaveId()).containsKey(Direction.NORTH);
      case "SOUTH": return neighbourList.get(currCave.getCaveId()).containsKey(Direction.SOUTH);
      default: return false;
    }
  }

  @Override
  public List<Integer> visitedLocations() {
    return visitedLocations;
  }

  @Override
  public int getCurrentPlayerPosition() {
    return player.getCurrentPos().getCaveId();
  }

  @Override
  public int getStartCave() {
    return startCave.getCaveId();
  }

  @Override
  public int getEndCave() {
    return endCave.getCaveId();
  }

  @Override
  public String printDungeon() {
    StringBuilder dungeon = new StringBuilder();
    int count = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (cavesList.get(count) == startCave) {
          dungeon.append(" S")
                  .append("{");
          if (count < 100) {
            dungeon.append("00" + count);
          } else {
            dungeon.append(count);
          }

          dungeon.append("}");
        } else if (cavesList.get(count) == endCave) {
          dungeon.append(" E")
                  .append("{");
          if (count < 100) {
            dungeon.append("00" + count);
          } else {
            dungeon.append(count);
          }

          dungeon.append("}");
        } else if (cavesList.get(count).isTunnel()) {
          dungeon.append(" T")
                  .append("{");
          if (count < 100) {
            dungeon.append("00" + count);
          } else {
            dungeon.append(count);
          }

          dungeon.append("}");
        } else {
          dungeon.append(" C")
                  .append("{");
          if (count < 100) {
            dungeon.append("00" + count);
          } else {
            dungeon.append(count);
          }

          dungeon.append("}");
        }
        count++;
      }
      dungeon.append("\n");
    }
    return dungeon.toString();
  }

  @Override
  public List<String> getDirections(int caveId) {
    List<String> directions = new ArrayList<>();
    Map<Direction, Integer> neighbours = neighbourList.get(caveId);
    for (Map.Entry<Direction, Integer> entry : neighbours.entrySet()) {
      directions.add(entry.getKey().toString());
    }
    return directions;
  }

  @Override
  public String playerTreasure() {
    StringBuilder treasure = new StringBuilder("\n" + player.getName());
    Map<String, Integer> treasureList = player.getCollectedTreasure();
    if (treasureList.size() > 0) {
      treasure.append(" has collected: ");
      for (Map.Entry<String, Integer> entry : treasureList.entrySet()) {
        treasure.append("\n" + entry.getValue() + " " + entry.getKey());
      }
    } else {
      treasure.append(" has not collected any treasure.");
    }
    return treasure.toString();
  }

  @Override
  public String playerDesc() {
    StringBuilder sb = new StringBuilder();
    sb.append(getPlayer().getName()).append(" is currently at ")
            .append(player.getCurrentPos().getCaveId()).append(" with ")
            .append(player.getArrows()).append(" arrows.")
            .append(playerTreasure());

    return sb.toString();
  }

  @Override
  public int getRows() {
    return this.rows;
  }

  @Override
  public int getColumns() {
    return this.columns;
  }

  @Override
  public List<Caves> getCavesList() {
    return new ArrayList<>(this.cavesList);
  }

  @Override
  public Player getPlayer() {
    return (Player) this.player;
  }

  @Override
  public List<int[]> getMst() {
    return mst;
  }

  @Override
  public int getInterconnectivity() {
    return interconnectivity;
  }

  @Override
  public boolean isWrapping() {
    return this.wrapping;
  }

  @Override
  public int getTreasureAmount() {
    return this.treasureAmount;
  }

  @Override
  public int getMonsterCount() {
    return this.monsterCount;
  }

  @Override
  public List<Integer> getArrowLocation() {
    return this.arrowLocation;
  }

}
