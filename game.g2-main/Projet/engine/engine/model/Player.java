package engine.model;

import brain.Bot;

import fsm.PlayerBot;
import game.Stunts.PlayerStunt;
import engine.ICategory;

public class Player extends Entity {
	protected int coinsCount;
	protected int fruitsCount;
	public Bot bot;
	PlayerStunt m_stunt;
	// public boolean pacgumActive;
	// private long pacgumActivatedTime;
	public int player_max_lives = 3;
	public boolean dead;
	public int projectile_ammo = 1;


	public Player(Model m, int r, int c, int o) {
		super(m, r, c, o);
		m_stunt = new PlayerStunt(m, this);
		this.bot = new PlayerBot(m, this);
		this.coinsCount = 0;
		this.fruitsCount = 0;
		// this.pacgumActive = false;
		this.dead = false;
	}

	public boolean getdead() {
		return dead;
	}

	public void setdead(boolean b) {
		dead = b;
	}

	/*
	 * public void setpacgumActive(boolean p) { this.bot.setPac(p); pacgumActive =
	 * p; if (p) { pacgumActivatedTime = System.currentTimeMillis(); } }
	 * 
	 * 
	 * public boolean getpacgumActive() { pacgumActive = this.bot.getPac(); return
	 * pacgumActive; }
	 */

	public void setcoinsCount(int c) {
		this.coinsCount = c;
	}

	public int getcoinsCount() {
		return coinsCount;
	}

	public void setfruitsCount(int f) {
		this.fruitsCount = f;
	}

	public int getfruitsCount() {
		return fruitsCount;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	public Bot getBot() {
		return bot;
	}

	@Override
	public PlayerStunt stunt() {
		return m_stunt;
	}

	// New method to handle PacGum timing
	public void checkPacGumTimeout() {

//		System.out.println(getpacgumActive());
//		System.out.println(System.currentTimeMillis() - pacgumActivatedTime >= 5000) ;

		if (this.bot.getPac() && ((System.currentTimeMillis() - this.bot.pacgumActivatedTime) >= 5000)) {
			// System.out.println(pacgumActivatedTime);
			this.bot.setPac(false);
			System.out.println(System.currentTimeMillis() - this.bot.pacgumActivatedTime);

			// System.out.println("PacGum has expired and is now deactivated.");
		}
	}

	/*
	 * Move this entity up one row.
	 */
	public void up() {
		move(-1, 0); // move(delta col, delta row)
	}

	/*
	 * Move this entity down one row.
	 */
	public void down() {
		move(1, 0);
	}

	/*
	 * Move this entity left one column.
	 */
	public void left() {
		move(0, -1);
	}

	/*
	 * Move this entity right one column.
	 */
	public void right() {
		move(0, 1);
	}

	////////////////////////////// movements in cm
	public void upflu() {
		face(270);
		moveFluid(0.0f, -2f);
	} // cm

	public void downflu() {
		face(90);
		moveFluid(0.0f, 2f);

	}

	public void leftflu() {
		face(180);
		moveFluid(-2f, 0.0f);
	}

	public void rightflu() {
		face(0);
		moveFluid(2f, 0.0f);
	}

	@Override
	public ICategory category() {
		return game.Categories.PLAYER;
	}
}
