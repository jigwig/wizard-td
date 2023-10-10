package WizardTD;

import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class UserInterface {

    private App app;
    private List<Buttons> elements;

    public UserInterface(App app) {
        this.app = app;
        elements = new ArrayList<>();

        // Initialize all UI buttons, bars, etc. here
        elements.add(new Buttons(app, 648, 50, "FF", "2x Speed", 'f', 0));
        elements.add(new Buttons(app, 648, 100, "P", "PAUSE", 'p', 0));
        elements.add(new Buttons(app, 648, 150, "T", "Build Tower", 't', 100));
        elements.add(new Buttons(app, 648, 200, "U1", "Upgrade range", '1', 20)); // Upgrade costs may vary
        elements.add(new Buttons(app, 648, 250, "U2", "Upgrade speed", '2', 20));
        elements.add(new Buttons(app, 648, 300, "U3", "Upgrade damage", '3', 20));
        elements.add(new Buttons(app, 648, 350, "M", "Mana Pool", 'm', 0));
    }

    public void draw(int mouseX, int mouseY) {

        app.noStroke();
        app.fill(132, 115, 74, 255);  // Fill with the background color
        app.rect(App.WIDTH - App.SIDEBAR, 0, App.SIDEBAR, App.HEIGHT);  // Rectangle on the right

        // Drawing a rectangle on top
        app.rect(0, 0, App.WIDTH, App.TOPBAR);  // Rectangle on the top

        app.stroke(0);

        for (Buttons button : elements) {
            button.update(mouseX, mouseY);
        }

        for (Buttons button : elements) {
            button.draw();
        }


    }

    public void onClick(int mouseX, int mouseY) {
        for (Buttons button : elements) {
            button.onClick(mouseX, mouseY);
            if (button.matchesHotkey('t') && button.isActive()) {
                // Check if the user has enough mana to place a tower
                if (app.manaSystem.spendMana(app.configReader.getTowerCost())) {
                    // Place the tower
                    app.board.placeTower(mouseX, mouseY, app.towerImg);
                } 
            }
        }
    }
    

    public void handleHotkey(char key) {
        for (Buttons button : elements) {
            button.onKey(key);
        }
    }
}
