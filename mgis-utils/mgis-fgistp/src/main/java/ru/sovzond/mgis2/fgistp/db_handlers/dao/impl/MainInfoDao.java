package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DaoBase;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.MainInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class MainInfoDao extends DaoBase<MainInfo> {
	private static final String TABLE_NAME = "ENT_BASICS2";

	public MainInfoDao(DataSourceContainer dataSource) {
		super(dataSource);
	}

	public void create(MainInfo mainInfo) throws SQLException {

		Long nextId = 0l;
		Long parentId = 0l;

		getDataSource().exec(cn1 -> {
			PreparedStatement stmt = cn1.prepareCall("INSERT INTO " + TABLE_NAME + " (ID, OKTMO_CODE, parent_id) VALUES (?, ?, ?)");
			stmt.setLong(1, nextId);
			stmt.setNString(2, mainInfo.OKTMO_CODE);
			stmt.setLong(3, parentId);
			stmt.execute();
		});
	}

	public void update(MainInfo mainInfo) throws SQLException {
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("UPDATE " + TABLE_NAME + " SET  GEN_P1_ID = ?, SP_P1_ID = ?, UPDATE1_ID = ? WHERE ID = ?");
			stmt.setLong(1, mainInfo.GEN_P1_ID.id);
			stmt.setLong(2, mainInfo.SP_P1_ID.id);
			stmt.setLong(3, mainInfo.UPDATE1_ID.id);
			stmt.setLong(4, mainInfo.id);
			stmt.execute();
		});
	}

}
