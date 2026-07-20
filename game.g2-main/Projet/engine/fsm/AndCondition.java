package fsm;

import engine.model.Entity;

public class AndCondition extends ConditionGAL {
	private final ConditionGAL c1, c2;

	public AndCondition(ConditionGAL c1, ConditionGAL c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public boolean eval(Entity e) {
		return c1.eval(e) && c2.eval(e);
	}
}