package entities;

import core.State;
import main.GameLib;

import java.awt.*;
import java.util.List;

public class Enemy2 extends Entidade {

        private double velocity;
        private double angle;
        private double rotationVelocity;
        private double radius;

        private double explosionStart;
        private double explosionEnd;

        private List<Entidade> enemyProjectiles;

        public Enemy2(double x, double y,
                      State state,
                      List<Entidade> enemyProjectiles) {

                super(x, y, state);

                this.velocity = 0.42;
                this.angle = (3 * Math.PI) / 2;
                this.rotationVelocity = 0.0;
                this.radius = 12.0;

                this.explosionStart = 0;
                this.explosionEnd = 0;

                this.enemyProjectiles = enemyProjectiles;
        }

        @Override
        public double getRadius() {
                return radius;
        }

        @Override
        public void morrer() {

                long currentTime = System.currentTimeMillis();

                this.state = State.EXPLODING;
                this.explosionStart = currentTime;
                this.explosionEnd = currentTime + 500;
        }

        @Override
        public void draw() {

                if (this.state == State.EXPLODING) {

                        long currentTime = System.currentTimeMillis();

                        double alpha =
                                (currentTime - explosionStart) /
                                        (explosionEnd - explosionStart);

                        alpha = Math.max(0.0, Math.min(1.0, alpha));

                        GameLib.drawExplosion(
                                position_X,
                                position_Y,
                                alpha
                        );

                } else if (this.state == State.ACTIVE) {

                        GameLib.setColor(Color.MAGENTA);
                        GameLib.drawDiamond(
                                position_X,
                                position_Y,
                                radius
                        );
                }
        }

        @Override
        public void update(long delta, long currentTime) {

                if (state == State.EXPLODING) {

                        if (currentTime > explosionEnd) {
                                state = State.INACTIVE;
                        }

                        return;
                }

                if (state != State.ACTIVE) {
                        return;
                }


                if (position_X < -10 ||
                        position_X > GameLib.WIDTH + 10) {

                        state = State.INACTIVE;
                        return;
                }

                boolean shootNow = false;
                double previousY = position_Y;


                position_X += velocity * Math.cos(angle) * delta;
                position_Y += velocity * Math.sin(angle) * delta * (-1.0);

                angle += rotationVelocity * delta;


                double threshold = GameLib.HEIGHT * 0.30;

                if (previousY < threshold &&
                        position_Y >= threshold) {

                        if (position_X < GameLib.WIDTH / 2) {
                                rotationVelocity = 0.003;
                        } else {
                                rotationVelocity = -0.003;
                        }
                }


                if (rotationVelocity > 0 &&
                        Math.abs(angle - (3 * Math.PI)) < 0.05) {

                        rotationVelocity = 0.0;
                        angle = 3 * Math.PI;
                        shootNow = true;
                }


                if (rotationVelocity < 0 &&
                        Math.abs(angle) < 0.05) {

                        rotationVelocity = 0.0;
                        angle = 0.0;
                        shootNow = true;
                }


                if (shootNow) {

                        double[] angles = {
                                (3 * Math.PI) / 2 + Math.PI / 8,
                                (3 * Math.PI) / 2,
                                (3 * Math.PI) / 2 - Math.PI / 8
                        };

                        for (double a : angles) {

                                a += Math.random() * Math.PI / 6
                                        - Math.PI / 12;

                                enemyProjectiles.add(

                                        new EnemyProjectile(position_X, position_Y, 0.30, a)
                                );
                        }
                }
        }
}