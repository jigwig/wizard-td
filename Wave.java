package WizardTD;

import java.util.ArrayList;
import java.util.List;

public class Wave {
    private int duration;
    private int preWavePause;
    private List<MonsterConfig> monsters;
    private int elapsedTime;
    private int nextSpawnTime;

    public Wave(int duration, int preWavePause) {
        this.duration = duration;
        this.preWavePause = preWavePause;
        this.monsters = new ArrayList<>();
        this.elapsedTime = 0;
        this.nextSpawnTime = 0;
    }

    public void addMonsterConfig(MonsterConfig monsterConfig) {
        for (int i = 0; i < monsterConfig.getQuantity(); i++) {
            monsters.add(monsterConfig);
        }
    }

    public void update(int deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime < preWavePause * 1000) {
            return;
        }
        nextSpawnTime += deltaTime;
    }

    public boolean shouldSpawn(int monstersSpawned) {
        if (monstersSpawned < monsters.size() && nextSpawnTime >= (duration / monsters.size()) * 1000) {
            nextSpawnTime = 0;
            return true;
        }
        return false;
    }

    public MonsterConfig getNextMonster() {
        return monsters.remove(0);
    }
}
