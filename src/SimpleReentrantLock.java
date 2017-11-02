/**
 * Created by mballa on 02.11.2017.
 * renetrant lock
 * doesnt ensure fairness . Has a lockCounter to maintain number of times lock is entered by a thread
 */
public class SimpleReentrantLock implements Lock {
    private Boolean isLocked = false;
    private String lockedThread = null;
    private int lockCounter=0;

    @Override
    public void lock() {
        synchronized (this) {
            while (isLocked && !Thread.currentThread().getName().equals(lockedThread)) { // we shal wait if it is diferrent thread .
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isLocked = true;
            lockCounter++;
            lockedThread = Thread.currentThread().getName();
            System.out.println("Locked by :" + lockedThread);
        }
    }

    @Override
    public void unlock() {
        if (isLocked && lockedThread.equals(Thread.currentThread().getName())) {
            synchronized (this) {
                lockCounter--;
                if(lockCounter == 0) {
                    isLocked = false;
                    notifyAll();
                }
            }
        }
    }
}
