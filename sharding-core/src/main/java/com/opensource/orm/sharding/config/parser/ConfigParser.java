/**
 * 
 */
package com.opensource.orm.sharding.config.parser;

import org.w3c.dom.Element;

/**
 * @author luolishu
 * 
 */
public interface ConfigParser<T> {

	T parse(Element e);

}
