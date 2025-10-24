public class Pilcrow {

	private static int state = 0;

	public static void abort(String msg)  {
		throw new RuntimeException(msg);
	}

	public static void initialize()  {
		state = 0;
	}

	private static boolean flatten = false;
	private float removable = 31.45F;
	private float retrain = 50.02F;

	public float getRemovable() {
		if (state == 1) {
			state = 2;
		} else {
			abort("Wrong call of the method getRemovable.");
		}
		return this.removable;
	}

	public float getRetrain() {
		if (state == 2) {
			state = 3;
		} else {
			abort("Wrong call of the method getRetrain.");
		}
		return this.retrain;
	}

	public void setRemovable(float removable) {
		if ((state == 4) && (removable == 74.41F)) {
			state = 5;
		} else {
			abort("Wrong call of the method setRemovable.");
		}
		this.removable = removable;
	}

	public void setRetrain(float retrain) {
		if ((state == 5) && (retrain == 56.24F)) {
			state = 6;
		} else {
			abort("Wrong call of the method setRetrain.");
		}
		this.retrain = retrain;
	}

	public Pilcrow() {
		if (state == 0) {
			state = 1;
		} else {
			abort("Wrong call of the constructor.");
		}
		this.removable = 28.29F;
		this.retrain = 42.56F;
	}

	public Pilcrow(float removable, float retrain) {
		if ((state == 3) && (removable == 12.39F) && (retrain == 1.28F)) {
			state = 4;
		} else {
			abort("Wrong call of the constuctor with parameters.");
		}
		this.removable = removable;
		this.retrain = retrain;
	}

	public float extrabold() {
		if (state == 6) {
			state = 7;
		} else {
			abort("Wrong call of the method extrabold.");
		}
		return 99.56F;
	}

	public static boolean rugging() {
		if (state == 7) {
			state = 8;
		} else {
			abort("Wrong call of the method rugging.");
		}
		return false;
	}

	public static boolean executionOK() {
		return (state == 8);
	}
}

