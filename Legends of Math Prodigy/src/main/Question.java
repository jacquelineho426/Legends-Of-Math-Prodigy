package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Question {
	
	private String question;
	private String answer;
	private JLabel qText;
	private JButton addAnswer;
	private boolean used;
	Game gp;
	
	
	// constructor: creates a quesiton object which has a number and answer
	public Question (String [] questionInfo) {
		this.question = questionInfo [0];
		this.answer = questionInfo[1];
		this.used = false;
	}
	
	public void setUsed(boolean state) {
		this.used = state;
	}
	
	public String getQuestion () {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}

}
