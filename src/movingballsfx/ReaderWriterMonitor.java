/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private final Lock lock = new ReentrantLock();
    private final Condition readerOKToGo = lock.newCondition();
    private final Condition writerOKToGo = lock.newCondition();

    public ReaderWriterMonitor(MovingBallsFX app){
        
        this.app = app;
    }
    
    public void enterReader()  throws InterruptedException {
        lock.lock();
        try {
            readersWaiting++;
            while (writersActive > 0) {
                readerOKToGo.await();
            }
            readersWaiting--;
            readersActive++;
        } finally {
            lock.unlock();
        }
    }

    public void exitReader() {
        lock.lock();
        try {
            readersActive--;
            if(app.readersHavePriority){
                readerOKToGo.signalAll();
                writerOKToGo.signal();
            }else{
                if((new Random()).nextBoolean()){
                    readerOKToGo.signalAll();
                    writerOKToGo.signal();
                }else{
                    writerOKToGo.signal();
                    readerOKToGo.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void enterWriter() throws InterruptedException {
        lock.lock();
        try {
            writersWaiting++;
            while (writersActive > 0 || readersActive > 0) {
                writerOKToGo.await();
            }
            writersWaiting--;
            writersActive++;
        } finally {
                lock.unlock();
        }
    }
    
    public  void exitWriter(){
        writersActive--;
        lock.lock();
        try {
            if(app.readersHavePriority){
                readerOKToGo.signalAll();
                writerOKToGo.signal();
            }else{
                if((new Random()).nextBoolean()){
                    readerOKToGo.signalAll();
                    writerOKToGo.signal();
                }else{
                    writerOKToGo.signal();
                    readerOKToGo.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
