package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

import Entity.Player;


// Game Panel for the opening game screen
public class OpenScreen extends JPanel implements MouseListener {

	
	public String gameState = "start"; // keeps track of the state of the screen
	// start = main menu, // rules = rules screen // play = choose player screen
	public static String character = "wizard";

	public OpenScreen () {
		this.setPreferredSize(new Dimension(1000, 600));
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		if (gameState.equals("start")) {
			Image startScreen = Toolkit.getDefaultToolkit().getImage("images/startScreen.png");
			g.drawImage(startScreen, 0, 0, this);
		}
		
		else if (gameState.equals("play")) {
			Image playerScreen =  Toolkit.getDefaultToolkit().getImage("images/playerScreen.png");
			g.drawImage(playerScreen, 0, 0, this);
			drawPlayerScreen(g);
		}
		
		else if (gameState.equals("rules")) {
			Image rules = Toolkit.getDefaultToolkit().getImage("images/rules.png");
			g.drawImage(rules, 0, 0, this);
		}
		
		g2.dispose();
	}
	
	// draws the screen where the user can choose a plyer 
	public void drawPlayerScreen(Graphics g) {
		
		Image [] characters = {Toolkit.getDefaultToolkit().getImage("images/chooseWizard.png"), Toolkit.getDefaultToolkit().getImage("images/chooseKnight.png"), Toolkit.getDefaultToolkit().getImage("images/chooseElf.png") } ;
		int num = 0;		
		if (character.equals("knight"))
			num = 1;
		
		else if (character.equals("elf"))
			num = 2;
		
		g.drawImage(characters [num], 0, 0, this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		// if it is the opening screenL
		if (gameState.equals("start")) {
			// start button:
			if (e.getX() >= 384 && e.getX() <= 602 && e.getY() >= 422 && e.getY() <= 475) {
				gameState = "play";
				repaint();
			}
			// rules button:
			else if (e.getX() >= 384 && e.getX() <= 602 && e.getY() >= 502 && e.getY() <= 554) {
				gameState = "rules";
				repaint();
			}
		}
		
		// if it is the choose a player screen:
		else if (gameState.equals("play")) {
			// if user chooses wizard:
			if (e.getX() >= 205 && e.getX() <= 370 && e.getY() >= 269 && e.getY() <= 490) {
				character = "wizard";
				repaint();
			}
			
			// if user chooses the knight:
			else if (e.getX() >= 421 && e.getX() <= 588 && e.getY() >= 269 && e.getY() <= 490) {
				character = "knight";
				repaint();
			}
			
			// if user chooses the elf:
			else if (e.getX() >= 635 && e.getX() <= 801 && e.getY() >= 269 && e.getY() <= 490) {
				character = "elf";
				repaint();
			}
			
			// if user presses start game button:
			if (e.getX() >= 440 && e.getX() <= 575 && e.getY() >= 516 && e.getY() <= 555) {

				gameState = "map";
				try {
					Main.characterChosen = true;
					Main.startGame();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				repaint();
			}
		}
		
		// if user clicks rules button:
		else if (gameState.equals("rules")) {
			if (e.getX() >= 719 && e.getX() <= 904 && e.getY() >= 495 && e.getY() <= 557) {
				gameState = "start";
				repaint();
			}
		}
 		
	}
	
	// getter method: returns the state of the game
	public String getState () {
		return gameState;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
