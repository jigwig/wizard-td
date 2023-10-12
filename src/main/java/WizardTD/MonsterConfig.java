package WizardTD;

public class MonsterConfig {

    private String type;
    private float hp;
    private float speed;
    private float armour;
    public  float manaGainedOnKill;
    private int quantity;

    public MonsterConfig(String type, float hp, float speed, float armour, float manaGainedOnKill, int quantity) {
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedOnKill = manaGainedOnKill;
        this.quantity = quantity;
    }

    // Getter methods for all attributes
    public String getType() {
        return type;
    }

    public float getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public float getArmour() {
        return armour;
    }

    public float getManaGainedOnKill() {
        return manaGainedOnKill;
    }

    public int getQuantity() {
        return quantity;
    }
}
