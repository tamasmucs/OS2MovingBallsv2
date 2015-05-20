/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private final Ball ball;
    private MovingBallsFX app;

    public BallRunnable(Ball ball, MovingBallsFX app) {
        this.ball = ball;
        this.app = app;
    }

    private void busySleep(long ms) {
        long end = System.currentTimeMillis() + ms;

        while (System.currentTimeMillis() < end) {
            // busy waiting...
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if(ball.isEnteringCs()){
                    if(!ball.isWriter){
                        app.RW.enterReader();
                    }else{
                        app.RW.enterWriter();
                    }
                }
                ball.move();
                if(ball.isLeavingCs()){
                    if(!ball.isWriter){
                        app.RW.exitReader();
                    }else{
                        app.RW.exitWriter();
                    }
                }
                busySleep(ball.getSpeed());
            }
            //When interrupted...
            if(ball.isInCs()){
                if(ball.isWriter){
                    app.RW.exitWriter();
                }else{
                    app.RW.exitReader();
                }
            }
        } catch (InterruptedException ex) {}
    }
}
