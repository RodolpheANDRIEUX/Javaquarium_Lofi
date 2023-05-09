package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


public class CasualFish extends Fish {

    public CasualFish(FishTankRender fishTankRender) {
        super(fishTankRender);

        this.x = (int) (Math.random() * (MainFrame.WIDTH - 50) + 40);
        this.y = (int) (Math.random() * (MainFrame.HEIGHT - 50) + 40);
        this.size = (Math.random() * 10) + 10;
        this.orientation = Math.random() * 360;
        this.speed = SPEED / size;
        this.direction = orientation;
        startTime = System.currentTimeMillis();
        tail = new LinkedList<>();
        tailLength = (int) (size * TAIL);
    }

    public CasualFish(FishTankRender fishTankRender, int x, int y, double size, double orientation) {
        super(fishTankRender);

        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = SPEED / size;
        this.orientation = orientation;
        this.direction = orientation;
        startTime = System.currentTimeMillis();
        tail = new LinkedList<>();
        tailLength = (int) (size * TAIL);
    }

    @Override
    public void paint(Graphics g) {
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
        g.setColor(new Color(100, 255, 200));
        g.fillPolygon(xPoints, yPoints, 3);

        if (!tail.isEmpty()) {

            double i = size * 0.62;
            for (Point p : tail) {
                ;
                g.fillOval((int) (p.getX() - (i / 2)), (int) (p.getY() - (i / 2)), (int) i, (int) i);
                i -= 0.25 + (speed / (0.3 * size));
            }
            // debug
            g.setColor(new Color(0, 0, 255, 64));
//            g.drawOval((int) (x-200), (int) (y-200),400 , 400);
            int actualDirX =(int) (x + 40 * Math.cos(Math.toRadians(direction)));
            int actualDirY = (int) (y +40 * Math.sin(Math.toRadians(direction)));
            g.drawLine((int) x, (int) y, actualDirX, actualDirY);
        }
    }

    @Override
    public void update(BufferedImage aquariumImage) {

        // update different values
        age = (System.currentTimeMillis() - startTime) / 1000.0;

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