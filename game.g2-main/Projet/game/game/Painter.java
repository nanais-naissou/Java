package game;

import java.awt.Graphics2D;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.tasks.Task;

public class Painter implements Runnable {
	private Game m_game;
	private Canvas m_canvas;
	private int m_ncols, m_nrows;
	private Task m_task;

	private Boolean selectedMode = null;

	Painter(Canvas canvas, int nr, int nc) {
		m_nrows = nr;
		m_ncols = nc;
		m_canvas = canvas;
		canvas.set(new PaintListener());
		// throw new RuntimeException("NYI");
	}

	@Override
	public void run() {
		m_canvas.repaint();
		m_task = Task.task();
		m_task.post(this, 40);
		
		// throw new RuntimeException("NYI");
	}

	public void setMode(boolean mode) {
		this.selectedMode = mode;
	}

	class PaintListener implements Canvas.PaintListener {

		@Override
		public void paint(Canvas canvas, Graphics g) {
			if (m_game != null) {
				Graphics2D g2 = g.getGraphics2D();
				m_game.paint(canvas, g2);
			}
		}

		@Override
		public void visible(Canvas canvas) {
			boolean useHardMode = selectedMode != null && selectedMode;
			m_game = new Game(canvas, m_nrows, m_ncols, useHardMode);
			// rendu periodique
			m_task = Task.task();
			// 1er tick
			m_task.post(Painter.this, 0);
		}

		@Override
		public void revoked(Canvas canvas) {
			System.exit(0);
		}

	}

}
