package imagemanagesystem;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.IntBuffer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.util.Callback;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author MK
 */
public class FileTreeView {

    //获得文件系统视觉管理
    public static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();
    
    private final FileTreeItem fileTreeItem = new FileTreeItem();
    private final TreeView<File> treeView = new TreeView<>();
    private final File mainFile = FILE_SYSTEM_VIEW.getRoots()[0];

    public FileTreeView() {

        fileTreeItem.setValue(mainFile);
        treeView.setRoot(fileTreeItem);
        //treeView.setShowRoot(false);
        Show.setPath(fileTreeItem);
        treeView.setCellFactory(new FileCallback());
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if (newValue!=null&&oldValue !=newValue) {
                             Show.setPath(newValue);
                        }
                    }
                });

        }

    class FileCallback implements Callback<TreeView<File>, TreeCell<File>> {

            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new FileTreeCell();
            }
        }

        class FileTreeCell extends TreeCell<File> {

            @Override
            protected void updateItem(File file, boolean empty) {

                super.updateItem(file, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(FILE_SYSTEM_VIEW.getSystemDisplayName(file));
                    setGraphic(getFileIconToNode(file));
                }

            }
        }
        //设置树节点的图标
    public static Canvas getFileIconToNode(File file) {

        //获取系统文件的图标
        Image image = (Image) ((ImageIcon) cn.util.FileTreeItem.fileSystemView
                .getSystemIcon(file)).getImage();
        //构建图片缓冲区，设定图片缓冲区的大小和背景，背景为透明
        BufferedImage bi = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.BITMASK);
        bi.getGraphics().drawImage(image, 0, 0, null);          //把图片画到图片缓冲区
        //将图片缓冲区的数据转换成int型数组
        int[] data = ((DataBufferInt) bi.getData().getDataBuffer()).getData();
        //获得写像素的格式模版
        WritablePixelFormat<IntBuffer> pixelFormat
                = PixelFormat.getIntArgbInstance();
        Canvas canvas = new Canvas(bi.getWidth() + 2, bi.getHeight() + 2);    //新建javafx的画布
        //获取像素的写入器
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        //根据写像素的格式模版把int型数组写到画布
        pixelWriter.setPixels(1, 1, bi.getWidth(), bi.getHeight(),
                pixelFormat, data, 0, bi.getWidth());
        //设置树节点的图标
        return canvas;

    }

    public static TreeView getTreeView() {
          
        return new FileTreeView().treeView;
       

    }

}
