/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.util;

import java.io.File;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author MK
 */
public class FileTreeItem extends TreeItem {

    private File file = null;      //保存节点的有关文件

    //获得文件系统视觉管理
    public static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    public FileTreeItem() {
        super();
    }

    public FileTreeItem(File value) {
        super(fileSystemView.getSystemDisplayName(value));
        file = value;
    }

    public FileTreeItem(File value, Node graphic) {

        super(fileSystemView.getSystemDisplayName(value), graphic);
        file = value;
    }

    //设置文件

    public void setFile(File file) {

        this.file = file;

    }

    //获得文件

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return fileSystemView.getSystemDisplayName(file);
    }

}
