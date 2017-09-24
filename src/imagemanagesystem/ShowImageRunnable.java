package imagemanagesystem;

import cn.util.Functional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MK
 */
public class ShowImageRunnable implements Runnable {

   // private final Stack<Integer> list = new Stack<>();
    private final List<Integer> list=new ArrayList<>();
    private final Functional functional;
    private final int column;
    private byte[] rowFlags;

    public ShowImageRunnable(int column,Functional f) {
        this.column=column;
        this.functional=f;
    }

    @Override
    public void run() {
        while (true) {
            if (!list.isEmpty()) {   
                int row=list.remove(list.size()-1);
                if (rowFlags[row]==0) {
                    rowFlags[row]=1;
                    functional.method(row,column);
                }
                
            }else{       
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ex) {
                   // Logger.getLogger(ShowImageRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean add(int range) {
        for (Integer integer : new ArrayList<>(list)) {
            if (integer==range
                    ||rowFlags[integer]!=0) {
                list.remove(integer);
            }
        }
        
        boolean flag=rowFlags[range]==0;
        if (flag) {
             list.add(range);
        }
       
       return flag;
    }
    
    public void addAll(List<Integer> range) {
   
        list.addAll(range);
       
    }
    
    public void init(int length) {   
        list.clear();  
        rowFlags=new byte[length];
    }

    public int getColumn() {
        return column;
    }
}
