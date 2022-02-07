package view;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import dungeon.ReadOnlyDungeon;
import location.Caves;

/**
 * Cave details panel to display details of the cave where the user is currently present.
 */
public class CaveDetailsPanel extends JPanel {

  private final ReadOnlyDungeon model;

  /**
   * Constructs an object of the CaveDetailsPanel.
   * @param model Readonly Dungeon model
   */
  public CaveDetailsPanel(ReadOnlyDungeon model) {
    this.model = model;
    this.setLayout(new GridLayout(4, 2));
    this.setVisible(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setFont(new Font("Serif", Font.BOLD, 14));
    if (model.getCavesList().get(model.getCurrentPlayerPosition()).isTunnel()) {
      g2d.drawString("Tunnel Details", 75, 10);
    } else {
      g2d.drawString("Cave Details", 75, 10);
    }

    g2d.setFont(new Font("Serif", Font.PLAIN, 14));

    int caveId = model.getCurrentPlayerPosition();
    Caves cave = model.getCavesList().get(caveId);

    BufferedImage arrowImage = null;
    String location = "img/";
    try {
      arrowImage = ImageIO.read(ClassLoader.getSystemResource(location + "arrow-black.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2d.drawImage(arrowImage, 10, 21, null);
    g2d.drawString("(Arrows)", 75, 32);
    g2d.drawString(cave.getArrows() + "", 150, 32);

    BufferedImage diamondImage = null;
    try {
      diamondImage = ImageIO.read(ClassLoader.getSystemResource(location + "diamond.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2d.drawImage(diamondImage, 10, 41, null);
    g2d.drawString("(Diamond)", 75, 62);

    BufferedImage rubiesImage = null;
    try {
      rubiesImage = ImageIO.read(ClassLoader.getSystemResource(location + "ruby.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2d.drawImage(rubiesImage, 10, 73, null);
    g2d.drawString("(Rubies)", 75, 94);

    BufferedImage sapphireImage = null;
    try {
      sapphireImage = ImageIO.read(ClassLoader.getSystemResource(location + "emerald.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2d.drawImage(sapphireImage, 10, 105, null);
    g2d.drawString("(Sapphire)", 75, 122);

    Map<String, Integer> treasureMap = cave.getTreasureList();

    if (treasureMap.containsKey("Diamond")) {
      g2d.drawString(treasureMap.get("Diamond") + " ", 150, 62);
    } else {
      g2d.drawString("0", 150, 62);
    }

    if (treasureMap.containsKey("Rubies")) {
      g2d.drawString(treasureMap.get("Rubies") + " ", 150, 94);
    } else {
      g2d.drawString("0", 150, 94);
    }

    if (treasureMap.containsKey("Sapphire")) {
      g2d.drawString(treasureMap.get("Sapphire") + " ", 150, 122);
    } else {
      g2d.drawString("0", 150, 122);
    }

  }
}
