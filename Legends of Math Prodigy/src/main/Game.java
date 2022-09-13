package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import Entity.Player;
import World.GameMap;
import World.Node;

// Main Game JPanel that displays the game map with all the event nodes
public class Game extends JPanel implements MouseListener, Runnable, ActionListener {
	
	// global variables:
	public static String character = "wizard"; // keeps track of the user's main player
	public boolean nodePressed = false; // keeps track of whether or not a node is pressed
	Thread gameThread;
	int fps = 60;
	Player player;
	HashMap <JLabel, Node> nodes; //  stores all the JLabels on the screen and the corresponding nodes
	LinkedList <Question> questions;
	public Node clickedNode; // stores the last clicked node of the player
	public GameMap map;
	public String state = "game"; // keeps track of the state of the game screen
	boolean drawNode = false;
	boolean correctGuess = false;
	public boolean diceRolled = false;
	public String direction; 
	Question question;
	JLabel qText;
	JButton addAnswer;
	DiceEvent de;
	public int [] availableNodes = new int [2];
	public boolean newBattle;
	int diceCounter = 0;
	int cardCounter = 1;
	Stack <Image> cardImages = new Stack <>();

	
	// constructor
	public Game() throws IOException {
		this.setPreferredSize(new Dimension(Main.screenWidth, Main.screenHeight));
		this.addMouseListener(this);
		this.setDoubleBuffered(true);
		
		createMap();
		player = new Player(this, map, character);
		gameThread = new Thread (this);
		gameThread.start();
		availableNodes [0] = 1;
		availableNodes [1] = 2;
		
		questions = new LinkedList <>();
		
		loadQuestions();
	}
	
	// loads the questions from the text file and creates a Question object for each quesiton on the file
	public void loadQuestions() throws FileNotFoundException {
		Scanner questionFile = new Scanner (new File ("easyMath.txt"));
		while (questionFile.hasNextLine()) {
			String [] questionInfo = questionFile.nextLine().split("=");
			Question question = new Question (questionInfo);
			questions.add(question);
		}
		questionFile.close();
	}

	// checks which character the user choose and sets it as the player
	public void chooseCharacter() {
		character = OpenScreen.character;
		player.setPlayerImage(character);
	}
	
	// creates the game map
	// no return type or parameters
	public void createMap () throws IOException {
		Scanner textFile = new Scanner (new File ("nodes.txt"));
		map = new GameMap(this, textFile);
		textFile.close();
	}
	
	// generates a random question for the user
	// no return type or parameters
	public void startQuestion() {
		int num = (int) ((Math.random() * (questions.size() - 0)) + 0);
		question = questions.get(num);
		state = "question";
	}
	
	// rolls a random dice on the dice event screen and displays the cards accordingly
	// no return type or parameters
	public void startDice () {
		diceRolled = false;
		diceCounter = 0;
		cardCounter = 1;
		cardImages.clear();
		for (int i = 6; i > 0; i--) {
			String cardName = "dice/card" + i + ".png";
			Image cardImg = Toolkit.getDefaultToolkit().getImage(cardName);
			cardImages.push(cardImg);
		}
		de = new DiceEvent();
		state = "dice";
	}
	
	// paint component
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		map.draw(g2);
		// if the game state is "game", the screen is displaying the map
		if (state.equals("game")) {
			Image arrows = Toolkit.getDefaultToolkit().getImage("images/arrows.png");
			
			player.draw(g2);		
			g.drawImage(arrows, 800, 450, this);
		}
		
		// if the game state is "question", the screen is displaying the question screen
		else if (state.equals("question")) {
			// EDIT: MAY CHANGE DEPENDING ON LEVEL OF DIFFICULTY
			String qImage = "easyQuestions/" + Integer.parseInt(question.getQuestion()) + ".png";
			Image questionPop = Toolkit.getDefaultToolkit().getImage(qImage);
			Image addAnswButton = Toolkit.getDefaultToolkit().getImage("images/addAnswerButton.png");
			Image giveUpButton = Toolkit.getDefaultToolkit().getImage("images/giveUp.png");
			g2.drawImage(questionPop, 0, 0, this);
			g2.drawImage(giveUpButton, 351, 430, this);
			g2.drawImage(addAnswButton, 351, 350, this);	
			
			if (correctGuess) {
				Image correct = Toolkit.getDefaultToolkit().getImage("images/correctScreen.png");
				g.drawImage(correct, 0, 0, this);
			}
			
		}
		
		// if the game state is "dice", the screen is displaying the dice event screen
		else if (state.equals("dice")) {
			Image diceEvent = Toolkit.getDefaultToolkit().getImage("dice/diceEvent.png");
			Image rollDice = Toolkit.getDefaultToolkit().getImage("dice/rollDiceButton.png");
			Image dice = Toolkit.getDefaultToolkit().getImage("dice/dice.png");
			Image card = cardImages.peek();
			
			g2.drawImage(diceEvent, 0, 0, this);
			
			if (diceRolled) {
				String diceStr = "dice/dice"+ de.getDiceNum() + ".png"; 
				dice = Toolkit.getDefaultToolkit().getImage(diceStr);
				flipCards(g);
			}	
			else {
				g2.drawImage(rollDice, 0, 0, this);
			}
			
			g2.drawImage(dice, 0, 0, this);
			g2.drawImage(card, 0, 0, this);
			
		}
		
		else if (state.equals("end")) {
			Image victoryScreen = Toolkit.getDefaultToolkit().getImage("images/endScreen.png");
			g.drawImage(victoryScreen, 0, 0, this);
		}
	}

	// flips the cards on the dice event screen depending on the number rolled from the dice
	private void flipCards(Graphics g) {
		// TODO Auto-generated method stub
		if (cardImages.size() > 0 && diceRolled && cardCounter < de.getDiceNum()) {
			diceCounter++;
			if (diceCounter > 60) {
				cardImages.pop();
				diceCounter = 0;
				cardCounter ++;
			}
		}
		else {
			cardImages.push(de.getCardImg());
		}
	}

	public void update() {
		player.update();
		map.update();	
	
	}

	public void run() {

		double drawInterval = 1000000000/fps;
		double nextDrawTime = System.nanoTime() + drawInterval;

		while (gameThread != null) {

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

	// Mouse listener methods:
	
	public void mouseClicked(MouseEvent e) {
		
		if (state.equals("question")) {
			// checks if user clicks  the add answer button
			if (e.getX() >= 359 && e.getX() <= 645 && e.getY() >= 357 && e.getY() <= 415) {
				
				String answer = JOptionPane.showInputDialog(this, "Add Answer (Enter the number value ONLY):");
				while (!correctGuess && answer != null) {
					// checks if the answer is correct
					if (answer.equalsIgnoreCase(question.getAnswer())) {
						correctGuess = true;
						repaint();
					}
					else {
						answer = JOptionPane.showInputDialog(this, "Incorrect Answer. (Enter the number value ONLY):");	
					}	
				}
			}
			
			
			if (correctGuess) {
				if (e.getX() >= 389 && e.getX() <= 606 && e.getY() >= 426 && e.getY() <= 479 ) {
					questions.remove(question);
					state = "game";
					correctGuess = false;
				}
			}
			
			else {
				if (e.getX() >= 362 && e.getX() <= 645 && e.getY() >= 436 && e.getY() <= 487) {
					System.exit(0);
				}
			}
		}
		
		else if (state.equals("dice")) {
			
			if (!diceRolled) {
				// checks if user clicks "roll dice" button
				if (e.getX() >= 592 && e.getX() <= 785 && e.getY() >= 312 && e.getY() <= 357) {

					diceRolled = true;
				}
			}
			// exits the dice event screen if the dice is rolled and user clicks "continue button"
			if (diceRolled) {
				if (e.getX() >= 592 && e.getX() <= 785 && e.getY() >= 390 && e.getY() <= 440) {
					Player.setSpecialPowers(true, de.getDiceNum());
					state = "game";
				}
			}
		}
		
	}

	public void mousePressed(MouseEvent e) {
		// checks if user is pressing down on the arrows on the map to move the map view
		if (state.equals("game")) {
			if (e.getX() >=  814 && e.getX() <= 853 && e.getY() >= 509 && e.getY() <= 535 && map.getWorldX() > 0) {
				direction = "left";
			}
			
			if (e.getX() >=  860 && e.getX() <= 887 && e.getY() >= 473 && e.getY() <= 507 && map.getWorldY() > 0) {
				direction = "up";
			}
			
			if (e.getX() >=  895 && e.getX() <= 930 && e.getY() >= 509 && e.getY() <= 535 && map.getWorldX() < 978) {
				direction = "right";
			}
			
			if (e.getX() >=  857 && e.getX() <= 891 && e.getY() >= 543 && e.getY() <= 571 && map.getWorldY() < 201) {
				direction = "down";
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		// checks if user releases their mouse from the arrows
		if (state.equals("game")) {
			if (e.getX() >=  814 && e.getX() <= 853 && e.getY() >= 509 && e.getY() <= 535 || 
				e.getX() >=  860 && e.getX() <= 887 && e.getY() >= 473 && e.getY() <= 507 ||
				e.getX() >=  895 && e.getX() <= 930 && e.getY() >= 509 && e.getY() <= 535 || 
				e.getX() >=  857 && e.getX() <= 891 && e.getY() >= 543 && e.getY() <= 571) {
					
				direction = "stop";
			}
		}	
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


	
}
