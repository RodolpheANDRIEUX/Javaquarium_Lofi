package Aquarium.Objects.Animals;

import Aquarium.MainFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class Jellyfish extends Animal {
    private int jumpTimer = 0;
    private double direction = 0;
    private final Queue<Point> trail;

    public Jellyfish() {
        this.x = (int) (Math.random() * (MainFrame.WIDTH-50) + 25);
        this.y = (int) (Math.random() * (MainFrame.HEIGHT-50) + 25);
        this.size = (int) (Math.random() * 10 + 20);
        trail = new LinkedList<>();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(203, 255, 216, 30));
        g.fillOval((int) x, (int) (y - size/2), (int) size, (int) (size/2));

        if (!trail.isEmpty()) {
            Point trailPosition = trail.peek();
            g.fillOval((int) trailPosition.getX(), (int) (trailPosition.getY() - size / 2), (int) size, (int) (size / 2));
        }
    }

    @Override
    public void update(BufferedImage aquariumImage) {
        if (y >= 100){
            int jumpFrequency = 40000;
            int jump = (int) (Math.random() * jumpFrequency / y);
            if (jump == 0 || y > MainFrame.HEIGHT - 10) {
                jumpTimer = 10;
                if (x < 40 + (size/2) ){
                    direction = (int) (Math.random() * 4);
                } else if (x >= MainFrame.WIDTH - (40 + size/2)){
                    direction = (int) ((Math.random() * 4) - 4);
                } else {
                    direction = (int) ((Math.random() * 8) - 4);
                }
            }
        }

        if (jumpTimer > 0) {
            y -= 8;
            x += direction;
            jumpTimer--;
        } else {
            y += (25/size);
        }

        trail.add(new Point((int) x, (int) y));
        int trailDelay = 6;
        if (trail.size() > trailDelay) {
            trail.poll();
        }
    }

    @Override
    public void paintFishVision(Graphics2D gFish) {

    }
}

