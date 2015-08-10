package ru.sovzond.mgis2.fgistp.db_handlers;

import ru.sovzond.mgis2.fgistp.db_handlers.dao.DataSourceContainer;
import ru.sovzond.mgis2.fgistp.db_handlers.dao.impl.*;
import ru.sovzond.mgis2.fgistp.db_handlers.model.*;
import ru.sovzond.mgis2.fgistp.model.Entry;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DatabasePersister {

	public static final String DATE_TIME_FORMAT = "y-M-d'T'H:m:s";
	private DataSourceContainer dataSourceContainer;
	private MainInfoDao mainInfoDao;
	private CommonPartDao commonPartDao;
	private SpecialPartDao specialPartDao;
	private DocumentUpdatesInfoDao documentUpdatesInfoDao;
	private DictionaryDao dictionaryDao;

	public DatabasePersister(DataSourceContainer dataSourceContainer) {
		this.dataSourceContainer = dataSourceContainer;
		mainInfoDao = new MainInfoDao(dataSourceContainer);
		commonPartDao = new CommonPartDao(dataSourceContainer);
		specialPartDao = new SpecialPartDao(dataSourceContainer);
		documentUpdatesInfoDao = new DocumentUpdatesInfoDao(dataSourceContainer);
		dictionaryDao = new DictionaryDao(dataSourceContainer);
	}

	private MainInfo buildMainInfo(String cadastr, String oktmo_code) {
		// MAIN_INFO
		MainInfo mainInfo = new MainInfo();
		mainInfo.CADASTR = cadastr;
		mainInfo.OKTMO_CODE = oktmo_code;
		return mainInfo;
	}

	private SpecialPart buildSpecialPart(
			MainInfo mainInfo,
			String kod_d_r,
			String kod_d_t_p,
			String form_pr,
			String mas,
			int num_kart,
			String cadastr,
			String num_p_ed,
			String id_num_k,
			String comp_doc
	) {
		// SPECIAL_PART
		SpecialPart specialPart = new SpecialPart();
		specialPart.ID_NUM_K = id_num_k;
		specialPart.KOD_D_R = kod_d_r;
		specialPart.KOD_D_T_P = kod_d_t_p;
		specialPart.FORM_PR = form_pr;
		specialPart.MAS = mas;
		specialPart.NUM_KART = num_kart;
		specialPart.CADASTR = cadastr;
		specialPart.NUM_P_ED = num_p_ed;
		specialPart.OSN_SV = mainInfo;
		specialPart.COMP_DOC = comp_doc;
		return specialPart;
	}

	private DocumentUpdatesInfo buildUpdatesInfo(
			int num_kar,
			String reg_num,
			Date dat_reg,
			String otv_lic,
			MainInfo mainInfo
	) {
		// DOCUMENT_UPDATES_INFO
		DocumentUpdatesInfo updatesInfo = new DocumentUpdatesInfo();
		updatesInfo.NUM_KAR = num_kar;
		updatesInfo.REG_NUM = reg_num;
		updatesInfo.DAT_REG = dat_reg;
		updatesInfo.OTV_LIC = otv_lic;
		updatesInfo.OSN_SV = mainInfo;
		return updatesInfo;
	}

	private CommonPart buildCommonPart(
			MainInfo mainInfo,
			String kod_doc,
			String dop_name,
			Date d_u_p,
			String org_u_d,
			String reg_num,
			String prost_ind,
			String comp_doc
	) {
		// COMMON_PART
		CommonPart commonPart = new CommonPart();
		commonPart.KOD_DOC = kod_doc;
		commonPart.DOP_NAME = dop_name;
		commonPart.D_U_P = d_u_p;
		commonPart.ORG_U_D = org_u_d;
		commonPart.REG_NUM = reg_num;
		commonPart.PROST_IND = prost_ind;
		commonPart.COMP_DOC = comp_doc;
		commonPart.OSN_SV = mainInfo;
		return commonPart;
	}

	public void acquire(List<Entry> entries, List<DocumentArchive> documents) throws ParseException, SQLException {

		for (Entry entry : entries) {
			String cadastralNumber = entry.getProperties().get("d:ID"); // TODO: CadastralNumber ???
			String oktmo = entry.getProperties().get("d:OKTMO");
			MainInfo mainInfo = buildMainInfo(cadastralNumber, oktmo);


			for (DocumentArchive documentArchive : documents) {
				Entry document = documentArchive.getDocument();
				Map<String, String> docProps = document.getProperties();

				LandDocument doc = new LandDocument();

				String docName = docProps.get("d:FULL_DOC_NAME");
				String docNumber = docProps.get("d:APPROVAL_DOC_NUM");
				String docRegNum = docProps.get("d:ORDER_NUMBER");
				Date docRegDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(docProps.get("d:DOCUMENT_DATE"));
				String docExecutorOrganization = docProps.get("d:DOC_DEVELOPER");
				String docExecutorPerson = docProps.get("d:EXECUTOR_FIO");
				//+ ", " + docProps.get("d:EXECUTOR_POST") + ", " + docExecutorPerson + ", " + docProps.get("d:EXECUTOR_PHONE")

				doc.name = docName;
				doc.C12061 = docNumber;
				doc.DATA = docRegDate;
				doc.AD = docExecutorPerson; // TODO: DocumentAuthor: ???
				doc.FL_UL_ID2 = ""; // TODO: Document Requestor: ???

				int mapNumber = 0;
				for (Entry file : document.getFiles()) {

					Map<String, String> fileProps = file.getProperties();

					String fileName = fileProps.get("d:NAME");
					String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
					String fileTypeName = fileProps.get("d:TYPE_NAME");

					if (fileExtension.toLowerCase().contains("doc") || fileExtension.toLowerCase().contains("pdf")) {
						// TODO: add document to COMMON PART

					} else {
						// TODO: add document to SPECIAL PART
						mapNumber++;
					}

					CommonPart commonPart = buildCommonPart(
							mainInfo,
							docNumber, // Код документа (Классификатор документов, размещаемых в ИС ОГД)
							docName, // Дополнительное наименование
							new SimpleDateFormat(DATE_TIME_FORMAT).parse(docProps.get("d:APPROVAL_DOC_DATE")), // Дата утверждения документа
							docExecutorOrganization, // Организация, утвердившая документ (Юридические лица, обособленные подразделения и иные неюридические лица, Физические лица и предприниматели без образования юридического лица)
							docRegNum, // Регистрационный номер
							null, // Пространственный индекс
							null  // Связанные документы (Приказ)
					);
					mainInfo.GEN_P1_ID = commonPart;

					SpecialPart specialPart = buildSpecialPart(
							mainInfo,
							fileTypeName, // Код типа карты по классификатору документов (Классификатор документов, размещаемых в ИС ОГД)
							fileTypeName, // Код типа карты по классификатору документов территориального планирования (Классификатор документов территориального планирования Российской Федерации и субъектов Российской Федерации)
							fileExtension, // Форма представления (Классификатор формы представления документа)
							"", // Масштаб (Справочник масштабов)
							mapNumber,  // Номер карты по порядку
							cadastralNumber, // Кадастровый номер
							"", // Номер планировочной единицы
							"", // Идентификационный номер карты
							""  // Связанные документы
					);
					mainInfo.SP_P1_ID = specialPart;

					DocumentUpdatesInfo updatesInfo = buildUpdatesInfo(
							0,  // Номер карточки
							docRegNum, // Регистрационный номер
							docRegDate, // Дата регистрации документа
							docExecutorPerson, // Ответственное лицо
							mainInfo // Основные сведения
					);
					mainInfo.UPDATE1_ID = updatesInfo;

					//
					saveMainInfo(mainInfo);
					saveCommonPart(commonPart);
					saveSpecialPart(specialPart);
					saveUpdatesInfo(updatesInfo);
					saveMainInfo(mainInfo);
				}
			}
		}
	}

	public void saveMainInfo(MainInfo mainInfo) throws SQLException {
		//mainInfoDao.create(mainInfo);
		System.out.println(mainInfo);
	}

	public void saveCommonPart(CommonPart commonPart) throws SQLException {
		//commonPartDao.create(commonPart);
		System.out.println(commonPart);
	}

	public void saveSpecialPart(SpecialPart specialPart) throws SQLException {
		//specialPartDao.create(specialPart);
		System.out.println(specialPart);
	}

	public void saveUpdatesInfo(DocumentUpdatesInfo updatesInfo) throws SQLException {
		//documentUpdatesInfoDao.create(updatesInfo);
		System.out.println(updatesInfo);
	}
}
