package WizardTD;

import processing.core.*;
import java.util.*;

public class Tower {
    protected App app;
    protected PVector position;
    protected float range;
    protected float firingSpeed;
    protected float damage;
    protected List<Fireball> fireballs;
    protected float timeSinceLastFire;
    protected int cost;
    protected int rangeLevel;
    protected int speedLevel;
    protected int damageLevel;

    public Tower(App app, float x, float y) {
        this.app = app;
        this.position = new PVector(x, y);
        this.cost = (int) app.configReader.getTowerCost();
        this.range = app.configReader.getInitialTowerRange();
        this.firingSpeed = app.configReader.getInitialTowerFiringSpeed();
        this.damage = (float) app.configReader.getInitialTowerDamage();
        this.fireballs = new ArrayList<>();
        this.timeSinceLastFire = 0;
        this.rangeLevel = 0;
        this.speedLevel = 0;
        this.damageLevel = 0;
    }

    public int getNextUpgradeCost(String upgradeType) {
        return 20 + 10 * getUpgradeLevel(upgradeType);
    }

    public boolean upgrade(String upgradeType, int playerMana) {
        int upgradeCost = getNextUpgradeCost(upgradeType);
        if (playerMana >= upgradeCost) {
            if ("range".equals(upgradeType)) {
                range += 32;
                rangeLevel++;
            } else if ("speed".equals(upgradeType)) {
                firingSpeed += 0.5;
                speedLevel++;
            } else if ("damage".equals(upgradeType)) {
                damage += app.configReader.getInitialTowerDamage() / 2;
                damageLevel++;
            }
            // Deduct mana here
            app.manaSystem.spendMana(upgradeCost);
            return true;
        }
        return false;
    }
    
    public void applyUpgrades(List<String> selectedUpgrades, int playerMana) {
        for (String upgrade : new ArrayList<>(selectedUpgrades)) { // Create a copy to avoid ConcurrentModificationException
            boolean success = upgrade(upgrade, playerMana);
            if(success) {
                selectedUpgrades.remove(upgrade); // Remove only if upgrade was successful
                playerMana = (int)app.manaSystem.getCurrentMana(); // Refresh playerMana after a successful upgrade
            }
        }
    }
    
    
    

    private int getUpgradeLevel(String upgradeType) {
        if ("range".equals(upgradeType)) {
            return rangeLevel;
        } else if ("speed".equals(upgradeType)) {
            return speedLevel;
        } else if ("damage".equals(upgradeType)) {
            return damageLevel;
        }
        return -1;
    }

    public void update() {
        timeSinceLastFire += 1f / App.FPS;

        for (Fireball fireball : new ArrayList<>(fireballs)) {
            fireball.move();
            if (fireball.hasReachedTarget()) {
                fireballs.remove(fireball);
                fireball.target.takeDamage(this.damage);
            }
        }

        if (timeSinceLastFire >= (1f / firingSpeed)) {
            for (Enemy enemy : app.activeMonsters) {
                if (isInRange(enemy)) {
                    fireAtEnemy(enemy);
                    break;
                }
            }
        }
    }

    protected boolean isInRange(Enemy enemy) {
        float distance = PVector.dist(position, enemy.getPosition());
        return distance <= range;
    }

    private void fireAtEnemy(Enemy enemy) {
        Fireball fireball = new Fireball(app, position.x, position.y, enemy, damage);
        fireballs.add(fireball);
        timeSinceLastFire = 0;
    }

    public void draw() {
        // Draw the tower image based on upgrade levels
        app.imageMode(app.CORNER);
        if (rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) {
            app.image(app.tower2Img, position.x - App.CELLSIZE / 2, position.y - App.CELLSIZE / 2);
        } else if (rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) {
            app.image(app.tower1Img, position.x - App.CELLSIZE / 2, position.y - App.CELLSIZE / 2);
        } else {
            app.image(app.towerImg, position.x - App.CELLSIZE / 2, position.y - App.CELLSIZE / 2);
        }
    
        // Set a smaller text size for O and X
        app.textSize(10);
    
        // Range upgrade level: Magenta "O"s at the top left
        int displayRangeLevel = rangeLevel - ((rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) ? 1 : 0);
        displayRangeLevel -= ((rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) ? 1 : 0);
        app.fill(255, 0, 255);
        for (int i = 0; i < displayRangeLevel; i++) {
            app.text("O", position.x - 16 + (i * 8), position.y - 16);
        }
    
        // Speed upgrade level: Blue square border in the center
        int displaySpeedLevel = speedLevel - ((rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) ? 1 : 0);
        displaySpeedLevel -= ((rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) ? 1 : 0);
        if (displaySpeedLevel > 0) {
            app.stroke(0, 0, 255);
            app.strokeWeight(displaySpeedLevel);
            app.noFill();
            app.rect(position.x - 10, position.y - 10, 20, 20);
        }
        app.strokeWeight(2);  // Reset stroke weight to default
    
        // Damage upgrade level: Magenta "X"s at the bottom left
        int displayDamageLevel = damageLevel - ((rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) ? 1 : 0);
        displayDamageLevel -= ((rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) ? 1 : 0);
        app.fill(255, 0, 255);
        for (int i = 0; i < displayDamageLevel; i++) {
            app.text("X", position.x - 16 + (i * 8), position.y + 16);
        }
    
        // Reset text size to default (or any other value you usually use)
        app.textSize(12);
    
        // Highlight the tower's range when hovered over the tower
        if (isHovered(app.mouseX, app.mouseY)) {
            app.stroke(255, 255, 0);  // Yellow
            app.noFill();
            app.ellipse(position.x, position.y, range * 2, range * 2);
        }
    
        // Draw fireballs
        for (Fireball fireball : fireballs) {
            fireball.draw();
        }
    }
    
    
    
    

    public boolean isHovered(int mouseX, int mouseY) {
        float distance = PVector.dist(position, new PVector(mouseX, mouseY));
        return distance <= App.CELLSIZE / 2;  // Check if within the tower's size, not range
    }


    public int getCost() {
        return this.cost;
    }
}
