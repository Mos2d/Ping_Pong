//Assignment 3
package ppPackage;

import java.awt.Color;
import acm.graphics.GObject;
import acm.graphics.GOval;
import static ppPackage.ppSimParams.*;


public class ppBall extends Thread{

	// Instance variables

	double Xo, X, Vx; // X position and velocity variables
	double Yo, Y, Vy; // Y position and velocity variables
	
	
	private double Xinit; // Initial position of ball - X
	private double Yinit; // Initial position of ball - Y
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private boolean traceOn;
	
	private ppTable table; // Instance of ping-pong table
	private ppPaddle myPaddle; // Instance of the right paddle
	private ppPaddleAgent theAgent; //Instance of the the agent paddle
	boolean hasEnergy;
	GOval myBall; // Graphics object representing ball
	/**
	* The constructor for the ppBall class copies parameters to instance variables, creates an
	* instance of a GOval to represent the ping-pong ball, and adds it to the display.
	*
	* @param Xinit - starting position of the ball X (meters)
	* @param Yinit - starting position of the ball Y (meters)
	* @param Vo - initial velocity (meters/second)
	* @param theta - initial angle to the horizontal (degrees)
	* @param color - ball color (Color)
	* @param loss - loss on collision ([0,1])
	* @param table - a reference to the ppTable class used to manage the display
	* @param traceOn - boolean that states whether to trace the ball or not
	*/
	public ppBall(double Xinit, double Yinit, double Vo, double theta, Color color, double
	loss, ppTable table, boolean traceOn) {
	this.Xinit=Xinit; // Copy constructor parameters to instance variables
	this.Yinit=Yinit;
	this.Vo=Vo;
	this.theta=theta;
	this.loss=loss;
	this.table=table;
	this.traceOn=traceOn;
	
	
	myBall = new GOval((Xinit * SCALE), scrHEIGHT-(( Yinit+bSize )*SCALE), bSize * 2 * SCALE, bSize * 2 * SCALE);
	myBall.setFilled(true); 
	myBall.setColor(color);
	}
	/**
	 *Used to return the ball used in this thread, that is created within a ppBall instance.
	 */
	public GObject getBall() {
		return myBall;
		
	}
	
	/**
	* In a thread, the run method is NOT started automatically (like in Assignment 1).
	* Instead, a start message must be sent to each instance of the ppBall class, e.g.,
	* ppBall myBall = new ppBall (--parameters--);
	* myBall.start();
	* The body of the run method is essentially the simulator code you wrote for A1.
	*/
	public void run() {
	// Initialize simulation parameters
		
		double VoxMax=Vo*Math.cos(theta*Pi/180)*1.2; // Initial velocity components in // X and Y
		double VoyMax=Vo*Math.sin(theta*Pi/180)*1.2;
		
		double Vox=Vo*Math.cos(theta*Pi/180); // Initial velocity components in // X and Y
		double Voy=Vo*Math.sin(theta*Pi/180);
		
		double time = 0; // time (reset at each interval)
		double Vt = bMass*g /(4*Pi*bSize*bSize*k); // Terminal velocity

		double KEx=ETHR,KEy=ETHR; // Kinetic energy in X and Y directions
		double PE=ETHR; //Potential Energy
		
		
		Xo=Xinit+bSize; // Initial X offset
		Yo=Yinit; // Initial Y offset
		
		hasEnergy=true;
		
	// Main simulation loop
		while (hasEnergy) {
	    
	    
		X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));
		Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
		Vx = Vox*Math.exp(-g*time/Vt);
		Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;
		
		
		
		time += TICK; // Increment time
		
		//Collision with the ground
		if (Vy<0 && Yo+Y<=bSize) {
			KEx=0.5*bMass*Vx*Vx*(1-loss);
		    KEy=0.5*bMass*Vy*Vy*(1-loss);
		    PE=0;
		  
		    Vox=Math.sqrt(2*KEx/bMass);
		    Voy=Math.sqrt(2*KEy/bMass);
		    
		    if (Vx<0)Vox=-Vox;
		    	
		    
		   
		    time=0; // time is reset at every collision
			Xo+=X; // need to accumulate distance between collisions
			Yo=bSize; // the absolute position of the ball on the ground
			X=0; // (X,Y) is the instantaneous position along an arc,
			Y=0;// Absolute position is (Xo+X,Yo+Y).
			
			if ((KEx+KEy+PE)<ETHR) hasEnergy=false;
		    }
		
			//Paddle collision
		    if (Vx>0 && (Xo+X)>myPaddle.getX()-bSize-ppPaddleW/2) {
		    	//This if statement checks whether the ball collides with the paddle
		    	if (myPaddle.contact(Xo+X,Yo+Y))
		    	{
		    		KEx=0.5*bMass*Vx*Vx*(1-loss);
		    		KEy=0.5*bMass*Vy*Vy*(1-loss);
		    		PE=bMass*g*Y;
			    
		    		//Calculating the velocity of the ball
		    		//Adding gained energy from the paddle to change the velocity of the ball
		    		Vox=-Math.min(Math.sqrt(2*KEx/bMass)*ppPaddleXgain,VoxMax); // Scale X component of velocity
			    	Voy=Math.min(Math.sqrt(2*KEy/bMass)*ppPaddleYgain*myPaddle.getSgnVy(),VoyMax*myPaddle.getSgnVy()); // Scale Y + same dir. as paddle
			    
			    	
			    
			    	time=0; // time is reset 
			    	Xo=myPaddle.getX()-bSize-ppPaddleW/2; // Update X offsets
			    	Yo+=Y; // update Y offsets
			    	X=0; // X is reset for the next time interval
			    	Y=0;// Y is reset for the next time interval
				
		    	}   else
		    		{
		    			//if no collision the program terminates and agent score increments
		    			incAgentScore();
		    			hasEnergy=false;
		    	    }
		    }
		    
		    //Collision with the Agent
		    if (Vx<0 && (Xo+X)<=theAgent.getX()+bSize+ppPaddleW/2) {
		    	if(theAgent.contact(Xo+X,Yo+Y)) 
		    	{
		    		KEx=0.5*bMass*Vx*Vx*(1-loss);
		    		KEy=0.5*bMass*Vy*Vy*(1-loss);
		    		PE=bMass*g*Y;
			    
		    		//Calculating the velocity of the ball
		    		//Adding gained energy from the paddle to change the velocity of the ball
		    		Vox=Math.min(Math.sqrt(2*KEx/bMass)*ppAgentXgain,VoxMax); // Scale X component of velocity
		    		Voy=Math.min(Math.sqrt(2*KEy/bMass)*ppAgentYgain*theAgent.getSgnVy(),VoyMax*theAgent.getSgnVy()); // Scale Y + same dir. as paddle			    
		    		
		    		if (Vy<0)Voy=-Voy;
			   
		    		time=0; // time is reset at every collision
		    		Xo=theAgent.getX()+bSize+ppPaddleW/2; // Update X offsets
		    		Yo+=Y; // update Y offsets
		    		X=0; // X is reset for the next time interval
		    		Y=0;// Y is reset for the next time interval
		    	} 	else
	    			{
	    				//if no collision the program terminates and player score increments
		    			incPlayerScore();
	    				hasEnergy=false;
	    			}
		    }
	        // Update and display
		    myBall.setLocation(table.toScrX(Xo+X),table.toScrY(Yo+Y)); // Screen units
		    
		    table.getDisplay().agent.setText(Integer.toString(getAScore()));
		    table.getDisplay().player.setText(Integer.toString(getPScore()));
		    
		    
		    //creating the tracing balls
		    if(traceOn) {
		    	table.getDisplay().add (new GOval(table.toScrX(Xo+X) + bSize * SCALE, table.toScrY(Yo+Y) + bSize * SCALE, 1, 1));
		    }
		    // Pause display
	        table.getDisplay().pause(TICK*1000);
	        
	        if (DEBUG)
			    System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",
			    time,Xo+X,Yo+Y,Vx,Vy);
		}
		
		
	}
	
	/**
	 * Returns the Y position of the ball.
	 * @return Yo + Y
	 */
	public double getY() {
		return Yo+Y;
	}
	
	/**
	 * Checks if the ball is still in play and the game is still on.
	 * @return hasEnergy
	 */
	public boolean ballInPlay(){
		return hasEnergy;
	}
	
	/**
	 * Sets the value of the reference to the Player paddle.
	 * @param myPaddle
	 */
	public void setPaddle(ppPaddle myPaddle) {
		this.myPaddle = myPaddle;
	}
	
	/**
	 * Sets the value of the reference to the Agent paddle.
	 * @param theAgent
	 */
	public void setAgent (ppPaddleAgent theAgent) {
		this.theAgent = theAgent; 
	}
	
	/**
	 * A method that increments player score.
	 */
	public void incPlayerScore() {
		table.getDisplay().playerScore++;
	}
	
	/**
	 * A method that increments agent score.
	 */
	public void incAgentScore() {
		table.getDisplay().agentScore++;
	}
	
	/**
	 * Returns the player score.
	 * @return playerScore
	 */
	public int getPScore() {
		return table.getDisplay().playerScore;
	}
	
	/**
	 * Returns the agent score.
	 * @return playerScore
	 */
	public int getAScore() {
		return table.getDisplay().agentScore;
	}
	
}
