package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;
import Aquarium.Objects.Food;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


public class CasualFish extends Fish {
    private boolean speedUp;
    private Color color;

    public CasualFish(FishTankRender fishTankRender) {
        super(fishTankRender);
        this.x = (int) (Math.random() * (MainFrame.WIDTH - 100) + 50);
        this.y = (int) (Math.random() * (MainFrame.HEIGHT - 100) + 50);
        this.size = (Math.random() * 10) + 10;
        this.orientation = Math.random() * 360;
        this.speed = SPEED / size;
        this.direction = orientation;
        startTime = System.currentTimeMillis();
        tail = new LinkedList<>();
        tailLength = (int) (size * 0.5);
        initColor();
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
        tailLength = (int) (size * 0.5);
        initColor();
    }

    private void initColor(){
        int rndColor =(int) (Math.random() * 4);
        switch (rndColor){
            case 1 -> color = new Color(255, 127, 125);
            case 2 -> color = new Color(83, 167, 136);
            case 3 -> color = new Color(83, 127, 255);
            case 4 -> color = new Color(112, 211, 139);
            default -> color = new Color(203, 255, 216);
        }
    }

    @Override
    public void behave(BufferedImage aquariumImage, int angle, int distance, int visionX, int visionY) {
        // si on est dans le cadre
        if (aquariumImage != null && visionX >= 0 && visionX < aquariumImage.getWidth() && visionY >= 0 && visionY < aquariumImage.getHeight()) {
            int pixel = aquariumImage.getRGB(visionX, visionY);
            Color pixelColor = new Color(pixel);

            if (pixelColor.equals(Color.RED)) { // food
                double reach = 10;
                double headX = size * Math.cos(Math.toRadians(orientation)) + x;
                double headY = size * Math.sin(Math.toRadians(orientation)) + y;
                if (headX - visionX <= reach && headX - visionX >= -reach && headY - visionY <= reach && headY - visionY >= -reach) {
                    Food foodP = fishTankRender.eventEat(visionX, visionY);
                    eat(foodP, fishTankRender.getFoodList());
                    return;
                }
                speedUp = true;
                target(visionX, visionY, 0.06);
            } else if (pixelColor.equals(Color.BLUE)){
                avoid(visionX, visionY, 0.002);
            }

        } else if (distance <= 300 && angle >  -5 && angle < 5) { // si il y a le bord dans les 10° face au poisson
            avoid(visionX, visionY, 0.002);
        }
    }

    private void drawCasualFish(Graphics g, Color fillColor) {
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
        drawCasualFish(gFish, Color.WHITE);
    }

    @Override
    public void paint(Graphics g) {
        drawCasualFish(g, color);
    }

    @Override
    public void update(BufferedImage aquariumImage) {

        // update different values
        age = (System.currentTimeMillis() - startTime) / 1000.0;
        tailLength = (int) (size * 0.6);

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
        // horizontalité de mouvement
        if (direction > 90 && direction <= 180 || direction > -90 && direction <= 0){
            direction ++;
        } else if (direction > 0 && direction <= 90 || direction > -180 && direction <= -90){
            direction --;
        }

        // actions
        if(speedUp){
            speed = SPEED * 1.5 / size;
        } else {
            speed = SPEED / size;
        }
        move();
        speedUp = false;

        // tail update
        tail.addFirst(new Point((int) x, (int) y));
        if (tail.size() > tailLength) {
            tail.removeLast();
        }
    }


}