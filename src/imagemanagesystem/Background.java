
package imagemanagesystem;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author MK
 */
public class Background {
    
     private static final StackPane stackPane = new StackPane();
     
     private static final HBox hBox =Show.showPane();
     private static final BorderPane borderPane = new BorderPane();
     
    
    static {
        stackPane.getChildren().addAll(hBox,borderPane);
        borderPane.setVisible(false);
        
    }
    
    public static void showImage(Image image) {
        
        //borderPane.getChildren().clear();
        borderPane.setCenter(new ImageView(image));
        hBox.setVisible(false);
        borderPane.setVisible(true);
       
    }

    public static StackPane getStackPane() {
        return stackPane;
    }
    
    
}
