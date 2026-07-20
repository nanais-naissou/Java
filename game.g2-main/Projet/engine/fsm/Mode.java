package fsm;

import java.util.ArrayList;
import java.util.List;

public class Mode {
	private final State state;
	private final List<Transition> transitions = new ArrayList<>();

	public Mode(State state) {
		this.state = state;
	}

	public String getName() {
		return state.getName();
	}

	public void add(Transition t) {
		transitions.add(t);
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public State getState() {
		return state;
	}
}
