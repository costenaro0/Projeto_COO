package main;

import core.State;
import entities.Background;
import entities.Enemy1;
import entities.Entidade;
import entities.Player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		boolean running = true;
		GameLib.initGraphics();

		List<Entidade> player_Projectiles = new ArrayList<>();
		Player player = new Player(GameLib.WIDTH / 2.0, GameLib.HEIGHT * 0.90, State.ACTIVE, player_Projectiles);

		List<Entidade> enemy_Projectiles = new ArrayList<>();
		List<Entidade> enemies = new ArrayList<>();

		int[] starsPerLayer = {50, 30, 20};
		double[] speedPerLayer = {0.045, 0.060, 0.070};
		double[] sizePerLayer = {2.0, 2.5, 3.0};
		Color[] colorPerLayer = {Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY};

		List<Entidade> backgrounds = new ArrayList<>();
		for (int layer = 0; layer < starsPerLayer.length; layer++) {
			for (int i = 0; i < starsPerLayer[layer]; i++) {
				double x = Math.random() * GameLib.WIDTH;
				double y = Math.random() * GameLib.HEIGHT;
				backgrounds.add(new Background(x, y, speedPerLayer[layer], sizePerLayer[layer], colorPerLayer[layer]));
			}
		}

		long delta;
		long currentTime = System.currentTimeMillis();

		long nextEnemy1 = currentTime + 2000;

		while (running) {

			delta = System.currentTimeMillis() - currentTime;
			currentTime = System.currentTimeMillis();

			if (currentTime > nextEnemy1) {
				enemies.add(new Enemy1(Math.random() * (GameLib.WIDTH - 20) + 10, -200.0, State.ACTIVE, enemy_Projectiles, player));
				nextEnemy1 = currentTime + 500;
			}

			player.update(delta, currentTime);

			for (Entidade bg : backgrounds) {
				bg.update(delta, currentTime);
			}
			for (Entidade proj : player_Projectiles) {
				proj.update(delta, currentTime);
			}
			for (Entidade enemy : enemies) {
				enemy.update(delta, currentTime);
			}
			for (Entidade proj : enemy_Projectiles) {
				proj.update(delta, currentTime);
			}

			player_Projectiles.removeIf(p -> p.getState() == State.INACTIVE);
			enemy_Projectiles.removeIf(p -> p.getState() == State.INACTIVE);
			enemies.removeIf(e -> e.getState() == State.INACTIVE);

			for (Entidade bg : backgrounds) {
				bg.draw();
			}

			player.draw();

			for (Entidade proj : player_Projectiles) {
				proj.draw();
			}
			for (Entidade enemy : enemies) {
				enemy.draw();
			}
			for (Entidade proj : enemy_Projectiles) {
				proj.draw();
			}

			for (Entidade proj : player_Projectiles) {
				for (Entidade enemy : enemies) {
					if (proj.getState() == State.ACTIVE && enemy.getState() == State.ACTIVE
							&& proj.colideCom(enemy)) {
						proj.morrer();
						enemy.morrer();
					}
				}
			}

			for (Entidade proj : enemy_Projectiles) {
				if (proj.getState() == State.ACTIVE && player.getState() == State.ACTIVE
						&& proj.colideCom(player)) {
					proj.morrer();
					player.morrer();
				}
			}

			for (Entidade enemy : enemies) {
				if (enemy.getState() == State.ACTIVE && player.getState() == State.ACTIVE
						&& enemy.colideCom(player)) {
					enemy.morrer();
					player.morrer();
				}
			}

			GameLib.display();
		}
	}
}