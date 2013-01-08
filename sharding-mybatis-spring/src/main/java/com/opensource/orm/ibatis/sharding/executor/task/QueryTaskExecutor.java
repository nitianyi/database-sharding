package com.opensource.orm.ibatis.sharding.executor.task;

import java.sql.SQLException;
import java.util.List;

/**
 * @author luolishu
 *
 */
public interface QueryTaskExecutor {

	public <E>List<E> execute(List<QueryTask> tasks) throws SQLException;
}
