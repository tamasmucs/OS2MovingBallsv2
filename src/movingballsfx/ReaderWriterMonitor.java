/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

/**
 *
 * @author tamas
 */
public class ReaderWriterMonitor {
    private int writersActive = 0;
    private int readersActive = 0;
    private int writersWaiting = 0;
    private int readersWaiting = 0;
    public MovingBallsFX app;
    
    public ReaderWriterMonitor(MovingBallsFX app){
        this.app = app;
    }
    
    public synchronized void enterReader()  throws InterruptedException {
        readersWaiting++;
        while (writersActive > 0) { 
            wait(); 
        }
        readersWaiting--;
        readersActive++;
    }

    public synchronized void exitReader() {
        readersActive--;
        notifyAll();
    }
    
    public synchronized void enterWriter() throws InterruptedException {
        writersWaiting++;
        while (writersActive > 0 || readersActive > 0 || (this.readersWaiting > 0 && app.readersHavePriority == true)) {
            wait();
        }
        writersWaiting--;
        writersActive++;
    }
    
    public synchronized void exitWriter(){
        writersActive--;
        notifyAll();
    }
}
