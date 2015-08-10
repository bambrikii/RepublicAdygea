package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DaoBase;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.CommonPart;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class CommonPartDao extends DaoBase<CommonPart> {
	private static final String TABLE_NAME = "ENT_GEN_P2";

	public CommonPartDao(DataSourceContainer dataSourceContainer) {
		super(dataSourceContainer);
	}

	@Override
	public void create(CommonPart obj) throws SQLException {
		// TODO:
		Long nextId = 0l;
		Long parentId = 0l;
		getDataSource().exec(cn1 -> {
			PreparedStatement stmt = cn1.prepareCall("INSERT INTO " + TABLE_NAME + " (id, name, parent_id, KOD_DOC, DOP_NAME, D_U_P, ORG_U_D, REG_NUM)" +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, nextId);
			stmt.setNString(2, obj.KOD_DOC);
			stmt.setLong(3, parentId);
			stmt.setNString(4, obj.KOD_DOC);
			stmt.setNString(5, obj.DOP_NAME);
			stmt.setDate(6, new java.sql.Date(obj.D_U_P.getTime()));
			stmt.setNString(7, obj.ORG_U_D);
			stmt.setNString(8, obj.REG_NUM);
			stmt.execute();
		});
	}

	@Override
	public void update(CommonPart obj) throws SQLException {
		// TODO:
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("UPDATE " + TABLE_NAME + " SET OSN_SV =? WHERE id = ?");
			stmt.setLong(1, obj.OSN_SV.id);
			stmt.setLong(2, obj.id);
			stmt.execute();
		});
	}
}
