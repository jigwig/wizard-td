package WizardTD;

import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaveConfig {
    private float duration;
    private float preWavePause;
    private List<MonsterConfig> monsters;

    public WaveConfig(float duration, float preWavePause, JSONArray monstersJson) {
        this.duration = duration;
        this.preWavePause = preWavePause;

        monsters = new ArrayList<>();
        for (int j = 0; j < monstersJson.size(); j++) {
            JSONObject monsterJson = monstersJson.getJSONObject(j);
            MonsterConfig monsterConfig = new MonsterConfig(
                monsterJson.getString("type"),
                monsterJson.getFloat("hp"),
                monsterJson.getFloat("speed"),
                monsterJson.getFloat("armour"),
                monsterJson.getFloat("mana_gained_on_kill"),
                monsterJson.getInt("quantity")
            );
            monsters.add(monsterConfig);
        }
    }

    // Getter methods for all attributes
    public float getDuration() {
        return duration;
    }

    public float getPreWavePause() {
        return preWavePause;
    }

    public List<MonsterConfig> getMonsters() {
        return monsters;
    }
}
