package Examples;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcHashmapEx {


	public static void main(String[] args) {
		ConcurrentHashMap<Integer, BlockingQueue<Integer>> concurrentHashMap = new ConcurrentHashMap<>();
		int columnCount = 100;
		int rowCount = 10;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				new Thread(new CalcMatrix(i, j, concurrentHashMap)).start();
			}
		}
		for (int i = 0; i < 10; i++) {
			new Thread(new SumTask(concurrentHashMap, columnCount, i)).start();
		}
	}

	static class CalcMatrix implements Runnable {
		int w;
		int k;
		ConcurrentHashMap<Integer, BlockingQueue<Integer>> concurrentHashMap;

		CalcMatrix(int w, int k, ConcurrentHashMap<Integer, BlockingQueue<Integer>> concurrentHashMap) {
			this.w = w;
			this.k = k;
			this.concurrentHashMap = concurrentHashMap;
		}

		@Override
		public void run() {
			concurrentHashMap.putIfAbsent(w, new LinkedBlockingQueue<>());
			BlockingQueue<Integer> blockingQueue = concurrentHashMap.get(w);
			try {
				blockingQueue.put(Matrix.value(w, k));
			} catch (InterruptedException e) {
				Thread.currentThread()
					.interrupt();
				e.printStackTrace();
			}
		}
	}

	static class SumTask implements Runnable {
		ConcurrentHashMap<Integer, BlockingQueue<Integer>> concurrentHashMap;
		int columnCount;
		int rowId;

		SumTask(ConcurrentHashMap<Integer, BlockingQueue<Integer>> concurrentHashMap, int columnCount, int rowId) {
			this.concurrentHashMap = concurrentHashMap;
			this.columnCount = columnCount;
			this.rowId = rowId;
		}

		@Override
		public void run() {
			int sum = 0;
			concurrentHashMap.putIfAbsent(rowId, new LinkedBlockingQueue<>());
			BlockingQueue<Integer> blockingQueue = concurrentHashMap.get(rowId);
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
}
