package WizardTD;

import java.util.*;
import processing.core.*;


public class ManaSystem {
    private App app;
    private float currentMana;
    private float manaCap;
    private float manaGainedPerSecond;
    private float manaPoolSpellCost;
    private float manaPoolSpellCostIncreasePerUse;
    private float manaPoolSpellCapMultiplier;
    private float manaPoolSpellManaGainedMultiplier;
    private float originalManaGainedPerSecond;
    public static float originalManaGainedOnKill;  // Static variable for original mana gained on kill
    public static float globalManaGainedOnKill;  // Static variable for global access

    public ManaSystem(App app, float initialMana, float initialManaCap, float initialManaGainedPerSecond) {
        this.app = app;
        this.currentMana = initialMana;
        this.manaCap = initialManaCap;
        this.manaGainedPerSecond = initialManaGainedPerSecond;
        this.originalManaGainedPerSecond = initialManaGainedPerSecond;  // Store the original value
        this.manaPoolSpellCost = app.configReader.getManaPoolSpellInitialCost();
        this.manaPoolSpellCostIncreasePerUse = app.configReader.getManaPoolSpellCostIncreasePerUse();
        this.manaPoolSpellCapMultiplier = app.configReader.getManaPoolSpellCapMultiplier();
        this.manaPoolSpellManaGainedMultiplier = app.configReader.getManaPoolSpellManaGainedMultiplier();
        originalManaGainedOnKill = MonsterConfig.manaGainedOnKill;  // Store the original value
        globalManaGainedOnKill = originalManaGainedOnKill;  // Initialize global value
    }

    public void update(float FPS) {
        currentMana += manaGainedPerSecond / FPS;
        System.out.println("managained: " + manaGainedPerSecond + "current mana: " + currentMana);

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

    public boolean castManaPoolSpell() {
        if (currentMana >= manaPoolSpellCost) {
            currentMana -= manaPoolSpellCost;
            manaCap *= manaPoolSpellCapMultiplier;

            // Add the increment to the mana gained per second
            manaGainedPerSecond += (manaPoolSpellManaGainedMultiplier - 1) * originalManaGainedPerSecond;

            // Add the increment to the global mana gained on kill
            MonsterConfig.manaGainedOnKill += (manaPoolSpellManaGainedMultiplier - 1) * originalManaGainedOnKill;

            // Increase the cost for the next spell
            manaPoolSpellCost += manaPoolSpellCostIncreasePerUse;

            return true;
        }
        return false;
    }
    

    public float getCurrentMana() {
        return currentMana;
    }
    

    public float getManaCap() {
        return manaCap;
    }

    public float getManaPoolSpellCost() {
        return this.manaPoolSpellCost;
    }

    public void deductMana(float amount) {
        currentMana -= amount;
        if (currentMana < 0) {
            currentMana = 0;
        }
    }

    public void addMana(float amount) {
        currentMana += amount;
        if (currentMana > manaCap) {
            currentMana = manaCap;
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
        manaGainedPerSecond = app.configReader.getInitialManaGainedPerSecond();
        manaPoolSpellCost = app.configReader.getManaPoolSpellInitialCost();
        manaPoolSpellCostIncreasePerUse = app.configReader.getManaPoolSpellCostIncreasePerUse();
        manaPoolSpellCapMultiplier = app.configReader.getManaPoolSpellCapMultiplier();
        manaPoolSpellManaGainedMultiplier = app.configReader.getManaPoolSpellManaGainedMultiplier();
        
    }

}
