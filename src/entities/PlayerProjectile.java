package entities;

import core.State;
import main.GameLib;

import java.awt.*;

public class PlayerProjectile extends Entidade {

    private double velocity_X;
    private double velocity_Y;

    public PlayerProjectile(double position_X, double position_Y) {
        super(position_X, position_Y, State.ACTIVE);
        this.velocity_X = 0.0;
        this.velocity_Y = -1.0;
    }

    public double getRadius() {
        return 2.0;
    }

    @Override
    public void update(long delta, long currentTime) {
        if (this.state == State.ACTIVE) {

            this.position_X += this.velocity_X * delta;
            this.position_Y += this.velocity_Y * delta;

            if (this.position_Y < 0) {
                this.state = State.INACTIVE;
            }
        }
    }

    @Override
    public void draw() {
        if (this.state == State.ACTIVE) {
            GameLib.setColor(Color.GREEN);
            GameLib.drawLine(this.position_X, this.position_Y - 5, this.position_X, this.position_Y + 5);
            GameLib.drawLine(this.position_X - 1, this.position_Y - 3, this.position_X - 1, this.position_Y + 3);
            GameLib.drawLine(this.position_X + 1, this.position_Y - 3, this.position_X + 1, this.position_Y + 3);
        }
    }
}
