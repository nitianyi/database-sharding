/**
 * 
 */
package com.opensource.orm.sharding.config.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import com.opensource.orm.sharding.config.ConfigurationException;
import com.opensource.orm.sharding.config.TableConfig;
import com.opensource.orm.sharding.config.TableDatabaseConfig;
import com.opensource.orm.sharding.utils.StringUtils;
import com.opensource.orm.sharding.utils.xml.DomUtils;

/**
 * @author luolishu
 * 
 */
public class TableConfigParser implements ConfigParser<TableConfig> {
	TableDatabaseConfigParser databaseParser = new TableDatabaseConfigParser();

	public TableConfig parse(Element e) {
		TableConfig tableConfig = new TableConfig();
		tableConfig.setName(e.getAttribute("name"));
		this.parseTableNames(e, tableConfig);
		this.parseHash(e, tableConfig);
		this.parseDatabaseConfig(e, tableConfig);
		return tableConfig;
	}

	private void parseTableNames(Element e, TableConfig tableConfig) {
		Element namesEl = DomUtils.getChildElementByTagName(e, "names");
		List<String> tables = new LinkedList<String>();
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
		} else if (StringUtils.isNotBlank(e.getAttribute("length"))) {
			int length = Integer.valueOf(e.getAttribute("length"));
			for (int i = 0; i < length; i++) {
				tables.add(tableConfig.getName() + (i + 1));
			}
		} else {
			throw new ConfigurationException(
					"configuration Error!table names is not right!table name="
							+ tableConfig.getName());
		}
		tableConfig.setTables(tables.toArray(new String[] {}));
	}

	private void parseHash(Element e, TableConfig tableConfig) {

		Element hashColumns = DomUtils.getChildElementByTagName(e,
				"hashcolumns");
		String generator = hashColumns.getAttribute("generator");
		if (generator != null && generator.trim().length() > 0) {
			tableConfig.setGenerator(generator);
		} else {
			tableConfig.setGenerator("default");
		}
		List<Element> columnElList = DomUtils.getChildElementsByTagName(
				hashColumns, "column");
		List<String> columns = new LinkedList<String>();
		for (Element item : columnElList) {
			columns.add(item.getTextContent().trim());
		}
		tableConfig.setColumns(columns.toArray(new String[] {}));
		Element scriptEl = DomUtils.getChildElementByTagName(hashColumns,
				"script");
		if (scriptEl != null) {
			tableConfig.setScript(scriptEl.getTextContent());
		}
	}

	private void parseDatabaseConfig(Element e, TableConfig tableConfig) {
		Element databaseConfigEl = DomUtils.getChildElementByTagName(e,
				"database-config");
		List<Element> childs = DomUtils.getChildElements(databaseConfigEl);
		List<TableDatabaseConfig> databaseList = new LinkedList<TableDatabaseConfig>();
		for (Element child : childs) {
			databaseList.add(databaseParser.parse(child, tableConfig));
		}
		tableConfig.setDatabaseConfigs(databaseList);
	}

}
