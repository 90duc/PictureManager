package imagemanagesystem;

import static imagemanagesystem.FileTreeView.getTreeView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author MK
 */
public class Show {

    private static final Show show = new Show();

    private static final int IMAGE_COLUMN = 7;
    private static final int IMAGE_ROW = 5;
    private static final int IMAGE_COUNT = IMAGE_COLUMN * IMAGE_ROW;

    public static final double LENGTH = (ImageFile.IMAGE_WIDTH + 20) * IMAGE_COLUMN;
    private static final HBox hBox = new HBox();

    private FileTreeItem treeItem;
    private final VBox vBox = new VBox();
    private final Label label = new Label();
    private final ScrollPane scrollPane = new ScrollPane();
    private final FlowPane flowPane = new FlowPane();
    private final ArrayList<ImageFile> imageFiles = new ArrayList<>();
    private int totalRows;

    public Show() {

        vBox.getChildren().add(label);
        vBox.getChildren().add(scrollPane);

        scrollPane.setContent(flowPane);
        scrollPane.setPrefHeight(1000);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        flowPane.widthProperty().addListener(e -> {

            scrollPane.setMinWidth(flowPane.getWidth());
            label.setMinWidth(flowPane.getWidth());
        });
        flowPane.setAlignment(Pos.TOP_LEFT);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        flowPane.setHgap(20);
        flowPane.setVgap(20);
        flowPane.setPrefWrapLength(LENGTH);

        label.setPadding(new Insets(5, 5, 5, 5));
        label.setStyle("-fx-border-color:lightgray;-fx-background-color:white");
        label.setMinWidth(LENGTH + 40);

        buildLoadImageThreads();
    }

    private void buildLoadImageThreads() {
        for (int i = 0; i < IMAGE_COLUMN; i++) {
            ShowImageRunnable sir = new ShowImageRunnable(i, this::getImage);
            Thread thread = new Thread(sir);
            threads.put(thread, sir);
            thread.start();
        }

    }

    private void loadImage() {

        flowPane.getChildren().clear();
        imageFiles.clear();

        ArrayList<File> files = new ArrayList<>();
        for (File f : treeItem.getValue().listFiles()) {
            if (this.isImage(f.getName())) {
                files.add(f);
            }
        }

        totalRows = (int) Math.ceil(files.size() * 1.0 / IMAGE_COLUMN);
        initLoadImageThreads();

        if (files.isEmpty()) {
            return;
        }

        int count = files.size() > IMAGE_COUNT ? IMAGE_COUNT : files.size();
        for (int i = 0; i < count; i++) {
            ImageFile imageFile = new ImageFile(files.get(i));
            imageFiles.add(imageFile);           
        }
        flowPane.getChildren().addAll(imageFiles);        
        Platform.runLater(() -> {
            initImages(0);
        });
        if (files.size() > IMAGE_COUNT) {
            Thread thread = new Thread(() -> {
                ArrayList<ImageFile> list = new ArrayList<>();
                for (int j = IMAGE_COUNT; j < files.size(); j++) {
                    ImageFile imageFile = new ImageFile(files.get(j));
                    list.add(imageFile);

                }
                Platform.runLater(() -> {
                    flowPane.getChildren().addAll(list);
                });
                imageFiles.addAll(list);
            });
            thread.start();

        }
        addListen();
    }

    private void addListen() {

        scrollPane.vvalueProperty().addListener((Observable e) -> {
            int base = (int) ((totalRows-IMAGE_ROW) * scrollPane.getVvalue());
            initImages(base);

        });
    }

    private final HashMap<Thread, ShowImageRunnable> threads = new HashMap<>();

    private void showImage1(int range) {

        if (!imageFiles.isEmpty()) {

            for (int i = 0; i < IMAGE_COLUMN; i++) {
                int column = i;
                Thread thread = new Thread(() -> {
                    int starIndex = range * IMAGE_COUNT + column;
                    try {
                        for (int j = 0; j < IMAGE_ROW; j++) {
                            imageFiles.get(starIndex + j * IMAGE_COLUMN).setImage();
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                });

                thread.start();
            }

        }

    }

    private void initLoadImageThreads() {
        for (Thread t : threads.keySet()) {
            threads.get(t).init(totalRows);
        }

    }

    private void initImages(final int startRow) {

        for (int i = startRow + IMAGE_ROW + 1; i > startRow - 1; i--) {
            if (isOnImageFileRows(i)) {
                for (Thread t : threads.keySet()) {
                    if (threads.get(t).add(i)
                            &&t.getState().equals(Thread.State.TIMED_WAITING)) {
                        t.interrupt();
                    }
                }

            }
        }
    }

    public boolean isOnImageFileRows(int row) {
        return row >= 0 && row < totalRows;
    }

    public void getImage(int row, int column) {

        int index = row * IMAGE_COLUMN + column;
        if (index >= 0 && index < imageFiles.size()) {
            imageFiles.get(index).setImage();
        }

    }

    private boolean isImage(String fileName) {

        fileName = fileName.toLowerCase();

        return fileName.endsWith(".jpg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".bmp");
    }

    public static void setPath(Object object) {

        show.treeItem = (FileTreeItem) object;
        show.label.setGraphic(FileTreeView.getFileIconToNode(show.treeItem.getValue()));
        show.label.setText(show.treeItem.getValue().toString());
        show.loadImage();

    }

    public static HBox showPane() {

        hBox.getChildren().addAll(getTreeView(), show.vBox);
        return hBox;
    }

    public static Show getInstanceShow() {
        return show;
    }

    public ArrayList<ImageFile> getImageFiles() {
        return imageFiles;
    }

}
