package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GRect;

public class ppPaddle extends Thread {
	//Declaring variables
	private double Vx,Vy;
	double Y,lastX,lastY;
	double X;
	private ppTable myTable;
	private GRect paddle;
	
	/**
	 * A constructor that creates the paddle with a specific color and adding it to the screen. The initial coordinates is set by the X and Y arguments of the constructor
	 * @param X
	 * @param Y
	 * @param myColor
	 * @param myTable
	 */
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable) 
	{
		this.X=X;
		this.Y=Y;
		this.myTable=myTable;
		this.paddle = new GRect(myTable.toScrX(X-ppPaddleW/2),myTable.toScrY(Y+ppPaddleH/2),ppPaddleW*SCALE,ppPaddleH*SCALE);
		this.paddle.setFilled(true);
		this.paddle.setFillColor(myColor);
		myTable.getDisplay().add(paddle);
	}
	
	public void run() 
	{
		while (true) 
		{
			//Calculating the velocity of the paddle by calculating the differences in Y's and X's in a given time
			Vx=(X-lastX)/TICK;
			Vy=(Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			myTable.getDisplay().pause(TICK*TIMESCALE); // Time to mS
		}
	}
	/**
	 * 
	 * @return Returns the velocity of the paddle in the X direction (which will be 0 here as X is fixed).
	 */
	public double getVx() 
	{
		return Vx;
	}
	/**
	 * 
	 * @return Returns the velocity of the paddle in the Y direction
	 */
	public double getVy() {
		
		return Vy;
	}
	/**
	 * Sets the X position of the paddle.
	 * @param X
	 */
	public void setX(double X) {
		this.X = X;
	}
	/**
	 * Sets the Y position of the paddle.
	 * @param Y
	 */
	public void setY(double Y) {
		this.Y = Y;
		paddle.setLocation(myTable.toScrX(X+ppPaddleW/2), myTable.toScrY(Y+ppPaddleH/2));
	}
	/**
	 * 
	 * @return Returns the X position of the paddle.
	 */
	public double getX() {
		return X;
	}
	/**
	 * 
	 * @return Returns the Y position of the paddle.
	 */
	public double getY() {
		return this.Y;
	}
	/**
	 * 
	 * @returnReturns the sign of the Y velocity of the paddle.
	 */
	public double getSgnVy() {
		return (Vy<0)? -1:1; 
	}
	/**
	 * 
	 * @param Sx
	 * @param Sy
	 * @return Returns true if a surface at position (Sx,Sy) is deemed to be in contact with the paddle.
	 */
	public boolean contact (double Sx, double Sy) {
		if(Sy >= getY()-ppPaddleH/2-bSize && Sy <= getY()+ppPaddleH/2+bSize) {
			return true;
		}else {
			return false;
		}
	}

}
