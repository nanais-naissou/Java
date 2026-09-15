package brain;

import engine.ICategory;

public class Category implements engine.ICategory {
	private final String name;
	private final Category parent;

	public Category(String name, Category parent) {
		this.name = name;
		this.parent = parent;
	}

	public String name() {
		return name;
	}

	public Category parent() {
		return parent;
	}

	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Category c) {
		return name.equals(c.name);
	}

	@Override
	public boolean equals(Object c) {
		if (c == null)
			return false;
		if (!(c instanceof Category)) {
			return false;
		}
		return equals((Category) c);

	}

	@Override
	public boolean specializes(Category c) {
		Category cur = this;
		while (cur != null) {
			if (cur.equals(c))
				return true;
			cur = cur.parent;
		}
		return false;
	}

}
