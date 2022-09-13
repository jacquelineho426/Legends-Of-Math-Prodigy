package Entity;

import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import Battle.Battle;
import World.GameMap;
import main.Game;
import main.Main;

public class Player extends Entity{

	Game gp;
	GameMap map;
	
	
	public static boolean startBattle;
	public int playerX; // x-coord of the player on the map
	public int playerY; // y-coord of the player on the map
	public String state;
	public static boolean [] specialPowers; // {"heal", "block", "blank", "spear", "poison", "blank"};
	
	// Constuctor: instantiates the main player object on the map
	public Player (Game gp, GameMap map, String character) {
		this.gp = gp;
		this.map = map;
		playerX = 50;
		playerY = 350;
		state = "main";
		startBattle = false;
		specialPowers = new boolean [6];
		setDefaultValues();
		setPlayerImage(character);
	}
	
	public void setDefaultValues () {
		mapX = 50;
		mapY = 350;
		speed = 3;
	}
	
	public static void setSpecialPowers (boolean state, int cardNum) {
		specialPowers[cardNum-1] = state;
	}
	
	// method to change the image of the player depending on which character the user chose
	// no return type, parameter: takes in a string indicating which character was chosen
	public void setPlayerImage(String character) {

		String imgName1 = "images/" + character + "Walk1.png";
		String imgName2 = "images/" + character + "Walk2.png";
		System.out.println(imgName1);
		walk1 = Toolkit.getDefaultToolkit().getImage(imgName1);
		walk2 = Toolkit.getDefaultToolkit().getImage(imgName2);
		
	}
	
	public void update () {
		
		// animating the movement of the character on the map
		if (gp.direction == "up") {
			if (map.getWorldY() > 0)
				playerY += speed;
		}	
		if (gp.direction == "down") {
			if (map.getWorldY() < 201)
				playerY -= speed;
		}	
		if (gp.direction == "right") {
			if (map.getWorldX() < 978)
				playerX -= speed;
		}
		if (gp.direction == "left") {
			if (map.getWorldX() > 0)
				playerX += speed;
		}
		
		// checks if a node was pressed on the map and moves the player accordingly to the node
		if (gp.nodePressed) {
			if (playerX < gp.clickedNode.getX() - map.getWorldX()) {
				playerX += speed;
				if (playerY > gp.clickedNode.getY() - map.getWorldY()) {
					playerY -= speed;
				}
				else if (playerY < gp.clickedNode.getY() - map.getWorldY()) {
					playerY += speed;
				}
			}
			else {
				
				if (gp.clickedNode.getType().equals("battle")) {
					Main.startBattle();
				}
				
				else if (gp.clickedNode.getType().equals("question")) {
					gp.startQuestion();
				}
				
				else if (gp.clickedNode.getType().equals("dice")) {
					gp.startDice();
				}
				
				else if (gp.clickedNode.getType().equals("faceoff")) {
					gp.state = "end";
				}
				gp.nodePressed = false;
			}
		}
		
		spriteCounter ++;
		if (spriteCounter > 20) {
			if (spriteNum == 1)
				spriteNum = 2;
			else
				spriteNum = 1;
			
			spriteCounter = 0;
		}
	}
	
	public void draw (Graphics2D g2) {
		
		Image image = null;
		
		
		if (spriteNum == 1)
			image = walk1;
		if (spriteNum == 2)
			image = walk2;
		
		g2.drawImage(image, playerX, playerY, null);
		
	}
}
