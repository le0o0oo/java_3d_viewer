package org.leo.tridimensional_viewer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import org.leo.tridimensional_viewer.managers.FilesManager;
import org.leo.tridimensional_viewer.managers.RendererManager;
import org.leo.tridimensional_viewer.managers.TabManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EditorController {
  @FXML
  private TabPane tabPane;
  @FXML
  private ListView files_list;

  @FXML
  private void initialize() {
    System.out.println("EditorController initialized!");

    TabManager.addTab("tab test");
    files_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        System.out.println("Selected item: " + newValue);
        RendererManager.setModelPath(FilesManager.getBasePath().resolve(newValue.toString()).toString());
      }
    });


    updateFilesList();
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  /**
   * Add a tab in a raw way. I recommend using TabManager.addTab
   *
   * @param name name of the tab
   * @param id   a unique id for it.
   * @return the created Tab
   * @throws IllegalArgumentException if the id is already in use
   */
  public Tab addTabRaw(String name, String id) {
    for (Tab t : tabPane.getTabs()) {
      if (id.equals(t.getId())) {
        throw new IllegalArgumentException("A tab with id '" + id + "' already exists.");
      }
    }

    Tab tab = new Tab(name);
    tab.setId(id);
    tab.setClosable(true);

    tabPane.getTabs().add(tab);
    System.out.println("Created tab with ID: " + id);

    return tab;
  }

  /**
   * Retrieve a tab by id
   *
   * @param id id of the tab
   * @return Tab object
   */
  public Tab getTabById(String id) {
    for (Tab t : tabPane.getTabs()) {
      if (id.equals(t.getId())) {
        return t;
      }
    }
    return null;
  }

  public void updateFilesList() {
    files_list.getItems().clear();
    FilesManager.files.forEach(file -> {
      files_list.getItems().add(file);
    });
  }
}