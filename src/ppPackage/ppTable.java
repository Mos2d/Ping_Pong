package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GRect;

public class ppTable {
    ppSimPaddleAgent dispRef; //initializing the graphics method that will be used in the program
    ppPaddle myPaddle; 
    /**
     * Used to create the table of the ping-pong game (ground, left wall and right wall) on the graphics display used in ppSim which will be written in the parameter.
     * @param dispRef
     */
	public ppTable(ppSimPaddleAgent dispRef) {
		
		this.dispRef = dispRef;
		
		//Creating the ground plane
		GRect ground = new GRect(0, 600, 1280, 1);
		ground.setFilled(true);
		ground.setColor(Color.black);
		dispRef.add(ground);
		
		
		
	}
	/**
	 * This utility method is used to convert world units to screen units to be used to update position of the ball and other objects in the x-coordinate
	 * 
	 * 
	 * @param X
	 * @return Screen Units of X offsets
	 */
	
	public double toScrX(double X) {
		return (int) ((X-bSize)*SCALE);	
	}
	
	/**
	 * This utility method is used to convert world units to screen units to be used to update position of the ball and other objects in the y-coordinate
	 * 
	 * 
	 * @param Y
	 * @return Screen Units of Y offsets
	 */
	public double toScrY(double Y) {
		return (int) (scrHEIGHT-(Y+bSize)*SCALE);
	}
	
	/**
	 * Converts screen X to world X
	 * @param ScrX
	 * @return X in world units
	 */
	public double ScrtoX (double ScrX) {
		return  (ScrX/SCALE+bSize);
	}
	/**
	 * Converts screen Y to world Y
	 * @param ScrY
	 * @return Y in world units
	 */
	public double ScrtoY (double ScrY) {
		
		return  ((scrHEIGHT - ScrY)/SCALE)-bSize;
	}
	
	/**
	 * Returns a reference to the object that exports the graphics methods used by this program
	 * 
	 */
	public ppSimPaddleAgent getDisplay() {
		return dispRef;
		
	}
	
	/**
	 * Removes paddles, ball, trace, and ground. Then creating a new ground.
	 */
	void newScreen() {
		dispRef.removeAll();
		GRect ground = new GRect(0, 600, 1280, 1);
		ground.setFilled(true);
		ground.setColor(Color.black);
		dispRef.add(ground);
	}
	
}

