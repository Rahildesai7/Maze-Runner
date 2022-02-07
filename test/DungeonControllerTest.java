import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import controller.DungeonController;
import controller.IDungeonController;
import dungeon.Dungeon;
import dungeon.IDungeon;

/**
 * DungeonControllerTest class to test the Controller.
 */
public class DungeonControllerTest {
  IDungeon mockDungeon;
  Random random;
  StringBuffer log;

  /**
   * Setup method to initialize Dungeon.
   */
  @Before
  public void setup() {
    log = new StringBuffer();
    random = new Random(1);
    mockDungeon = new MockDungeon(log);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() throws IOException {
    StringReader reader = new StringReader("Player n SOUTH n EAST n SOUTH");
    Appendable gameLog = new FailingAppendable();
    DungeonController dungeonController = new DungeonController(reader, gameLog);
    dungeonController.playGame(null);
  }

  @Test(expected = IOException.class)
  public void testFailingAppendable() throws IOException {
    IDungeon dungeon = new Dungeon(5, 5, 5, true, 25, 10, random);
    StringReader reader = new StringReader("Player n SOUTH n EAST n SOUTH");
    Appendable gameLog = new FailingAppendable();
    IDungeonController dungeonController = new DungeonController(reader, gameLog);
    dungeonController.playGame(dungeon);
  }

  @Test
  public void testAmount() {
    mockDungeon.allocateTreasure(25);
    assertEquals("25 amount received", log.toString());
  }

  @Test
  public void playerMovements() throws IOException {
    IDungeon dungeon = new Dungeon(5, 5, 5, true, 25, 6, random);
    StringReader input = new StringReader(
            "y xyz 1 xyz NORTH NORTH SOUTH n EAST n SOUTH n SOUTH n WEST Sapphire n WEST");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    controller.playGame(dungeon);
    List<String> gameLogList = Arrays.asList(gameLog.toString().split("\\n"));

    assertTrue(gameLogList.contains("Player1 is currently at 23 with 3 arrows."));
    assertTrue(gameLogList.contains("You hit in darkness"));
    assertTrue(gameLogList.contains("Player1 is currently at 3 with 2 arrows."));
    assertTrue(gameLogList.contains("Please enter a number"));
    assertTrue(gameLogList.contains("Please enter a valid direction (NORTH, SOUTH, EAST, WEST)"));
    assertTrue(gameLogList.contains("Invalid move"));
    assertTrue(gameLogList.contains("You smell something terrible nearby"));
    assertTrue(gameLogList.contains("Move not available. Please enter from the available moves:"));
  }

  @Test
  public void testPlayerKilledByMonster() throws IOException {
    IDungeon dungeon = new Dungeon(5, 5, 5, true, 25, 6, random);
    StringReader input = new StringReader(
            "y xyz 1 xyz NORTH NORTH SOUTH n EAST n SOUTH n SOUTH n WEST Sapphire n WEST");
    Appendable gameLog = new StringBuilder();
    DungeonController controller = new DungeonController(input, gameLog);
    controller.playGame(dungeon);
    List<String> gameLogList = Arrays.asList(gameLog.toString().split("\\n"));

    assertEquals(
            "Player killed by Monster because of this move. Better luck next time",
            gameLogList.get(gameLogList.size() - 1));
  }

  @Test
  public void testPlayerReachedEnd() throws IOException {
    IDungeon dungeon = new Dungeon(5, 5, 5, true, 25, 6, random);
    StringReader input = new StringReader(
            "n SOUTH n EAST n SOUTH y 1 SOUTH SOUTH Sapphire y 1 WEST WEST y 1 WEST");
    Appendable gameLog = new StringBuilder();
    DungeonController controller = new DungeonController(input, gameLog);
    controller.playGame(dungeon);
    List<String> gameLogList = Arrays.asList(gameLog.toString().split("\\n"));

    assertTrue(gameLogList.contains("Player1 is currently at 9 with 3 arrows."));
    assertTrue(gameLogList.contains("You damaged a monster"));
    assertTrue(gameLogList.contains("Player1 is currently at 14 with 2 arrows."));
    assertTrue(gameLogList.contains("Treasure collected"));
    assertEquals("Player1 has collected: ", gameLogList.get(gameLogList.size() - 2));
    assertEquals("1 Sapphire", gameLogList.get(gameLogList.size() - 1));
    assertEquals("Reached end cave", gameLogList.get(gameLogList.size() - 4));
  }

}
