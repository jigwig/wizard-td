package WizardTD;

import processing.core.*;
import java.io.*;
import java.util.*;

public class Board {
    private App app;
    private Tile[][] tiles;
    private char[][] boardLayout;
    private int wizardX;
    private int wizardY;
    private List<Tower> towers = new ArrayList<>();

    public static final int offsetY = App.HEIGHT - (App.BOARD_WIDTH * App.CELLSIZE); // Calculate offset to move board down 

    


    public Board(App app, String layoutFileName) {
        this.app = app;
        loadLayout(layoutFileName);
        initTiles();
        
    }

    private void loadLayout(String layoutFileName) {
        boardLayout = new char[App.BOARD_WIDTH][App.BOARD_WIDTH];
        try {
            Scanner scanner = new Scanner(new File(layoutFileName));
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                while (line.length() < App.BOARD_WIDTH) {
                    line += " "; // Pad with spaces if shorter than BOARD_WIDTH
                }
                boardLayout[row] = line.toCharArray();
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTiles() {
        tiles = new Tile[App.BOARD_WIDTH][App.BOARD_WIDTH];
        for (int i = 0; i < App.BOARD_WIDTH; i++) {
            for (int j = 0; j < App.BOARD_WIDTH; j++) {
                tiles[i][j] = new Tile(app, this, i, j, boardLayout[j][i]);
                if (tiles[i][j].getTileType() == TileType.WIZARD_HOUSE) {
                    wizardX = i;
                    wizardY = j;
                }
            }
        }
    }

    public char[][] getBoardLayout() {
        return boardLayout;
    }

    public void placeTower(int x, int y, PImage image) {
        int cellX = (int) Math.floor((float) x / App.CELLSIZE);
        int cellY = (int) Math.floor((float) (y - offsetY) / App.CELLSIZE);
        
        if (cellX >= 0 && cellX < App.BOARD_WIDTH && cellY >= 0 && cellY < App.BOARD_WIDTH) {
            if (boardLayout[cellY][cellX] == ' ') {
                // Calculate the center of the cell for tower placement
                int centerX = (cellX * App.CELLSIZE) + (App.CELLSIZE / 2);
                int centerY = (cellY * App.CELLSIZE) + (App.CELLSIZE / 2) + offsetY;
                
                towers.add(new Tower(app, centerX, centerY, image));
                
                // Mark the board layout to indicate that a tower has been placed.
                boardLayout[cellY][cellX] = 'T'; 
            }
        }
    }
    
    

    public void draw() {
        for (int i = 0; i < App.BOARD_WIDTH; i++) {
            for (int j = 0; j < App.BOARD_WIDTH; j++) {
                tiles[i][j].draw(offsetY);
            }
        }


        for (Tower tower : towers) {
            tower.draw();
        }

        tiles[wizardX][wizardY].draw(offsetY);
    }
    
    public void reset() {
        towers.clear();
    }
}
