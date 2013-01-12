/**
 * 
 */
package com.opensource.orm.sharding.config.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import com.opensource.orm.sharding.config.TableConfig;
import com.opensource.orm.sharding.config.TableDatabaseConfig;
import com.opensource.orm.sharding.config.TableDatabaseConfig.MasterConfig;
import com.opensource.orm.sharding.config.TableDatabaseConfig.SlaveConfig;
import com.opensource.orm.sharding.utils.StringUtils;
import com.opensource.orm.sharding.utils.xml.DomUtils;

/**
 * @author luolishu
 * 
 */
public class TableDatabaseConfigParser {

	public TableDatabaseConfig parse(Element e, TableConfig tableConfig) {
		TableDatabaseConfig databaseConfig = new TableDatabaseConfig();
		if (StringUtils.isNotBlank(e.getAttribute("readwrite"))) {
			databaseConfig.setReadwrite(Boolean.valueOf(e
					.getAttribute("readwrite")));
		}
		databaseConfig.setTables(this.parseTables(e, tableConfig).toArray(
				new String[] {}));
		databaseConfig.setMasters(parseMasters(e));
		databaseConfig.setSlaves(this.parseSlaves(e));
		return databaseConfig;
	}

	List<String> parseTables(Element e, TableConfig tableConfig) {
		List<String> tables = new LinkedList<String>();
		Element databaseTableConfig = DomUtils.getChildElementByTagName(e,
				"tables");
		Element namesEl = DomUtils.getChildElementByTagName(
				databaseTableConfig, "names");

		if (namesEl != null) {
			List<Element> nameList = DomUtils.getChildElements(namesEl);
			for (Element item : nameList) {
				if ("name".equalsIgnoreCase(item.getNodeName())) {
					tables.add(item.getTextContent().trim());
				}
				if ("value".equalsIgnoreCase(item.getNodeName())) {
					tables.addAll(Arrays.asList(item.getTextContent().split(
							",|;")));
				}
			}
		} else if (StringUtils.isNotBlank(databaseTableConfig
				.getAttribute("range"))) {
			String ranges[] = databaseTableConfig.getAttribute("range").split(
					"-");
			int start = Integer.valueOf(ranges[0]), end = Integer
					.valueOf(ranges[1]);
			for (int i = start; i <= end; i++) {
				tables.add(tableConfig.getTables()[i-1]);
			}
		}
		return tables;
	}

	List<MasterConfig> parseMasters(Element e) {
		List<MasterConfig> mastersList = new LinkedList<MasterConfig>();
		String mastersStr = e.getAttribute("masters");
		String mastersArr[] = mastersStr.split(",");
		for (String item : mastersArr) {
			MasterConfig master = new MasterConfig();
			String[] itemArr = item.split(":");
			master.setDataSourceId(itemArr[0]);
			if (itemArr.length > 1) {
				master.setWeights(Integer.valueOf(itemArr[1]));
			}
			mastersList.add(master);
		}
		return mastersList;
	}

	List<SlaveConfig> parseSlaves(Element e) {
		List<SlaveConfig> slavesList = new LinkedList<SlaveConfig>();
		String slavesStr = e.getAttribute("slaves");
		String slavesArr[] = slavesStr.split(",");
		for (String item : slavesArr) {
			SlaveConfig slave = new SlaveConfig();
			String[] itemArr = item.split(":");
			slave.setDataSourceId(itemArr[0]);
			if (itemArr.length > 1) {
				slave.setWeights(Integer.valueOf(itemArr[1]));
			}
			slavesList.add(slave);
		}
		return slavesList;
	}
}
