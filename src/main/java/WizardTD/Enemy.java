package WizardTD;

import java.util.*;
import processing.core.*;


public class Enemy {
    private App app;
    private PImage image;
    private PVector position;
    private float speed;
    private float health;
    private float armour;
    private float maxHealth;
    private float manaGainedOnKill;
    private List<PVector> path;
    private int pathIndex;
    private boolean isDead;
    private List<PImage> deathAnimationFrames;
    private int deathAnimationFrameCounter = 0;
    private PVector deathPosition;
    private float freezeTimer = 0;
    private PImage frozenImage;


    public Enemy(App app, PImage image, String type, float spawnX, float spawnY, float health, float speed, float armour, float manaGainedOnKill, List<PVector> path) {
        this.app = app;
        this.image = image;
        this.position = new PVector(spawnX, spawnY);
        this.speed = speed;
        this.health = health;
        this.maxHealth = health;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.path = path;
        this.pathIndex = 1;
        this.deathAnimationFrames = new ArrayList<>();

        if ("gremlin".equals(type)) {
            this.deathAnimationFrames.add(app.gremlinDeathImg1);
            this.deathAnimationFrames.add(app.gremlinDeathImg2);
            this.deathAnimationFrames.add(app.gremlinDeathImg3);
            this.deathAnimationFrames.add(app.gremlinDeathImg4);
            this.deathAnimationFrames.add(app.gremlinDeathImg5);
            this.frozenImage = app.frozenGremlinImg;
        } else if ("beetle".equals(type)) {
            this.deathAnimationFrames.add(app.beetleDeathImg1);
            this.deathAnimationFrames.add(app.beetleDeathImg2);
            this.deathAnimationFrames.add(app.beetleDeathImg3);
            this.deathAnimationFrames.add(app.beetleDeathImg4);
            this.deathAnimationFrames.add(app.beetleDeathImg5);
            this.frozenImage = app.frozenBeetleImg;
        } else if ("worm".equals(type)) {
            this.deathAnimationFrames.add(app.wormDeathImg1);
            this.deathAnimationFrames.add(app.wormDeathImg2);
            this.deathAnimationFrames.add(app.wormDeathImg3);
            this.deathAnimationFrames.add(app.wormDeathImg4);
            this.deathAnimationFrames.add(app.wormDeathImg5);
            this.frozenImage = app.frozenWormImg;
        }
    }

    public void move() {
        updateFreeze();
        if (freezeTimer > 0) {
            return;
        }

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
    
    

    public void takeDamage(float damage) {
        // System.out.println("asd" + armour);
        float actualDamage = (damage * armour); // Damage scaled by armour
        this.health -= actualDamage; // Deduct the actual damage from health
        // System.out.println("Damage Taken: " + actualDamage + " New Health: " + this.health);  // Debug line
    
        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }

    public void freeze(float duration) {
        if (freezeTimer <= 0) {  // Only reset the timer if it's not already running
            freezeTimer = duration;
        }
    }
    

    public void updateFreeze() {
        if (freezeTimer > 0) {
            freezeTimer -= 1f / App.FPS;
        }
        if (freezeTimer < 0) { 
            freezeTimer = 0;
        }
    }

    public void die() {
        deathPosition = position.copy();
        float actualManaGainedOnKill = manaGainedOnKill * app.manaSystem.getManaGainedMultiplier();
        app.manaSystem.addMana(actualManaGainedOnKill);
        isDead = true;
    }

    public boolean isDeathAnimationOver() {
        return isDead && deathAnimationFrameCounter >= deathAnimationFrames.size() * 4;
    }


    public boolean isDead() {
        return isDead;
    }

    public void draw() {
        if (isDead) {
            if (deathAnimationFrameCounter < deathAnimationFrames.size() * 4) {
                PImage frameToShow = deathAnimationFrames.get(deathAnimationFrameCounter / 4);
                app.image(frameToShow, deathPosition.x, deathPosition.y);
                deathAnimationFrameCounter++;
            }
        } else {
            if (freezeTimer > 0) {
                app.image(frozenImage, position.x, position.y);
            } else {
                app.image(image, position.x, position.y);
            }
            drawHealthBar();
        }
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
