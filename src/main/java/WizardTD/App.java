package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public boolean isGameLost = false;


    public String configPath;

    public static final Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public Board board;

    public boolean isPaused = false; 
    public boolean isFastForwarded = false; 

    public PImage grassImg;
    public PImage shrubImg;
    public PImage pathImg0;
    public PImage pathImg1;
    public PImage pathImg2;
    public PImage pathImg3;
    public PImage towerImg;
    public PImage tower1Img;
    public PImage tower2Img;
    public PImage freezeTowerImg;
    public PImage wizardHouseImg;
    public PImage gremlinImg;
    public PImage beetleImg;
    public PImage wormImg;
    public PImage fireballImg;
    public PImage gremlinDeathImg1;
    public PImage gremlinDeathImg2;
    public PImage gremlinDeathImg3;
    public PImage gremlinDeathImg4;
    public PImage gremlinDeathImg5;
    

    public UserInterface userInterface;

    public MonsterConfig monsterConfig;

    public ConfigReader configReader;

    public WaveManager waveManager;

    public List<Enemy> activeMonsters = new ArrayList<>();
    public Pathfinder pathfinder;
    public ManaSystem manaSystem;





    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    Enemy enemy;

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        this.configReader = new ConfigReader(this, configPath);

        userInterface = new UserInterface(this);

        grassImg = loadImage("src/main/resources/WizardTD/grass.png");
        shrubImg = loadImage("src/main/resources/WizardTD/shrub.png");
        pathImg0 = loadImage("src/main/resources/WizardTD/path0.png");
        pathImg1 = loadImage("src/main/resources/WizardTD/path1.png");
        pathImg2 = loadImage("src/main/resources/WizardTD/path2.png");
        pathImg3 = loadImage("src/main/resources/WizardTD/path3.png");
        towerImg = loadImage("src/main/resources/WizardTD/tower0.png");
        tower1Img = loadImage("src/main/resources/WizardTD/tower1.png");
        tower2Img = loadImage("src/main/resources/WizardTD/tower2.png");
        freezeTowerImg = loadImage("src/main/resources/WizardTD/freezeTower (2).png");
        wizardHouseImg = loadImage("src/main/resources/WizardTD/wizard_house.png");
        gremlinImg = loadImage("src/main/resources/WizardTD/gremlin.png");
        beetleImg = loadImage("src/main/resources/WizardTD/beetle.png");
        wormImg = loadImage("src/main/resources/WizardTD/worm.png");
        fireballImg = loadImage("src/main/resources/WizardTD/fireball.png");
        gremlinDeathImg1 = loadImage("src/main/resources/WizardTD/gremlin1.png");
        gremlinDeathImg2 = loadImage("src/main/resources/WizardTD/gremlin2.png");
        gremlinDeathImg3 = loadImage("src/main/resources/WizardTD/gremlin3.png");
        gremlinDeathImg4 = loadImage("src/main/resources/WizardTD/gremlin4.png");
        gremlinDeathImg5 = loadImage("src/main/resources/WizardTD/gremlin5.png");

        
        String layoutFileName = configReader.getLayout();
        board = new Board(this, layoutFileName);

        manaSystem = new ManaSystem(this, configReader.getInitialMana(), configReader.getInitialManaCap(), configReader.getInitialManaGainedPerSecond());

        waveManager = new WaveManager(this, configReader.getWaves());

        pathfinder = new Pathfinder(board);
        

    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed() {
        if (key == 'r' || key == 'R') {
            if (isGameLost) {
                resetGame();
            }

        }
        userInterface.handleHotkey(Character.toLowerCase(key));
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        userInterface.onClick(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /*@Override
    public void mouseDragged(MouseEvent e) {

    }*/

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        if (!isGameLost) {
            background(132, 115, 74, 225);

            // Always draw the game board, regardless of whether the game is paused or not
            board.draw();

            // Always update and draw the enemies, regardless of whether the game is paused or not
            for (Enemy enemy : activeMonsters) {
                enemy.draw();
            }

            if (!isPaused) {
                // Game logic
                tick();
                if (isFastForwarded) {
                    tick();
                }
            }

            // Always draw user interface elements
        userInterface.draw(mouseX, mouseY);

        waveManager.draw();

        manaSystem.draw(WIDTH, TOPBAR);
        

        } else {
            textSize(50);
            fill(255, 0, 0);  // Set the text color to red
            text("YOU LOST", BOARD_WIDTH * CELLSIZE / 2, BOARD_WIDTH * CELLSIZE / 2);
        }


    }

    // NEW: Your tick function
    private void tick() {
        // System.out.println("Tick function called."); // Debug print
        // Update all game logic here.
        // For example, update the board, wave manager, and move enemies.
        
        board.draw();
        waveManager.update();
        for (Enemy enemy : activeMonsters) {
            enemy.move();
            enemy.draw();
        }

        Iterator<Enemy> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.isDead() && enemy.isDeathAnimationOver()) {
                iterator.remove();
            }
        }

        manaSystem.update(FPS);

        for (Tower tower : board.towers) {
            tower.update();  // Update each tower's state
        }


    }

    public void resetGame() {
        board.reset();
        waveManager.reset();
        manaSystem.reset();
        userInterface.resetButtons();  
        isGameLost = false;
        isFastForwarded = false;
    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
