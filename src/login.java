
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class login extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        login.primaryStage = primaryStage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("SincroEstoque");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxml) throws IOException {
        try {
            Parent pane = FXMLLoader.load(login.class.getResource(fxml));
            primaryStage.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}