package fsm;

import java.util.List;

import brain.Bot;
import brain.MainCategories;
import engine.model.Direction;
import engine.model.Entity;

/**
 * Condition pour vérifier la présence ou l'absence d'un mur dans une direction
 */
public class WallCheck extends ConditionGAL {
	private Direction direction;
	private boolean wallExpected;

	/**
	 * Constructeur
	 * 
	 * @param direction    La direction à vérifier
	 * @param wallExpected true si on s'attend à trouver un mur, false sinon
	 */
	public WallCheck(Direction direction, boolean wallExpected) {
		this.direction = direction;
		this.wallExpected = wallExpected;
	}

	@Override
	public boolean eval(Entity e) {
		// Récupère le bot associé à l'entité
		Bot bot = e.bot;

		// Vérifie les entités dans la direction spécifiée
		List<Entity> cellEntities = bot.cell(direction);

		// Vérifie la présence d'obstacles
		boolean hasWall = false;
		if (cellEntities != null) {
			for (Entity entity : cellEntities) {
				if (entity.category().specializes(MainCategories.OBSTACLE)) {
					hasWall = true;
					break;
				}
			}
		}

		// Retourne vrai si la présence/absence de mur correspond à ce qu'on attend
		return hasWall == wallExpected;
	}
}
