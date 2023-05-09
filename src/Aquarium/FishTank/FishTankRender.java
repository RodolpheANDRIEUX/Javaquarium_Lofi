package Aquarium.FishTank;

import Aquarium.MainFrame;
import Aquarium.Objects.Animals.Animal;
import Aquarium.Objects.Animals.Fish.CasualFish;
import Aquarium.Objects.Animals.Jellyfish;
import Aquarium.Objects.Food;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class FishTankRender extends JPanel implements MouseListener, MouseMotionListener {
    private final java.util.List<Animal> animals;
    private final java.util.List<Food> food;
    private BufferedImage backgroundImage;
    private int mouseX, mouseY;

    // graphics
    private boolean lightOn = true;
    private boolean pixelArtOn = true;

    public FishTankRender() {
        food = new ArrayList<>();
        animals = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("assets/background.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int delay;
        delay = 10;
        ActionListener render = e -> {
            update();
            repaint();
        };

        Timer timer = new Timer(delay, render);
        timer.start();
    }

    public java.util.List<Food> getFoodList() {
        return food;
    }

    // used to recreate the environment for the fish so he can see!
    private BufferedImage createFishVision() {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }

        BufferedImage fishVision = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gFish = fishVision.createGraphics();

        for (Food foodP : food) {
            gFish.setColor(Color.red);
            gFish.fillOval((int) foodP.getX(), (int) foodP.getY(), 5, 5);
        }

        for (Animal animal : animals) {
            animal.paint(gFish);
        }

        gFish.dispose();
        return fishVision;
    }

    public void update(){
        BufferedImage aquariumImage = createFishVision();
        for (Animal animal : animals) {
            animal.update(aquariumImage);
        }
        for (Food food : food) {
            food.update();
        }
    }

    public void addFood(Food foodParticle) {
        food.add(foodParticle);
        repaint();
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        BufferedImage sceneImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = sceneImage.createGraphics();

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, MainFrame.HEIGHT-1, MainFrame.WIDTH-1, MainFrame.HEIGHT-1);
        g2d.drawLine(MainFrame.WIDTH-1, 0, MainFrame.WIDTH-1, MainFrame.HEIGHT-1);
        g2d.drawLine(0 , 0, 0, MainFrame.HEIGHT);
        g2d.drawLine(0, 0, MainFrame.WIDTH, 0);

        for (Food foodP : food) {
            g2d.setColor(Color.red);
            g2d.fillOval((int) foodP.getX(), (int) foodP.getY(), 5, 5);
        }

        for (Animal animal : animals) {
            animal.paint(g2d);
        }

        if (lightOn){
            GradientPaint gradient = new GradientPaint((float) getWidth() /2, 0, new Color(100, 255, 200, 26), (float) getWidth() /2, getHeight(), new Color(0, 0, 0, 0));
            g2d.setPaint(gradient);
        } else {
            g2d.setPaint(new Color(0, 0, 0, 120));
        }
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if(pixelArtOn){
            g.drawImage(pixelArtAfterEffect(width, height, sceneImage, 3), 0, 0, null);
        } else {
            g.drawImage(pixelArtAfterEffect(width, height, sceneImage, 1), 0, 0, null);
        }
    }

    private Image pixelArtAfterEffect(int width, int height, BufferedImage sceneImage, int scaleFactor){
        int newWidth = width / scaleFactor;
        int newHeight = height / scaleFactor;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gResized = resizedImage.createGraphics();
        gResized.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gResized.drawImage(sceneImage, 0, 0, newWidth, newHeight, null);
        gResized.dispose();

        BufferedImage pixelArtImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gPixelArt = pixelArtImage.createGraphics();
        gPixelArt.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gPixelArt.drawImage(resizedImage, 0, 0, width, height, null);
        gPixelArt.dispose();

        return pixelArtImage;
    }

    // returns the food that has been eaten at those coordinates
    public Food eventEat(int x, int y){
        for (Food foodP : food) {
            int fX = (int) foodP.getX();
            int fY = (int) foodP.getY();
            if (fX - x <= 5 && fX - x >= -5 && fY - y <= 5 && fY - y >= -5) {
                return foodP;
            }
        }
        return null;
    }

    public void pixelArtSwitch(){
        if (pixelArtOn){
            pixelArtOn = false;
            return;
        }
        pixelArtOn = true;
    }


    public void lightSwitch(){
        if (lightOn){
            lightOn = false;
            return;
        }
        lightOn = true;
    }






    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        addFood(new Food(mouseX, mouseY));
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

}
