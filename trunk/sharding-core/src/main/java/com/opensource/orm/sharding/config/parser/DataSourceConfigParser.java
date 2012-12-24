/**
 * 
 */
package com.opensource.orm.sharding.config.parser;

import java.util.List;
import java.util.Properties;

import org.w3c.dom.Element;

import com.opensource.orm.sharding.config.DataSourceConfig;
import com.opensource.orm.sharding.utils.xml.DomUtils;

/**
 * @author luolishu
 * 
 */
public class DataSourceConfigParser implements ConfigParser<DataSourceConfig> {

	public DataSourceConfig parse(Element e) {
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setId(e.getAttribute("id"));
		dataSourceConfig.setRef(e.getAttribute("ref"));
		dataSourceConfig.setParent(e.getAttribute("parent"));
		dataSourceConfig.setAbstract("true".equalsIgnoreCase(e
				.getAttribute("abstract")));
		List<Element> childs = DomUtils.getChildElements(e);
		Properties properties = new Properties();
		dataSourceConfig.setProperties(properties);
		for (Element item : childs) {
			properties.put(item.getNodeName(), item.getTextContent().trim());
			if ("url".equalsIgnoreCase(item.getNodeName())) {
				dataSourceConfig.setUrl(item.getTextContent().trim());
			}
			if ("user".equalsIgnoreCase(item.getNodeName())) {
				dataSourceConfig.setUser(item.getTextContent().trim());
			}
			if ("password".equalsIgnoreCase(item.getNodeName())) {
				dataSourceConfig.setPassword(item.getTextContent().trim());
			}
		}

		return dataSourceConfig;
	}

}
