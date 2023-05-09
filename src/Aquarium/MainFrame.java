package Aquarium;

import Aquarium.FishTank.FishTankRender;
import Aquarium.Objects.Animals.Fish.CasualFish;
import Aquarium.Objects.Animals.Fish.PufferFish;
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

        fishTankRender.addAnimal(new CasualFish(fishTankRender, 100, 100, 50, 0));
        fishTankRender.addAnimal(new CasualFish(fishTankRender, 100, 100, 20, 0));
        fishTankRender.addAnimal(new PufferFish(fishTankRender));

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

        JMenu mainMenu = new JMenu("Buy");
        menuBar.add(mainMenu);
        JMenu subMenu = new JMenu("Animal");
        mainMenu.add(subMenu);
        JMenuItem fish = new JMenuItem("Fish");
        fish.addActionListener(e -> {
            // TODO: buy Fish
            fishTankRender.addAnimal(new CasualFish(fishTankRender));
        });
        subMenu.add(fish);
        JMenuItem jellyfish = new JMenuItem("Jellyfish");
        jellyfish.addActionListener(e -> {
            // TODO: buy jellyFish
            fishTankRender.addAnimal(new Jellyfish());
        });
        subMenu.add(jellyfish);

        setJMenuBar(menuBar);
    }

    public void addToolBar(){
        toolBar = new JToolBar();
        JButton fishButton = new JButton("Add Fish");
        fishButton.addActionListener(e -> fishTankRender.addAnimal(new CasualFish(fishTankRender)));
        JButton jellyfishButton = new JButton("Add Jellyfish");
        jellyfishButton.addActionListener(e -> fishTankRender.addAnimal(new Jellyfish()));
        JButton lightButton = new JButton("Light");
        lightButton.addActionListener(e -> fishTankRender.lightSwitch());
        JButton pixelArt = new JButton("PixelArt");
        pixelArt.addActionListener(e -> fishTankRender.pixelArtSwitch());
        toolBar.add(pixelArt);
        toolBar.add(lightButton);
        toolBar.add(fishButton);
        toolBar.add(jellyfishButton);
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
