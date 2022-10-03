package IS;
import robocode.*;
import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * TopGun - a robot by Nate Chandler, Tanner Bacon, Yun-Chen Lee, and Bryson Barrow
 */
public class TopGun extends Robot
{
	boolean forward = true;
	/**
	 * run: TopGun's default behavior
	 */
	public void run() {
		// Set color and ensure that the gun will turn with the robot
		setColors(Color.black,Color.darkGray,Color.blue); // body,gun,radar -- Set the colors
		setAdjustRadarForRobotTurn(false); // Keep the radar still when bot turns
        setAdjustGunForRobotTurn(false); // Keep the gun still when bot turns
		int turn = 1;

		// Robot main loop
		while(true) {
		
			// Every other loop we will change directions
			if (turn > 0) {
				ahead(100);
				forward = true;
				turnLeft(30);
	            turnGunLeft(180);
			} else {
            	back(60);
				forward = false;
	            turnRight(30);
	            turnGunRight(180);
			}	

			// Alternate turn variable
			turn *= -1;
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double Energy =  e.getEnergy(); // Gets the energy of TopGun
		double Distance = e.getDistance(); // Gets the dstance of the scanned robot		
		
		// If we see a robot moving slowly, we will shoot twice
		if (e.getVelocity() <= 1.5) {
			fire(5);
			fire(5);
		} 

		if (Energy > 50) // If the energy level is over 50 do this
		{ 
			if (Distance < 50) // If enemy is less than 50 away do this
			{
				fire(5);
			}
			else if (Distance < 100) // If enemy is less than 100 away do this
			{
				fire(4);
			}
			else if (Distance < 150) // If enemy is less than 150 away do this
			{
				fire(3);
			}
			else if (Distance < 200) // If enemy is less than 200 away do this
			{
				fire(3);
			}	
			else
			{
				fire(2); // If enemy is over 200 away do this
			}
		}
		else if (Energy > 25) // If the energy level is < 50 and > 25
		{
			fire(3);
		}
		else // If energy is less than 25
		{
			if (Distance < 100) {
				fire(3);
			}
			else {
				fire(1);
			}
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {

		// Change body color to indicate the power of the bullet
		if (e.getPower() >= 3) {
			setBodyColor(Color.red);
		} else if (e.getPower() >=2) {
			setBodyColor(Color.orange);
		} else {
			setBodyColor(Color.yellow);
		}
		
		// The robot will only attempt to run from the bullet if it is far enough from the wall that it won't trap itself
		if (!nearWall()) { 

			// This will evade by turning and then moving if the bullet comes from the front or the back
			if ((e.getBearing() > -50 && e.getBearing() < 50) || (e.getBearing() < -130 && e.getBearing() > 130)) {
				// if the bullet comes from the front then turn right, evade, and turn left to scan the robot
				if (e.getBearing() > -50 && e.getBearing() < 50) {
					turnRight(60);
					ahead(150);
					forward = true;
				} else { // if the bullet was from behind then turn right, evade, and keep turning right to scan the robot
					turnRight(120);
					ahead(50);
					forward = true;
				}
			} else { // if the hit came from the side, move forward first and then turn to scan the robot
				ahead(150);
				forward = true;
				if (e.getBearing() < -50 && e.getBearing() > -130) {
					turnLeft(e.getBearing() + 45);
				} else {
					turnRight(e.getBearing() + 45);
				}
				back(20);
				forward = false;
			}
		}
		setBodyColor(Color.black);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		turnRight(20);		

		if (forward) { 	//if moving forward bounce off the wall
			back(200);
			forward = false;
			turnRight(8);
		} else{
			ahead(200); //if moving backward bounch off the wall
			forward = true;
			turnRight(8);
		}
	}	
	
	public void onWin(WinEvent e) { // Victory dance
		for (int i = 0; i < 50; i++) {
			turnGunRight(360);
			if (i % 0 == 0){
				setBodyColor(Color.green);
			} else {
				setBodyColor(Color.orange);
			}
			turnRight(40);
			if (i % 0 == 0){
				setBodyColor(Color.yellow);
			} else {
				setBodyColor(Color.red);
			}
		}
	}

	// This function will test if the robot is within 100 distance of the wall.
	public boolean nearWall() {
		if(forward){
		    if(getX() + 100 >= getBattleFieldWidth() && getHeading() > 270 - 45 && getHeading() < 270 + 45)
		    return true;
		    if(getX() - 100 <= 0 && getHeading() > 45 && getHeading() < 135)
		    return true;
		    if(getY() + 100 >= getBattleFieldHeight() && getHeading() > 180 - 45 && getHeading() < 180 + 45)
		    return true;
		    if(getY() - 100 <= 0 && getHeading() > 360 - 45 && getHeading() < 45)
		    return true;
		    }
		else{
			if(getX() + 100 >= getBattleFieldWidth() && getHeading() > 45 && getHeading() < 135)
			return true;
			if(getX() - 100 <= 0 && getHeading() > 270 - 45 && getHeading() < 270 + 45)
			return true;
			if(getY() + 100 >= getBattleFieldHeight() && getHeading() > 360 - 45 && getHeading() < 45)
			return true;
			if(getY() - 100 <= 0 && getHeading() > 180 - 45 && getHeading() < 180 + 45)
			return true;
		}
			return false;
	}			
}