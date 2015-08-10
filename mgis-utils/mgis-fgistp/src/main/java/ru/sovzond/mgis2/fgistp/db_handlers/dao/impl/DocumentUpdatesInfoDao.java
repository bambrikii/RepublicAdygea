package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DaoBase;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.DocumentUpdatesInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class DocumentUpdatesInfoDao extends DaoBase<DocumentUpdatesInfo> {
	private static final String TABLE_NAME = "ENT_UPDATE2";

	public DocumentUpdatesInfoDao(DataSourceContainer dataSourceContainer) {
		super(dataSourceContainer);
	}

	@Override
	public void create(DocumentUpdatesInfo obj) throws SQLException {
		// TODO:
		Long nextId = 0l;
		Long parentId = 0l;
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("INSERT INTO " + TABLE_NAME + " (id, name, parent_id, NUM_KAR, REG_NUM, DAT_REG, OTV_LIC)" +
					" VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, nextId);
			stmt.setNString(2, obj.REG_NUM);
			stmt.setLong(3, parentId);
			stmt.setLong(4, obj.NUM_KAR);
			stmt.setNString(5, obj.REG_NUM);
			stmt.setDate(6, new java.sql.Date(obj.DAT_REG.getTime()));
			stmt.setNString(7, obj.OTV_LIC);
			stmt.execute();
		});
	}

	@Override
	public void update(DocumentUpdatesInfo obj) throws SQLException {
		// TODO:
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("UPDATE " + TABLE_NAME + " SET OSN_SV =? WHERE id = ?");
			stmt.setLong(1, obj.OSN_SV.id);
			stmt.setLong(2, obj.id);
			stmt.execute();
		});
	}
}
