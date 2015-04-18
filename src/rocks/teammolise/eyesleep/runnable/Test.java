package rocks.teammolise.eyesleep.runnable;

import rocks.teammolise.eyesleep.Controller;

public class Test {
	public static void main(String[] args) throws Exception {
		Controller ctr = new Controller(20, 20, 20, 1, 3);
		ctr.sleep();
	}
}
