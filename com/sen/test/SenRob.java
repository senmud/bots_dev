package com.sen.test;
import java.awt.Color;

import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * SenRob - a robot by (your name here)
 */
public class SenRob extends AdvancedRobot
{
	private double curFirePower = 1.0, curAccurate = 0.5;
	private boolean curNeedFire = false;

	private boolean curNeedEsc = false, curNeedScan = true;

	static private int counter = 0;
	/**
	 * run: SenRob's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		setColors(Color.green, Color.green, Color.red);

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			doMove();
			doSearchTarget();
			doFire();
			doEsc();
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		curNeedFire = measureTarget(e);

		if (curAccurate <= 0.1) {
			curFirePower = Rules.MIN_BULLET_POWER;
		}else if (curAccurate <= 0.3) {
			curFirePower = Rules.MIN_BULLET_POWER + (Rules.MAX_BULLET_POWER-Rules.MIN_BULLET_POWER)/3;
		} else if (curAccurate >= 0.6 && curAccurate < 0.9) {
			curFirePower = Rules.MIN_BULLET_POWER + (Rules.MAX_BULLET_POWER-Rules.MIN_BULLET_POWER)*2/3;
		} else if (curAccurate >= 0.9) {
			curFirePower = Rules.MAX_BULLET_POWER;
		}

		curNeedScan = false;
		measureRadar(e);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//curNeedEsc = true;
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		curNeedEsc = true;
	}
	
	private void doMove() {
		setAhead(5);
	}

	private void doSearchTarget() {
		//if (counter++ % 10 == 0 && (curNeedScan == true || getRadarTurnRemaining() == 0)) {
		//	curNeedScan = true;
		//	setTurnRadarLeft(60);
		//}
		setTurnRadarLeft(360);
	}

	private void doFire() {
		if (curNeedFire == true) {
			setFire(curFirePower);
		}

		curNeedFire = false;
	}

	private void doEsc() {
		if (curNeedEsc == true) {
 			//setBack(5);
			setTurnLeft(90);
		}
		curNeedEsc = false;
	}

	private boolean measureTarget(ScannedRobotEvent e) {
		double gunturn = turnRate(measureDirect(e.getBearing(), getGunHeading()));
		if (getGunTurnRemaining() <= 5) {
			setTurnGunRight(gunturn);
		}

		if (e.getDistance() < 30) {
			curAccurate = 0.9;
		} else if (e.getDistance() < 200) {
			curAccurate = 0.6;
		} else if (e.getDistance() < 600) {
			curAccurate = 0.3;
		} else {
			return false;
		}

		if (gunturn >= -5 && gunturn <= 5) {
			return true;
		}
		return false;
	}

	private void measureRadar(ScannedRobotEvent e) {
		double radarturn = turnRate(measureDirect(e.getBearing(), getRadarHeading()));
		if (radarturn > 0) {
			setTurnRadarRight(radarturn);
		} else if (radarturn < 0) {
			setTurnRadarLeft(radarturn);
		}
	}

	private double measureDirect(double bearing, double heading) {
		//double gunleft = getGunTurnRemaining();
		//double gunhead = getGunHeading();

		double enemydirect = getHeading() + bearing;
		enemydirect = enemydirect >= 360 ? enemydirect - 360 : enemydirect;
		enemydirect = enemydirect < 0 ? enemydirect + 360 : enemydirect;

		double turn = enemydirect - heading;
		if (turn < -180) {
			turn += 360;
		} else if (turn > 180) {
			turn = -(360 - turn);
		}

		if (turn >= 180 || turn <= -180) {
			return 0;
		}
		return turn;
	}

	private double turnRate(double turn) {
		/*
		if (turn >= 60) {
			return 10;
		} else if (turn >= 20) {
			return 5;
		} else if (turn >0) {
			return 2;
		} else if (turn <= -60) {
			return -10;
		} else if (turn <= -20) {
			return -5;
		} else if (turn < 0) {
			return -2;
		}*/

		return turn;
	}
}
