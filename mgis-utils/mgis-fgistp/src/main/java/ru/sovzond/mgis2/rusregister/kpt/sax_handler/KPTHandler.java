package ru.sovzond.mgis2.rusregister.kpt.sax_handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ru.sovzond.mgis2.rusregister.kpt.model.*;
import ru.sovzond.mgis2.rusregister.kpt.shapefile_handlers.KPTShapefileWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class KPTHandler extends DefaultHandler {
	private KPTShapefileWriter writer;
	private SimpleDateFormat yyyymmdd;

	public KPTHandler(KPTShapefileWriter writer) {
		this.writer = writer;
		yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
	}

	private Parcel parcel;
	private SpatialElement spatialElement;
	private SpelementUnit spelementUnit;
	private Ordinate ordinate;

	private boolean isParcelStarted;
	private boolean isAreaStarted;
	private boolean isAreaAreaStarted;
	private boolean isAreaUnitStarted;
	private boolean isNameStarted;
	private boolean isLocationStarted;
	private boolean isInboundsStarted;
	private boolean isAddressStarted;
	private boolean isOKATOStarted;
	private boolean isKLADRStarted;
	private boolean isRegionStarted;
	private boolean isDistrictStarted;
	private boolean isCityStarted;
	private boolean isLocalityStarted;
	private boolean isStreetStarted;
	private boolean isLevel1Started;
	private boolean isNoteStarted;
	private boolean isCategoryStarted;
	private boolean isUtilizationStarted;
	private boolean isCadastralCostStarted;
	private boolean isEntitySpatialStarted;
	private boolean isSpatialElementStarted;
	private boolean isSpelementUnitStarted;
	private boolean isOrdinateStarted;


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
			case "Parcel":
				isParcelStarted = true;
				parcel = new Parcel();
				parcel.setCadastralNumber(attributes.getValue("CadastralNumber"));
				parcel.setState(attributes.getValue("State"));
				try {
					parcel.setDateCreated(yyyymmdd.parse(attributes.getValue("DateCreated")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case "Area":
				if (isAreaStarted) {
					isAreaAreaStarted = true;
				}
				isAreaStarted = true;
				break;
			case "Unit":
				isAreaUnitStarted = true;
				break;
			case "Name":
				isNameStarted = true;
				break;
			case "Location":
				isLocationStarted = true;
				break;
			case "inBounds":
				isInboundsStarted = true;
				break;
			case "Address":
				isAddressStarted = true;
				parcel.setAddress(new Address());
				break;
			case "OKATO":
				isOKATOStarted = true;
				break;
			case "KLADR":
				isKLADRStarted = true;
				break;
			case "Region":
				isRegionStarted = true;
				break;
			case "District":
				isDistrictStarted = true;
				parcel.getAddress().setDistrictName(attributes.getValue("Name"));
				parcel.getAddress().setDistrictType(attributes.getValue("Type"));
				break;
			case "City":
				isCityStarted = true;
				parcel.getAddress().setCityName(attributes.getValue("Name"));
				parcel.getAddress().setCityType(attributes.getValue("Type"));
				break;
			case "Locality":
				isLocalityStarted = true;
				parcel.getAddress().setLocalityName(attributes.getValue("Name"));
				parcel.getAddress().setLocalityType(attributes.getValue("Type"));
				break;
			case "Street":
				isStreetStarted = true;
				parcel.getAddress().setStreetName(attributes.getValue("Name"));
				parcel.getAddress().setStreetType(attributes.getValue("Type"));
				break;
			case "Level1":
				isLevel1Started = true;
				parcel.getAddress().setLevel1Type(attributes.getValue("Type"));
				parcel.getAddress().setLevel1Value(attributes.getValue("Value"));
				break;
			case "Note":
				isNoteStarted = true;
				break;
			case "Category":
				isCategoryStarted = true;
				break;
			case "Utilization":
				isUtilizationStarted = true;
				parcel.setUtilization(attributes.getValue("Utilization"));
				parcel.setUtilizationByDoc(attributes.getValue("ByDoc"));
				break;
			case "CadastralCost":
				isCadastralCostStarted = true;
				parcel.setCadastralCost(Double.parseDouble(attributes.getValue("Value")));
				parcel.setCadastralUnit(attributes.getValue("Unit"));
				break;
			case "EntitySpatial":
				isEntitySpatialStarted = true;
				EntitySpatial entitySpatial = new EntitySpatial();
				entitySpatial.setEntSys(attributes.getValue("EntSys"));
				parcel.setEntitySpatial(entitySpatial);
				break;
			case "ns3:SpatialElement":
				isSpatialElementStarted = true;
				spatialElement = new SpatialElement();
				break;
			case "ns3:SpelementUnit":
				isSpelementUnitStarted = true;
				spelementUnit = new SpelementUnit();
				spelementUnit.setTypeUnit(attributes.getValue("TypeUnit"));
				spelementUnit.setSubNumb(Integer.parseInt(attributes.getValue("SuNmb")));
				break;
			case "ns3:Ordinate":
				isOrdinateStarted = true;
				ordinate = new Ordinate();
				ordinate.setX(Double.parseDouble(attributes.getValue("X")));
				ordinate.setY(Double.parseDouble(attributes.getValue("Y")));
				ordinate.setOrdNmb(Integer.parseInt(attributes.getValue("OrdNmb")));
				String deltaGeopoint = attributes.getValue("DeltaGeopoint");
				if (deltaGeopoint != null) {
					ordinate.setDeltaGeopoint(Double.parseDouble(deltaGeopoint));
				} else {
					ordinate.setDeltaGeopoint(0);
				}
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "Parcel":
				writer.write(parcel);
				isParcelStarted = false;
				break;
			case "Area":
				if (isAreaAreaStarted) {
					isAreaAreaStarted = false;
				}
				if (isAreaStarted && !isAreaAreaStarted) {
					isAreaStarted = false;
				}
				break;
			case "Unit":
				isAreaUnitStarted = false;
				break;
			case "Name":
				isNameStarted = false;
				break;
			case "Location":
				isLocationStarted = false;
				break;
			case "inBounds":
				isInboundsStarted = false;
				break;
			case "Address":
				isAddressStarted = false;
				break;
			case "OKATO":
				isOKATOStarted = false;
				break;
			case "KLADR":
				isKLADRStarted = false;
				break;
			case "Region":
				isRegionStarted = false;
				break;
			case "District":
				isDistrictStarted = false;
				break;
			case "City":
				isCityStarted = false;
				break;
			case "Locality":
				isLocalityStarted = false;
				break;
			case "Street":
				isStreetStarted = false;
				break;
			case "Level1":
				isLevel1Started = false;
				break;
			case "Note":
				isNoteStarted = false;
				break;
			case "Category":
				isCategoryStarted = false;
				break;
			case "Utilization":
				isUtilizationStarted = false;
				break;
			case "CadastralCost":
				isCadastralCostStarted = false;
				break;
			case "EntitySpatial":
				isEntitySpatialStarted = false;
				break;
			case "ns3:SpatialElement":
				isSpatialElementStarted = false;
				parcel.getEntitySpatial().getSpatialElements().add(spatialElement);
				break;
			case "ns3:SpelementUnit":
				isSpelementUnitStarted = false;
				spatialElement.getSpelementUnits().add(spelementUnit);
				break;
			case "ns3:Ordinate":
				isOrdinateStarted = false;
				spelementUnit.getOrdinates().add(ordinate);
				break;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String str = String.valueOf(ch, start, length);
		if (isParcelStarted) {
			if (isAreaAreaStarted) {
				parcel.setArea(str);
			} else if (isAreaUnitStarted) {
				parcel.setAreaUnit(str);
			} else if (isNameStarted) {
				parcel.setName(str);
			} else if (isInboundsStarted) {
				parcel.setLocationInBounds(Integer.parseInt(str));
			} else if (isOKATOStarted) {
				parcel.getAddress().setOkato(str);
			} else if (isKLADRStarted) {
				parcel.getAddress().setKladr(str);
			} else if (isRegionStarted) {
				parcel.getAddress().setRegion(str);
			} else if (isNoteStarted) {
				parcel.getAddress().setNote(str);
			} else if (isCategoryStarted) {
				parcel.setCategory(str);
			}
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		super.error(e);
	}
}
