package Battle;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Profile {

	private Image image;
	private BufferedImage bar;
	private int width;
	
	// constructor: creates a profile to keep track of the player's life and health during a battlr
	public Profile (Image image) {
		this.image = image;

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/barHealth.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.width = img.getWidth();
		this.bar = img;
				
	}
	
	// getter and setter methids:
	public Image getImage() {
		return image;
	}
	
	// this method changes the image of the profile on the screen if the user has died
	// no return type
	// parameters: takes in the type of player
	public void setImage (String type) {
		if (type.equals("enemy")) {
			this.image = Toolkit.getDefaultToolkit().getImage("images/deadEnemyProfile.png");
		}
		else {
			String profileImg = "images/dead" + type + "Profile.png";
			this.image = Toolkit.getDefaultToolkit().getImage(profileImg);
		}
		
	}
	public Image getBarImg() {
		return bar;
	}
	
	// this method changes the width of the bar on the user's profile
	// no return type
	// paramters: take sin the player's current health
	public void setWidth (int health) {
		int newWidth = (int)(((double)health / 100.00 ) * (double) this.bar.getWidth());
		this.width = newWidth;
		if (newWidth > 0) {
			// crops the length of the image to show that their health has decreased
			this.bar = this.bar.getSubimage(0, 0, newWidth, this.bar.getHeight());
		}
		else {
			this.width = 0;
		}
	}
	
	//  this method resets the width back to normal if user's health is restored
	// no return or paramaters
	public void resetWidth () {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/barHealth.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.width = img.getWidth();
		this.bar = img;
	}
	
	public int getWidth() {
		return width;
	}
	
	
}
