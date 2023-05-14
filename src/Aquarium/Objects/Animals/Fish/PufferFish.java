package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PufferFish extends Fish{
    private int cdPuff = 0;
    private int cdNewDestination = 0;
    private final int realSize;
    private int randomX;
    private int randomY;

    public PufferFish(FishTankRender fishTankRender){
        super(fishTankRender);
        this.x = 200;
        this.y = 200;
        this.realSize = 10;
        this.size = 10;
        direction = 45;
    }

    @Override
    public void behave(BufferedImage aquariumImage, int angle, int distance, int visionX, int visionY) {
        if (aquariumImage != null && visionX >= 0 && visionX < aquariumImage.getWidth() && visionY >= 0 && visionY < aquariumImage.getHeight()) {
            if (distance < realSize){
                int pixelColor = aquariumImage.getRGB(visionX, visionY);
                Color color = new Color(pixelColor);

                if(color.equals(Color.WHITE) || color.equals(Color.BLUE)){
                    cdPuff = 200;
                }

            }
        }
    }

    @Override
    public void paintFishVision(Graphics2D gFish) {
        paint(gFish);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(150, 200, 60));
        g.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
        g.setColor(new Color(255, 255, 200));
        g.fillOval( (int) (x + (size - (0.95 * size)) - (size / 2)), (int) (y + (size - (0.9*size)) - (size / 2)), (int) (0.95*size), (int) (0.9*size));

        // debug
//        g.setColor(new Color(255, 120, 2));
//        g.fillOval(randomX, randomY, 10, 10);
//        g.drawLine((int) x, 0, (int) x, MainFrame.HEIGHT);
//        g.drawLine(0, (int) y, MainFrame.WIDTH, (int) y);
    }

    @Override
    public void update(BufferedImage aquariumImage) {

        if (cdPuff > 0 ){
            switch (cdPuff){
                case 200, 1 -> size = realSize * 3;
                case 199, 2 -> size = realSize * 6;
                case 198, 3 -> size = realSize * 9;
                default -> size = realSize * 10;
            }
            cdPuff--;
        } else {
            size = realSize;
        }
        speed = 20 / size;

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
        selectNewDestination();
        move();
    }

    public void selectNewDestination(){
        if(cdNewDestination > 0){
            target(randomX, randomY, 0.01);
            cdNewDestination--;
            return;
        }
        cdNewDestination = (int) (Math.random() * 200);
        randomX = (int) (Math.random() * MainFrame.WIDTH);
        randomY = (int) (Math.random() * MainFrame.HEIGHT);
    }

}

