package ru.sovzond.mgis2.fgistp.db_handlers.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class MainInfo {
	public Long id;

	public String CADASTR;
	public String OKTMO_CODE;
	public CommonPart GEN_P1_ID;
	public SpecialPart SP_P1_ID;
	public DocumentUpdatesInfo UPDATE1_ID;

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("CADASTR", CADASTR)
				.append("OKTMO_CODE", OKTMO_CODE)
				.append("GEN_P1_ID", GEN_P1_ID)
				.append("SP_P1_ID", SP_P1_ID)
				.append("UPDATE1_ID", UPDATE1_ID)
				.build();
	}
}
