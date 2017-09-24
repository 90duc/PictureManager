package cn.util;

import imagemanagesystem.ImageFile;
import imagemanagesystem.Show;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author MK
 */
public class ImageDialogue extends Stage {

    private final List<ImageFile> imageFiles;
    private ImageFile imageFile;
    private final Stage mainStage;
    private final BorderPane mainVBox = new BorderPane();
    private final HBox buttonHBox = new HBox();
    private final  ImageView imageView = new ImageView();
    private final Button yesButton = new Button("确定");
    private final Button noButton = new Button("取消");

    private boolean autoPlay = false;

    public ImageDialogue(Stage parentStage) {

        buildButton();
        buildPage();
        Scene scene = new Scene(mainVBox, 700, 800);

        this.setScene(scene);
        this.mainStage = parentStage;
        this.initOwner(mainStage);
        this.initModality(Modality.WINDOW_MODAL);
        this.setResizable(false);
        this.show();

        imageFiles = Show.getInstanceShow().getImageFiles();
        
        imageView.setFitHeight(500);
        imageView.setFitWidth(600);
        mainVBox.setCenter(imageView);
         
    }

    private HBox leftHBox;
    private HBox rightHBox;

    private void buildPage() {

        ImageView leftImageView = new ImageView("images/left.png");       
        leftHBox = new HBox(leftImageView);
        leftHBox.setVisible(false);
        leftHBox.setMaxSize(leftImageView.getFitWidth(), leftImageView.getFitHeight());
        leftHBox.setOnMouseClicked(e -> {
            if (hasLastImage()) {
                lastImage();
            } else {
                leftImageView.setVisible(false);
            }
        });

        StackPane leftStackPane = new StackPane(leftHBox);
        leftStackPane.setOnMouseEntered(e -> {
            leftHBox.setVisible(hasLastImage());
        });
        leftStackPane.setOnMouseExited(e -> {
            leftHBox.setVisible(false);
        });
        leftStackPane.setPadding(new Insets(10, 10, 10, 10));

        ImageView rightImageView = new ImageView("images/right.png");
        rightHBox = new HBox(rightImageView);
        rightHBox.setMaxSize(rightImageView.getFitWidth(), rightImageView.getFitHeight());
        rightHBox.setOnMouseClicked(e -> {
            nextImage();
        });
        rightHBox.setVisible(false);
        StackPane rightStackPane = new StackPane(rightHBox);
        rightStackPane.setOnMouseEntered(e -> {
            rightHBox.setVisible(hasNextImage());
        });
        rightStackPane.setOnMouseExited(e -> {
            rightHBox.setVisible(false);
        });
        rightStackPane.setPadding(new Insets(10, 10, 10, 10));

        mainVBox.setLeft(leftStackPane);
        mainVBox.setRight(rightStackPane);

    }

    public void setImage(ImageFile imageFile) {

        this.imageFile = imageFile;
       
        Thread thread=new Thread(()->{          
            imageView.setImage(new Image(imageFile.getFile().toURI().toString()));           
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        
        
    }

    private void autoPlay() {
        if (autoPlay == true) {
            autoPlay = false;
            return;
        }
        new Thread(() -> {
            autoPlay = true;
            while (autoPlay||this.isShowing()) {
                Platform.runLater(() -> {
                    int index;
                    if (hasNextImage()) {
                       index=imageFiles.indexOf(imageFile)+1;
                    } else {
                       index=0;
                    } 
                    setImage(imageFiles.get(index));
                });

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    //  Logger.getLogger(ImageDialogue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    private void buildButton() {

        buttonHBox.getChildren().addAll(yesButton, noButton);
        buttonHBox.setAlignment(Pos.BASELINE_RIGHT);

        buttonHBox.setSpacing(20);
        yesButton.setOnAction(e -> {
            this.close();

        });
        noButton.setOnAction(e -> {
            autoPlay();
        });
        mainVBox.setBottom(buttonHBox);

    }

    private void nextImage() {
        if (hasNextImage()) {
            setImage(imageFiles.get(imageFiles.indexOf(imageFile) + 1));
            rightHBox.setVisible(hasNextImage());
        }

    }

    private void lastImage() {
        if (hasLastImage()) {
            setImage(imageFiles.get(imageFiles.indexOf(imageFile) - 1));
            leftHBox.setVisible(hasLastImage());
        }

    }

    private boolean hasNextImage() {
        return imageFiles.indexOf(imageFile) + 1 < imageFiles.size();
    }

    private boolean hasLastImage() {
        return imageFiles.indexOf(imageFile) > 0;
    }
}
