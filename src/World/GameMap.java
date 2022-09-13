package World;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Image;
import java.awt.Toolkit;

import main.Game;
import main.MouseInput;

public class GameMap {
	// Global variables:
	public static Game gp;
	Image image;
	
	// where the left corner of the screen is on the png
	int worldX = 0;
	int worldY = 0;
	int labelX;
	int labelY;
	
	HashMap <JLabel, Node> nodes = new HashMap <>(); // stores all the JLabels on the map and their corresponding nodes  
	ImageIcon imageIc = new ImageIcon ("images/null.png");
	
	public final int speed;
	
	// Constructor:
	public GameMap(Game gp, Scanner textFile) {
		this.gp = gp;
		this.image = Toolkit.getDefaultToolkit().getImage("images/map.png");
		speed = 3;
		
		textFile.nextLine();
		
		// reads in a text file with all the information of the nodes (i.e what type of node it is, where its located)
		while (textFile.hasNextLine())
		{
			String line = textFile.nextLine();
			String [] nodeInfo = line.split(" ");
			Node node = new Node (nodeInfo[0], nodeInfo [1], nodeInfo [2], nodeInfo [3], nodeInfo [4], nodeInfo [5], this);
			
			JLabel label = new JLabel(imageIc);
			setDefaultValues(nodeInfo [4], nodeInfo [5]);
			label.setBounds(labelX, labelY, 100, 50);
			
			// adds a mouse listener to each label so the user can click or interact with the nodes on the map
			label.addMouseListener(new MouseInput(label, gp));
			label.setBackground(Color.red);
			label.setVisible(true);
			gp.setLayout(null);
			gp.add(label);
			nodes.put(label, node);
		}
	}
	
	public void setDefaultValues(String x, String y) {
		labelX = worldX + (Integer.parseInt(x));
		labelY = worldY + (Integer.parseInt(y));
	}
	
	public void update () {
		
		// moves the view of the map depending on which arrow the user is pressing down on
		if (gp.direction == "up" && worldY > 0) {
			worldY -= speed;
			for (JLabel label : nodes.keySet()) {
				gp.remove(label);
				label.setBounds(label.getX(), label.getY() + speed, 100, 50);
				gp.add(label);
			}
		}
		if (gp.direction == "down" && worldY < 201) {
			worldY += speed;
			for (JLabel label : nodes.keySet()) {
				gp.remove(label);
				label.setBounds(label.getX(), label.getY() - speed, 100, 50);
				gp.add(label);
			}
		}
		if (gp.direction == "right" && worldX < 978) {
			worldX += speed;
			for (JLabel label : nodes.keySet()) {
				gp.remove(label);
				label.setBounds(label.getX() - speed, label.getY(), 100, 50);
				gp.add(label);
			}
		}
		if (gp.direction == "left" && worldX > 0) {
			worldX -= speed;
			for (JLabel label : nodes.keySet()) {
				gp.remove(label);
				label.setBounds(label.getX() + speed, label.getY(), 100, 50);
				gp.add(label);
			}
		}
	}
	
	// getter method: returns the HashMap storing all the JLabels and nodes on the screen
	public HashMap <JLabel, Node> getNodes () {
		return nodes;
	}
	// getter methods: returns the x and y-coordinates of where the left corner of the screen is located on the map png 
	public int getWorldX() {
		return worldX;
	}
	
	public int getWorldY() {
		return worldY;
	}
	
	public void draw (Graphics2D g2) {
		
		g2.drawImage(this.image, 0, 0, 1000, 600, worldX, worldY, worldX+1000, worldY+600, null);
		
	}
}

