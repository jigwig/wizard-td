package WizardTD;

import processing.core.*;
import java.util.*;

public class Tower {
    private App app;
    private PVector position;
    private float range;
    private float firingSpeed;
    private float damage;
    private List<Fireball> fireballs;
    private float timeSinceLastFire;
    private int cost;
    private int rangeLevel;
    private int speedLevel;
    private int damageLevel;

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
        for (String upgrade : new ArrayList<>(selectedUpgrades)) { // Create a copy
            boolean success = upgrade(upgrade, playerMana);
            if(success) {
                selectedUpgrades.remove(upgrade); // Remove only if upgrade was successful
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

    private boolean isInRange(Enemy enemy) {
        float distance = PVector.dist(position, enemy.getPosition());
        return distance <= range;
    }

    private void fireAtEnemy(Enemy enemy) {
        Fireball fireball = new Fireball(app, position.x, position.y, enemy, damage);
        fireballs.add(fireball);
        timeSinceLastFire = 0;
    }

    public void draw() {
        app.imageMode(app.CORNER);
        app.image(app.towerImg, position.x - App.CELLSIZE / 2, position.y - App.CELLSIZE / 2);

        // Highlight the tower's range when hovered over the tower
        if (isHovered(app.mouseX, app.mouseY)) {
            app.stroke(255, 255, 0);  // Yellow
            app.noFill();
            app.ellipse(position.x, position.y, range * 2, range * 2);
        }

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
