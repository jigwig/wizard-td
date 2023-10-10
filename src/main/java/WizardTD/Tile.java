package WizardTD;

import processing.core.PImage;

public class Tile {



    private App app;
    private Board board;
    private int x, y;
    private PImage image;
    private TileType type;

    public Tile(App app, Board board, int x, int y, char tileChar) {
        this.app = app;
        this.board = board;
        this.x = x;
        this.y = y;

        switch (tileChar) {
            case ' ':
                this.image = app.grassImg;
                this.type = TileType.GRASS;
                break;
            case 'S':
                this.image = app.shrubImg;
                this.type = TileType.SHRUB;
                break;
            case 'X':
                this.image = determinePathImage();
                this.type = TileType.PATH;
                break;
            case 'W':
                this.image = app.wizardHouseImg;
                this.type = TileType.WIZARD_HOUSE;
                break;
        }
    }

    public TileType getTileType() {
        return type;
    }

    public void draw(int offsetY) {        
        // Always draw grass as the base layer
        app.imageMode(App.CENTER);
        
        app.image(app.grassImg, (x + 0.5f) * App.CELLSIZE, (y + 0.5f) * App.CELLSIZE + offsetY);
        
        // Now draw the specific tile type on top
        app.image(image, (x + 0.5f) * App.CELLSIZE, (y + 0.5f) * App.CELLSIZE + offsetY);
    }
    
    

    private PImage determinePathImage() {
        boolean leftIsPath = (x > 0 && board.getBoardLayout()[y][x - 1] == 'X');
        boolean rightIsPath = (x < App.BOARD_WIDTH - 1 && board.getBoardLayout()[y][x + 1] == 'X');
        boolean upIsPath = (y > 0 && board.getBoardLayout()[y - 1][x] == 'X');
        boolean downIsPath = (y < App.BOARD_WIDTH - 1 && board.getBoardLayout()[y + 1][x] == 'X');
        
        int pathNeighbors = 0;
        if (leftIsPath) pathNeighbors++;
        if (rightIsPath) pathNeighbors++;
        if (upIsPath) pathNeighbors++;
        if (downIsPath) pathNeighbors++;

        if (pathNeighbors == 1) {
            if (leftIsPath || rightIsPath) {
                return app.pathImg0; // Horizontal
            } else {
                return app.rotateImageByDegrees(app.pathImg0, 90); // Vertical (rotated)
            }
        } else if (pathNeighbors == 2) {
            if (leftIsPath && rightIsPath) {
                return app.pathImg0; // Horizontal
            } else if (upIsPath && downIsPath) {
                return app.rotateImageByDegrees(app.pathImg0, 90); // Vertical
            } else if (leftIsPath && downIsPath) {
                return app.pathImg1; // Right-angle (south-west, default)
            } else if (rightIsPath && downIsPath) {
                return app.rotateImageByDegrees(app.pathImg1, -90); // Right-angle (south-east)
            } else if (leftIsPath && upIsPath) {
                return app.rotateImageByDegrees(app.pathImg1, 90); // Right-angle (north-west)
            } else {
                return app.rotateImageByDegrees(app.pathImg1, 180); // Right-angle (north-east)
            }
        } else if (pathNeighbors == 3) {
            if (!leftIsPath) {
                return app.rotateImageByDegrees(app.pathImg2, -90); // T (no left)
            } else if (!rightIsPath) {
                return app.rotateImageByDegrees(app.pathImg2, 90); // T (no right)
            } else if (!upIsPath) {
                return app.pathImg2; // T (no up, default)
            } else {
                return app.rotateImageByDegrees(app.pathImg2, 180); // T (no down)
            }
        } else {
            return app.pathImg3; // + shaped path
        }
    }
}
