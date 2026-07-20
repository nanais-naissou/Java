package fsm;

import engine.model.Direction;
import engine.model.Entity;
import fsm.ActionGAL;

public class Turn extends ActionGAL {
	private final Direction direction; // Direction dans laquelle tourner (ex : gauche, droite)

	public Turn(Direction direction) {
		this.direction = direction;
	}

	@Override
	public void exec(Entity e) {
		System.out.println("execution du turn ");
		e.bot.turn(direction);
	}

}