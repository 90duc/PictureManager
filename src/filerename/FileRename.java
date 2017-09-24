
package filerename;

import java.io.File;

/**
 *
 * @author MK
 */
public class FileRename {

   
    public static void main(String[] args) {
       File file=new File("G:11\\AAAB");
       File[] files=file.listFiles();
        for (int i = 0; i < files.length; i++) {
            
            files[i].renameTo(new File(file,String.format("%04d.jpg", i)));
        }
    }
    
}
