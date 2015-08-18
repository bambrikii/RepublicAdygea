package ru.sovzond.mgis2.fgistp.db_handlers.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class SpecialPart {
	public Long id;

	/**
	 * Код типа карты по классификатору документов (Классификатор документов, размещаемых в ИС ОГД)
	 */
	public String KOD_D_R;
	/**
	 * Код типа карты по классификатору документов территориального планирования (Классификатор документов территориального планирования Российской Федерации и субъектов Российской Федерации)
	 */
	public String KOD_D_T_P;
	/**
	 * Форма представления (Классификатор формы представления документа)
	 */
	public String FORM_PR;
	/**
	 * Scale, Масштаб (Справочник масштабов)
	 */
	public String MAS;
	/**
	 * Номер карты по порядку
	 */
	public Integer NUM_KART;
	/**
	 * Кадастровый номер
	 */
	public String CADASTR;
	/**
	 * Номер планировочной единицы
	 */
	public String NUM_P_ED;
	/**
	 * Идентификационный номер карты
	 */
	public String ID_NUM_K;
	/**
	 * Связанные документы
	 */
	public String COMP_DOC;
	public MainInfo OSN_SV;

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("KOD_D_R", KOD_D_R)
				.append("KOD_D_T_P", KOD_D_T_P)
				.append("FORM_PR", FORM_PR)
				.append("MAS", MAS)
				.append("NUM_KART", NUM_KART)
				.append("CADASTR", CADASTR)
				.append("NUM_P_ED", NUM_P_ED)
				.append("ID_NUM_K", ID_NUM_K)
				.append("COMP_DOC", COMP_DOC)
				.build();
	}
}
