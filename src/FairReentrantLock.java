import java.util.ArrayList;
import java.util.List;

/**
 * Created by mballa on 02.11.2017.
 * fair lock. doesnt support reentrancy.
 * it will maintain Queue of threads waiting and each will receive the lock in the order of their request
 */
public class FairReentrantLock implements Lock{
    private boolean isLocked = false;
    private String lockedThread = null;
    private int lockCounter = 0;
    List<MonitorObject> threadQueue = new ArrayList<>();


    @Override
    public void lock() {

        //we will increase the lockCounter if the same thread is calling again to get lock
        synchronized (this){
            if(Thread.currentThread().getName().equals(lockedThread)){
                lockCounter++;
                System.out.println("Lock obtained by: " + Thread.currentThread().getName());
                return;

            }
        }
        //we will wait on the monitor of the calling thread if it is already locked by other thread
        MonitorObject monitorObject = new MonitorObject();
        synchronized (this){
            threadQueue.add(monitorObject);
            System.out.println("lock requested by :"+Thread.currentThread().getName());
        }
        //wait if :
        //1) locked by other thread
        //2) not the next member of Q
        while(isLocked || !(threadQueue.get(0).equals(monitorObject))){
            monitorObject.dowait();
        }

        //To give lock
        synchronized (this){
            isLocked=true;
            lockCounter++;
            lockedThread=Thread.currentThread().getName();
            threadQueue.remove(0);
            System.out.println("lock obtained by :"+Thread.currentThread().getName());
        }

    }

    @Override
    public void unlock() {
        if(isLocked && Thread.currentThread().getName().equals(lockedThread)){
            synchronized (this){
                lockCounter--;
                if(lockCounter == 0) {
                    isLocked = false;
                    lockedThread = null;
                    System.out.println("Lock released by: " + Thread.currentThread().getName());
                    if (!threadQueue.isEmpty()) {
                        threadQueue.get(0).donotify();
                    }
                }
            }
        }
    }
}
