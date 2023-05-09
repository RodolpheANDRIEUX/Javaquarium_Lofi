package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PufferFish extends Fish{

    public PufferFish(FishTankRender fishTankRender){
        super(fishTankRender);

        this.x = 200;
        this.y = 200;
        this.size = 6;
        speed = 1;
        direction = 45;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(150, 200, 60));
        g.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
    }

    @Override
    public void update(BufferedImage aquariumImage) {

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

}

