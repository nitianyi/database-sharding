package com.opensource.orm.sharding.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luolishu
 * 
 */
public class TableDatabaseConfig {
	private boolean readwrite;
	private String tables[];
	private List<MasterConfig> masters = new LinkedList<MasterConfig>();
	private List<SlaveConfig> slaves = new LinkedList<SlaveConfig>();

	public boolean isReadwrite() {
		return readwrite;
	}

	public void setReadwrite(boolean readwrite) {
		this.readwrite = readwrite;
	}

	public String[] getTables() {
		return tables;
	}

	public void setTables(String[] tables) {
		this.tables = tables;
	}

	public List<MasterConfig> getMasters() {
		return masters;
	}

	public void setMasters(List<MasterConfig> masters) {
		this.masters = masters;
	}

	public List<SlaveConfig> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<SlaveConfig> slaves) {
		this.slaves = slaves;
	}

	public static class MasterConfig {
		String dataSourceId;
		int weights;

		public String getDataSourceId() {
			return dataSourceId;
		}

		public void setDataSourceId(String dataSourceId) {
			this.dataSourceId = dataSourceId;
		}

		public int getWeights() {
			return weights;
		}

		public void setWeights(int weights) {
			this.weights = weights;
		}

		@Override
		public String toString() {
			return "MasterConfig [dataSourceId=" + dataSourceId + ", weights="
					+ weights + "]";
		}

	}

	public static class SlaveConfig {
		String dataSourceId;
		int weights;

		public String getDataSourceId() {
			return dataSourceId;
		}

		public void setDataSourceId(String dataSourceId) {
			this.dataSourceId = dataSourceId;
		}

		public int getWeights() {
			return weights;
		}

		public void setWeights(int weights) {
			this.weights = weights;
		}

		@Override
		public String toString() {
			return "SlaveConfig [dataSourceId=" + dataSourceId + ", weights="
					+ weights + "]";
		}

	}

	@Override
	public String toString() {
		return "TableDatabaseConfig [readwrite=" + readwrite + ", tables="
				+ Arrays.toString(tables) + ", masters=" + masters
				+ ", slaves=" + slaves + "]";
	}

}
