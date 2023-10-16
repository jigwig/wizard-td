package WizardTD;

import java.util.*;

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
        elements.add(new Buttons(app, 648, 400, "Z", "Build Freeze Tower", 'z', 120));

    }

    public void draw(int mouseX, int mouseY) {
        // Fill the sidebar with a background color
        app.noStroke();
        app.fill(132, 115, 74, 255);  // Fill with the background color
        app.rect(App.WIDTH - App.SIDEBAR, 0, App.SIDEBAR, App.HEIGHT);  // Rectangle on the right
    
        // Drawing a rectangle on top
        app.rect(0, 0, App.WIDTH, App.TOPBAR);  // Rectangle on the top
    
        app.stroke(0);
    
        // Update and draw each button
        for (Buttons button : elements) {
            button.update(mouseX, mouseY);
        }
    
        for (Buttons button : elements) {
            button.draw();
        }
    
        // Check if hovering over a tower
        Tower selectedTower = app.board.getSelectedTower(mouseX, mouseY);
        if (selectedTower != null) {
            // Check which upgrade buttons are active
            List<String> activeUpgrades = new ArrayList<>();
            if (getButtonByHotkey('1').isActive()) activeUpgrades.add("range");
            if (getButtonByHotkey('2').isActive()) activeUpgrades.add("speed");
            if (getButtonByHotkey('3').isActive()) activeUpgrades.add("damage");
    
            // If any upgrade buttons are active, draw the upgrade cost summary
            if (!activeUpgrades.isEmpty()) {
                drawUpgradeCostSummary(activeUpgrades, selectedTower);
            }
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
                boolean placed = app.board.placeTower(mouseX, mouseY, selectedUpgrades, false);
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

        Buttons freezeTowerButton = getButtonByHotkey('z');
        if (freezeTowerButton != null && freezeTowerButton.isActive()) {
            if (120 <= app.manaSystem.getCurrentMana()) {  // Assume the freeze tower costs 120 mana
                boolean placed = app.board.placeTower(mouseX, mouseY, selectedUpgrades, true);
                if (placed) {
                    app.manaSystem.spendMana(120);
                }
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
    
    public void drawUpgradeCostSummary(List<String> selectedUpgrades, Tower selectedTower) {
        int sidebarX = App.WIDTH - App.SIDEBAR + 12;  // Move a bit to the left to make the box wider
        int rectWidth = App.SIDEBAR - 36;  // Slightly wider
        
        // Calculate the dynamic height of the middle box based on the number of selected upgrades
        int middleBoxHeight = 18 * selectedUpgrades.size();
        
        // Calculate the Y-coordinate for the bottom of the middle box
        int middleBoxBottomY = App.HEIGHT - 60 - middleBoxHeight;  // Slightly lower
        
        // Draw the top box
        app.stroke(0);
        app.fill(255);
        app.rect(sidebarX, middleBoxBottomY - 24, rectWidth, 24);  // Slightly shorter
        app.fill(0);
        app.text("Upgrade Cost", sidebarX + 6, middleBoxBottomY - 8);
        
        // Draw the middle box
        app.stroke(0);
        app.fill(255);
        app.rect(sidebarX, middleBoxBottomY, rectWidth, middleBoxHeight);
        
        // Populate the middle box with the costs of each upgrade
        app.fill(0);
        int yOffset = 18;
        for (String upgrade : selectedUpgrades) {
            int cost = selectedTower.getNextUpgradeCost(upgrade);
            float costWidth = app.textWidth(String.valueOf(cost));
            
            app.text(upgrade + ":", sidebarX + 6, middleBoxBottomY + yOffset - 6);  // Closer to the border
            app.text(cost, sidebarX + rectWidth - costWidth - 6, middleBoxBottomY + yOffset - 6);  // Dynamic alignment
            yOffset += 18;
        }
        
        // Draw the bottom box
        int totalCost = selectedUpgrades.stream().mapToInt(selectedTower::getNextUpgradeCost).sum();
        float totalCostWidth = app.textWidth(String.valueOf(totalCost));
        
        app.stroke(0);
        app.fill(255);
        app.rect(sidebarX, middleBoxBottomY + middleBoxHeight, rectWidth, 22);  // Slightly shorter
        app.fill(0);
        app.text("Total:", sidebarX + 6, middleBoxBottomY + middleBoxHeight + 14);
        app.text(totalCost, sidebarX + rectWidth - totalCostWidth - 6, middleBoxBottomY + middleBoxHeight + 14);  // Dynamic alignment
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
