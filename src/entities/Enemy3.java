package entities;

import core.State;
import main.GameLib;

import java.awt.*;


public class Enemy3 extends Entidade {

    private double velocity;
    private double radius;

    private double movementAngle;
    private double rotationAngle;
    private double rotationVelocity;
    private double turnSpeed;

    private double explosionStart;
    private double explosionEnd;

    private long spawnTime;
    private long lifeTime;

    private Player player;

    public Enemy3(double x,
                  double y,
                  State state,
                  Player player) {

        super(x, y, state);

        this.player = player;

        this.velocity = 0.18;
        this.radius = 15.0;

        this.movementAngle = Math.PI / 2;
        this.rotationAngle = 0;
        this.rotationVelocity = 0.008;
        this.turnSpeed = 0.0015;

        this.explosionStart = 0;
        this.explosionEnd = 0;

        this.spawnTime = System.currentTimeMillis();
        this.lifeTime = 3000;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public void morrer() {

        long currentTime = System.currentTimeMillis();

        state = State.EXPLODING;
        explosionStart = currentTime;
        explosionEnd = currentTime + 500;
    }

    @Override
    public void update(long delta, long currentTime) {

        long elapsedTime = currentTime - spawnTime;

        if (state == State.EXPLODING) {

            if (currentTime > explosionEnd) {
                state = State.INACTIVE;
            }

            return;
        }

        if (state != State.ACTIVE) {
            return;
        }


        if (position_Y < -20) {
            state = State.INACTIVE;
            return;
        }


        if (elapsedTime < lifeTime) {

            double dx = player.position_X - position_X;
            double dy = -(player.position_Y - position_Y);

            double targetAngle = Math.atan2(dy, dx);

            double angleDifference = targetAngle - movementAngle;

            while (angleDifference > Math.PI) {
                angleDifference -= 2 * Math.PI;
            }

            while (angleDifference < -Math.PI) {
                angleDifference += 2 * Math.PI;
            }

            if (angleDifference > turnSpeed * delta) {
                movementAngle += turnSpeed * delta;
            }
            else if (angleDifference < -turnSpeed * delta) {
                movementAngle -= turnSpeed * delta;
            }
            else {
                movementAngle = targetAngle;
            }
        }


        position_X += velocity * Math.cos(movementAngle) * delta;
        position_Y += velocity * Math.sin(movementAngle) * delta * (-1);


        rotationAngle += rotationVelocity * delta;
    }

    @Override
    public void draw() {

        if (state == State.EXPLODING) {

            long currentTime = System.currentTimeMillis();

            double alpha =
                    (currentTime - explosionStart)
                            / (explosionEnd - explosionStart);

            alpha = Math.max(0.0, Math.min(1.0, alpha));

            GameLib.drawExplosion(position_X,
                    position_Y,
                    alpha);

        } else if (state == State.ACTIVE) {

            GameLib.setColor(Color.ORANGE);

            drawStar();
        }
    }


    private void drawStar() {

        int pontas = 8;

        double raioExterno = radius;
        double raioInterno = radius / 2.5;

        double[] xs = new double[pontas * 2];
        double[] ys = new double[pontas * 2];


        for (int i = 0; i < pontas * 2; i++) {

            double angle =
                    rotationAngle + i * Math.PI / pontas;

            double r =
                    (i % 2 == 0)
                            ? raioExterno
                            : raioInterno;

            xs[i] = position_X + Math.cos(angle) * r;
            ys[i] = position_Y + Math.sin(angle) * r;
        }


        for (int i = 0; i < xs.length; i++) {

            int next = (i + 1) % xs.length;

            GameLib.drawLine(
                    xs[i],
                    ys[i],
                    xs[next],
                    ys[next]
            );
        }
    }
}
