package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

public class Tower {
    private App app;
    private int x, y;
    private PImage image;

    public Tower(App app, int x, int y, PImage image) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void draw() {
        app.image(image, x, y);
    }

}
