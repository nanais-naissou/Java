package fsm;

import java.util.concurrent.locks.Condition;

import brain.Bot;
import engine.model.Entity;

public class Conjonction extends ConditionGAL {
    // Champs pour stocker les deux conditions à combiner
    private ConditionGAL cond1, cond2;

    // Constructeur : prend deux conditions comme arguments
    public Conjonction(ConditionGAL c1, ConditionGAL c2) {
        this.cond1 = c1;
        this.cond2 = c2;
    }

    @Override
    public boolean eval(Entity e) {
        // Retourne true si les deux conditions sont vraies
        return cond1.eval(e) & cond2.eval(e);
    }

	
}
