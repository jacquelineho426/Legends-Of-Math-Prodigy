package Battle;

import java.awt.*;
import java.io.IOException;
import javax.swing.JLabel;
import java.util.*;
import main.Main;
import Battle.Battle;

public class Hero {

	// global variables:
	private int x; // coordinates of the hero on the battle field
	private int y;
	private int num;
	private int health; // the health of the user
	private String name;
	private int level;
	private int lifeCap; // the life capacity
	private Hexagon hex;
	private boolean clickedAttack; // checks if hero clicks attack button
	private boolean isDead; // checks if hero is alive or dead
	private String type; // finds the hero type: wizard, knight, elf, or prince
	private int attackAmt;
	private Image walk1 = Toolkit.getDefaultToolkit().getImage("images/battleWizard1.png");
	private Image walk2 = Toolkit.getDefaultToolkit().getImage("images/battleWizard2.png");
	private LinkedList <JLabel> addedLabels = new LinkedList <>();
	private Profile profile;
	private Image icon;
	private Image board;
	
	public Image [] boards = {Toolkit.getDefaultToolkit().getImage("images/dumbleInfo.png"), Toolkit.getDefaultToolkit().getImage("images/geraldInfo.png"), Toolkit.getDefaultToolkit().getImage("images/augustInfo.png"), Toolkit.getDefaultToolkit().getImage("images/romeoInfo.png")};
	
	private int spriteCounter = 0;
	private int spriteNum = 1;
	private boolean spearUsed = false; // checks if user uses spear power
	private boolean poisonUsed = false; // checks if user uses poison power
	
	int count = 0;
	Battle panel;
	
	// constructor: creates a hero object for the battle
	public Hero (int x, int y, Battle panel, Hexagon hex, int num, int attackAmt, String name) {
		this.x = x;
		this.y = y;
		this.num = num;
		this.panel = panel;
		this.hex = hex;
		this.hex.setState(num);
		this.clickedAttack = false;
		this.isDead = false;
		this.health = 100;
		this.attackAmt = attackAmt;
		this.type = "wizard";
		this.profile = new Profile (Toolkit.getDefaultToolkit().getImage("images/wizardProfile.png"));
		panel.profiles.add(this.profile);
	}
	
	// constructor: instantiates a special type of hero with unique attributes
	public Hero (String name, int level, int lifeCap, int attackAmt, Image icon, String type, int i) {
		this.name = name;
		this.level = level;
		this.lifeCap = lifeCap;
		this.attackAmt = attackAmt;
		this.icon = icon;
		this.type = type;
		this.board = boards [i];
		String profileName = "images/" + type + "profile.png";
		Profile newProf = new Profile(Toolkit.getDefaultToolkit().getImage(profileName));
		this.profile =  newProf;
		
	}
	// resets the values back to its original state
	public void resetValues(int x, int y, Hexagon hex, int attackAmt) {
		this.x = x;
		this.y = y;
		this.hex = hex;
		this.hex.setState(this.num);
		this.clickedAttack = false;
		this.addedLabels.clear();
		this.isDead = false;
		this.health = 100;
		this.attackAmt = attackAmt;
	}
	
	// assigns the unique attributes to the hero the user chose to play with
	public void defineHero (String name, int attackAmt, int lifeCap, String type) {
		this.name = name;
		this.attackAmt = attackAmt;
		this.lifeCap = lifeCap;
		this.type = type;
		String profileName = "images/" + type + "profile.png";
		Profile newProf = new Profile(Toolkit.getDefaultToolkit().getImage(profileName));
		this.profile =  newProf;
		String img1 = "images/battle" + type + "1.png";
		String img2 = "images/battle" + type + "2.png";
		this.walk1 =  Toolkit.getDefaultToolkit().getImage(img1);
		this.walk2 = Toolkit.getDefaultToolkit().getImage(img2);
	}

	// getter methods: returns the user's instance variables --> no paramters
	public Image getBoard() {
		return board;
	}
	
	public String getType() {
		return type;
	}
	
	public int getAttackAmt() {
		return attackAmt;
	}
	
	public int getLifeCap() {
		return lifeCap;
	}
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public int getNum () {
		return num;
	}
	
	public Hexagon getHex () {
		return hex;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	// setter methods: changes the value of the user's instance variables --> no return type
	public void setHex (Hexagon hex) {
		this.hex = hex;
	}
	
	public void setDead (boolean state) {
		this.isDead = state;
	}
	
	public void setHealth (int attackAmt) {
		this.health = this.health - (100/attackAmt);
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean getDead () {
		return isDead;
	}
	
	public void setClickedAttack(boolean state) {
		this.clickedAttack = state;
	}
	
	public LinkedList <JLabel> getAddedLabels() {
		return addedLabels;
	}
	
	
	public void draw (Graphics2D g2) {
		Image image = null; 

		if (count == 150) {
			// check if attack kills enemy
			if (panel.power.equals("spear") || (Math.max(panel.target.getX(), panel.heroes.get(panel.heroTurn - 1).getX()) - Math.min(panel.target.getX(), panel.heroes.get(panel.heroTurn - 1).getX()) < 150 
				&& Math.max(panel.target.getY(), panel.heroes.get(panel.heroTurn -1).getY()) - Math.min(panel.target.getY(), panel.heroes.get(panel.heroTurn - 1).getY()) < 150)) {
				
				// if the attack affects the enemy, decreases it health
				panel.enemies.get(panel.labels.get(panel.target).getState()-4).setHealth(this.attackAmt);
				panel.enemies.get(panel.labels.get(panel.target).getState()-4).getProfile().setWidth(panel.enemies.get(panel.labels.get(panel.target).getState()-4).getHealth());
				panel.labels.get(panel.target).setImage(Toolkit.getDefaultToolkit().getImage("images/hexagon.png"));
				
				
				// if enemy's health is 0, the enemy is dead
				if (panel.enemies.get(panel.labels.get(panel.target).getState()-4).getHealth() <= 0 || panel.power.equals("poison")) {
					panel.enemies.get(panel.labels.get(panel.target).getState()-4).setDead(true);
					panel.enemies.get(panel.labels.get(panel.target).getState()-4).getProfile().setImage("enemy");
			
					
				}
				// if user uses spear special power
				if (panel.power.equals("spear")) {
					
					spearUsed = true;
				}
				
				// if user uses poison special power
				if (panel.power.equals("poison")) {
					
					panel.enemies.get(panel.labels.get(panel.target).getState()-4).setHealth(100);
					panel.enemies.get(panel.labels.get(panel.target).getState()-4).getProfile().setWidth(panel.enemies.get(panel.labels.get(panel.target).getState()-4).getHealth());
					panel.labels.get(panel.target).setImage(Toolkit.getDefaultToolkit().getImage("images/hexagon.png"));
					poisonUsed = true;
				}
			}
			this.clickedAttack = false;
			panel.changeTurns();
			count = 0;
		}
		
		// if user clciks the attack button
		if (this.clickedAttack) {
			String img1 = "images/attack" + this.type + "1.png";
			String img2 = "images/attack" + this.type + "2.png";
			if (spriteNum == 1) {
				
				image = Toolkit.getDefaultToolkit().getImage(img1);
			}
			else
				image = Toolkit.getDefaultToolkit().getImage(img2);
			count++;
		}
		
		else {
			if (spriteNum == 1)
				image = walk1;
			if (spriteNum == 2)
				image = walk2;
		}
		
		// if it is the current hero's turn
		if (this.num == panel.heroTurn && panel.turn == 1 && !(this.isDead)) {

			g2.drawImage(this.hex.getImage(), this.hex.getX(), this.hex.getY(),  null);	
			
			for (JLabel label : addedLabels) {
				Hexagon hex = panel.labels.get(label);
				g2.drawImage(hex.getImage(), hex.getX(), hex.getY(),  null);
			}
			
			// displays the hex based grid the user can move to
			if (panel.displayHexagons) {
				int row = this.hex.getRow();
				int col = this.hex.getCol();
				
				int [][] coords = {{row-1, col-1}, {row-1, col}, {row, col-1}, {row, col+1}, {row+1, col-1}, {row+1, col}};
				
				if (row % 2 == 0) {
					coords [0][1] = col+1;
					coords [4][1] = col+1;
				}
		
				for (int i = 0; i < coords.length; i++) {
					if (coords[i][0] > -1 && coords[i][0] < 7 && coords[i][1] > -1 && coords[i][1] < 9) {
						Hexagon hex = panel.hexagons[coords[i][0]][coords[i][1]];
						if (hex.getState() == 0 || hex.getState() == this.num || hex.getState () == 7) {
							hex.setState(7);
							panel.add(hex.getLabel());
							addedLabels.add(hex.getLabel());
						}
					}
				}
				panel.displayHexagons = false;
			}
			
			// checks if the user clicks attack or a hexagon and preforms the action accordingly
			else {
				if (panel.hexPressed || this.clickedAttack) {
					if (panel.hexPressed) {
						this.x = panel.clickedHex.getX();
						this.y = panel.clickedHex.getY()-35;
						this.hex.setState(0);
						this.hex = panel.clickedHex;
						this.hex.setState(this.num);
						panel.hexPressed = false;
					}
					for (JLabel label : addedLabels ) {
						if (panel.labels.get(label).getState() == 7)
							panel.labels.get(label).setState(0);
						panel.remove(label);
					}
					addedLabels.clear();
				}
			
			}
			
			// checks if the user uses a special power
			if (panel.powerUsed) {
				// heal power is used, user's player's health is restored to 100
				if (panel.power.equals("heal")) {
					this.health = 100;
					this.profile.resetWidth();
				}
				
				// spear power is used, user can attack any enemy on the screen
				else if (panel.power.equals("spear")) {
					
					if (spearUsed) {
						panel.powerUsed = false;
						panel.power = "none";
						spearUsed = false;
					}
				}
				
				// poison power is used, user can poison a close enemy and kill them instantly 
				else if (panel.power.equals("poison")) {
		
					if (poisonUsed) {
						panel.powerUsed = false;
						panel.power = "none";
						poisonUsed = false;
					}
				}
				
			}
		}
		// checks if the player is dead, if so, skip turn:
		else if (this.num == panel.heroTurn && panel.turn == 1 && this.isDead) {
			panel.changeTurns();
		}
		
		if (!(this.isDead))
			g2.drawImage(image, this.x, this.y, null);	
	}
	
	public void update () {
		spriteCounter ++;
		if (spriteCounter > 20) {
			if (spriteNum == 1)
				spriteNum = 2;
			else
				spriteNum = 1;
			
			spriteCounter = 0;
		}
	}
}
