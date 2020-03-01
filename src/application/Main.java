package application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) {
    StackPane root = new StackPane();

    WebView webView = new WebView();
    File html = new File("html/index.html");
    webView.getEngine().load(html.toURI().toString());

    root.getChildren().add(webView);

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());

    File logo = new File("html/images/logo.png");

    primaryStage.getIcons().add(new Image(logo.toURI().toString()));
    primaryStage.setTitle("Sportiza");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}
