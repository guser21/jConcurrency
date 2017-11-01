package examples;


import java.util.ArrayList;
import java.util.concurrent.*;

public class ThreadPoolExample {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ArrayList<ArrayList<Future<Integer>>> arrayLists = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			arrayLists.add(new ArrayList<>());
			for (int j = 0; j < 100; j++) {
				arrayLists.get(i)
					.add(executorService.submit(new CalTask(i, j)));
			}
		}
		for (int i = 0; i < 10; i++) {
			ArrayList<Future<Integer>> arrayList = arrayLists.get(i);
			int sum=arrayList.stream()
				.mapToInt(integerFuture -> {
					try {
						return integerFuture.get();
					} catch (InterruptedException | ExecutionException e) {
						Thread.currentThread()
							.interrupt();
						e.printStackTrace();
					}
					return -1;
				}).sum();
			System.out.println(sum);
		}
		executorService.shutdown();
		executorService.awaitTermination(10,TimeUnit.MILLISECONDS);
		System.out.println("Done");
	}

	static class CalTask implements Callable<Integer> {
		int w, k;

		CalTask(int w, int k) {
			this.w = w;
			this.k = k;
		}

		@Override
		public Integer call() throws Exception {
			return Matrix.value(w, k);
		}
	}
}
