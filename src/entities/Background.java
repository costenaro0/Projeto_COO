package entities;

import core.State;
import main.GameLib;
import java.awt.Color;

public class Background extends Entidade {

    private double speed;
    private double size;
    private Color color;

    public Background(double x, double y, double speed, double size, Color color) {
        super(x, y, State.ACTIVE);
        this.speed = speed;
        this.size = size;
        this.color = color;
    }

    @Override
    public void update(long delta, long currentTime) {
        this.position_Y += this.speed * delta;
        this.position_Y = this.position_Y % GameLib.HEIGHT;
    }

    @Override
    public void draw() {
        if (Math.random() > 0.95) {
            GameLib.setColor(Color.WHITE);
        }
        else {
            GameLib.setColor(this.color);
        }
        GameLib.fillRect(this.position_X, this.position_Y, this.size, this.size);
    }
}