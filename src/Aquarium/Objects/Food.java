package Aquarium.Objects;

import Aquarium.MainFrame;

public class Food {
    private final double x;
    private double y;

    public Food(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void update(){
        if (y < MainFrame.HEIGHT-3){
            y ++;
        }
    }

}
