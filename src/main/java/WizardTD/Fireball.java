package WizardTD;

import processing.core.PVector;


public class Fireball {
    private App app;
    private PVector position;
    public Enemy target;  // Note that the target is of type Enemy
    private float damage;
    private static final float SPEED = 5.0f;

    public Fireball(App app, float x, float y, Enemy target, float damage) {
        this.app = app;
        this.position = new PVector(x, y);
        this.target = target;
        this.damage = damage;
    }

    public void move() {
        PVector direction = PVector.sub(target.getPosition(), position);  // Use target's getPosition() method
        direction.normalize();
        direction.mult(SPEED);
        position.add(direction);
    }

    public boolean hasReachedTarget() {
        float distance = PVector.dist(position, target.getPosition());  // Use target's getPosition() method
        return distance <= SPEED;
    }

    public void draw() {
        app.image(app.fireballImg, position.x, position.y);
    }

    public float getDamage() {
        return this.damage;
    }
}
