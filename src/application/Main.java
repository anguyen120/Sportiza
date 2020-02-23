package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
    primaryStage.setTitle("Sportiza");
    // TODO: Fix aspect ratio 4k screens 
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();
    primaryStage.setScene(new Scene(root, width, height));
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
