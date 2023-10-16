package WizardTD;

import processing.core.*;
import java.util.*;

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
        float radius = 20; // Radius of the charging circle
        float percentage = timeSinceLastFreeze / FREEZE_INTERVAL; // Calculate the percentage
        float angle = 360 * percentage; // Convert the percentage to an angle in degrees
        app.stroke(0, 255, 0); // Green outline
        app.fill(0, 255, 0, 128); // Green, semi-transparent fill
        app.arc(position.x, position.y, radius, radius, app.radians(-90), app.radians(-90 + angle)); // Draw the arc
    }

    
}
