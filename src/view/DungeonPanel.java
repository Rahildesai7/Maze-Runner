package view;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import controller.commands.NewGame;
import controller.commands.Quit;
import controller.commands.Restart;
import dungeon.ReadOnlyDungeon;
import location.Caves;

/**
 * DungeonPanel to display the dungeon board.
 */
public class DungeonPanel extends JPanel implements ActionListener {

  private final ReadOnlyDungeon model;
  private final IDungeonView view;
  private final String location = "img/";
  private JDialog gameStateDialog;
  private int playerLoc = -1;
  private final static int gridSize = 64;

  /**
   * Constructs dungeon panel object.
   *
   * @param model ReadOnly model
   * @param dungeonView Dungeon view
   */
  public DungeonPanel(ReadOnlyDungeon model, DungeonView dungeonView) {
    this.model = model;
    this.view = dungeonView;

    this.setFocusable(true);

    try {
      run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void run() throws IOException {

    int rows = model.getRows();
    int col = model.getColumns();

    this.setLayout(new GridLayout(rows, col));
    this.setVisible(true);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < col; j++) {
        Caves cave = model.getCavesList().get(i * col + j);
        String directions = model.getPossibleDirections(cave);
        JLabel label = new JLabel();

        BufferedImage image = ImageIO.read(ClassLoader.getSystemResource(
                location + imageNameCreation(directions)));

        label.setIcon(new ImageIcon(image));
        this.add(label);
      }
    }

  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    int rows = model.getRows();
    int col = model.getColumns();

    drawBlack(rows, col, g);

    if (!model.getPlayer().getIsAlive()) {
      gameEndDialog("Chomp, chomp, chomp, you are eaten by an Otyugh!");
    } else if (model.getCurrentPlayerPosition() == model.getEndCave()) {
      gameEndDialog("Congratulations! you reached the end cave");
    } else {

      drawSmell(col, g);
      drawMonster(rows, col, g);
      drawPlayer(col, g);

    }
  }

  private void drawBlack(int rows, int col, Graphics g) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < col; j++) {
        if (!model.visitedLocations().contains(i * col + j)) {

          String fPath = location + "black.png";
          try {
            BufferedImage blackImage = ImageIO.read(ClassLoader.getSystemResource(fPath));
            g.drawImage(blackImage, j * gridSize,
                    i * gridSize, null);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private void drawMonster(int rows, int col, Graphics g) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < col; j++) {
        Caves cave = model.getCavesList().get(i * col + j);
        if (model.visitedLocations().contains(cave.getCaveId()) && cave.getMonster() != null) {
          String fPath = location + "otyugh.png";
          try {
            BufferedImage monsterImage = ImageIO.read(ClassLoader.getSystemResource(fPath));
            g.drawImage(monsterImage, j * gridSize + 5,
                    i * gridSize + 5, null);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private void drawSmell(int col, Graphics g) {
    int smell = model.calcSmell();

    if (smell == 2) {
      String smellPath = location + "stench02.png";
      try {
        BufferedImage smellImage = ImageIO.read(ClassLoader.getSystemResource(smellPath));
        g.drawImage(smellImage, (model.getCurrentPlayerPosition() % col) * gridSize,
                (model.getCurrentPlayerPosition() / col) * gridSize, null);
      } catch (IOException e) {
        System.out.println(smellPath);
      }

    } else if (smell == 1) {
      String smellPath = location + "stench01.png";
      try {
        BufferedImage smellImage = ImageIO.read(ClassLoader.getSystemResource(smellPath));
        g.drawImage(smellImage, (model.getCurrentPlayerPosition() % col) * gridSize,
                (model.getCurrentPlayerPosition() / col) * gridSize, null);
      } catch (IOException | IllegalArgumentException e) {
        System.out.println(smellPath);
      }
    }
  }

  private void drawPlayer(int col, Graphics g) {
    String fPath = location + "player.png";
    try {
      BufferedImage playerImage = ImageIO.read(ClassLoader.getSystemResource(fPath));
      g.drawImage(playerImage, (model.getCurrentPlayerPosition() % col) * gridSize + 18,
              (model.getCurrentPlayerPosition() / col) * gridSize + 12, null);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (model.getCurrentPlayerPosition() != playerLoc) {
      playerLoc = model.getCurrentPlayerPosition();
      this.scrollRectToVisible(new Rectangle((model.getCurrentPlayerPosition() % col) * gridSize,
              (model.getCurrentPlayerPosition() / col) * gridSize, 100, 100));
    }

  }

  private String imageNameCreation(String directions) {
    char[] tempArray = directions.toCharArray();
    Arrays.sort(tempArray);
    directions = new String(tempArray);
    switch (directions) {
      case "E":
        return "E.png";
      case "S":
        return "S.png";
      case "W":
        return "W.png";
      case "N":
        return "N.png";
      case "EN":
        return "NE.png";
      case "ES":
        return "ES.png";
      case "EW":
        return "EW.png";
      case "NS":
        return "NS.png";
      case "NW":
        return "NW.png";
      case "SW":
        return "SW.png";
      case "ENS":
        return "NES.png";
      case "ENW":
        return "NEW.png";
      case "ESW":
        return "ESW.png";
      case "NSW":
        return "SWN.png";
      case "ENSW":
        return "NESW.png";
      default:
        return "blank.png";
    }
  }

  private void gameEndDialog(String gameStat) {
    gameStateDialog = new JDialog();
    gameStateDialog.setLayout(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    gameStateDialog.add(new JLabel(gameStat), constraints);

    constraints.gridwidth = 1;
    constraints.gridy = 1;
    JButton btnRestart = new JButton("Restart");
    btnRestart.addActionListener(this);
    gameStateDialog.add(btnRestart, constraints);

    constraints.gridx = 1;
    JButton btnQuit = new JButton("Quit");
    btnQuit.addActionListener(this);
    gameStateDialog.add(btnQuit, constraints);

    gameStateDialog.setBounds(100, 100, 500, 300);
    gameStateDialog.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String status = ((JButton) e.getSource()).getText();

    if (status.equals("Restart")) {
      this.view.close();
      gameStateDialog.dispose();
      Restart restart = new Restart();
      restart.run(model);
    } else if (status.equals("New Game")) {
      view.close();
      Random random = new Random(1);
      gameStateDialog.dispose();
      NewGame newGame = new NewGame();
      newGame.run(random);
    } else {
      Quit quit = new Quit();
      quit.run();
    }
  }
}