package imagemanagesystem;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author MK
 */
public class ImageManageSystem extends Application {

    private static Stage stage=null;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) {

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(Background.getStackPane());
        
        Scene scene=new Scene(stackPane,1200, 800);
        stage.setScene(scene);
        ImageManageSystem.stage=stage;
  
        stage.show();
        stage.setOnCloseRequest(e->{
            System.exit(0);
        });

    }
    


    public static void main(String[] args) {      
            launch(args);  
            
    }
   
}
