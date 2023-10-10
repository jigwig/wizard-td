package WizardTD;

import java.util.*;
import processing.core.PImage;
import processing.core.PVector;

public class Enemy {
    private App app;
    private PImage image;
    private PVector position;
    private float speed;
    private int health;
    private int armor;
    private int maxHealth;
    private float manaGainedOnKill;
    private List<PVector> path;
    private int pathIndex;
    private boolean isDead;
    private int deathFrame;
    private int deathFrameCount;

    public Enemy(App app, PImage image, String type, float spawnX, float spawnY, int health, float speed, int armor, float manaGainedOnKill, List<PVector> path) {
        this.app = app;
        this.image = image;
        this.position = new PVector(spawnX, spawnY);
        this.speed = speed;
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.manaGainedOnKill = manaGainedOnKill;
        this.path = path;
        this.pathIndex = 1;
        
        
    }

    public void move() {
        if (pathIndex < path.size()) {
            PVector target = path.get(pathIndex);
            float targetX = (target.x + 0.5f) * App.CELLSIZE;
            float targetY = (target.y + 0.5f) * App.CELLSIZE + Board.offsetY;
            PVector direction = PVector.sub(new PVector(targetX, targetY), position);
            direction.normalize();
            direction.mult(speed);
            position.add(direction);
    
            if (PVector.dist(position, new PVector(targetX, targetY)) < speed) {
                pathIndex++;
            }
        } else {
            // The enemy has reached the wizard's house.
            // Reset position to the start of the path.
            PVector spawnPoint = path.get(0);
            float spawnX = (spawnPoint.x + 0.5f) * App.CELLSIZE;
            float spawnY = (spawnPoint.y + 0.5f) * App.CELLSIZE + Board.offsetY;
            this.position = new PVector(spawnX, spawnY);
            this.pathIndex = 1;
    
            // Deduct mana based on enemy's health.
            app.manaSystem.deductMana(this.health);
            if (app.manaSystem.getCurrentMana() <= 0) {
                app.isGameLost = true;
            }
        }
    }
    
    

    public void takeDamage(int damage) {
        this.health -= (damage * armor);
        if(this.health <= 0) {
            this.health = 0;
            die();
        }
    }

    private void die() {
        // Handle enemy death logic here.
        // For example, award the player with some mana.
    }

    public void draw() {
        app.image(image, position.x, position.y);
        drawHealthBar();
    }

    private void drawHealthBar() {
        app.noStroke();
        float healthBarWidth = 30;
        float healthBarHeight = 2;
        float xOffset = position.x - healthBarWidth / 2;
        float yOffset = position.y - 15;

        app.fill(255, 0, 0);
        app.rect(xOffset, yOffset, healthBarWidth, healthBarHeight);
        app.fill(0, 255, 0);
        app.rect(xOffset, yOffset, (health / (float) maxHealth) * healthBarWidth, healthBarHeight);
    }

    public PVector getPosition() {
        return position;
    }

}
