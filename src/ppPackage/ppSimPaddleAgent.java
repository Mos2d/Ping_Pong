package ppPackage;

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class ppSimPaddleAgent extends GraphicsProgram {
	
	//Declaring instances
	private double iYinit;
	private double iLoss;
	private double iVel;
	private double iTheta;
	private ppTable myTable;
	private ppBall myBall;
	private ppPaddle myPaddle;
	private ppPaddleAgent theAgent;
	public int playerScore=0; // Initial score of the player
	public int agentScore=0; // Initial score of the agent
	
	private JToggleButton Trace;
	public JLabel agent;
	public JLabel player;
	
	public void init() 
	{
		this.resize(scrWIDTH+OFFSET,scrHEIGHT+OFFSET);
		
		//Create 4 Jbuttons and add them
		Trace = new JToggleButton("Trace", true);
		agent = new JLabel("0");
		player = new JLabel("0");
		
		agent.setForeground(Color.blue);
		player.setForeground(Color.red);
		
		
		add(new JButton("Clear"),SOUTH);
		add(new JButton("New Serve"),SOUTH);
		add(Trace,SOUTH);
		add(new JButton("Quit"),SOUTH);
		
		add(new JLabel("Agent"),NORTH);
		add(agent,NORTH);
		add(new JLabel("Human"),NORTH);
		add(player,NORTH);
		//add(new JLabel(Integer.toString(myBall.getPScore())),NORTH);
		
		
		
		//Listeners
		addMouseListeners();
		addActionListeners();
		
		//Random number generator
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
		iLoss = rgen.nextDouble(EMIN, EMAX);
		iVel = rgen.nextDouble(VoMIN, VoMAX);
		iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);
		
		myTable = new ppTable(this);
		myBall = newBall();
		
		myPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit,ColorPaddle,myTable);
		theAgent = new ppPaddleAgent(ppAgentXinit,ppAgentYinit,ColorAgent,myTable);
		
		myBall.setPaddle(myPaddle);
		myBall.setAgent(theAgent);
		theAgent.attachBall(myBall);
		
		add(myBall.getBall()); // Each thread must be explicitly started
		theAgent.start();
		myPaddle.start();
		myBall.start();
		
		
		
	}
	
	/**
	 * This method creates an instance of a ball and then returns this instance
	 * @return The instance of the created ball
	 */
	ppBall newBall()	
	{
		
		myBall = new ppBall(XINIT,iYinit,iVel,iTheta,Color.RED,iLoss,myTable,Trace.isSelected());
		return myBall;
		
	}
	
	public void mouseMoved(MouseEvent e) {
		myPaddle.setY(myTable.ScrtoY((double)e.getY()));
		}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Clear")) {		
			myTable.newScreen();
			clearScore();
		}
		else if (command.equals("New Serve")) {
			if(!myBall.ballInPlay()) {
				
				myTable.newScreen();
				
				RandomGenerator rgen = RandomGenerator.getInstance();
				rgen.setSeed(RSEED);
				
				iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
				iLoss = rgen.nextDouble(EMIN, EMAX);
				iVel = rgen.nextDouble(VoMIN, VoMAX);
				iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);
				
				myTable = new ppTable(this);
				myBall = newBall();
				
				myPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit,ColorPaddle,myTable);
				theAgent = new ppPaddleAgent(ppAgentXinit,ppAgentYinit,ColorAgent,myTable);
				
				myBall.setPaddle(myPaddle);
				myBall.setAgent(theAgent);
				theAgent.attachBall(myBall);
				
				add(myBall.getBall()); // Each thread must be explicitly started
				theAgent.start();
				myPaddle.start();
				myBall.start();
				
				
			}
		}
		else if (command.equals("Quit")) {
		System.exit(0);
		}
	}
	
	/**
	 * This method clears the score of the game by setting both values to zero and updating the label on screen
	 */
	public void clearScore() {
		agentScore=0;
		playerScore=0;
		
		agent.setText(Integer.toString(myBall.getAScore()));
	    player.setText(Integer.toString(myBall.getPScore()));
	}
	
}
