package ppPackage;

import java.awt.Color;



public class ppPaddleAgent extends ppPaddle{
	//Declaring variables
	private ppBall myBall;
	
	/**
	 * A constructor that creates the agent with a specific color and adding it to the screen. The initial coordinates is set by the X and Y arguments of the constructor
	 * @param X
	 * @param Y
	 * @param myColor
	 * @param myTable
	 */
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable) {
		super(X,Y,myColor,myTable);
		
	}
	
	public void run() 
	{
		while (true) 
		{
			//Calculating the velocity of the paddle by calculating the differences in Y's and X's in a given time
			
			this.setY(myBall.getY());
			
		}
	}
	
	
	/**
	 * Sets the value of the myBall instance variable in ppPaddleAgent.
	 * @param myBall
	 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}
