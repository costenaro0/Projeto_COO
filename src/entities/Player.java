package entities;

import core.State;
import main.GameLib;

import java.util.List;

public class Player extends Entidade {

    private double velocity_X;
    private double velocity_Y;
    private double radius;
    private double explosion_start;
    private double explosion_end;
    private long nextShot;
    private List<Entidade> player_Projectiles;

    public Player(double x, double y, State state, List<Entidade> player_Projectiles) {
        super(x, y, state);

        this.velocity_X = 0.25;
        this.velocity_Y = 0.25;
        this.radius = 12.0;
        this.explosion_start = 0;
        this.explosion_end = 0;
        this.nextShot = System.currentTimeMillis();
        this.player_Projectiles = player_Projectiles;
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    @Override
    public void morrer() {
        long currentTime = System.currentTimeMillis();
        this.state = State.EXPLODING;
        this.explosion_start = currentTime;
        this.explosion_end = currentTime + 2000;
    }

    @Override
    public void update(long delta, long currentTime) {
        if (this.state == State.EXPLODING) {
            if (currentTime > this.explosion_end) {
                this.state = State.ACTIVE;
            }
        }

        if (this.state == State.ACTIVE) {
            if (GameLib.iskeyPressed(GameLib.KEY_UP)) this.position_Y -= delta * this.velocity_Y;
            if (GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.position_Y += delta * this.velocity_Y;
            if (GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.position_X -= delta * this.velocity_X;
            if (GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.position_X += delta * this.velocity_X;
            if (GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis > this.nextShot) {
                    PlayerProjectile newShot = new PlayerProjectile(this.position_X, this.position_Y - 2 * this.radius);
                    this.player_Projectiles.add(newShot);
                    this.nextShot = currentTimeMillis + 100;
                }
            }
        }

        if (this.position_X < 0.0) this.position_X = 0.0;
        if (this.position_X >= GameLib.WIDTH) this.position_X = GameLib.WIDTH - 1;
        if (this.position_Y < 25.0) this.position_Y = 25.0;
        if (this.position_Y >= GameLib.HEIGHT) this.position_Y = GameLib.HEIGHT - 1;
    }

    @Override
    public void draw() {
        if (this.state == State.EXPLODING) {
            long currentTime = System.currentTimeMillis();
            double alpha = (currentTime - this.explosion_start) / (this.explosion_end - this.explosion_start);
            alpha = Math.max(0.0, Math.min(1.0, alpha));
            GameLib.drawExplosion(this.position_X, this.position_Y, alpha);
        } else if (this.state == State.ACTIVE) {
            GameLib.setColor(java.awt.Color.BLUE);
            GameLib.drawPlayer(this.position_X, this.position_Y, this.radius);
        }
    }
}
