package Aquarium.Objects.Animals.Fish;

import Aquarium.FishTank.FishTankRender;
import Aquarium.MainFrame;
import Aquarium.Objects.Animals.Animal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Banc extends Animal {
    final FishTankRender fishTankRender;
    protected LinkedList<Tetras> members;
    private int cdNewDestination = 0;
    private int randomX;
    private int randomY;


    public Banc(FishTankRender fishTankRender){
        this.fishTankRender = fishTankRender;
        members = new LinkedList<>();
        for (int i = 0 ; i <= 20 ; i++){
            members.add(new Tetras(fishTankRender));
        }
    }

    @Override
    public void paint(Graphics g) {
        for (Tetras tetra : members){
            tetra.paint(g);
        }
    }

    @Override
    public void update(BufferedImage aquariumImage) {
        selectNewDestination();
        for (Tetras tetra : members){
            tetra.getTargeted(randomX, randomY, 0.008);
            tetra.update(aquariumImage);
        }
    }

    @Override
    public void paintFishVision(Graphics2D gFish) {
        for (Tetras tetra : members){
            tetra.paint(gFish);
        }
    }

    public void selectNewDestination(){
        if(cdNewDestination > 0){
            cdNewDestination--;
            return;
        }
        cdNewDestination = (int) (Math.random() * 200);
        randomX = (int) (Math.random() * MainFrame.WIDTH);
        randomY = (int) (Math.random() * MainFrame.HEIGHT);
    }

}
