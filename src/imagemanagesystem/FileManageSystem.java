package imagemanagesystem;

import cn.util.FileTreeItem;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TreeView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;
import javax.swing.ImageIcon;

/**
 *
 * @author MK
 */
public class FileManageSystem {

    //选择文件的路径节点保存
    private static FileTreeItem choosedFileTreeItem;
    //获得文件系统视觉管理
    //private static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    //文件树的根目录
    private static final File mainDir = FileTreeItem.fileSystemView.getRoots()[0];
    //获得文件根目录的子目录，如计算机，库，用户，网络
    private static final File[] roostFiles = mainDir.listFiles();

    //文件树的视觉节点
    private final TreeView treeView = new TreeView();
    //文件树的树节点
    private final FileTreeItem treeItem = new FileTreeItem();
    //文件树的视觉展开节点数
    private int expandedItemCount;

    private FileManageSystem() {

        //设置树根节点
        treeItem.setValue(mainDir);
        //构建树的根目录的子目录节点
        setFirstFileTreeItem();

//   System.out.println("文件分隔符："+ System.getProperty("file.separator"));   //在 unix 系统中是＂／＂
//   System.out.println("路径分隔符："+ System.getProperty("path.separator"));   //在 unix 系统中是＂:＂
//   System.out.println("行分隔符："+ System.getProperty("line.separator"));   //在 unix 系统中是＂/n＂
//   System.out.println("用户的账户名称："+ System.getProperty("user.name"));
//   System.out.println("用户的主目录："+ System.getProperty("user.home"));
        //设置树视觉的根节点
        treeView.setRoot(treeItem);
        treeView.setShowRoot(false);           //不显示根节点

        setShow(treeItem);

        this.expandedItemCount = treeView.getExpandedItemCount();      //初始化文件树的视觉展开节点数
        //监听文件树的视觉展开节点数
        treeView.expandedItemCountProperty().addListener((Observable e) -> {

            //文件树的视觉展开节点数增加，对进行对树节点的延伸
            if (this.expandedItemCount < treeView.getExpandedItemCount()) {
                expandedItemOperation(treeItem);
            }
            this.expandedItemCount = treeView.getExpandedItemCount();

        });

        treeView.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                    setShow(newValue);
                });

    }

    private void setShow(Object newValue) {
//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
        FileTreeItem fileFileTreeItem = (FileTreeItem) newValue;
        if (fileFileTreeItem != choosedFileTreeItem) {
            choosedFileTreeItem = fileFileTreeItem;
            Show.setPath(fileFileTreeItem);
        }
//        }));
//        timeline.setCycleCount(1);
//        timeline.play();
    }

    //构建根目录的子目录节点
    private void setFirstFileTreeItem() {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        for (int i = 0; i < 4; i++) {
            File file = roostFiles[i];

            if (file.isDirectory()) {

                FileTreeItem treeI = new FileTreeItem(file);

                treeItem.getChildren().add(treeI);      //把树节点加入文件树视觉

                setFileTreeItemIcon(treeI);
                buildOneTreeNode(treeItem);
            }
        }

    }

    private void buildOneTreeNode(FileTreeItem treeItem) {
        treeItem.getChildren().add(new FileTreeItem());
    }

    //构建树节点的子节点
    private void buildTree(FileTreeItem treeItem) {
        //遍历文件目录中的文件，构建子节点
        for (File file : treeItem.getFile().listFiles()) {

            if (file.isDirectory()) {

                FileTreeItem treeI = new FileTreeItem(file);

                setFileTreeItemIcon(treeI);    //设置树节点的图标

                treeItem.getChildren().add(treeI);   //把树节点加入文件树视觉
            }

        }
    }

    //设置树节点的图标
    private void setFileTreeItemIcon(FileTreeItem item) {

        //获取系统文件的图标
        Image image = (Image) ((ImageIcon) FileTreeItem.fileSystemView
                .getSystemIcon(item.getFile())).getImage();
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
        item.setGraphic(canvas);

    }

    //展开树视觉节点，触发操作，把展开节点的孙子节点加到树上
    private void expandedItemOperation(FileTreeItem parentItem) {

        //把展开节点的子节点加载到childens里面
        ArrayList<FileTreeItem> childrens = new ArrayList<>(parentItem.getChildren());

        for (int i = 0; i < childrens.size(); i++) {
            FileTreeItem treeI = childrens.get(i);     //获取子节点
            //判断是否子节点展开
            if (treeI.isExpanded()) {

                loadGrandChildItem(treeI);             //拓展子节点的孙子节点，或者是父节点的曾孙节点
                childrens.addAll(treeI.getChildren());  //将子节点的孩子节点加入childrens里面
                //expandedItemOperation(treeI);

            }
        }

    }

    private void loadGrandChildItem(FileTreeItem parentItem) {

        //判断树节点是否是叶子节点
        if (!parentItem.isLeaf()) {
            //加载节点的子节点
            ObservableList<FileTreeItem> observable = parentItem.getChildren();
            for (FileTreeItem childItem : observable) {
                //子节点是否是叶子，父节点是否是网络
                if (childItem.isLeaf() && !parentItem.getFile().equals("网络")) {
                    //构建子节点的孩子节点，或者父节点的孙子节点
                    buildTree(childItem);
                }
            }

        }

    }

    public static Pane getFileSystemPane() {

        Pane pane = new Pane();
        //建造文件系统
        FileManageSystem fileSystem = new FileManageSystem();
        pane.getChildren().add(fileSystem.treeView);   //把树视觉加到面板

        return pane;
    }
}
