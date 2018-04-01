package assignment5;
/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Jerry Zhang
 * jz9954
 * 15465
 * Celine Lillie
 * Cml3665
 * 15460
 * Slip days used: 0
 * Spring 2018
 */

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter2 extends Critter {
	
	private int dir;
	
	public Critter2() {
		
	}
	
	/**
	 * Critter2 will only walk horizontally or vertically. It has an 80% chance to try to reproduce and
	 * make a Critter2. It has a .1% chance of reproducing and making a Critter1.
	 */
	public void doTimeStep() {
		dir = 2 * getRandomInt(4);
		walk(dir);
		int rand = getRandomInt(1000);
		if (rand > 200) {
			Critter2 child = new Critter2();
			reproduce(child, getRandomInt(8));
		}
		if (rand == 0) {
			Critter1 child = new Critter1();
			reproduce(child, getRandomInt(8));
		}
	}
	
	/**
	 * Critter2 will fight if it is walking horizontally, otherwise it will try to
	 * walk away.
	 */
	public boolean fight(String opponent) {
		if (dir == 0 || dir == 4) {
			return true;
		} else {
			int rand = getRandomInt(8);
			if(look(rand, false) == null) {
				walk(rand);
			}
			return false;
		}
	}
	
	/**
	 * "2" represents Critter2 on the map.
	 */
	public String toString() {
		return "2";
	}
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return Color.DARKMAGENTA;
	}
	
	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return Color.PLUM;
	}
}
