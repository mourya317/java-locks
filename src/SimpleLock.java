/**
 * Created by mballa on 02.11.2017.
 * it is simple lock
 * does not ensure fairness or reentrancy.
 */
public class SimpleLock implements Lock {
    private Boolean isLocked = false;
    private String lockedThread = null;

    @Override
    public void lock() {
        synchronized (this) {
            while (isLocked) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isLocked = true;
            lockedThread = Thread.currentThread().getName();
            System.out.println("Locked by :" + lockedThread);
        }
    }

    @Override
    public void unlock() {
        if (isLocked && lockedThread.equals(Thread.currentThread().getName())) {
            synchronized (this) {
                isLocked = false;
                notifyAll();
            }
        }
    }
}
