package view;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.WindowConstants;


import controller.IDungeonController;
import controller.commands.NewGame;
import controller.commands.Quit;
import controller.commands.Restart;
import dungeon.ReadOnlyDungeon;

/**
 * Main view of the game that implements Dungeon view interface.
 */
public class DungeonView extends JFrame implements IDungeonView, ActionListener {

  private final ReadOnlyDungeon model;
  private final DungeonPanel panel;
  private JMenuBar menuBar;

  /**
   * Constructs Dungeon view model.
   * @param model ReadOnly Dungeon model
   */
  public DungeonView(ReadOnlyDungeon model) {
    super("Dungeon game");
    this.model = model;
    this.setFocusable(true);

    this.setLayout(new GridBagLayout());

    this.setSize(700, 500);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setResizable(false);

    setupMenu();

    panel = new DungeonPanel(model, this);
    panel.setFocusable(true);

    panel.setMinimumSize(new Dimension(500, 380));

    StatsPanel statsPanel = new StatsPanel(model);
    statsPanel.setMinimumSize(new Dimension(300, 380));
    statsPanel.setVisible(true);

    JScrollPane pane = new JScrollPane();

    pane.setMinimumSize(new Dimension(500, 380));
    pane.getViewport().add(panel);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    pane.setVisible(true);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane, statsPanel);
    splitPane.setSize(500, 380);
    splitPane.setDividerLocation(0.8);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    constraints.gridx = 0;
    constraints.gridy = 0;
    this.add(menuBar, constraints);

    constraints.gridy = 1;
    constraints.weightx = 0;
    this.add(splitPane, constraints);

    InstructionsPanel instructionsPanel = new InstructionsPanel();
    instructionsPanel.setSize(700, 220);
    constraints.gridy = 2;
    constraints.anchor = GridBagConstraints.PAGE_END;
    this.add(instructionsPanel, constraints);
  }

  private void setupMenu() {
    menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem restart = new JMenuItem("Restart");
    restart.addActionListener(this);
    JMenuItem newGame = new JMenuItem("New Game");
    newGame.addActionListener(this);
    JMenuItem quit = new JMenuItem("Quit");
    quit.addActionListener(this);
    menu.add(newGame);
    menu.add(restart);
    menu.add(quit);

    menuBar.add(menu);

    this.add(menuBar);
  }

  @Override
  public void addClickListener(IDungeonController listener) {
    MouseAdapterView mouseAdapterView = new MouseAdapterView(listener, this, model);
    panel.addMouseListener(mouseAdapterView);
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void bindKeys(IDungeonController listener) {
    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("UP"), KeyMapAction.Action.MOVE_UP);
    panel.getActionMap().put(KeyMapAction.Action.MOVE_UP,
            new KeyMapAction(KeyMapAction.Action.MOVE_UP, this, listener));

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("DOWN"), KeyMapAction.Action.MOVE_DOWN);
    panel.getActionMap().put(KeyMapAction.Action.MOVE_DOWN,
            new KeyMapAction(KeyMapAction.Action.MOVE_DOWN, this, listener));

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("LEFT"), KeyMapAction.Action.MOVE_LEFT);
    panel.getActionMap().put(KeyMapAction.Action.MOVE_LEFT,
            new KeyMapAction(KeyMapAction.Action.MOVE_LEFT, this, listener));

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("RIGHT"), KeyMapAction.Action.MOVE_RIGHT);
    panel.getActionMap().put(KeyMapAction.Action.MOVE_RIGHT,
            new KeyMapAction(KeyMapAction.Action.MOVE_RIGHT, this, listener));

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke('p'), KeyMapAction.Action.PICKUP);
    panel.getActionMap().put(KeyMapAction.Action.PICKUP,
            new KeyMapAction(KeyMapAction.Action.PICKUP, this, listener));

    panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke('s'), KeyMapAction.Action.SHOOT);
    panel.getActionMap().put(KeyMapAction.Action.SHOOT,
            new KeyMapAction(KeyMapAction.Action.SHOOT, this, listener));
  }

  @Override
  public void close() {
    this.dispose();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = ((JMenuItem) e.getSource()).getText();
    if (command.equals("Restart")) {
      this.close();
      Restart restart = new Restart();
      restart.run(model);
    }

    if (command.equals("Quit")) {
      Quit quit = new Quit();
      quit.run();
    }

    if (command.equals("New Game")) {
      this.close();
      Random random = new Random(1);
      NewGame newGame = new NewGame();
      newGame.run(random);
    }
  }

}
