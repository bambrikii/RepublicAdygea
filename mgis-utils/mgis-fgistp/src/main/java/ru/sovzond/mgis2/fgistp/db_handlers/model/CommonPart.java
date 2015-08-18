package ru.sovzond.mgis2.fgistp.db_handlers.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class CommonPart {
	public Long id;

	/**
	 * Код документа (Классификатор документов, размещаемых в ИС ОГД)
	 */
	public String KOD_DOC;
	/**
	 * Дополнительное наименование
	 */
	public String DOP_NAME;
	/**
	 * Дата утверждения документа
	 */
	public Date D_U_P;
	/**
	 * Организация, утвердившая документ (Юридические лица, обособленные подразделения и иные неюридические лица, Физические лица и предприниматели без образования юридического лица)
	 */
	public String ORG_U_D;
	/**
	 * Регистрационный номер
	 */
	public String REG_NUM;
	/**
	 * Пространственный индекс
	 */
	public String PROST_IND;
	/**
	 * Связанные документы (Приказ)
	 */
	public String COMP_DOC;
	public MainInfo OSN_SV;

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("KOD_DOC", KOD_DOC)
				.append("DOP_NAME", DOP_NAME)
				.append("D_U_P", D_U_P)
				.append("ORG_U_D", ORG_U_D)
				.append("REG_NUM", REG_NUM)
				.append("PROST_IND", PROST_IND)
				.append("COMP_DOC", COMP_DOC)
				.build();
	}
}
