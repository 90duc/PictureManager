package imagemanagesystem;

import cn.util.CompressImage;
import cn.util.ImageDialogue;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author MK
 */
public final class ImageFile extends Pane {

    private static final Calendar calendar = GregorianCalendar.getInstance();
    private static Image IMAGE;
    public static final int IMAGE_HEIGHT = 80;
    public static final int IMAGE_WIDTH = 100;

    private final File file;
    private boolean isTruePicture = false;
    //private boolean isMiniPicture = false;
    private final VBox vBox = new VBox();
    private final ImageView imageView = new ImageView();
    private final Label imageFileLength = new Label();
    private final Label imageFileLastModifyTime = new Label();
    private final Label imageName = new Label();

    static {
        IMAGE = new Image("images/picture.png");
    }

    public ImageFile(File file) {

        this.file = file;

        imageView.setFitHeight(ImageFile.IMAGE_HEIGHT);
        imageView.setFitWidth(ImageFile.IMAGE_WIDTH);
        imageView.setImage(IMAGE);

        imageFileLength.setText((file.length() / 1024) + "kb");
        imageFileLength.setTextAlignment(TextAlignment.RIGHT);

        calendar.setTimeInMillis(file.lastModified());
        imageFileLastModifyTime.setText(calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        imageFileLastModifyTime.setTextAlignment(TextAlignment.RIGHT);

        imageName.setText(file.getName());
        imageName.setMaxWidth(IMAGE_WIDTH);
        vBox.getChildren().addAll(imageView, imageFileLength, imageFileLastModifyTime, imageName);

        vBox.setSpacing(5);
        this.getChildren().add(vBox);

        this.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {

                ImageDialogue id = new ImageDialogue(ImageManageSystem.getStage());
                id.setImage(this);
            }

        });

    }

    public void setImage() {

        if (!isTruePicture) {
            Canvas canvas = CompressImage.compressImage(file);
            Platform.runLater(() -> {
                Image image = canvas.snapshot(null, null);
                imageView.setImage(image);
            });
            isTruePicture = true;
        }

    }

    public File getFile() {
        return file;
    }
    
//     public static Image getIMAGE() {
//        return IMAGE;
//    }

    public Image getImage() {
        return imageView.getImage();
    }

}
