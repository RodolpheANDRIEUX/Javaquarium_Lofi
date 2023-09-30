package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;
import Aquarium.Objects.Food;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tetras extends Fish {
    private final Color color;


    public Tetras(FishTankRender fishTankRender) {
        super(fishTankRender);
        this.x = (int) (Math.random() * MainFrame.WIDTH);
        this.y = (int) (Math.random() * MainFrame.HEIGHT);
        this.size = (Math.random() * 5) + 10;
        this.orientation = Math.random() * 360;
        this.speed = (SPEED-10) / size;
        this.direction = orientation;
        startTime = System.currentTimeMillis();
        color = new Color(203, 255, 216);
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
                    return;
                }
                target(visionX, visionY, 0.06);
            } else if (pixelColor.equals(Color.BLUE)){
                avoid(visionX, visionY, 0.002);
            }
        } else if (distance <= 300 && angle >  -5 && angle < 5) { // si il y a le bord dans les 10° face au poisson
            avoid(visionX, visionY, 0.002);
        }
    }

    private void drawTetras(Graphics g, Color fillColor) {
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
    }

    @Override
    public void paintFishVision(Graphics2D gFish) {
        drawTetras(gFish, Color.WHITE);
    }

    @Override
    public void paint(Graphics g) {
        drawTetras(g, color);
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


    }

    public void getTargeted(int x, int y, double weight){
        target(x, y, weight);
    }

}
