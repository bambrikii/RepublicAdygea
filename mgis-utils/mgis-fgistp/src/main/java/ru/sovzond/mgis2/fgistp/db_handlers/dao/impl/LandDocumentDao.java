package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DaoBase;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.LandDocument;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class LandDocumentDao extends DaoBase<LandDocument> {

	private static final String TABLE_NAME = "ENT_PRIKAZ";

	public LandDocumentDao(DataSourceContainer dataSourceContainer) {
		super(dataSourceContainer);
	}

	@Override
	public void create(LandDocument obj) throws SQLException {
		Long nextId = 0l;
		Long parentId = 0l;
		getDataSource().exec(cn1 -> {
			PreparedStatement stmt = cn1.prepareCall("INSERT INTO " + TABLE_NAME + " (id, name, parent_id, C12061, DATA, AD, FL_UL_ID2)" +
					" VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, nextId);
			stmt.setNString(2, obj.C12061);
			stmt.setLong(3, parentId);
			stmt.setNString(4, obj.C12061);
			stmt.setDate(5, new java.sql.Date(obj.DATA.getTime()));
			stmt.setNString(6, obj.AD);
			stmt.setNString(7, obj.FL_UL_ID2);
			stmt.execute();
		});
	}

	@Override
	public void update(LandDocument obj) throws SQLException {
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("UPDATE " + TABLE_NAME + " SET ... ");
			stmt.execute();
		});
	}

}
