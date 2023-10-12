package WizardTD;

import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

public class UserInterface {

    private App app;
    private List<Buttons> elements;
    private List<String> selectedUpgrades = new ArrayList<>();


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
        }
    
        // Clear selected upgrades
        selectedUpgrades.clear();
    
        // Check for upgrade-related actions
        if (getButtonByHotkey('1') != null && getButtonByHotkey('1').isActive()) selectedUpgrades.add("range");
        if (getButtonByHotkey('2') != null && getButtonByHotkey('2').isActive()) selectedUpgrades.add("speed");
        if (getButtonByHotkey('3') != null && getButtonByHotkey('3').isActive()) selectedUpgrades.add("damage");
    
        // Check for tower-related actions
        Tower selectedTower = app.board.getSelectedTower(mouseX, mouseY);
        if (selectedTower != null) {
            selectedTower.applyUpgrades(selectedUpgrades, (int)app.manaSystem.getCurrentMana());
        }
    
        Buttons towerButton = getButtonByHotkey('t');
        if (towerButton != null && towerButton.isActive()) {
            if (app.configReader.getTowerCost() <= app.manaSystem.getCurrentMana()) {
                boolean placed = app.board.placeTower(mouseX, mouseY, selectedUpgrades);
                if (placed) {
                    app.manaSystem.spendMana(app.configReader.getTowerCost());
                }
            }
        }

        Buttons manaPoolButton = getButtonByHotkey('m');
        if (manaPoolButton != null && manaPoolButton.isActive()) {
            boolean casted = app.manaSystem.castManaPoolSpell();
            if (casted) {
                manaPoolButton.isActive = false; // Deactivate button after spell is cast
            }
        }
    }
    
    
    private Buttons getButtonByHotkey(char hotkey) {
        for (Buttons button : elements) {
            if (button.matchesHotkey(hotkey)) {
                return button;
            }
        }
        return null;
    }
    
    
    
    public List<String> getSelectedUpgrades() {
        return selectedUpgrades;
    }

    public void handleHotkey(char key) {
        for (Buttons button : elements) {
            button.onKey(key);
        }
    
        // Special case for 'M' key
        if (key == 'm' || key == 'M') {
            Buttons manaPoolButton = getButtonByHotkey('m');
            if (manaPoolButton != null) {
                manaPoolButton.toggleActiveState();  // Call the new toggle method
            }
        }
    }

    public void resetButtons() {
        for (Buttons button : elements) {
            button.isActive = false;
        }
    }
}
