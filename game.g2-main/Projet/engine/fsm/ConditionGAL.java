package fsm;

import engine.model.Entity;

public abstract class ConditionGAL {
	public abstract boolean eval(Entity e);

	public ConditionGAL and(ConditionGAL other) {
		return new AndCondition(this, other);
	}
}