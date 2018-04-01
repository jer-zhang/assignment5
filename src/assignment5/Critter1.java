package assignment5;
/* CRITTERS Critter1.java
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

public class Critter1 extends Critter {
	
	/**
	 * Critter1 will reproduce until it can't anymore.
	 */
	@Override
	public void doTimeStep() {
		while (getEnergy() > Params.min_reproduce_energy) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * Critter1 will try to avoid fights with other Critter1's by walking away,
	 * and will fight all other critters.
	 */
	@Override
	public boolean fight(String opponent) {
		if (!opponent.equals("1")) {
			return true;
		} else {
			int dir = getRandomInt(8);
			if (look(dir, false) == null) {
				walk(dir);
			}
			return false;
		}
	}
	
	/**
	 * "1" represents a Critter1 on the map.
	 */
	@Override
	public String toString() {
		return "1";
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.TRIANGLE;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return Color.SADDLEBROWN;
	}
	
	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return Color.BLANCHEDALMOND;
	}
	

}
