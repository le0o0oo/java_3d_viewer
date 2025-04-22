package org.leo.tridimensional_viewer.managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilesManager {
  private static String workingDir;
  private static Path basePath;
  public static ArrayList<String> files = new ArrayList<>();

  public static void setWorkingDir(String dir) {
    workingDir = dir;
    files.clear();
    basePath = Paths.get(dir);

    loadFiles();
  }

  public static String getWorkingDir() {
    return workingDir;
  }

  public static Path getBasePath() {
    return basePath;
  }

  public static void loadFiles() {
    if (workingDir == null) {
      System.err.println("Working directory not set.");
      return;
    }

    try {
      files.clear();
      files.addAll(Files.list(Paths.get(workingDir))
          .filter(Files::isRegularFile)
          .map(path -> path.getFileName().toString())
          .collect(Collectors.toList()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
