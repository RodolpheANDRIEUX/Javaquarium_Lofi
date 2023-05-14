package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Shark extends Fish {
    private final Color color;

    public Shark(FishTankRender fishTankRender) {
        super(fishTankRender);

        this.x = (int) (Math.random() * (MainFrame.WIDTH));
        this.y = (int) (Math.random() * (MainFrame.HEIGHT));
        this.size = (Math.random() * 10) + 40;
        this.orientation = Math.random() * 360;
        this.speed = SPEED / size;
        this.direction = orientation;
        startTime = System.currentTimeMillis();
        tail = new LinkedList<>();
        tailLength = (int) (size * TAIL);
        color = new Color(160, 200, 255);
    }

    @Override
    public void behave(BufferedImage aquariumImage, int angle, int distance, int visionX, int visionY) {
        // si on est dans le cadre
        if (aquariumImage != null && visionX >= 0 && visionX < aquariumImage.getWidth() && visionY >= 0 && visionY < aquariumImage.getHeight()) {
            int pixel = aquariumImage.getRGB(visionX, visionY);
            Color pixelColor =  new Color(pixel);

            if (pixelColor.equals(Color.WHITE)) {
                double reach = 9;
                double headX = size * Math.cos(Math.toRadians(orientation)) + x;
                double headY = size * Math.sin(Math.toRadians(orientation)) + y;
                if (headX - visionX <= reach && headX - visionX >= -reach && headY - visionY <= reach && headY - visionY >= -reach) {
//                    Food foodP = fishTankRender.eventEat(visionX, visionY);
//                    eat(foodP, fishTankRender.getFoodList());
                    // I dont want him to actually eat fishes, so it's empty here
                    return;
                }
                target(visionX, visionY, 0.001);
            }

        } else if (distance <= 300 && angle >  -5 && angle < 5) { // si il y a le bord dans les 10° face au poisson
            avoid(visionX, visionY, 0.002);
        }
    }

    private void drawShark(Graphics g, Color fillColor) {
        int BodyX = (int) (size / 3 * Math.sin(Math.toRadians(orientation)));
        int BodyY = (int) (size / 3 * Math.cos(Math.toRadians(orientation)));
        int[] xPoints = {
                (int) (x + BodyY * 3),
                (int) (x + BodyX),
                (int) (x - BodyX)
        };
        int[] yPoints = {
                (int) (y + BodyX * 3),
                (int) (y - BodyY),
                (int) (y + BodyY)
        };
        g.setColor(fillColor);
        g.fillPolygon(xPoints, yPoints, 3);

        if (!tail.isEmpty()) {
            double i = size * 0.62;
            for (Point p : tail) {
                g.fillOval((int) (p.getX() - (i / 2)), (int) (p.getY() - (i / 2)), (int) i, (int) i);
                i -= 0.25 + (speed / (0.3 * size));
            }
        }
    }

    @Override
    public void paintFishVision(Graphics2D gFish) {
        drawShark(gFish, Color.BLUE);
    }

    @Override
    public void paint(Graphics g) {
        drawShark(g, color);
    }

    @Override
    public void update(BufferedImage aquariumImage) {

        // update different values
        age = (System.currentTimeMillis() - startTime) / 1000.0;
        tailLength = (int) (size * TAIL);

        // behavior
        surroundingAnalysis(aquariumImage);

        // calculus
        checkColision();

        // direction
        if (direction > 180) {
            direction -= 360;
        } else if (direction < -180) {
            direction += 360;
        }

        // actions
        move();

        // tail update
        tail.addFirst(new Point((int) x, (int) y));
        if (tail.size() > tailLength) {
            tail.removeLast();
        }
    }

}
