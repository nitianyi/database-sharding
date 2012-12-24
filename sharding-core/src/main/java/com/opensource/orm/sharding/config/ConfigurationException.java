/**
 * 
 */
package com.opensource.orm.sharding.config;

/**
 * @author luolishu
 *
 */
public class ConfigurationException extends RuntimeException {

	public ConfigurationException() {
		super(); 
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ConfigurationException(String message) {
		super(message); 
	}

	public ConfigurationException(Throwable cause) {
		super(cause); 
	}

}
