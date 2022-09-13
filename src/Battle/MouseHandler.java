package Battle;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import Battle.Battle;
import main.Game;
import main.Main;


// this class assigns a mouse listener to each hexagon displayed on the battlefield so the user can interact with it
public class MouseHandler implements MouseListener {
	
	private JLabel label;
	Battle panel;
		
	public MouseHandler(JLabel label, Battle panel) {
		this.label = label;
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (panel.turn == 1 && panel.state.equals("battle")) {
			if (panel.labels.get(label).getState() >= 4 && panel.labels.get(label).getState() <= 6){
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/enemyHex.png"));
				panel.target = label;
			}
			else {
				panel.clickedHex = panel.labels.get(label);
				panel.hexPressed = true;
				panel.heroMove = 2;
			}
		}
		else if (panel.state.equals("start")) {
			if (panel.labels.get(label).getState() == 5) {
				Collections.sort(panel.heroList, new sortByName());
				panel.board = panel.heroList.get(panel.selectedPlayer).getBoard();
			}
			else if (panel.labels.get(label).getState() == 6) {
				// select player
				if (panel.choosePlayer == 2) {
					int i = 0;
					for (Hero hero : panel.heroList) {
						if (hero.getType().equals(Game.character))
							break;
						else
							i++;
					}
		
					panel.createHero(i, 1);
					
					String p2Name = "images/" + panel.heroList.get(panel.selectedPlayer).getType()+"Icon2.png";
					panel.p2 = Toolkit.getDefaultToolkit().getImage(p2Name);
					panel.createHero(panel.selectedPlayer, 2);
					panel.choosePlayer = 3;
				}
				else if (panel.choosePlayer == 3) {
					String p3Name = "images/" + panel.heroList.get(panel.selectedPlayer).getType() + "Icon2.png";
					panel.p3 = Toolkit.getDefaultToolkit().getImage(p3Name);
					panel.createHero(panel.selectedPlayer, 3);
					panel.profiles.clear();
					for (int i = 0; i < 3; i++) {
						panel.profiles.add(panel.heroes.get(i).getProfile());
						panel.profiles.add(panel.enemies.get(i).getProfile());
					}
					for (Hero hero : panel.heroes) {
						System.out.println(hero.getName());
					}
					panel.state = "battle";
				}
			}
		}
		
		else if (panel.state.equals("victory")) {
			try {
				Main.startGame();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (panel.turn == 1 && panel.state.equals("battle")) {
			if (panel.labels.get(label).getState() >= 4 && panel.labels.get(label).getState() <= 6) {
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/enemyHex.png"));
			}
			else
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/chosenHex.png"));
		}
	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (panel.turn == 1 && panel.state.equals("battle")) {
			if (!(panel.labels.get(label).getState() >= 4 && panel.labels.get(label).getState() <= 6))
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/hexagon.png"));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if (panel.turn == 1 && panel.state.equals("battle"))
			if (panel.labels.get(label).getState() >= 4 && panel.labels.get(label).getState() <= 6) {
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/enemyHex.png"));
			}
			else 
				panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/chosenHex.png"));
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if (panel.turn == 1 && !(label.equals(panel.target)) && panel.state.equals("battle")) {	
			panel.labels.get(label).setImage(Toolkit.getDefaultToolkit().getImage("images/hexagon.png"));
		}
			
	}

}
