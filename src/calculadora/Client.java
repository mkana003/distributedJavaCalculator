package calculadora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mkc
 */
public class Client extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ClientView.fxml"));
        Scene scene = new Scene(root, 257, 361);
        stage.setTitle("Calculadora");
        stage.setScene(scene);
        stage.show();
    }
    
}
