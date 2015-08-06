package ru.sovzond.mgis2.fgistp.db_handlers;

import ru.sovzond.mgis2.fgistp.db_handlers.model.CommonPart;
import ru.sovzond.mgis2.fgistp.db_handlers.model.DocumentUpdatesInfo;
import ru.sovzond.mgis2.fgistp.db_handlers.model.MainInfo;
import ru.sovzond.mgis2.fgistp.db_handlers.model.SpecialPart;
import ru.sovzond.mgis2.fgistp.model.Entry;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DatabasePersister {
	public void acquire(Entry targetFileName, InputStream inputStream) {
		// MAIN_INFO
		MainInfo mainInfo = new MainInfo();
		mainInfo.CADASTR = "";
		mainInfo.OKTMO_CODE = "";

		// COMMON_PART
		CommonPart commonPart = new CommonPart();
		commonPart.COMP_DOC = "";
		commonPart.D_U_P = Calendar.getInstance().getTime();
		commonPart.DOP_NAME = "";
		commonPart.KOD_DOC = "";
		commonPart.ORG_U_D = "";
		commonPart.OSN_SV = "";
		commonPart.PROST_IND = "";
		commonPart.REG_NUM = "";
		mainInfo.commonPart = commonPart;

		// SPECIAL_PART
		SpecialPart specialPart = new SpecialPart();
		specialPart.CADASTR = "";
		specialPart.COMP_DOC = "";
		specialPart.FORM_PR = "";
		specialPart.ID_NUM_K = "";
		specialPart.KOD_D_R = "";
		specialPart.KOD_D_T_P = "";
		specialPart.MAS = "";
		specialPart.NUM_KART = 0;
		specialPart.NUM_P_ED = "";
		mainInfo.SP_P1_ID = specialPart;
		specialPart.OSN_SV = mainInfo;

		// DOCUMENT_UPDATES_INFO
		DocumentUpdatesInfo updatesInfo = new DocumentUpdatesInfo();
		updatesInfo.DAT_REG = Calendar.getInstance().getTime();
		updatesInfo.NUM_KAR = 0;
		updatesInfo.OSN_SV = mainInfo;
		updatesInfo.OTV_LIC = "";
		updatesInfo.REG_NUM = "";


	}
}
