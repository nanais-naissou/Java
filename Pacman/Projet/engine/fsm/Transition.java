package fsm;

public class Transition {
	private final ConditionGAL condition;
	private final ActionGAL action;
	private final Mode target;

	public Transition(ConditionGAL cond, ActionGAL act, Mode tgt) {
		this.condition = cond;
		this.action = act;
		this.target = tgt;
	}

	public ConditionGAL getCondition() {
		return condition;
	}

	public ActionGAL getAction() {
		return action;
	}

	public Mode getTarget() {
		return target;
	}
}
