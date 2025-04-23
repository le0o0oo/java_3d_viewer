package org.leo.tridimensional_viewer.managers;

import javafx.scene.control.Tab;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.UUID;

import org.leo.tridimensional_viewer.controllers.EditorController;
import org.leo.tridimensional_viewer.managers.classes.ObjectView;
import org.leo.tridimensional_viewer.managers.classes.TabInstance;
import org.leo.tridimensional_viewer.Main;

import java.util.ArrayList;

public class TabManager {
  /**
   * tab id, tab instance
   */
  static HashMap<String, TabInstance> tabs = new HashMap<String, TabInstance>();
  static TabInstance cTab;

  /**
   * Use only this function to add tabs
   *
   * @param name name of the tab
   * @return a pair with (oldTab, newTab)
   */
  public static Pair<TabInstance, TabInstance> addTab(String name) {
    EditorController controller = Main.getLoader().getController();
    String uniqueId = UUID.randomUUID().toString();

    Tab newTab = controller.addTabRaw(name, uniqueId);

    ObjectView view = new ObjectView(name, uniqueId, newTab);

    TabInstance oldTab = cTab;
    cTab = view;

    tabs.put(uniqueId, view);

    System.out.println("TabManager added tab: " + name + " with ID: " + uniqueId);

    return new Pair<>(oldTab, view);
  }

  public static TabInstance getTabById(String id) {
    return tabs.get(id);
  }

  public static boolean existsByName(String name) {
    EditorController controller = Main.getLoader().getController();
    boolean found = false;

    for (var tab : controller.getTabPane().getTabs()) {
      if (name.equals(tab.getText())) found = true;
    }

    return found;
  }

  /**
   * Destroys a tab
   *
   * @param id the id of the tab
   */
  public static void removeTab(String id) {
    TabInstance tab = getTabById(id);

    tab.destroy();
    tabs.remove(id);
  }
}
