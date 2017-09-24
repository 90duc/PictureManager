package imagemanagesystem;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 *
 * @author MK
 */
public class FileTreeItem extends TreeItem<File> {

    //判断树节点是否被初始化，没有初始化为真
    private boolean notInitialized = true;

    //构造方法
    public FileTreeItem() {
    }

    public FileTreeItem(File file) {
        super(file);
    }

    public FileTreeItem(File value, Node graphic) {
        super(value, graphic);
    }

    //重写getchildren方法，让节点被展开时加载子目录
    @Override
    public ObservableList<TreeItem<File>> getChildren() {

        //没有加载子目录时，则加载子目录作为树节点的孩子
        if (this.notInitialized) {
            
            this.notInitialized = false;    //设置没有初始化为假

            /*判断树节点的文件是否是目录，
             *如果是目录，着把目录里面的所有的文件目录添加入树节点的孩子中。
             */
          
            if (this.getValue().isDirectory()) {
                for (File file : this.getValue().listFiles()) {
                    //如果文件是目录，则把它加到树节点上
                    if (file.isDirectory()) {
                        super.getChildren().add(new FileTreeItem(file));
                    }
                }

            }
        }
        return super.getChildren();
    }

    //重写叶子方法，如果该文件不是目录，则返回真
    @Override
    public boolean isLeaf() {
        System.out.println("leaf");
        return !this.getValue().isDirectory();
    }

    @Override
    public String toString() {
        return FileTreeView.FILE_SYSTEM_VIEW.getSystemDisplayName(this.getValue());
    }

}
