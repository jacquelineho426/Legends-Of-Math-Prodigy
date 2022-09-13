package Battle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.*;

import Entity.Player;
import World.GameMap;
import main.Game;
import main.Main;

public class Battle extends JPanel implements Runnable, MouseListener{
	
	// global variables 
	public String state;
	public ArrayList <Hero> heroList = new ArrayList <>(); // stores all the heros the user can choose from 
	public LinkedList <Hero> heroes = new LinkedList <>(); // stores the  3 heros on the battlefield
	public LinkedList <Enemy> enemies = new LinkedList <>(); // stores the 2 enemies on the battlefield
	public JLabel target; // keeps track of the current target enemy the user has selected
	static JFrame frame; 
	static Battle panel;
	public Hexagon clickedHex;
	public boolean hexPressed = false;
	public boolean findTarget = true;
	public boolean displayHexagons = true;
	public String power = "none";
	public boolean powerUsed = false;
	public boolean [] specialPowers = new boolean[6];
	HashMap <JLabel, Hexagon> labels = new HashMap <>(); // stores all the JLabels on the battlefield and their corresponding hexagond
	HashMap <Hero, JLabel> heroLabel = new HashMap <>();
	Queue <Profile> profiles = new LinkedList <>();


	public Image board = Toolkit.getDefaultToolkit().getImage("images/dumbleInfo.png");
	public Image p1 = Toolkit.getDefaultToolkit().getImage("images/null.png"); // represents player 1
	public Image p2 = Toolkit.getDefaultToolkit().getImage("images/null.png"); // represents player 2
	public Image p3 = Toolkit.getDefaultToolkit().getImage("images/null.png"); // represents player 3
	public int selectedPlayer = 0;
	public int choosePlayer = 2; 
	
	public int turn = 1; // keeps track of the turn: 1 --> hero 2 --> enemy
	int heroTurn = 1;
	int heroMove = 1;
	int enemyTurn = 4;
	
	boolean finalBattle = false;
	Hexagon [][] hexagons = new Hexagon [7][9]; // keeps track of all the hexagons on the battlefield (as shown below)
	
	//    0 1 2 3 4 5 6 7 8
	
	// 0  9 9 9 9 9 9 9 9 9 
	// 1  9 0 2 0 0 0 0 5 9 
	// 2  0 0 0 0 0 0 0 0 9 
	// 3  0 0 0 1 0 0 4 0 9 
	// 4  0 0 0 0 0 0 0 0 9 
	// 5  9 0 3 0 0 0 0 6 9 
	// 6  9 0 0 0 0 0 0 0 9
	
	// stsrting coordinates of the players/enemies on the screen
	int [][] startingPos = {{350, 220}, {250, 60}, {250, 370}, {650, 255}, {750, 95}, {750, 405}};
	int [][] startHexCoords = {{3, 3}, {1, 2}, {5, 2}, {3, 6}, {1, 7}, {5, 7}};
	int [][] naCoords = {{0, 0}, {1, 0}, {5, 0}, {6, 0}, {6, 8}, {0, 1}, {0, 2}, {0,3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {0, 8}} ;

	Thread bThread;

	// Constructor: creates the JPanel for the battle
	public Battle () {

		finalBattle = false;
		setPreferredSize(new Dimension (1000 , 600));
		addMouseListener(this);
		
		// creates the Hexagon objects for the hex-based tile on the grid
		// each hexagon has a state: 0 if empty, 9 if out of bounds, or a number that corresponds to the player that is on it
		// row
		for (int i = 0; i < 7; i++) {
			// column
			for (int j = 0; j < 9; j++) {
				Hexagon hexagon;
				if (i == 0|| ((i == 1 || i == 5) && j == 0) || (i == 6 && j == 0) || j == 8) {
					hexagon = new Hexagon(100 + 100*j, 15 + 80*i, this, i, j, 9);
				}
				else {
					if (i%2 == 0) 
						hexagon = new Hexagon(100 + 100*j, 15 + 80*i, this, i, j, 0);
					else
						hexagon = new Hexagon(50 + 100*j, 15 + 80*i, this, i, j, 0);	
				}
				
				hexagons [i][j] = hexagon;
			}
		}
		
		// creates 3 hero and 3 enemy objects for the battle at the starting positions
		for (int i = 0; i < 3; i++) {
			Hero hero = new Hero(startingPos[i][0], startingPos[i][1], this, hexagons [startHexCoords[i][0]][startHexCoords[i][1]], (1+i), 50, "dumble");
			Enemy enemy = new Enemy(startingPos[i+3][0], startingPos[i+3][1], this, hexagons [startHexCoords[i+3][0]][startHexCoords[i+3][1]], (4+i));
			heroes.add(hero);
			enemies.add(enemy);
		}
		
		// loads the charactersitics of each type of hero the user can choose to play with
		String [] types = {"wizard", "knight", "elf", "prince"};
		String [] names = {"dumble", "gerald", "august", "romeo"};
		int [] levels = {10, 5, 7, 3};
		int [] lives = {4, 3, 4, 2};
		int [] attackAmounts = {50, 50, 20, 25};
		Image [] icons = {Toolkit.getDefaultToolkit().getImage("images/wizardIcon.png"), Toolkit.getDefaultToolkit().getImage("images/knightIcon.png"), Toolkit.getDefaultToolkit().getImage("images/elfIcon.png"), Toolkit.getDefaultToolkit().getImage("images/princeIcon.png")};
		
		for (int i = 0; i < 4; i++) {
			Hero hero = new Hero (names[i], levels[i], lives[i], attackAmounts[i], icons[i], types[i], i);
			heroList.add(hero);
		}
		
		specialPowers = Player.specialPowers;
		state = "start";
		bThread = new Thread (this);
		bThread.start();
	}
	
	// resets all the values to its original state for a new battle
	// no parameters or return type
	public void resetValues() {
		turn = 1; 
		heroTurn = 1;
		heroMove = 1;
		enemyTurn = 4;	
		hexPressed = false;
		findTarget = true;
		displayHexagons = true;	
		state = "start";
		p2 = Toolkit.getDefaultToolkit().getImage("images/null.png");
		p3 = Toolkit.getDefaultToolkit().getImage("images/null.png");
		selectedPlayer = 0;
		choosePlayer = 2; 
		specialPowers = new boolean [4];
		power = "none";
		powerUsed = false;
		
		int index = 0;
		for (int i = 0; i < 6; i++) {
			if (i != 2 && i != 5) {
				specialPowers [index] = Player.specialPowers[i];
				index++;
			}
		}
		
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 0|| ((i == 1 || i == 5) && j == 0) || (i == 6 && (j == 0 || j == 8))) {
					hexagons [i][j].setState(9);
				}
				else {
					hexagons[i][j].setState(0);
				}
			}
		}
				
		int i = 0;
		for (Hero hero : heroes) {
			hero.resetValues(startingPos[i][0], startingPos[i][1], hexagons [startHexCoords[i][0]][startHexCoords[i][1]], 50);
			i++;
		}
		
		int j = 0;
		for (Enemy enemy : enemies) {
			enemy.resetValues(startingPos[j+3][0], startingPos[j+3][1], hexagons [startHexCoords[j+3][0]][startHexCoords[j+3][1]]);
			j++;
		}
	}

	public void paintComponent (Graphics g) {
		
		if (state.equals("start")) {
			Image background = Toolkit.getDefaultToolkit().getImage("images/qBackground.png");
			Image sideBar = Toolkit.getDefaultToolkit().getImage("images/sideBar.png");
			Image banner = Toolkit.getDefaultToolkit().getImage("images/playerBanner.png");
			Image blankIcon = Toolkit.getDefaultToolkit().getImage("images/blankIcon.png");
			
			String name = Game.character;
			String p1Icon = "images/" + name + "Icon2.png";
			p1 = Toolkit.getDefaultToolkit().getImage(p1Icon);
			
			g.drawImage(background, 0, 0, this); 
			g.drawImage(board, 0, 0, this);
			g.drawImage(blankIcon, 0, 0, this);
			
			g.drawImage(p1, 21, 149, this);
			g.drawImage(p2, 21, 228, this);
			g.drawImage(p3, 21, 313, this);
			g.drawImage(banner, 0, 0, this);
			
			int i = 0;
			for (Hero hero : heroList) {
				g.drawImage(hero.getIcon(), 159, 174+ 75*i, this);
				i++;
			}
			g.drawImage(sideBar, 0, 0, this);
			
		}
		else {
			Graphics2D g2 = (Graphics2D) g;
			Image background = Toolkit.getDefaultToolkit().getImage("images/battleGround.png");
			Image attackButton = Toolkit.getDefaultToolkit().getImage("images/attackButton.png");
			Image endButton = Toolkit.getDefaultToolkit().getImage("images/endTurnButton.png");
			Image banner = Toolkit.getDefaultToolkit().getImage("images/banner.png");
			Image specialPowersBar = Toolkit.getDefaultToolkit().getImage("images/specialPowersBar.png");
			g.drawImage(background, 0, 0, this); 
			g.drawImage(attackButton, 10, 485, this);
			g.drawImage(endButton, 885, 485, this);
			g.drawImage(specialPowersBar, 0, 0, this);
			
			for (Hero hero : heroes) {
				hero.draw(g2);
			}
			for (Enemy enemy : enemies) {
				enemy.draw(g2);
			}
			
			for (int j = 0; j < 4; j++) {
				if (specialPowers[j]) {
					String power = "powers/" + j + ".png";
					g.drawImage(Toolkit.getDefaultToolkit().getImage(power), 0, 0, this);
				}
			}
				
			int k = 0;
			for (Profile profile : profiles) {
				if (k == 0) {
					g.drawImage(profile.getImage(), 100, 0, this);
					if (profile.getWidth() > 0)
						g.drawImage(profile.getBarImg(), 123, 83, this);
				}
				else {
					g.drawImage(profile.getImage(), 200+100*k, 0, this);
					if (profile.getWidth() > 0)
						g.drawImage(profile.getBarImg(), 223+100*k, 83, this);	
				}
				k++;
			}
			g.drawImage(banner, 200, 0, this);
			
			if (powerUsed) {
				String powerInstructions = "powers/" + power + "1.png";
				g.drawImage(Toolkit.getDefaultToolkit().getImage(powerInstructions), 0, 0, this);
			}
			
			if (state.equals("victory")) {
				Image victoryScreen = Toolkit.getDefaultToolkit().getImage("images/victoryScreen.png");
				g.drawImage(victoryScreen, 0, 0, this);
			}
			
			if (state.equals("defeat")) {
				Image endScreen = Toolkit.getDefaultToolkit().getImage("images/defeatScreen.png");
				g.drawImage(endScreen, 0, 0, this);
			}
		}
	}
	
	// changes the turn of the players 
	// if turn is 1 - hero, changes to 2  - enemy
	public void changeTurns() {
	
		int eCount = 0;
		for (Enemy enemy : enemies) {
			if (enemy.getDead()) {
				eCount++;
			}
		}
		
		int hCount = 0;
		for (Hero hero : heroes) {
			if (hero.getDead()) {
				hCount++;
			}
		}
		
		if (eCount == 3) {
			state = "victory";
			Main.battleDone = true;
		}
		
		if (hCount == 3) {
			state = "defeat";
		}
		
		else {
			// changes the turn
			if (turn == 1) {
				heroMove = 1;
				target = null;
				findTarget = true;
				displayHexagons = true;
				if (heroTurn < 3) {
					heroTurn++;
				}
				else
					heroTurn = 1;
				
				// if user activates the block power, blocks the next enemy's turn
				if (power.equals("block")) {
					turn = 1;
					powerUsed = false;
					power = "none";
				}
				else
					turn = 2;
			}
			else {
				findTarget = true;
				if (enemyTurn < 6) {		
					enemyTurn ++;
				}
				else 
					enemyTurn = 4;
					
				turn = 1;
			}
			
			profiles.offer(profiles.poll());
			powerUsed = false; 
			power = "none";
		}
	}
	
	
	// creates a hero based on the players the user chose to play with
	public void createHero (int num, int pNum) {
		String name = heroList.get(num).getName();
		System.out.println("name: "+ name);
		int attackAmt = heroList.get(num).getAttackAmt();
		int lifeCap = heroList.get(num).getLifeCap();
		String type = heroList.get(num).getType();
		heroes.get(pNum-1).defineHero(name, attackAmt, lifeCap, type);
		
	}
	
	public void update () {
		
		if (!state.equals("start")) {
			for (Hero hero : heroes) {
				hero.update();
			}
			
			for (Enemy enemy : enemies) {
				enemy.update();
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double drawInterval = 1000000000/60;
		double nextDrawTime = System.nanoTime() + drawInterval;

		while (bThread != null) {
			update();
			repaint();

			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime / 1000000;

				if (remainingTime < 0)
					remainingTime = 0;

				Thread.sleep((long)remainingTime);
				nextDrawTime += drawInterval;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		// EDIT: MIGHT BE MESSED UP CUZ OF JLABELS ON BATTLE SCREEN
		if (state.equals("start")) {
			// sort by name
			if (e.getX() >= 307 && e.getX() <= 524 && e.getY() >= 115 && e.getY() <= 156) {
				Collections.sort(heroList, new sortByName());
				board = heroList.get(selectedPlayer).getBoard();
			}
			if (e.getX() >= 602 && e.getX() <= 819 && e.getY() >= 115 && e.getY() <= 156) {
				Collections.sort(heroList, new sortByLevel());
				board = heroList.get(selectedPlayer).getBoard();
			}
			
			if (e.getX() >= 163 && e.getX() <= 240) {
				if (e.getY() >= 179 && e.getY() <= 244) {
					// click the first icon in list
					board = heroList.get(0).getBoard();
					selectedPlayer = 0;
				}
				else if (e.getY() >= 251 && e.getY() <= 319) {
					board = heroList.get(1).getBoard();
					selectedPlayer = 1;
				}
				else if (e.getY() >= 330 && e.getY() <= 396) {
					board = heroList.get(2).getBoard();;
					selectedPlayer = 2;
				}
				else if (e.getY() >= 405 && e.getY() <= 468) {
					board = heroList.get(3).getBoard();
					selectedPlayer = 3;
				}
			}
			
			if (e.getX() >= 675 && e.getX() <= 847 && e.getY() >= 500 && e.getY() <= 540) {
				if (choosePlayer == 2) {
					int i = 0;
					for (Hero hero : heroList) {
						if (hero.getType().equals(Game.character))
							break;
						else
							i++;
					}
		
					createHero(i, 1);
					
					String p2Name = "images/" + heroList.get(selectedPlayer).getType()+"Icon2.png";
					p2 = Toolkit.getDefaultToolkit().getImage(p2Name);
					createHero(selectedPlayer, 2);
					choosePlayer = 3;
				}
				else if (choosePlayer == 3) {
					String p3Name = "images/" + heroList.get(selectedPlayer).getType() + "Icon2.png";
					p3 = Toolkit.getDefaultToolkit().getImage(p3Name);
					createHero(selectedPlayer, 3);
					profiles.clear();
					for (int i = 0; i < 3; i++) {
						profiles.add(heroes.get(i).getProfile());
						profiles.add(enemies.get(i).getProfile());
					}
					for (Hero hero : heroes) {
						System.out.println(hero.getName());
					}
					state = "battle";
				}
			}
		}
		else  if (state.equals("battle")){
			
			if (turn == 1) {			
				// next turn:
				if (e.getX() >= 890 && e.getX() <= 975 && e.getY() >= 510 && e.getY() <= 562) {
					for (JLabel label : heroes.get(heroTurn-1).getAddedLabels()) {
						remove(label);
						if (labels.get(label).getState() == 7)
							labels.get(label).setState(0);
					}
					heroes.get(heroTurn-1).getAddedLabels().clear();
					changeTurns();
				}
				
				// attack button:
				if (e.getX() >= 15 && e.getX() <= 105 && e.getY() >= 510 && e.getY() <= 566) {
					if (target != null)
						heroes.get(heroTurn-1).setClickedAttack(true);
				}	
				
				
				if (e.getX() >= 9 && e.getX() <= 57) {
					// heal power
					if (e.getY() >= 182 && e.getY() <= 219) {
						if (specialPowers [0]) {
							 powerUsed = true;
							 power = "heal";
						}
					}
					// block power
					if (e.getY() >= 252 && e.getY() <= 287) {
						if (specialPowers [1]) {
							powerUsed = true;
							power = "block";
						}
					}
					// spear
					if (e.getY() >= 319 && e.getY() <= 351) {
						if (specialPowers [2]) {
							powerUsed = true;
							power = "spear";
						}
					}
					//poison
					if (e.getY() >= 388 && e.getY() <= 418) {
						if (specialPowers [3]) {
							powerUsed = true;
							power = "poison";
						}
					}
				}
			
			}	
		}
		
		// if user wins the battle
		else if (state.equals("victory")) {
			
			if (e.getX() >= 385 && e.getX() <= 606 && e.getY() >= 419 && e.getY() <= 473) {
				try {
					Main.battleDone = true;
					Main.startGame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		// if user loses the battle
		else if (state.equals("defeat")) {
			if (e.getX() >= 387 && e.getX() <= 612 && e.getY() >= 425 && e.getY() <= 478) {
				System.exit(0);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
	

