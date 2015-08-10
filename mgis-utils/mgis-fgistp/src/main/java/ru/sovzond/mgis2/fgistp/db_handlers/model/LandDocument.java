package ru.sovzond.mgis2.fgistp.db_handlers.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class LandDocument {
	public Long id;

	public String name;
	/**
	 * Номер документа
	 */
	public String C12061;
	/**
	 * Дата документа
	 */
	public Date DATA;
	/**
	 * Автор документа
	 */
	public String AD;
	/**
	 * Заявитель документа (Юридические лица, обособленные подразделения и иные неюридические лица, Физические лица и предприниматели без образования юридического лица)
	 */
	public String FL_UL_ID2;

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("C12061", C12061)
				.append("DATA", DATA)
				.append("AD", AD)
				.append("FL_UL_ID2", FL_UL_ID2)
				.build();
	}
}
