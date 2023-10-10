package WizardTD;

import java.util.*;
import processing.core.*;


public class ManaSystem {
    private App app;
    private float currentMana;
    private float manaCap;
    private float manaGainedPerSecond;

    public ManaSystem(App app, float initialMana, float initialManaCap, float initialManaGainedPerSecond) {
        this.app = app;
        this.currentMana = initialMana;
        this.manaCap = initialManaCap;
        this.manaGainedPerSecond = initialManaGainedPerSecond;
    }

    public void update(float FPS) {
        currentMana += manaGainedPerSecond / FPS;
        currentMana = Math.min(currentMana, manaCap);
    }

    public boolean spendMana(float amount) {
        if (currentMana >= amount) {
            currentMana -= amount;
            return true;
        }
        return false;
    }

    public void increaseManaCap(float multiplier) {
        manaCap *= multiplier;
    }

    public void increaseManaGained(float multiplier) {
        manaGainedPerSecond *= multiplier;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public float getManaCap() {
        return manaCap;
    }

    public void deductMana(float amount) {
        currentMana -= amount;
        if (currentMana < 0) {
            currentMana = 0;
        }
    }

    public void draw(int width, int topBarHeight) {
        float manaBarHeight = topBarHeight * 0.5f; 
        float verticalOffset = (topBarHeight - manaBarHeight) / 2;
    
        // Draw black borders
        app.strokeWeight(2); // Set stroke weight for thicker border
        app.stroke(0); // Black border
        
        // Draw the white mana bar
        app.fill(255, 255, 255);
        float manaBarWidth = (float) width * 0.45f; // 45% of screen width
        float manaBarStartX = width - manaBarWidth - 35 ; // Start from the right side
        app.rect(manaBarStartX, verticalOffset, manaBarWidth, manaBarHeight);
        
        // Draw the actual mana in aqua
        app.fill(0, 255, 255);
        float manaFilledWidth = (currentMana / manaCap) * manaBarWidth;
        app.rect(manaBarStartX, verticalOffset, manaFilledWidth, manaBarHeight);
        
        // Draw text to show current mana and mana cap
        app.fill(0); // Black text
        app.textSize(18);
        String manaText = String.format("%d / %d", (int) currentMana, (int) manaCap);
        float textX = manaBarStartX + (manaBarWidth / 2) - 73;
        float textY = verticalOffset + manaBarHeight / 2 + 7;
        app.text(manaText, textX, textY);
        
        // Draw "MANA:" text to the left of the bar
        app.textSize(20);
        app.text("MANA:", manaBarStartX - 70, textY);
        
 
    }
    
    public void reset() {
        currentMana = app.configReader.getInitialMana();
        manaCap = app.configReader.getInitialManaCap();
    }

}
