package movingballsfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Joris
 */
public class ThreadOverview implements Runnable {

    private final Label labelHeader;
    private final Label labelInfo;

    public ThreadOverview() {
        labelHeader = new Label(" id prio  name                   state      #blocked   cpu(ms)");
        labelInfo = new Label("");
        labelHeader.setFont(Font.font("DejaVu Sans Mono", 12));
        labelInfo.setFont(Font.font("DejaVu Sans Mono", 12));
        VBox root2 = new VBox();
        root2.getChildren().add(labelHeader);
        root2.getChildren().add(labelInfo);
        Scene scene2 = new Scene(root2, 900, 240);
        Stage secondStage = new Stage();
        secondStage.setX(100);
        secondStage.setY(10);
        secondStage.initStyle(StageStyle.UTILITY);
        secondStage.setTitle("thread overview");
        secondStage.setScene(scene2);
        secondStage.show();
        
        Thread t = new Thread (null, this, "Thread-Overview");
        t.start();
    }

    private int getPrio(Thread[] threads, long id)
    {
        for (Thread t : threads)
        {
            try {
                if (t.getId() == id) {
                    return (t.getPriority());
                }
            } catch (NullPointerException e) {
                // thread might have been finished....
                return (-1);
            }
        }
        return (-1);
    }
    
    private void updateThreadInfo() {
        Thread[] threads = new Thread[Thread.activeCount()];
        String s = "";
        ThreadMXBean tb = ManagementFactory.getThreadMXBean();
        ThreadInfo[] til = tb.dumpAllThreads(false, false);
        String format = "%3d %2d %-25s %-13s %5d %9.3f %10d %2d";
        
        Thread.enumerate(threads);
        
        for (ThreadInfo ti : til) {
            int hash = -1;
            if (ti.getLockInfo() != null)
            {
                hash = ti.getLockInfo().getIdentityHashCode();
            }
            s += String.format(format,
                    ti.getThreadId(), 
                    getPrio(threads, ti.getThreadId()),
                    ti.getThreadName(), 
                    ti.getThreadState().toString(),
                    ti.getBlockedCount(), 
                    tb.getThreadCpuTime(ti.getThreadId()) / 1000000.,
                    hash,
                    ti.getLockOwnerId()
            );
            MonitorInfo[] mil = ti.getLockedMonitors();
            s += " " + mil.length;
            LockInfo[] lil = ti.getLockedSynchronizers();
            s += " " + lil.length;
            for (MonitorInfo mi : mil) {
                s += " " + mi.getIdentityHashCode();
            }
            s += "\n";
        }
        labelInfo.setText(s);

    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            while (true) {
                Thread.sleep(100);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateThreadInfo();
                    }
                });
            }
        } catch (InterruptedException ex) {
        }
    }
}
