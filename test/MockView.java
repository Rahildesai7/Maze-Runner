import java.io.IOException;

import controller.IDungeonController;
import view.IDungeonView;

/**
 * Mock view used for testing the view.
 */
public class MockView implements IDungeonView {

  Appendable appendable;

  /**
   * Constructs mock view with appendable object to assert method calls.
   * @param appendable appendable object
   */
  public MockView(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void addClickListener(IDungeonController listener) {
    try {
      appendable.append("addClickListener ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void refresh() {
    try {
      appendable.append("refresh ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void makeVisible() {
    try {
      appendable.append("make visible ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void bindKeys(IDungeonController listener) {
    try {
      appendable.append("bind keys ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    try {
      appendable.append("close ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
