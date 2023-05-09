package Aquarium.Objects.Animals;

import Aquarium.FishTank.FishTankRender;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Animal {
    protected double x, y;
    protected double size;
    protected double age = 0;

    public abstract void paint(Graphics g);
    public abstract void update(BufferedImage aquariumImage);
}
