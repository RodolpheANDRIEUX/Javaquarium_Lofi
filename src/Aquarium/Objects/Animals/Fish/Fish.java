package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;
import Aquarium.Objects.Animals.Animal;
import Aquarium.Objects.Food;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class Fish extends Animal {
    protected final int SPEED = 80;
    protected final double TAIL = 2;

    private final FishTankRender fishTankRender;
    protected double speed, orientation;
    private double ripple = 0;
    protected double direction;
    protected long startTime;
    protected LinkedList<Point> tail;
    protected int tailLength;

    public Fish(FishTankRender fishTankRender) {
        this.fishTankRender = fishTankRender;
    }


    public void move() {
        ripple = 0.6 * size * Math.sin(60 * age / (size * size / 40));
        orientation = direction + ripple;

        x += speed * Math.cos(Math.toRadians(orientation));
        y += speed * Math.sin(Math.toRadians(orientation));
    }

    public void checkColision() {
        double headX = x + size * Math.cos(Math.toRadians(orientation));
        double headY = y + size * Math.sin(Math.toRadians(orientation));

        if (headX <= 0 || headX >= MainFrame.WIDTH) {
            direction = 180 - direction - ripple;
        }
        if (headY <= 0 || headY >= MainFrame.HEIGHT) {
            direction = -direction - ripple;
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

        for (int i = -visionAngle / 2; i <= visionAngle / 2; i += 2) {
            double directionX = Math.cos(Math.toRadians(direction + i));
            double directionY = Math.sin(Math.toRadians(direction + i));

            for (int j = 1; j <= visionRadius; j += 4) {
                int visionX = (int) (x + j * directionX);
                int visionY = (int) (y + j * directionY);

                // si on est dans le cadre
                if (aquariumImage != null && visionX >= 0 && visionX < aquariumImage.getWidth() && visionY >= 0 && visionY < aquariumImage.getHeight()) {
                    int pixelColor = aquariumImage.getRGB(visionX, visionY);
                    Color color = new Color(pixelColor);

                    if (color.equals(Color.RED)) {
                        double reach = 10;
                        double headX = size * Math.cos(Math.toRadians(orientation)) + x;
                        double headY = size * Math.sin(Math.toRadians(orientation)) + y;
                        if (headX - visionX <= reach && headX - visionX >= -reach && headY - visionY <= reach && headY - visionY >= -reach) {
                            Food foodP = fishTankRender.eventEat(visionX, visionY);
                            eat(foodP, fishTankRender.getFoodList());
                            return;
                        }
                        target(visionX, visionY, 0.06);
                        return;
                    }

                } else if (j > (visionAngle / 2) - 5 && j < (visionAngle / 2) + 5) { // si il y a le bord dans les 10° face au poisson
                    avoid(visionX, visionY, 0.0025);
                } else {
                    break;
                }
            }
        }
    }

    public void eat(Food foodP, java.util.List<Food> foodList) {
        if (size <= 50) {
            size += 3;
        }
        tailLength = (int) (size * TAIL);
        speed = SPEED / size;
        if (foodP != null && foodList != null) {
            foodList.remove(foodP);
        }
    }

    @Override
    public abstract void paint(Graphics g);

    @Override
    public abstract void update(BufferedImage aquariumImage);
}
