package entities;

import core.State;
import main.GameLib;

import java.awt.*;
import java.util.List;

public class Enemy1 extends Entidade {

    private double velocity;
    private double angle;
    private double rotation_velocity;
    private double radius;
    private double explosion_start;
    private double explosion_end;
    private long nextShoot;
    private List<Entidade> enemy_Projectiles;
    private Player player;

    public Enemy1(double x, double y, State state, List<Entidade> enemy_Projectiles, Player player) {
        super(x, y, state);

        this.velocity = 0.20 + Math.random() * 0.15;
        this.angle = (3 * Math.PI) / 2;
        this.rotation_velocity = 0.0;
        this.radius = 9.0;
        this.explosion_start = 0;
        this.explosion_end = 0;
        this.nextShoot = System.currentTimeMillis() + 500;
        this.enemy_Projectiles = enemy_Projectiles;
        this.player = player;
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
        this.explosion_end = currentTime + 500;
    }

    @Override
    public void draw() {
        if (this.state == State.EXPLODING) {
            long currentTime = System.currentTimeMillis();
            double alpha = (currentTime - this.explosion_start) / (this.explosion_end - this.explosion_start);
            alpha = Math.max(0.0, Math.min(1.0, alpha));
            GameLib.drawExplosion(this.position_X, this.position_Y, alpha);
        }
        else if (this.state == State.ACTIVE) {
            GameLib.setColor(Color.CYAN);
            GameLib.drawCircle(this.position_X, this.position_Y, this.radius);
        }
    }

    @Override
    public void update(long delta, long currentTime) {
        if (this.state == State.EXPLODING) {
            if (currentTime > this.explosion_end) {
                this.state = State.INACTIVE;
            }
            return;
        }

        if (this.state == State.ACTIVE) {
            if (this.position_Y > GameLib.HEIGHT + 10) {
                this.state = State.INACTIVE;
                return;
            }

            this.position_X += this.velocity * Math.cos(this.angle) * delta;
            this.position_Y += this.velocity * Math.sin(this.angle) * delta * (-1.0);
            this.angle += this.rotation_velocity * delta;

            if (currentTime > this.nextShoot && this.position_Y < player.position_Y) {
                this.enemy_Projectiles.add(new EnemyProjectile(this.position_X, this.position_Y, 0.45, (3 * Math.PI) / 2));
                this.nextShoot = (long) (currentTime + 200 + Math.random() * 500);
            }
        }
    }
}
