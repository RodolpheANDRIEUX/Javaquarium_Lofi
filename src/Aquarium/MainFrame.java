package Aquarium;

import Aquarium.FishTank.FishTankRender;
import Aquarium.Objects.Animals.Fish.*;
import Aquarium.Objects.Animals.Jellyfish;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;


public class MainFrame extends JFrame {
    private final FishTankRender fishTankRender;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    public static int WIDTH;
    public static int HEIGHT;

    public MainFrame() {
        setTitle("Aquarium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        fishTankRender = new FishTankRender();
        add(fishTankRender);

        addMenuBar();
        addToolBar();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateDimensions();
            }
        });
    }

    public void addMenuBar(){
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
            JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);


        JMenu BuyMenu = new JMenu("Buy");
        menuBar.add(BuyMenu);

        JMenu buyTypeMenu = new JMenu("Animal");
        BuyMenu.add(buyTypeMenu);

        JMenu buyFishMenu = new JMenu("Fish");
        buyTypeMenu.add(buyFishMenu);

        JMenuItem casualFish = new JMenuItem("Casual Fish");
        casualFish.addActionListener(e -> {
            fishTankRender.addAnimal(new CasualFish(fishTankRender));
        });
        buyFishMenu.add(casualFish);

        JMenuItem shark = new JMenuItem("Shark");
        shark.addActionListener(e -> {
            fishTankRender.addAnimal(new Shark(fishTankRender));
        });
        buyFishMenu.add(shark);

        JMenuItem pufferFish = new JMenuItem("Puffer Fish");
        pufferFish.addActionListener(e -> {
            fishTankRender.addAnimal(new PufferFish(fishTankRender));
        });
        buyFishMenu.add(pufferFish);

        JMenuItem tetras = new JMenuItem("20 Tetras Fish");
        tetras.addActionListener(e -> {
            fishTankRender.addAnimal(new Banc(fishTankRender));
        });
        buyFishMenu.add(tetras);


        JMenuItem jellyfish = new JMenuItem("Jellyfish");
        jellyfish.addActionListener(e -> {
            fishTankRender.addAnimal(new Jellyfish());
        });
        buyTypeMenu.add(jellyfish);

        setJMenuBar(menuBar);
    }

    public void addToolBar(){
        toolBar = new JToolBar();
        JButton followButton = new JButton("Follow me");
        followButton.addActionListener(e -> fishTankRender.followSwitch());
        toolBar.add(followButton);

        JButton lightButton = new JButton("Light");
        lightButton.addActionListener(e -> {
            try {
                fishTankRender.lightSwitch();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        toolBar.add(lightButton);

        JButton pixelArt = new JButton("PixelArt");
        pixelArt.addActionListener(e -> fishTankRender.pixelArtSwitch());
        toolBar.add(pixelArt);

        add(toolBar, BorderLayout.NORTH);
        toolBar.setFloatable(false);
    }

    public void updateDimensions(){
        Dimension screenSize = getContentPane().getSize();
        int toolbarHeight = toolBar.getHeight();
        WIDTH = screenSize.width;
        HEIGHT = screenSize.height - toolbarHeight;
    }

}
