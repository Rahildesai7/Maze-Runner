package controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dungeon.IDungeon;
import location.Caves;

/**
 * DungeonController class implements IDungeonController interface
 * to connect the Driver and Dungeon Model.
 */
public class DungeonController extends AbstractController {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   *  @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(IDungeon dungeon) throws IOException {
    if (dungeon == null) {
      throw new IllegalArgumentException();
    }

    out.append("Dungeon is ready\n");
    out.append(dungeon.getRows() + " - ").append("rows, ")
            .append(dungeon.getColumns() + " - ").append("columns, ")
            .append(dungeon.getInterconnectivity() + " - ").append("interconnectivity, ")
            .append(dungeon.getTreasureAmount() + " - ").append("equipments amount, ")
            .append(dungeon.getMonsterCount() + " - ").append("monster count\n");
    out.append(dungeon.printDungeon()).append("\n");

    out.append("Start cave:").append(" ")
            .append(String.valueOf(dungeon.getStartCave())).append("\n");
    out.append("End cave:").append(" ")
            .append(String.valueOf(dungeon.getEndCave())).append("\n");

    List<Caves> cavesList = dungeon.getCavesList();

    while (true) {
      int caveId = dungeon.getCurrentPlayerPosition();
      if (!dungeon.getPlayer().getIsAlive()) {
        out.append("Player killed by Monster because of this move. Better luck next time\n");
        break;
      }
      out.append(dungeon.playerDesc()).append("\n");

      if (checkTreasure(cavesList, caveId)) {
        out.append(dungeon.checkTreasure().toString()).append("\n");
        out.append("What would you like to pick?\n");
        treasureCollection(dungeon, out);
      }

      if (checkArrows(cavesList, caveId)) {
        out.append("Arrow available, press 'y' to pick\n");
        if (collectArrows(scan.next())) {
          dungeon.collectArrows();
          out.append("Arrow collected").append("\n");
        } else {
          out.append("Arrow skipped").append("\n");
        }
      }

      int smell = dungeon.calcSmell();
      if (smell == 2) {
        out.append("You smell something terrible nearby").append("\n");
      } else if (smell == 1) {
        out.append("You smell something nearby").append("\n");
      }

      if (checkPlayerArrows(dungeon)) {
        out.append("Do you want to shoot? Press 'y' to shoot").append("\n");

        if (scan.next().equals("y")) {
          out.append("Enter distance\n");
          int distance = checkArrowDistance();

          out.append("Enter direction\n");
          String direction = scan.next();
          while (!checkMove(direction)) {
            out.append("Please enter a valid direction (NORTH, SOUTH, EAST, WEST)").append("\n");
            direction = scan.next();
          }

          int damage = dungeon.shootArrow(distance, direction);
          if (damage == 0) {
            out.append("You hit in darkness").append("\n");
          } else if (damage == 50) {
            out.append("You damaged a monster").append("\n");
          } else {
            out.append("You killed a monster").append("\n");
          }
        }
      }

      if (checkEndCave(dungeon, caveId)) {
        out.append("Reached end cave\n");
        out.append(dungeon.playerTreasure()).append("\n");
        break;
      }

      out.append(dungeon.getPlayerStats()).append("\n");
      out.append("Enter direction:\n");
      String direction = scan.next();
      while (!checkMove(direction)) {
        out.append("Please enter a valid direction (NORTH, SOUTH, EAST, WEST)").append("\n");
        direction = scan.next();
      }

      movePlayer(direction, dungeon);
    }
  }



  private boolean checkTreasure(List<Caves> cavesList, int caveId) {
    return cavesList.get(caveId).getTreasureList().size() > 0;
  }

  private void treasureCollection(IDungeon dungeon, Appendable out) throws IOException {
    while (true) {
      if (dungeon.collectTreasure(scan.next())) {
        out.append("Treasure collected\n");
        System.out.println();
        break;
      } else {
        out.append("Enter valid treasure\n");
      }
    }
  }

  private boolean checkArrows(List<Caves> cavesList, int caveId) {
    return cavesList.get(caveId).getArrows() == 1;
  }

  public boolean checkPlayerArrows(IDungeon dungeon) {
    return dungeon.getPlayer().getArrows() > 0;
  }

  private boolean collectArrows(String input) {
    return input.equals("y");
  }

  private int checkArrowDistance() throws IOException {
    int distance;
    while (true) {
      try {
        distance = scan.nextInt();
        break;
      } catch (InputMismatchException ime) {
        out.append("Please enter a number\n");
        scan.next();
      }
    }
    return distance;
  }

  private boolean checkEndCave(IDungeon dungeon, int caveId) {
    return dungeon.getEndCave() == caveId;
  }

  private boolean checkMove(String direction) {
    return direction.equals("NORTH")
            || direction.equals("SOUTH")
            || direction.equals("EAST")
            || direction.equals("WEST");
  }

  private void movePlayer(String direction, IDungeon dungeon) throws IOException {
    while (true) {
      try {
        dungeon.movePlayer(direction);
        break;
      } catch (IllegalArgumentException iae) {
        out.append(iae.getMessage()).append("\n");
        out.append("Move not available. Please enter from the available moves:\n");
        direction = scan.next();
        while (!checkMove(direction)) {
          out.append("Please enter a valid direction (NORTH, SOUTH, EAST, WEST)\n");
          direction = scan.next();
        }
      } catch (IllegalStateException ise) {
        out.append(ise.getMessage()).append("\n");
        break;
      }
    }
  }

}

