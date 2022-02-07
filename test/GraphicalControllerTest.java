import org.junit.Before;
import org.junit.Test;

import controller.DungeonGraphicalController;
import controller.IDungeonController;
import dungeon.IDungeon;
import view.IDungeonView;

import static org.junit.Assert.assertEquals;

/**
 * GraphicalControllerTest to test the methods of Graphical Controller.
 */
public class GraphicalControllerTest {

  IDungeon mockDungeon;
  IDungeonView dungeonView;
  IDungeonController controller;
  StringBuffer append;
  Appendable appendable;

  /**
   * setup method to setup mock view and mock dungeon.
   */
  @Before
  public void setup() {
    append = new StringBuffer();
    appendable = new StringBuffer();
    mockDungeon = new MockDungeon(append);
    dungeonView = new MockView(appendable);
    controller = new DungeonGraphicalController(dungeonView, mockDungeon);
  }

  @Test
  public void testPlayerMovement() {
    controller.handleMoveClick("NORTH");
    assertEquals("in mock dungeon with direction: NORTH", append.toString());
  }

  @Test
  public void testArrowShoot() {
    controller.handleShootClick(2, "SOUTH");
    assertEquals("In mock dungeon: 2 SOUTH", append.toString());
  }

  @Test
  public void testEquipmentCollection() {
    controller.handlePickupClick("Diamond");
    assertEquals("In mock dungeon: Diamond", append.toString());
  }

  @Test
  public void playGame() {
    controller.playGame();
    assertEquals("make visible bind keys addClickListener ", appendable.toString());
  }
}
