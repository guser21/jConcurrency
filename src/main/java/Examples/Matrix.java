package Examples;



public class Matrix {
	public static int value(final int w, final int k) {
		final int a = 2 * k + 1;
		try {
			//added to show the extent of concurrency
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (w + 1) * (a % 4 - 2) * a;
	}
}
