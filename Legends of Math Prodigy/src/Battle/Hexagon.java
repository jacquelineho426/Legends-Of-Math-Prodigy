package Battle;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import Battle.Battle;

public class Hexagon {
	
	private int x;
	private int y;
	private int row;
	private int col;
	private JLabel label;
	private Image image;
	private int state;
	Battle bp;

	// Constructor: instantiates a hexagon object for the hex tiles on the battlefield
	public Hexagon (int x, int y, Battle bp, int row, int col, int state) {
		
		this.x = x;
		this.y = y;
		this.row = row;
		this.col = col;
		this.bp = bp;
		this.state = state; // 1-3 = hero, 4-6 = enemy, 9 = out of bounds, 7 = available to move to, 0 = unoccupied
		this.image = Toolkit.getDefaultToolkit().getImage("images/hexagon.png");
		
		// creates a JLabel for each hexagon so the user can interact with it using mouse listener
		JLabel label = new JLabel();
		label.setBounds(x, y, 100, 100);
		label.setVisible(true);
		label.setBackground(null);
		label.addMouseListener(new MouseHandler(label, bp));
		this.label = label;
		bp.setLayout(null);
		
		bp.labels.put(label, this);
	}
	
	// getter and setter methods:
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public int getRow () {
		return row;
	}
	
	public int getCol () {
		return col;
	}

	public int getState () {
		return state;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public void setImage (Image image) {
		this.image = image;
	}
	
	public JLabel getLabel() {
		return label;
	}
}
