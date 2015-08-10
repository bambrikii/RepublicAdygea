package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DaoBase;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.SpecialPart;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class SpecialPartDao extends DaoBase<SpecialPart> {

	private static final String TABLE_NAME = "ENT_SP_P2";

	public SpecialPartDao(DataSourceContainer dataSource) {
		super(dataSource);
	}

	@Override
	public void create(SpecialPart obj) throws SQLException {
		// TODO:
		Long nextId = 0l;
		Long parentId = 0l;
		getDataSource().exec(cn1 -> {
			PreparedStatement stmt = cn1.prepareCall("INSERT INTO " + TABLE_NAME + " (id, name, parent_id, KOD_D_R, KOD_D_T_P, FORM_PR, MAS, NUM_KART, CADASTR, NUM_P_ED, ID_NUM_K, COMP_DOC, OSN_SV )" +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, nextId);
			stmt.setNString(2, obj.CADASTR);
			stmt.setLong(3, parentId);
			stmt.setNString(4, obj.KOD_D_R);
			stmt.setNString(5, obj.KOD_D_T_P);
			stmt.setNString(6, obj.FORM_PR);
			stmt.setNString(7, obj.MAS);
			stmt.setInt(8, obj.NUM_KART);
			stmt.setNString(9, obj.CADASTR);
			stmt.setNString(10, obj.NUM_P_ED);
			stmt.setNString(11, obj.ID_NUM_K);
			stmt.setNString(12, obj.COMP_DOC);
			stmt.setLong(13, obj.OSN_SV.id);
			stmt.execute();
		});
	}

	@Override
	public void update(SpecialPart obj) throws SQLException {
		// TODO:
		getDataSource().exec(cn -> {
			PreparedStatement stmt = cn.prepareCall("UPDATE " + TABLE_NAME + " SET OSN_SV = ? WHERE id = ? ");
			stmt.setLong(1, obj.OSN_SV.id);
			stmt.setLong(2, obj.id);
			stmt.execute();
		});
	}
}
