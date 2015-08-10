package ru.sovzond.mgis2.fgistp.db_handlers.dao;

import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public abstract class DaoBase<T> {
	private DataSourceContainer dataSource;

	public DaoBase(DataSourceContainer dataSource) {
		this.dataSource = dataSource;
	}

	protected DataSourceContainer getDataSource() {
		return dataSource;
	}

	public abstract void create(T obj) throws SQLException;

	public abstract void update(T obj) throws SQLException;
}
