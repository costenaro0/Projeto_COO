package entities;

import core.Drawable;
import core.State;
import core.Updatable;

public abstract class Entidade implements Updatable, Drawable {
    protected double position_X;
    protected double position_Y;
    protected State state;

    public Entidade(double position_X, double position_Y, State state) {
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.state = state;
    }

    public double getRadius() {
        return 0.0;
    }

    public boolean colideCom(Entidade other) {
        double dx = this.position_X - other.position_X;
        double dy = this.position_Y - other.position_Y;
        double dist = Math.sqrt((dx * dx) + (dy * dy));
        return dist < (this.getRadius() + other.getRadius());
    }

    public void morrer() {
        this.state = State.INACTIVE;
    }

    public double getPosition_X() {
        return this.position_X;
    }

    public double getPosition_Y() {
        return this.position_Y;
    }

    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
}
