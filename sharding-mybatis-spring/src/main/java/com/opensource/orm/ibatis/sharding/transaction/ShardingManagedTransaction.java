package com.opensource.orm.ibatis.sharding.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.transaction.Transaction;

public class ShardingManagedTransaction implements Transaction {

	final List<Transaction> transactions = new ArrayList<Transaction>();
	Transaction defaultTransaction = null;
	Transaction currentTransaction = null;

	public ShardingManagedTransaction(Transaction defaultTx) {
		currentTransaction = defaultTx;
		defaultTransaction = defaultTx;
	}

	public Connection getConnection() throws SQLException {
		return currentTransaction.getConnection();
	}

	public void commit() throws SQLException {
		for (Transaction tx : transactions) {
			tx.commit();
		}

	}

	public void setCurrentTransaction(Transaction tx) {
		this.currentTransaction = tx;
		transactions.add(tx);
	}

	public void rollback() throws SQLException {
		for (Transaction tx : transactions) {
			tx.rollback();
		}
	}

	public void close() throws SQLException {
		for (Transaction tx : transactions) {
			tx.close();
		}
	}

	public void clearTransactions() {
		transactions.clear();
	}

	public void reset() {
		currentTransaction = defaultTransaction;
	}

}
