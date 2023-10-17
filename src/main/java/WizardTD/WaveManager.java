package WizardTD;

import java.util.*;
import processing.core.*;

public class WaveManager {
    private App app;
    private List<WaveConfig> waves;
    private int currentWaveIndex = 0;
    private float timeSinceLastSpawn = 0;
    private Queue<MonsterConfig> monstersToSpawn = new LinkedList<>();
    private int totalMonstersInCurrentWave = 0;
    private float preWavePauseTimer = 0;
    private boolean isPreWavePause = true;
    private Pathfinder pathfinder;
    private float durationTimer = 0;    


    public WaveManager(App app, List<WaveConfig> waves) {
        this.app = app;
        this.waves = waves;
        this.pathfinder = new Pathfinder(app.board); 
        loadMonstersForCurrentWave();
    }

    private void loadMonstersForCurrentWave() {
        if (currentWaveIndex < waves.size()) {
            WaveConfig currentWave = waves.get(currentWaveIndex);
            for (MonsterConfig monsterConfig : currentWave.getMonsters()) {
                for (int i = 0; i < monsterConfig.getQuantity(); i++) {
                    monstersToSpawn.add(monsterConfig);
                }
            }
            totalMonstersInCurrentWave = monstersToSpawn.size();
        }
    }

    public void update() {
        if (isPreWavePause) {
            preWavePauseTimer += 1f / App.FPS; // Increment prewave timer
            WaveConfig currentWave = waves.get(currentWaveIndex);
            if (preWavePauseTimer >= currentWave.getPreWavePause()) {
                isPreWavePause = false;
                preWavePauseTimer = 0;
                timeSinceLastSpawn = Float.MAX_VALUE;
            }
        } else {
            durationTimer += 1f / App.FPS; // Increment the duration timer
            WaveConfig currentWave = waves.get(currentWaveIndex);
            if (monstersToSpawn.isEmpty() && durationTimer >= currentWave.getDuration() && currentWaveIndex < waves.size() - 1) {
                currentWaveIndex++;
                isPreWavePause = true; // Reset for the next wave
                loadMonstersForCurrentWave();
                durationTimer = 0;
            } else {
                timeSinceLastSpawn += 1f / App.FPS;
                if (timeSinceLastSpawn >= currentWave.getDuration() / totalMonstersInCurrentWave && !monstersToSpawn.isEmpty()) {
                    spawnMonster(monstersToSpawn.poll());
                    timeSinceLastSpawn = 0;
                }
            }
        }
    }


    private void spawnMonster(MonsterConfig monsterConfig) {
    List<PVector> path = pathfinder.getRandomPath();
    if (path != null && !path.isEmpty()) {
        PVector spawnPoint = path.get(0);
        float spawnX = (spawnPoint.x + 0.5f) * App.CELLSIZE;
        float spawnY = (spawnPoint.y + 0.5f) * App.CELLSIZE + Board.offsetY;
        PImage enemyImage;
        switch (monsterConfig.getType()) {
            case "gremlin":
                enemyImage = app.gremlinImg;
                break;
            case "beetle":
                enemyImage = app.beetleImg;
                break;
            case "worm":
                enemyImage = app.wormImg;
                break;
            default:
                throw new IllegalArgumentException("Invalid monster type: " + monsterConfig.getType());
        }

        if (enemyImage == null) {
            System.out.println("Enemy image is null for " + monsterConfig.getType());  // Debug line
            return;  // Skip the rest of the method
        }
        Enemy enemy = new Enemy(app, enemyImage, monsterConfig.getType(), spawnX, spawnY, (int) monsterConfig.getHp(), monsterConfig.getSpeed(), (float) monsterConfig.getArmour(), (float) monsterConfig.getManaGainedOnKill(), path);
        app.activeMonsters.add(enemy);
        }
    }

    public boolean allWavesCompleted() {
        return currentWaveIndex >= waves.size() - 1 && monstersToSpawn.isEmpty() && !isPreWavePause;
    }



    public void draw() {

        if (currentWaveIndex >= waves.size() - 1 && !isPreWavePause) {
            return;
        }

        float xPosition = 10;
        float yPosition = 30;

        app.textSize(24);
        app.fill(0);

        float time;
        WaveConfig currentWave = waves.get(currentWaveIndex);

        if (isPreWavePause) {
            time = currentWave.getPreWavePause();
        } else {
            time = waves.get(currentWaveIndex + 1).getPreWavePause() + currentWave.getDuration();
        }

        
        float timeLeft = time - (preWavePauseTimer + durationTimer);

        // The text will only show when there are more waves or monsters left to spawn.
        if (currentWaveIndex < waves.size() - 1 || !monstersToSpawn.isEmpty()) {

            String textToDisplay = "Wave " + (currentWaveIndex + (isPreWavePause? 1 : 2)) + " starts: " + ((int) Math.ceil(timeLeft -1));
            app.text(textToDisplay, xPosition, yPosition);
        }
    }

    public void reset() {
        currentWaveIndex = 0;
        timeSinceLastSpawn = 0;
        monstersToSpawn.clear();
        totalMonstersInCurrentWave = 0;
        preWavePauseTimer = 0;
        isPreWavePause = true;
        durationTimer = 0;
        app.activeMonsters.clear();
        loadMonstersForCurrentWave();


    }
}
