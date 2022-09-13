package main;

import java.awt.*;

public class DiceEvent {

	
	private int diceNum;  // the dice number that was rolled
	private String card;
	private Image cardImg; 
	
	// stores the card types
	String [] cards = {"heal", "block", "blank", "spear", "poison", "blank"};
	Image [] cardImgs = {Toolkit.getDefaultToolkit().getImage("dice/healCard.png"), Toolkit.getDefaultToolkit().getImage("dice/blockCard.png"), Toolkit.getDefaultToolkit().getImage("dice/blankCard.png"), Toolkit.getDefaultToolkit().getImage("dice/spearCard.png"), Toolkit.getDefaultToolkit().getImage("dice/poisonCard.png"), Toolkit.getDefaultToolkit().getImage("dice/blankCard.png")};  
	
	
	// constructor: creates a dice event
	public DiceEvent () {
		this.diceNum = (int) ((Math.random() * (6 - 1+1)) + 1);
		this.cardImg = cardImgs [diceNum-1];
	}
	
	// getter methods:
	public int getDiceNum() {
		return diceNum;
	}
	
	public Image [] getCardImgs() {
		return cardImgs;
	}
	
	public Image getCardImg () {
		return cardImg;
	}
}
