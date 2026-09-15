package engine;

import brain.Category;

public interface ICategory {
	public abstract boolean equals(Category c);

	public abstract boolean specializes(Category c);

}
