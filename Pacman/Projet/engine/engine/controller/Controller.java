package engine.controller;

import engine.IModel;
import engine.IView;
import oop.graphics.Canvas;

public abstract class Controller implements engine.IController {
	protected Canvas m_canvas;
	protected IView m_view;
	protected IModel m_model;
	protected boolean m_shift;
	protected boolean m_control;
	protected boolean m_alt;

	public Controller(Canvas canvas, IModel model, IView view) {
		m_canvas = canvas;
		m_model = model;
		m_view = view;
		canvas.set(new MouseListener());
		canvas.set(new KeyListener());
	}

	protected abstract void pressed(Canvas canvas, int keyCode, char keyChar);

	protected abstract void released(Canvas canvas, int keyCode, char keyChar);

	protected abstract void typed(Canvas canvas, char keyChar);

	protected abstract void pressed(Canvas canvas, int bno, int x, int y);

	protected abstract void released(Canvas canvas, int bno, int x, int y);

	protected abstract void moved(Canvas canvas, int px, int py);

	class KeyListener implements Canvas.KeyListener {

		@Override
		public void pressed(Canvas canvas, int keyCode, char keyChar) {
			// TODO Auto-generated method stub
			Controller.this.pressed(canvas, keyCode, keyChar);

		}

		@Override
		public void released(Canvas canvas, int keyCode, char keyChar) {
			// TODO Auto-generated method stub
			Controller.this.released(canvas, keyCode, keyChar);

		}

		@Override
		public void typed(Canvas canvas, char keyChar) {
			// TODO Auto-generated method stub
			Controller.this.typed(canvas, keyChar);

		}

	}

	class MouseListener implements Canvas.MouseListener {

		@Override
		public void moved(Canvas canvas, int x, int y) {
			// TODO Auto-generated method stub
			Controller.this.moved(canvas, x, y);

		}

		@Override
		public void pressed(Canvas canvas, int bno, int x, int y) {
			// TODO Auto-generated method stub
			Controller.this.pressed(canvas, bno, x, y);

		}

		@Override
		public void released(Canvas canvas, int bno, int x, int y) {
			// TODO Auto-generated method stub
			Controller.this.released(canvas, bno, x, y);

		}

	}

}
