package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;
import Aquarium.Objects.Animals.Animal;
import Aquarium.Objects.Food;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class Fish extends Animal {
    // const for formulas
    protected final int SPEED = 80;
    protected final double TAIL = 1.5;

    // technical vars
    final FishTankRender fishTankRender;
    protected long startTime;

    // dynamic
    protected double speed, orientation;
    protected double direction;
    private double ripple = 0;

    // draw
    protected LinkedList<Point> tail;
    protected int tailLength;

    public Fish(FishTankRender fishTankRender) {
        super();
        this.fishTankRender = fishTankRender;
    }

    public void move() {
        ripple = 0.6 * size * Math.sin(60 * age / (size * size / 40));
        orientation = direction + ripple;

        if( fishTankRender.isFollowMode()){
            target(fishTankRender.getMouseX(), fishTankRender.getMouseY(), 0.01);
        }

        x += speed * Math.cos(Math.toRadians(orientation));
        y += speed * Math.sin(Math.toRadians(orientation));
    }

    public void checkColision() {
        double headX = x + size * Math.cos(Math.toRadians(orientation));
        double headY = y + size * Math.sin(Math.toRadians(orientation));

        if (headX <= 0 || headX >= MainFrame.WIDTH) {
            target(MainFrame.WIDTH/2, (int) y, 0.1);
        }
        if (headY <= 0 || headY >= MainFrame.HEIGHT) {
            target((int) x, MainFrame.HEIGHT/2, 0.1);
        }
    }

    public double angleChangeToTarget(int targetX, int targetY) {
        double angleToMouse = Math.atan2(targetY - y, targetX - x);
        double angleToMouseDegrees = Math.toDegrees(angleToMouse);

        double angleDifference = angleToMouseDegrees - direction;
        if (angleDifference > 180) {
            return angleDifference - 360;
        } else if (angleDifference < -180) {
            return angleDifference + 360;
        }
        return angleDifference;
    }

    public void target(int targetX, int targetY, double weight) {
        direction += angleChangeToTarget(targetX, targetY) * weight * speed;
    }

    public void avoid(int targetX, int targetY, double weight) {
        direction -= angleChangeToTarget(targetX, targetY) * weight * speed;
    }

    public void surroundingAnalysis(BufferedImage aquariumImage) {
        int visionRadius = (int) (10 * size);
        int visionAngle = 200;

        // pour chaque 2 degres d'angle de vision
        for (int i = -visionAngle / 2; i <= visionAngle / 2; i += 2) {
            double directionX = Math.cos(Math.toRadians(orientation + i));
            double directionY = Math.sin(Math.toRadians(orientation + i));


            // tout les 4 pixels de distance
            for (int j = 1; j <= visionRadius; j += 4) {
                int visionX = (int) (x + j * directionX);
                int visionY = (int) (y + j * directionY);

                behave(aquariumImage, i, j, visionX, visionY);
            }
        }
    }

    public abstract void behave(BufferedImage aquariumImage, int i, int j, int visionX, int visionY);

    public void eat(Food foodP, java.util.List<Food> foodList) {
        if (foodP != null && foodList != null) {
            if (size <= 30) {
                size ++;
            }
            foodList.remove(foodP);
        }
    }
}
