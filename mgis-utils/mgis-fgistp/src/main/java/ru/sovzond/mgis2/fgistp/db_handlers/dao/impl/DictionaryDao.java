package ru.sovzond.mgis2.fgistp.db_handlers.dao.impl;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.model.dictionaries.ISOGDDocumentType;
import ru.sovzond.mgis2.fgistp.db_handlers.model.dictionaries.LegalPerson;
import ru.sovzond.mgis2.fgistp.db_handlers.model.dictionaries.RepresentationForm;
import ru.sovzond.mgis2.fgistp.db_handlers.model.dictionaries.Scale;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class DictionaryDao {

	private static final String LEGAL_PERSON_TABLE_NAME = "ENT_UL";
	private static final String SCALE_TABLE_NAME = "ENT_S_SCALE";
	private static final String REPRESENTATION_FORM_TABLE_NAME = "ENT_FORMA_DOC";
	private DataSourceContainer dataSource;

	public DictionaryDao(DataSourceContainer dataSource) {
		this.dataSource = dataSource;
	}

	public LegalPerson findLegalPerson(String name) throws SQLException {
		return this.dataSource.exec(new DataSourceContainer.ExecutableWithResult() {
			@Override
			public LegalPerson execute(Connection cn) throws SQLException {
				try (CallableStatement stmt = cn.prepareCall("SELECT * FROM " + LEGAL_PERSON_TABLE_NAME)) {
					try (ResultSet rs = stmt.getResultSet()) {
						if (rs.next()) {
							LegalPerson legalPerson = new LegalPerson();
							legalPerson.setId(rs.getLong("id"));
							legalPerson.setName(rs.getNString("NAMES2"));
							return legalPerson;
						}
					}
				}
				return createLegalPerson(cn, name);
			}
		});
	}

	private LegalPerson createLegalPerson(Connection cn, String name) throws SQLException {
		try (CallableStatement stmt = cn.prepareCall("INSERT INTO " + LEGAL_PERSON_TABLE_NAME + " (ID, NAME) VALUES (?, ?)")) {
			stmt.setLong(1, 0);
			stmt.setNString(2, "");
			LegalPerson legalPerson = new LegalPerson();
			legalPerson.setName(name);
			return legalPerson;
		}
	}

	public Scale findScale(String name) throws SQLException {
		return this.dataSource.exec(new DataSourceContainer.ExecutableWithResult() {
			@Override
			public Scale execute(Connection cn) throws SQLException {
				try (CallableStatement stmt = cn.prepareCall("SELECT * FROM " + SCALE_TABLE_NAME + " WHERE ")) {
					try (ResultSet rs = stmt.getResultSet()) {
						if (rs.next()) {
							Scale scale = new Scale();
							scale.setId(rs.getLong("id"));
							// scale.setName(rs.getNString("code"));
							scale.setName(rs.getNString("name"));
							return scale;
						}
					}
				}
				return null;
			}
		});
	}

	public RepresentationForm findRepresentationForm(String name) throws SQLException {
		return this.dataSource.exec(new DataSourceContainer.ExecutableWithResult() {
			@Override
			public RepresentationForm execute(Connection cn) throws SQLException {
				try (CallableStatement stmt = cn.prepareCall("SELECT * FROM " + REPRESENTATION_FORM_TABLE_NAME + " WHERE ")) {
					try (ResultSet rs = stmt.getResultSet()) {
						if (rs.next()) {
							RepresentationForm representationForm = new RepresentationForm();
							representationForm.setId(rs.getLong("id"));
							// representationForm.setCode(rs.getNString("code"));
							representationForm.setName(rs.getNString("name"));
							return representationForm;
						}
					}
				}
				return null;
			}
		});
	}

	public ISOGDDocumentType findISOGDdDocumentType(String name) throws SQLException {
		return this.dataSource.exec(new DataSourceContainer.ExecutableWithResult() {
			@Override
			public ISOGDDocumentType execute(Connection cn) throws SQLException {
				try (CallableStatement stmt = cn.prepareCall("SELECT * FROM " + REPRESENTATION_FORM_TABLE_NAME + " WHERE ")) {
					try (ResultSet rs = stmt.getResultSet()) {
						if (rs.next()) {
							ISOGDDocumentType isogdDocumentType = new ISOGDDocumentType();
							isogdDocumentType.setId(rs.getLong("id"));
							// isogdDocumentType.setName(rs.getNString("code"));
							isogdDocumentType.setName(rs.getNString("name"));
							return isogdDocumentType;
						}
					}
				}
				return null;
			}
		});
	}
}
