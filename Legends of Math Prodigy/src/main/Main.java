package main;
import java.awt.CardLayout;
import java.io.*;
import java.util.*;
import javax.swing.*;

import Battle.Battle;
// Jacqueline Ho
// ISU
// Ms. Wong
// January 25, 2022
// For my final culminating project, I have decided to code my very own version of Math Prodigy & Legends of Kingdom Rush. Set in a fantasy kingdom, 
// the fundamental objective of the turn-based, role-play game requires a user to complete an adventure-based quest in order to defeat the enemies in the realm. 
// During the quest, the user must traverse through a map until they reach the final node on the map: the node where the boss enemy is located, thus, resulting in 
// an ultimate face-off to determine if the user will either win or lose the game. Throughout the adventure, the user must complete various challenges and battles 
// to defeat any enemies they may encounter along the way; however, if unsuccessful in defeating the enemy, the game is ended. Each challenge or battle will incorporate
// various math problems that the user must complete before proceeding the quest. 

public class Main {
	
	// global variables:
	public static final int screenWidth = 1000;
	public static final int screenHeight = 600;
	public static JFrame frame;
	public static OpenScreen op;
	public static Game gp;
	public static Battle bp;
	public static Battle fp;
	public static JPanel panel;
	public static boolean battleDone = false; // checks if the first battle is complete
	public static boolean characterChosen = false;

	// main method: creates all the main JPanels and stores it in a Card Layout manager
	public static void main (String [] args) throws IOException {
	
		frame = new JFrame ("Legends of Math Prodigy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel = new JPanel(new CardLayout());
		
		op = new OpenScreen();
		gp = new Game();
		bp = new Battle();

		panel.add(op, "1");	
		panel.add(gp, "2");
		panel.add(bp, "3");
		
		frame.add(panel);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	// displays the main Game panel with the map
	public static void startGame() throws IOException {
		if (characterChosen) {
			gp.chooseCharacter();
			characterChosen = false;
		}
		CardLayout panelLayout = (CardLayout) panel.getLayout();
		panelLayout.show(panel, "2");
	}
	
	// displays the battle panel 
	public static void startBattle () {
		// resets the value if this is a new battle
		if (battleDone) {
			bp.resetValues();
		}
		CardLayout panelLayout = (CardLayout) panel.getLayout();
		panelLayout.show(panel, "3");
	}
	

	
}
