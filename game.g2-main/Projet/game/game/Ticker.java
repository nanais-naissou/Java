package game;

import oop.runtime.Task;

public class Ticker implements Runnable {
	private Game m_game;
	private long m_last;
	private static int m_delay = 10;

	public Ticker(Game game) {
		m_game = game;
		m_last = System.currentTimeMillis();
		m_game.tick(0); // premier tick
		Task.task().post(this, m_delay);
	}

	@Override
	public void run() {
		long now = System.currentTimeMillis();
		int elapsed = (int) (now - m_last);
		long tickStart = System.nanoTime();
		m_game.tick(elapsed);
		long tickEnd = System.nanoTime();
		m_game.tickTimes.add(tickEnd - tickStart);
		if (m_game.tickTimes.size() > 100)
			m_game.tickTimes.remove(0);
		m_last = now;
		Task.task().post(this, m_delay);
	}
}
