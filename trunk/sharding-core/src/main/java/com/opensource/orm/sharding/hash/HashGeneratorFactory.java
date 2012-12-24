package com.opensource.orm.sharding.hash;

import com.opensource.orm.sharding.config.TableConfig;

public abstract class HashGeneratorFactory {
	static HashGenerator scriptGenerator = new ScriptHashGenerator();
	static HashGenerator defaultHashGenerator = new DefaultHashGenerator();

	public static HashGenerator create(TableConfig config) {
		if (config.getScript() != null
				&& config.getScript().trim().length() > 0) {
			return scriptGenerator;
		}
		return defaultHashGenerator;
	}
}
