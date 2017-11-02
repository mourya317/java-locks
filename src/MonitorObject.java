/**
 * Created by mballa on 02.11.2017.
 * used by fair lock .
 * used by multiple threads to obtain lock
 */
public class MonitorObject {
    private boolean notified=false;
    public synchronized void dowait(){
        while(!notified){
            try{
                wait();
            }catch (InterruptedException e){

            }
        }
    }

    public synchronized void donotify(){
        notified=true;
        notify();
    }
}
