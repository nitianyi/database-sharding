package com.opensource.orm.sharding;

public class ShardingNotSupportException extends Exception {
	private static final long serialVersionUID = 9015671953615804636L;

	public ShardingNotSupportException() {
		super();
	}

	public ShardingNotSupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShardingNotSupportException(String message) {
		super(message);
	}

	public ShardingNotSupportException(Throwable cause) {
		super(cause);
	}

}
