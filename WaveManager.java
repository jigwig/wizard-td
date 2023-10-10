package WizardTD;

import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private App app;
    private List<Enemy> enemies;
    private PImage enemyImage;
    private List<Wave> waves;
    private int currentWaveIndex;
    private int spawnInterval;
    private int lastSpawnTime;
    private int monstersSpawned;

    public WaveManager(App app, String configFilePath) {
        this.app = app;
        this.enemies = new ArrayList<>();
        this.waves = new ArrayList<>();
        this.currentWaveIndex = 0;
        this.monstersSpawned = 0;

        JSONObject config = app.loadJSONObject(configFilePath);
        JSONArray wavesJson = config.getJSONArray("waves");
        for (int i = 0; i < wavesJson.size(); i++) {
            JSONObject waveJson = wavesJson.getJSONObject(i);
            Wave wave = new Wave(waveJson.getInt("duration"), waveJson.getInt("pre_wave_pause"));
            JSONArray monstersJson = waveJson.getJSONArray("monsters");
            for (int j = 0; j < monstersJson.size(); j++) {
                JSONObject monsterJson = monstersJson.getJSONObject(j);
                MonsterConfig monsterConfig = new MonsterConfig(
                    monsterJson.getString("type"),
                    monsterJson.getInt("hp"),
                    monsterJson.getFloat("speed"),
                    monsterJson.getFloat("armour"),
                    monsterJson.getFloat("mana_gained_on_kill"),
                    monsterJson.getInt("quantity")
                );
                wave.addMonsterConfig(monsterConfig);
            }
            waves.add(wave);
        }
    }

    public void update(int deltaTime) {
        Wave currentWave = waves.get(currentWaveIndex);
        currentWave.update(deltaTime);
        if(currentWave.shouldSpawn(monstersSpawned)) {
            spawnEnemy();
            monstersSpawned++;
        }

        for(Enemy enemy : enemies) {
            enemy.move();
        }
    }

    public void draw() {
        for(Enemy enemy : enemies) {
            enemy.draw();
        }
    }

    private void spawnEnemy() {
        Wave currentWave = waves.get(currentWaveIndex);
        MonsterConfig monsterConfig = currentWave.getNextMonster();
        
        PImage specificEnemyImage = app.loadImage(monsterConfig.getType() + ".png");  // Load image based on the type
    
        Enemy enemy = new Enemy(app, specificEnemyImage, monsterConfig.getType(), 0, app.height/2, (int)monsterConfig.getHp(), monsterConfig.getSpeed(), (int)monsterConfig.getArmour(), monsterConfig.getManaGainedOnKill());
        enemies.add(enemy);
    }
    
}       
