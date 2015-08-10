package ru.sovzond.mgis2.fgistp.db_handlers.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DocumentUpdatesInfo {
	public Long id;

	/**
	 * Номер карточки
	 */
	public Integer NUM_KAR;
	/**
	 * Регистрационный номер
	 */
	public String REG_NUM;
	/**
	 * Дата регистрации документа
	 */
	public Date DAT_REG;
	/**
	 * Ответственное лицо
	 */
	public String OTV_LIC;
	/**
	 * Основные сведения
	 */
	public MainInfo OSN_SV;

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("NUM_KAR", NUM_KAR)
				.append("REG_NUM", REG_NUM)
				.append("DAT_REG", DAT_REG)
				.append("OTV_LIC", OTV_LIC)
				.build();
	}
}
