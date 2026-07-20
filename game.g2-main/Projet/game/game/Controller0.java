package game;

import engine.IModel;
import engine.IView;
import engine.model.Player;
import engine.view.View;
import game.Entities.Projectile;
import oop.graphics.Canvas;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Task;

public class Controller0 extends engine.controller.Controller {
	private boolean rotateLeft = false;
	private boolean rotateRight = false;
	private boolean follow = false;
	public boolean movePlayer = false;
	boolean debugg = false;
	Player player;
	private boolean fire = false;

	public Controller0(Canvas canvas, IModel model, IView m_view) {
		super(canvas, model, m_view);
		player = m_model.player();
		assert (player != null);
	}

	public boolean isFiring() {
		return fire;
	}

	private void fireBullet() {
		if (player == null) {
			if (debugg) {
				System.out.println("fireBullet: player is null!");
			}
			return;
		}

		float startX = player.m_x_cm;
		float startY = player.m_y_cm;
		int angle = player.orientation();

		float offset = 30f;
		double rad = Math.toRadians(angle);
		float bulletX = startX + (float) Math.cos(rad) * offset;
		float bulletY = startY + (float) Math.sin(rad) * offset;
		if (debugg) {
			// ... existing code ...
			// ... rest of code ...
		}
		System.out.println("[DEBUG] Controller0: Firing bullet at " + bulletX + ", " + bulletY + " angle: " + angle);

		// Note : plus besoin de rayon ici !
		Projectile bullet = new Projectile((engine.model.Model) m_model, bulletX, bulletY, angle, m_view);
		m_model.addAt(bullet);
		if (m_view != null)
			m_view.birth(bullet);
	}

	@Override
	protected void pressed(Canvas canvas, int keyCode, char keyChar) {
		// TODO Auto-generated method stub

		if (keyCode == VirtualKeyCodes.VK_SHIFT) {
			m_shift = true;
			return;
		}
		if (keyCode == VirtualKeyCodes.VK_CONTROL) {
			m_control = true;
			m_view.debugging_opt(1);
			return;
		}
		if (keyCode == VirtualKeyCodes.VK_V) {
			follow = true;
//		    if (!followerRunning) {
//		        followerRunning = true;
//		        Task.task().post(new Follower());
//		        System.out.println("[DEBUG] V key pressed. Starting follower task.");
//		    }
			return;
		}
		if (keyCode == VirtualKeyCodes.VK_SPACE) {
			if (player.projectile_ammo>0) {
				fireBullet();
				player.projectile_ammo--;
				System.out.println("PLAYER AMMO: "+player.projectile_ammo);
			}
			
		}
		/*
		 * 
		 * if (keyCode == VirtualKeyCodes.VK_F) { fire = true; return; }
		 */

		switch (keyCode) {
		case VirtualKeyCodes.VK_LEFT:
			if (m_shift) {
				player.stunt().rotate(-90);
			} else {
				// player.left();
				player.stunt().left();
			}
			break;

		case VirtualKeyCodes.VK_RIGHT:
			if (m_shift) {
				player.stunt().rotate(90);
			} else {
				// player.right();
				if (debugg) {
					System.out.println("moved right");
				}
				player.stunt().right();
			}
			break;

		case VirtualKeyCodes.VK_UP:
			// player.upflu();
			player.stunt().up();
			break;

		case VirtualKeyCodes.VK_DOWN:
			player.stunt().down();
			break;

		case VirtualKeyCodes.VK_F:
			movePlayer = ! movePlayer;
			// Ticker.m_delay = 10;
			break;

		case VirtualKeyCodes.VK_U:
			movePlayer = false;
			break;
		case VirtualKeyCodes.VK_P:
			m_view.Z(0);
			break;
		case VirtualKeyCodes.VK_M:
			m_view.Z(1);
			break;
		case VirtualKeyCodes.VK_EQUALS:
			m_view.Z(2);
			break;

		default:
			break;

		}

		// canvas.repaint(); // met à jour l'affichage

	}

	@Override
	protected void released(Canvas canvas, int keyCode, char keyChar) {
		if (keyCode == oop.graphics.VirtualKeyCodes.VK_SHIFT) {
			m_shift = false;
		} else if (keyCode == VirtualKeyCodes.VK_CONTROL) {
			m_control = false;
			m_view.debugging_opt(0);
		}
		/*
		 * else if (keyCode == VirtualKeyCodes.VK_F) { fire = false; }
		 */
		else if (keyCode == VirtualKeyCodes.VK_V) {
			follow = false;
			// followerRunning = false;
		}
		

	}

	@Override
	protected void typed(Canvas canvas, char keyChar) {
		// nothing to do here.
	}

	@Override
	protected void pressed(Canvas canvas, int bno, int x, int y) {
		/*
		if (bno == 1) {
			rotateLeft = true;
			rotateRight = false;
			Task.task().post(new Rotator());
		} else if (bno == 3) {
			rotateRight = true;
			rotateLeft = false;
			Task.task().post(new Rotator());
		}
		*/
	}

	@Override
	protected void released(Canvas canvas, int bno, int x, int y) {
		/*
		if (bno == 1) {
			rotateLeft = false;
		} else if (bno == 3) {
			rotateRight = false;
		}
		*/
	}

	@Override
	protected void moved(Canvas canvas, int px, int py) {
		int canvasW = canvas.getWidth();
		int canvasH = canvas.getHeight();
		int nrows = m_model.nrows();
		int ncols = m_model.ncols();
		float cellW = (float) canvasW / ncols;
		float cellH = (float) canvasH / nrows;

		int col = (int) (px / cellW);
		int row = (int) (py / cellH);
		m_view.focus(px, py);
		// System.out.println("Mouse over cell: (" + row + ", " + col + ")");

	}

	public boolean follow() {
		return follow;
	}

	private class Rotator implements Runnable {

		@Override
		public void run() {

			if (rotateLeft) {
				player.stunt().rotate(-5);
				Task.task().post(this, 10);
			} else if (rotateRight) {
				player.stunt().rotate(5);
				Task.task().post(this, 10);
			}
		}
	}
	/*
	 * private class Follower implements Runnable {
	 * 
	 * @Override public void run() { if (follow) { m_view.focus(m_view.mX(),
	 * m_view.mY()); // maj du focus m_model.viseCursor(m_view.mX(), m_view.mY(),
	 * m_model.player(), m_canvas); Task.task().post(this, 10); // replanifie } else
	 * { followerRunning = false; // stop proprement } } }
	 */

}
