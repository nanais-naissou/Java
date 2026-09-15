package game.Avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

import engine.model.Entity;

public class ItemAvatar extends engine.view.Avatar {
	private BufferedImage[] itemFrames = new BufferedImage[3];
	private int chosenIndex;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public ItemAvatar(Entity e) {
		super(e);
		try {
			itemFrames[0] = ImageIO.read(getClass().getResource("/pacman-art/other/apple.png"));
			itemFrames[1] = ImageIO.read(getClass().getResource("/pacman-art/other/dot.png"));
			itemFrames[2] = ImageIO.read(getClass().getResource("/pacman-art/other/strawberry.png"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// Randomly pick one image for this item
		Random rand = new Random();
		chosenIndex = rand.nextInt(itemFrames.length);
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round((e.x()));
		int y_cm = Math.round((e.y()));
		int size = (int) (cellW_cm * 0.8);

		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);

		BufferedImage img = itemFrames[chosenIndex];
		if (img != null) {
			g.drawImage(img, -size / 2, -size / 2, size, size, null);
		} else {
			// Fallback: draw a green circle if images not found
			g.setColor(java.awt.Color.GREEN);
			g.fillOval(-size / 2, -size / 2, size, size);
		}

		g.setTransform(saved);
	}
}

//package game;
//
//import engine.model.Entity;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.geom.AffineTransform;
//import java.awt.Color;
//import java.awt.FontMetrics;
//
//public class ItemAvatar extends engine.view.Avatar {
//    public ItemAvatar(Entity e) { super(e); }
//    protected int cellW_cm = 100;
//	protected int cellH_cm = 100;
//    @Override
//    
//    public void render(Graphics2D g, float cellW, float cellH) {
//        int x_cm = Math.round((e.x()));
//		int y_cm = Math.round((e.y()));
//
//		int size = cellW_cm / 2;
//		Polygon hex = new Polygon();
//
//		hex.addPoint(0, -size);                        // Top
//	    hex.addPoint((int) (size * Math.cos(Math.PI / 6)), (int) (-size * Math.sin(Math.PI / 6))); // Top right
//	    hex.addPoint((int) (size * Math.cos(Math.PI / 6)), (int) (size * Math.sin(Math.PI / 6)));  // Bottom right
//	    hex.addPoint(0, size);                         // Bottom
//	    hex.addPoint((int) (-size * Math.cos(Math.PI / 6)), (int) (size * Math.sin(Math.PI / 6))); // Bottom left
//	    hex.addPoint((int) (-size * Math.cos(Math.PI / 6)), (int) (-size * Math.sin(Math.PI / 6))); // Top left
//
//
//		g.setColor(Color.GREEN);
//		int d = e.orientation();
//		double rot = Math.toRadians(d);
//		AffineTransform saved = g.getTransform();
//		
//		g.translate(x_cm, y_cm);
//		g.rotate(rot);
//		g.fillPolygon(hex);
//		 // Now draw the text "PLAYER" at the center of the avatar
//	    String text = "ITEM";
//	    FontMetrics fontMetrics = g.getFontMetrics();
//	    int textWidth = fontMetrics.stringWidth(text);
//	    int textHeight = fontMetrics.getHeight();
//	    int textX = -textWidth / 2;
//	    int textY = textHeight / 4; 
//	    
//	    g.setColor(Color.BLACK);
//	    g.drawString(text, textX, textY);
//		g.setTransform(saved);
//    }
//}
