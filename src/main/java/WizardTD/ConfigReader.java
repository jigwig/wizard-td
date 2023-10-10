package WizardTD;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private PApplet app;
    private JSONObject configData;

    // Attributes to store config data
    private String layout;
    private List<WaveConfig> waves;
    private float initialTowerRange;
    private float initialTowerFiringSpeed;
    private float initialTowerDamage;
    private float initialMana;
    private float initialManaCap;
    private float initialManaGainedPerSecond;
    private float towerCost;
    private float manaPoolSpellInitialCost;
    private float manaPoolSpellCostIncreasePerUse;
    private float manaPoolSpellCapMultiplier;
    private float manaPoolSpellManaGainedMultiplier;

    public ConfigReader(PApplet app, String configPath) {
        this.app = app;
        loadConfig(configPath);
    }

    private void loadConfig(String configPath) {
        configData = app.loadJSONObject(configPath);
        layout = configData.getString("layout");
        
        // Parse waves
        waves = new ArrayList<>();
        JSONArray wavesJson = configData.getJSONArray("waves");
        for (int i = 0; i < wavesJson.size(); i++) {
            JSONObject waveJson = wavesJson.getJSONObject(i);
            WaveConfig waveConfig = new WaveConfig(
                waveJson.getFloat("duration"),
                waveJson.getFloat("pre_wave_pause"),
                waveJson.getJSONArray("monsters")
            );
            waves.add(waveConfig);
        }

        // Parse other attributes
        initialTowerRange = configData.getFloat("initial_tower_range");
        initialTowerFiringSpeed = configData.getFloat("initial_tower_firing_speed");
        initialTowerDamage = configData.getFloat("initial_tower_damage");
        initialMana = configData.getFloat("initial_mana");
        initialManaCap = configData.getFloat("initial_mana_cap");
        initialManaGainedPerSecond = configData.getFloat("initial_mana_gained_per_second");
        towerCost = configData.getFloat("tower_cost");
        manaPoolSpellInitialCost = configData.getFloat("mana_pool_spell_initial_cost");
        manaPoolSpellCostIncreasePerUse = configData.getFloat("mana_pool_spell_cost_increase_per_use");
        manaPoolSpellCapMultiplier = configData.getFloat("mana_pool_spell_cap_multiplier");
        manaPoolSpellManaGainedMultiplier = configData.getFloat("mana_pool_spell_mana_gained_multiplier");
    }

    // Getter methods for all attributes
    public String getLayout() {
        return layout;
    }

    public List<WaveConfig> getWaves() {
        return waves;
    }

    public float getInitialTowerRange() {
        return initialTowerRange;
    }

    public float getInitialTowerFiringSpeed() {
        return initialTowerFiringSpeed;
    }

    public float getInitialTowerDamage() {
        return initialTowerDamage;
    }

    public float getInitialMana() {
        return initialMana;
    }

    public float getInitialManaCap() {
        return initialManaCap;
    }

    public float getInitialManaGainedPerSecond() {
        return initialManaGainedPerSecond;
    }

    public float getTowerCost() {
        return towerCost;
    }

    public float getManaPoolSpellInitialCost() {
        return manaPoolSpellInitialCost;
    }

    public float getManaPoolSpellCostIncreasePerUse() {
        return manaPoolSpellCostIncreasePerUse;
    }

    public float getManaPoolSpellCapMultiplier() {
        return manaPoolSpellCapMultiplier;
    }

    public float getManaPoolSpellManaGainedMultiplier() {
        return manaPoolSpellManaGainedMultiplier;
    }
}