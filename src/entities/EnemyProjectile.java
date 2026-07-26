package entities;

import core.State;
import main.GameLib;
import java.awt.Color;

public class EnemyProjectile extends Entidade {

    private double velocity;
    private double angle;
    private double radius;

    public EnemyProjectile(double x, double y, double velocity, double angle) {
        super(x, y, State.ACTIVE);

        this.velocity = velocity;
        this.angle = angle;
        this.radius = 2.0;
    }

    public double getRadius() {
        return this.radius;
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.RED);
        GameLib.drawCircle(this.position_X, this.position_Y, this.radius);
    }

    @Override
    public void update(long delta, long currentTime) {
        if (this.state == State.ACTIVE) {
            if (this.position_Y > GameLib.HEIGHT) {
                this.state = State.INACTIVE;
                return;
            }

            this.position_X += this.velocity * Math.cos(this.angle) * delta;
            this.position_Y += this.velocity * Math.sin(this.angle) * delta * (-1.0);
        }
    }
}