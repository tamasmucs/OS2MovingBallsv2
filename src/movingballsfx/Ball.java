package movingballsfx;

import java.util.Random;
import javafx.scene.paint.Color;

public class Ball {

    private int xPos;
    private final int yPos;
    private final int speed;
    private final int minX, maxX;
    private final Color color;
    private final int minCsX;
    private final int maxCsX;
    public boolean isWriter;
    
    public Ball(int minX, int maxX, int minCsX, int maxCsX, int yPos, Color color, boolean isWriter) {
        this.xPos = minX;
        this.yPos = yPos;
        this.minX = minX;
        this.maxX = maxX;
        this.minCsX = minCsX;
        this.maxCsX = maxCsX;
        this.speed = 10 + (new Random()).nextInt(5);
        this.color = color;
        this.isWriter = isWriter;
    }

    public void move() {
        xPos++;
        if (xPos > maxX) {
            xPos = minX;
        }
    }

    public int getXPos() {
        return xPos;
    }

   public int getYPos() {
        return yPos;
    }

    public Color getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isEnteringCs() {
        return xPos == minCsX;
    }
    
    public boolean isLeavingCs() {
        return xPos == maxCsX;
    }
    
    protected boolean isInCs() {
        return (xPos >= minCsX) && (xPos <= maxCsX);
    }
}
