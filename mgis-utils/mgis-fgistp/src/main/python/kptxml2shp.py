import xml.etree.ElementTree as ET;
import shapefile;
import os;
import sys;
import re;
import zipfile;


def convertKPTXmlToShape(dirName, sourceFileName):
    xml_file = dirName + sourceFileName
    shape_file = dirName + "shp/" + re.sub(".xml$", "", sourceFileName) + '.shp'
    projection = 'GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137.0,298.257223563]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]]'

    w = shapefile.Writer(shapefile.POLYGON)

    # create fields
    w.field("CadastralNumber")
    w.field("State")
    w.field("DateCreated")
    w.field("AreaArea")
    w.field("AreaUnit")
    w.field("Name")
    w.field("LocationInBounds")
    w.field("LocationAddressOKATO")
    w.field("LocationAddressKLADR")
    w.field("LocationAddressRegion")
    w.field("LocationAddressDistrictName")
    w.field("LocationAddressDistrictType")
    w.field("LocationAddressCityName")
    w.field("LocationAddressCityType")
    w.field("LocationAddressLocalityName")
    w.field("LocationAddressLocalityType")
    w.field("LocationAddressStreetName")
    w.field("LocationAddressStreetType")
    w.field("LocationAddressLevel1Type")
    w.field("LocationAddressLevel1Value")
    w.field("LocationAddressNode")
    w.field("Category")
    w.field("UtilizationUtilization")
    w.field("UtilizationByDoc")
    w.field("CadastralCostValue")
    w.field("CadastralCostUnit")

    tree = ET.parse(xml_file)
    root = tree.getroot()
    shapes = root.getchildren()

    for kpt in root.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlocks"):
        for block in kpt.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlock"):
            for parcels in block.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Parcels"):
                for parcel in parcels.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Parcel"):
                    coordinatesFound = False
                    cadastralNumber = parcel.attrib["CadastralNumber"]
                    print ("CadastralNumber:", cadastralNumber);
                    state = parcel.attrib["State"]
                    dateCreated = parcel.attrib["DateCreated"]
                    areaArea = ""
                    areaUnit = ""
                    name = ""
                    locationInBounds = ""
                    okato = ""
                    kladr = ""
                    region = ""
                    districtName = ""
                    districtType = ""
                    cityName = ""
                    cityType = ""
                    localityName = ""
                    localityType = ""
                    streetName = ""
                    streetType = ""
                    level1Type = ""
                    level1Value = ""
                    note = ""
                    category = ""
                    utilizationUtilization = ""
                    utilizationByDoc = ""
                    cadastralCostValue = ""
                    cadastralCostUnit = ""
                    for area in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area"):
                        areaArea = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area")
                        areaUnit = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Unit")
                    for location in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Location"):
                        for address in location.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Address"):
                            okato1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}OKATO")
                            if okato1 is not None:
                                okato = okato1.text

                            kladr1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}KLADR")
                            if kladr1 is not None:
                                kladr = kladr1.text

                            region1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Region")
                            if region1 is not None:
                                region = region1.text

                            district = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}District")
                            if district is not None:
                                districtName = district.attrib["Name"]
                                districtType = district.attrib["Type"]

                            city = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}City")
                            if city is not None:
                                cityName = city.attrib["Name"]
                                cityType = city.attrib["Type"]

                            locality = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Locality")
                            if locality is not None:
                                localityName = locality.attrib["Name"]
                                localityType = locality.attrib["Type"]

                            street = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Street")
                            if street is not None:
                                streetName = street.attrib["Name"]
                                streetType = street.attrib["Type"]

                            level1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Level1")
                            if level1 is not None:
                                level1Type = level1.attrib["Type"]
                                level1Value = level1.attrib["Value"]

                    category = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Category")

                    utilization = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Utilization")
                    if utilization is not None:
                        if hasattr(utilization, "Utilization"):
                            utilizationUtilization = utilization.attrib["Utilization"]
                        if hasattr(utilization, "ByDoc"):
                            utilizationByDoc = utilization.attrib["ByDoc"]

                    cadastralCost = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralCost")
                    if cadastralCost is not None:
                        cadastralCostValue = cadastralCost.attrib["Value"]
                        cadastralCostUnit = cadastralCost.attrib["Unit"]

                    entitySpatial = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}EntitySpatial")
                    if entitySpatial is not None:
                        entSys = entitySpatial.attrib["EntSys"]
                        for spatialElement in entitySpatial.iter(
                                "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpatialElement"):
                            part = []
                            for spelementUnit in spatialElement.iter(
                                    "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpelementUnit"):
                                for ordinate in spelementUnit.iter(
                                        "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}Ordinate"):
                                    # specify coordinates in X,Y order (longitude, latitude)
                                    ordNmb = ordinate.attrib["OrdNmb"]
                                    part.append([float(ordinate.attrib['X']), float(ordinate.attrib['Y'])])
                                    if hasattr(ordinate, "DeltaGeopoint"):
                                        deltaGeopoint = ordinate.attrib["DeltaGeopoint"]
                            w.poly(parts=[part])
                            coordinatesFound = True
                            # copy attributes

                    if coordinatesFound:
                        w.record(cadastralNumber, state, dateCreated, areaArea, areaUnit, name, locationInBounds, okato, kladr, region,
                                 districtName, districtType, cityName, cityType, localityName, localityType, streetName, streetType, level1Type,
                                 level1Value, note, category, utilizationUtilization, utilizationByDoc, cadastralCostValue, cadastralCostUnit)
                        w.save(shape_file)  # create the PRJ file

    with open(os.path.splitext(shape_file)[0] + os.extsep + 'prj', 'w') as prj:
        prj.write(projection)


def extractZIP(dirName, fileName):
    fh = open(dirName + fileName, 'rb')
    z = zipfile.ZipFile(fh)
    for name in z.namelist():
        print("Extracting: ", name)
        outpath = dirName
        z.extract(name, outpath)
    fh.close()


dirName = sys.argv[1]
if not os.path.exists(dirName + "shp"):
    os.makedirs(dirName + "shp")

if len(sys.argv) > 2:
    for i in range(2, len(sys.argv)):
        fileName = sys.argv[i]
        if fileName.endswith(".zip"):
            extractZIP(dirName, fileName)
            # convertKPTXmlToShape(dirName, fileName)
        else:
            if fileName.endswith(".xml"):
                convertKPTXmlToShape(dirName, fileName)
else:
    for fileName in os.listdir(dirName):
        if fileName.endswith(".zip"):
            extractZIP(dirName, fileName)

    for fileName in os.listdir(dirName):
        if fileName.endswith(".xml"):
            convertKPTXmlToShape(dirName, fileName)
