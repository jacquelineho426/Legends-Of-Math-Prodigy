package World;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.*;

import main.MouseInput;

public class Node {
	GameMap map;
	
	private int num;
	private String type;
	private int width;
	private int height;
	// Where the node is on the png
	private int x;
	private int y;
	
	// constructor: instantiates a node object to represent each node on the screen
	public Node (String num, String type, String width, String height, String x, String y, GameMap map) {
		this.map = map;
		this.num = Integer.parseInt(num);
		this.type = type;
		this.width = Integer.parseInt(width);
		this.height = Integer.parseInt(height);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
	}
	
	public String getType() {
		return type;
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	public int getNum() {
		return num;
	}
}
