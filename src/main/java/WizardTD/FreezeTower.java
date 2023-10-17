package WizardTD;

import java.lang.Math;

public class FreezeTower extends Tower {
    private float freezeDuration;
    private float timeSinceLastFreeze; // New attribute
    private static final float FREEZE_INTERVAL = 5.0f; // Interval between freezes in seconds

    public FreezeTower(App app, float x, float y) {
        super(app, x, y);
        this.freezeDuration = 2.0f;  // Freeze duration in seconds
        this.timeSinceLastFreeze = 0; // Initialize it to zero
    }

    @Override
    public void update() {
        timeSinceLastFreeze += 1f / App.FPS; // Update the timer

        if (timeSinceLastFreeze >= FREEZE_INTERVAL) { // Check if enough time has passed
            for (Enemy enemy : app.activeMonsters) {
                if (isInRange(enemy)) {
                    freezeEnemy(enemy);
                }
            }
            timeSinceLastFreeze = 0; // Reset the timer
        }
    }

    private void freezeEnemy(Enemy enemy) {
        enemy.freeze(freezeDuration);
    }

    @Override
    public boolean upgrade(String upgradeType, int playerMana) {
        // Disable upgrades by returning false
        return false;
    }

    @Override
    public void draw() {
        app.imageMode(app.CORNER);
        app.image(app.freezeTowerImg, position.x - App.CELLSIZE / 2, position.y - App.CELLSIZE / 2);
    
        // Draw the charging circle
        float radius = 20;
        float percentage = timeSinceLastFreeze / FREEZE_INTERVAL;
        float angle = 360 * percentage;
        app.stroke(0, 255, 0);
        app.fill(0, 255, 0, 128);
        app.arc(position.x, position.y, radius, radius, (float) Math.toRadians(-90), (float) Math.toRadians(-90 + angle));
    
        // Draw the shockwave
        if (timeSinceLastFreeze < 0.5) {  // Only show the shockwave for 0.5 second after the freeze
            float shockwaveScale = timeSinceLastFreeze / 0.5f;  // Scale based on time elapsed since last freeze
            float shockwaveRadius = shockwaveScale * range;  // Scale the radius
            
            // First, more transparent shockwave
            app.stroke(65, 220, 245, 50);
            app.noFill();
            app.ellipse(position.x, position.y, shockwaveRadius * 2, shockwaveRadius * 2);
            
            // Second, slightly less transparent and smaller shockwave
            app.stroke(65, 220, 245, 100);
            app.noFill();
            app.ellipse(position.x, position.y, (shockwaveRadius - 10) * 2, (shockwaveRadius - 10) * 2);
        }
    
        // Draw the tower's range when hovered
        if (isHovered(app.mouseX, app.mouseY)) {
            app.stroke(65, 220, 245);
            app.noFill();
            app.ellipse(position.x, position.y, range * 2, range * 2);
        }
    }
    
    
}
