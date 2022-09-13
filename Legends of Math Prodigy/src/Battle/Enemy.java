package Battle;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import Battle.Battle;

public class Enemy {

	// instance variables:
	private int x;
	private int y;
	private int num;
	private Hexagon hex; // keeps track of the current hexagon the enemy is located at
	private boolean isDead; // keeps track of whether or not the enemy is dead
	private boolean makeAttack;
	private boolean removed;
	private int health;
	private Profile profile; // the profile of the enemy at the top of the screen

	Hero target; // the hero the enemy is targeting
	int hypotenuse, rise, run;
	Battle panel;
	
	private int spriteCounter = 0;
	private int spriteNum = 1;
	
	int count = 0;
	
	
	// Constructor: instantiates an enemy object for the battle
	public Enemy (int x, int y, Battle panel, Hexagon hex, int num) {
		this.x = x;
		this.y = y;
		this.num = num;
		this.hex = hex;
		this.hex.setState(num);
		this.panel = panel;
		this.isDead = false;
		this.makeAttack = false;
		this.removed = false;
		this.health = 100;
		this.profile = new Profile(Toolkit.getDefaultToolkit().getImage("images/enemyProfile.png"));
		panel.add(this.hex.getLabel());
	}
	
	// resets all the values back to its original state for a new battle
	public void resetValues(int x, int y, Hexagon hex) {
		this.x = x;
		this.y = y;
		this.hex = hex;
		this.hex.setState(this.num);
		this.isDead = false;
		this.removed = false;
		this.health = 100;
		this.profile = new Profile(Toolkit.getDefaultToolkit().getImage("images/enemyProfile.png"));
	}
	
	// getter methods:
	public int getNum() {
		return num;
	}
	
	public boolean getDead() {
		return isDead;
	}
	public Profile getProfile() {
		return profile;
	}
	// setter methpods:
	public void setDead(boolean state) {
		this.isDead = state;
	}
	
	public void setHealth (int attackAmt) {
		this.health = this.health - attackAmt;
	}
	
	public int getHealth() {
		return health;
	}
	
	
	// helps the enemy locate a target on the screen and moves it accordingly during its turn
	public void findTarget(boolean newTarget) {
		int [] shortest = new int [3];
		shortest [0] = 1000;
		
		// finds the closest hero to the enemy on the screen
		for (Hero hero : panel.heroes) {
			if (!hero.getDead()) {
				rise = (this.hex.getY() - hero.getHex().getY());
				run = (this.hex.getX() - hero.getHex().getX());
				hypotenuse = (int) Math.sqrt(Math.pow(rise, 2) + Math.pow(run, 2));
				
				if (hypotenuse < shortest[0]) {
					shortest [0] = hypotenuse;
					shortest [1] = rise;
					shortest [2] = run;
					target = hero;
				}
			}
		}
		panel.findTarget = false;

		// finds the current location of the enemy on the grid based tile
		int currentRow = this.hex.getRow();
		int currentCol = this.hex.getCol();
		
		// finds the new tile for the enemy to move to
		int [][] coords = {{currentRow+1, currentCol-1}, {currentRow+1, currentCol+1}, {currentRow+1, currentCol}, {currentRow-1, currentCol-1}, {currentRow-1, currentCol+1}, {currentRow-1, currentCol}, {currentRow, currentCol-1}, {currentRow, currentCol+1}};
		if (currentRow % 2 == 0) {
			coords [0][1] = currentCol+1;
			coords [3][1] = currentCol+1;
		}
		
		int direction = 0;
		
		if (newTarget) {
			boolean isValid = true;
			// checks if the enemy's move is valid and within boundaries
			do {
				isValid = true;
				direction = (int) (Math.random()*(7-0+1) + 0);
		
				// if enemy wants to move up, makes sure they cant move out of the screen
				if (currentRow == 1) {
					System.out.println("1");
					if (direction >= 3 && direction <= 5) {
						isValid = false;
					}
				}
				// if enemy wants to move up, makes sure they cant move out of the screen
				else if (currentRow == 6) {
					System.out.println("2");
					if (direction >= 0 && direction <= 2) {
						isValid = false;
 
					}
				}
				// if enemy wants to move left, makes sure they cant move out of the screen
				if (currentCol == 0) {
					System.out.println("3");
					if (direction == 0 || direction == 3 || direction == 6) {
						isValid = false;
					}		
				}
				// if enemy wants to move right, makes sure they cant move out of the screen
				else if (currentCol == 7) {
					System.out.println("4");
					if (direction == 1 || direction == 4 || direction == 7) {
						isValid = false;
					}
				}
			} while (!isValid);

		}
		else {
			// move enemy down
			if (shortest[1] <= -80) {
				if (currentRow != 6) {
					// move enemy left
					if (shortest [2] > 0) {
						direction = 0;
					}
					// move enemy right
					else if (shortest [2] < 0)
						direction = 1;
					// move down only
					else
						direction = 2;
				}
			}
			
			// move enemy up
			else if (shortest[1] >= 80) {
				if (currentRow != 0) {
					// move enemy left
					if (shortest [2] > 0) 
						direction = 3;
					// move enemy right
					else if (shortest [2] < 0)
						direction = 4;
					// move down only
					else
						direction = 5;
				}
			}
			else if (shortest [1] == 0) {
				// move left only
				if (shortest [2] > 0) 
					direction = 6;
				// move right only 
				else if (shortest [2] < 0)
					direction = 7;
			}
		}
		
		// checks if there is a player on the hexagon the enemy wants to move to, if so, makes an attack instead:
		for( Hero hero : panel.heroes) {
			if ((panel.hexagons[coords[direction][0]][coords[direction][1]]).equals(hero.getHex())) {
				target = hero;
				this.makeAttack = true; 
			}
		}
		if (!this.makeAttack) {
			// checks if there is another enemy on the hexagon the enemy wants to move to, if so, finds a new tile to move to
			if ((panel.hexagons[coords[direction][0]][coords[direction][1]]).getState() == 4 || (panel.hexagons[coords[direction][0]][coords[direction][1]]).getState() == 5 || (panel.hexagons[coords[direction][0]][coords[direction][1]]).getState() == 6 || (panel.hexagons[coords[direction][0]][coords[direction][1]]).getState() == 9) {
				findTarget(true);	
			}
			
			else {
				// moves the enemy to the new hexagon
				this.hex.setState(0);
				this.hex = panel.hexagons[coords[direction][0]][coords[direction][1]];
				this.hex.setState(this.num);
			}
		}
		
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
	
	public void draw (Graphics2D g2) 
	{	
		Image image = null;
		if (this.hex != null)
			panel.add(this.hex.getLabel());
		
		if (count == 150) {
			this.makeAttack = false;
			target.setHealth(target.getLifeCap());
			target.getProfile().setWidth(target.getHealth());
			
			if (target.getHealth() <= 0) {
				target.getProfile().setImage(target.getType());
				target.setDead(true);
				target.getHex().setState(0);
				target.setHex(null);
			}
			
			panel.changeTurns();
			count = 0;
		}
		
		if (this.makeAttack) {
			if (spriteNum == 1)
				image = Toolkit.getDefaultToolkit().getImage("images/attackEnemy.png");
			else
				image = Toolkit.getDefaultToolkit().getImage("images/enemy.png");
			count++;
		}

		else
			image = Toolkit.getDefaultToolkit().getImage("images/enemy.png");
		
		if (panel.turn == 2 && this.num == panel.enemyTurn && !this.isDead) {
			if (panel.findTarget) {
				findTarget(false);
			}
			
			if (!this.makeAttack) {
				if (this.x != this.hex.getX() || this.y != this.hex.getY()) {
					if (this.x != this.hex.getX()) {
						if (this.x < this.hex.getX()) {
							this.x += 5;
						}
						else {
							this.x -=5;
						}
					}
					
					if (this.y != this.hex.getY()) {
						if (this.y < this.hex.getY()) {
							this.y += 5;
						}
						else {
							this.y -= 5;
						}
					}
				}
				
				else {
					panel.changeTurns();
				}
			}
			
		}
		
		else if (panel.turn == 2 && this.num == panel.enemyTurn && this.isDead) {
			if (!this.removed) {
				this.hex.setState(0);
				this.hex = null;
				this.removed = true;				
			}
			panel.changeTurns();
		}
		
		// checks if enemy is dead, if so, removes them from the screen
		if (this.isDead && !(this.removed)) 
			image = Toolkit.getDefaultToolkit().getImage("images/enemyDead.png");	
		
		else if (!(this.isDead)) {
			g2.drawImage(this.hex.getImage(), this.hex.getX(), this.hex.getY(),  null);
			g2.drawImage(image, this.x, this.y-35, null);	
		}
		
		if (this.makeAttack) {
			if (spriteNum == 1) {
				Image attack = Toolkit.getDefaultToolkit().getImage("images/punchEffect.png");
				g2.drawImage(attack, this.x-40, this.y-35, null);
			}
		}
	}
}
