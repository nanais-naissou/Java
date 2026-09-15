package fsm;

import engine.model.Entity;

public class True extends ConditionGAL {

	@Override
	public boolean eval(Entity e) {
		return true;
	}

}