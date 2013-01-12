/**
 * 
 */
package com.opensource.orm.ibatis.sharding.executor.task;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.opensource.orm.sharding.config.DefaultConfigurationManager;

/**
 * @author luolishu
 * 
 */
public class ConcurrentQueryTaskExecutor implements QueryTaskExecutor {

	@Override
	public <E> List<E> execute(final List<QueryTask> tasks) throws SQLException {
		final List<E> results = new LinkedList<E>();
		CyclicBarrier barrier = new CyclicBarrier(tasks.size()+1, new Runnable() {

			@Override
			public void run() {
				for (QueryTask task : tasks) {
					if (task.getResults() != null) {
						results.addAll(task.getResults());
					}
				}

			}
		});
		for (QueryTask task : tasks) {
			task.setBarrier(barrier);
			DefaultConfigurationManager.getInstance().getQueryThreadPool()
					.execute(task);
		}
		try {
			barrier.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (QueryTask task : tasks) {
			if (task.sqlException != null) {
				throw task.sqlException;
			}
			if (task.runtimeException != null) {
				throw task.runtimeException;
			}
		}
		return results;
	}
}
