package examples;


import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueExample {
	static class CalcMatrix implements Runnable {
		int w;
		int k;
		BlockingQueue<Integer> blockingQueue;

		CalcMatrix(int w, int k, BlockingQueue<Integer> blockingQueue) {
			this.w = w;
			this.k = k;
			this.blockingQueue = blockingQueue;
		}

		@Override
		public void run() {
			try {
				blockingQueue.put(Matrix.value(w, k));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

	static class SumTask implements Runnable {
		BlockingQueue<Integer> blockingQueue;
		int columnCount;

		SumTask(BlockingQueue<Integer> blockingQueue, int columnCount) {
			this.blockingQueue = blockingQueue;
			this.columnCount = columnCount;
		}

		@Override
		public void run() {
			int sum = 0;
			for (int i = 0; i < columnCount; i++) {
				try {
					sum = sum + blockingQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(sum);
		}
	}

	public static void main(String[] args) {
		ArrayList<BlockingQueue<Integer>> blockingQueueArrayList=new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			blockingQueueArrayList.add(new LinkedBlockingDeque<>());
			for (int j = 0; j < 100; j++) {
				new Thread(new CalcMatrix(i,j,blockingQueueArrayList.get(i))).start();
			}
		}
		for (int i = 0; i < 10; i++) {
			new Thread(new SumTask(blockingQueueArrayList.get(i),100)).start();
		}

	}

}
