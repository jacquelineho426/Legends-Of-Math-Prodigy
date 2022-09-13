package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Entity.Player;
import World.Node;

// this class creates a mouse listener for each node label on the map so the user can interact with it
public class MouseInput implements MouseListener {
	JLabel label;
	Game gp;
	// this array keeps track of the nodes that the current node of that index can reach to (0 is a placeholder if the node can only access one other node)
	int [][] nodeInfo = {{3, 0}, {4, 0}, {5, 7}, {6, 8}, {9, 0}, {8, 0}, {9, 11}, {11, 0}, {10, 12}, {12, 0}, {13, 15}, {14, 0}, {15, 0}, {16, 19}, {17, 19}, {18, 0}, {21, 0}, {22, 0}, {18, 20}, {22, 0}, {20, 0}}; 
	public MouseInput(JLabel label, Game gp) {
		this.label = label;
		this.gp = gp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (gp.state.equals("game")) {
			gp.nodes = gp.map.getNodes();
			for (int i = 0; i < 2; i++) {
				// checks if the user has reached the final node
				if (gp.nodes.get(label).getNum() == 22) {
					gp.nodePressed = true;
					gp.clickedNode = gp.nodes.get(label); 
				}
				// finds the available nodes the player can move to based on the current node
				else if (gp.nodes.get(label).getNum() == gp.availableNodes[i]) {
					gp.nodePressed = true;
					gp.clickedNode = gp.nodes.get(label); 
					gp.availableNodes [0] = nodeInfo[gp.nodes.get(label).getNum()-1][0];
					gp.availableNodes [1] = nodeInfo[gp.nodes.get(label).getNum()-1][1];
				}	
			}
		}
		// if it is a dice event screen and user clicks on a label
		if (gp.state.equals("dice")) {
			if (gp.diceRolled) {
				// checks if they click on the continue button
				if (label.getX() >= 592 && label.getX() <= 785 && label.getY() >= 390 && label.getY() <= 440) {
					Player.setSpecialPowers(true, gp.de.getDiceNum());
					gp.state = "game";
				}
			}
		
		}
		
		// if it is a quesiton screen and user clicks on a label
		if (gp.state.equals("question")) {
			// checks if they click on the add answer button
			if (label.getX() >= 359 && label.getX() <= 645 && label.getY() >= 357 && label.getY() <= 415) {
				String answer = JOptionPane.showInputDialog(this, "Add Answer (Enter the number value ONLY):");
				while (!(gp.correctGuess) && answer != null) {
					if (answer.equalsIgnoreCase(gp.question.getAnswer())) {
						gp.correctGuess = true;
						gp.repaint();
					}
					else {
						answer = JOptionPane.showInputDialog(this, "Incorrect Answer.(Enter the number value ONLY):");	
					}	
				}
			}
		}
	}

	// these mouse methods check if the user interacts with a JLabel on the map that represents a node
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (gp.state.equals("game")) {
			gp.nodes = gp.map.getNodes();
			for (int i = 0; i < 2; i++) {
				if (gp.nodes.get(label).getNum() == gp.availableNodes[i]) {
					ImageIcon image = new ImageIcon("images/node.png");
					label.setIcon(image);
				}	
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (gp.state.equals("game")) {
			ImageIcon image = new ImageIcon("images/null.png");
			label.setIcon(image);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		gp.nodes = gp.map.getNodes();
		if (gp.state.equals("game")) {
			for (int i = 0; i < 2; i++) {
				if (gp.nodes.get(label).getNum() == gp.availableNodes[i]) {
					ImageIcon image = new ImageIcon("images/node.png");
					label.setIcon(image);
				}	
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (gp.state.equals("game")) {
			ImageIcon image = new ImageIcon("images/null.png");
			label.setIcon(image);
		}
	}

}
