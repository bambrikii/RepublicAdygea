import xml.etree.ElementTree as ET;
import os;
import sys;
import re;
import zipfile;
import datetime;

import shapefile;


def convert_kpt_xml_to_shape(dirName, sourceFileName):
    xml_file = dirName + sourceFileName
    shape_file = dirName + "shp/" + re.sub(".xml$", "", sourceFileName) + '.shp'
    projection = 'GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137.0,298.257223563]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]]'

    w = shapefile.Writer(shapefile.POLYGON)
    # w.autoBalance = 1

    # create fields
    w.field("CadNum", "C", 125)
    w.field("State", "C", 120)
    w.field("DtCreatd", "C", 10)
    w.field("AreaArea", "C", 120)
    w.field("AreaUnit", "C", 120)
    w.field("Name", "C", 120)
    w.field("LInBnds", "C", 120)
    w.field("AOKATO", "C", 120)
    w.field("AKLADR", "C", 120)
    w.field("ARegion", "C", 254)
    w.field("ADistrNm", "C", 254)
    w.field("ADistrTp", "C", 254)
    w.field("ACityNm", "C", 254)
    w.field("ACityTp", "C", 254)
    w.field("ALocalNm", "C", 254)
    w.field("ALocalTyp", "C", 254)
    w.field("AStreNm", "C", 254)
    w.field("AStreTp", "C", 254)
    w.field("ALvl1Tp", "C", 254)
    w.field("ALvl1Vl", "C", 254)
    w.field("AddrNote", "C", 254)
    w.field("AddrNote2", "C", 254)
    w.field("Category", "C", 254)
    w.field("UtilUtlz", "C", 254)
    w.field("UtilByDoc", "C", 254)
    w.field("CadCstVl", "C", 235)
    w.field("CadCstUnt", "C", 235)

    tree = ET.parse(xml_file)
    root = tree.getroot()

    for kpt in root.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlocks"):
        for block in kpt.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlock"):
            for parcels in block.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Parcels"):
                for parcel in parcels.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Parcel"):
                    coordinates_found = False
                    cadastral_number = parcel.attrib["CadastralNumber"].strip();
                    # print("cadnum:*", cadastral_number);
                    state = parcel.attrib["State"].strip();
                    date_created = "" + datetime.datetime.strptime(parcel.attrib["DateCreated"].strip(), "%Y-%m-%d").date().strftime("%Y-%m-%d");
                    area_area = None
                    area_unit = None
                    name = None
                    location_in_bounds = None
                    okato = None
                    kladr = None
                    region = None
                    district_name = None
                    district_type = None
                    city_name = None
                    city_type = None
                    locality_name = None
                    locality_type = None
                    street_name = None
                    street_type = None
                    level1_type = None
                    level1_value = None
                    note = None
                    category = None
                    utilization_utilization = None
                    utilization_by_doc = None
                    cadastral_cost_value = None
                    cadastral_cost_unit = None
                    for area in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area"):
                        area_area1 = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area")
                        if area_area1 is not None:
                            area_area = area_area1.text.strip()
                        area_unit1 = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Unit")
                        if area_unit1 is not None:
                            area_unit = area_unit1.text.strip();
                    for location in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Location"):
                        for address in location.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Address"):
                            okato1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}OKATO")
                            if okato1 is not None:
                                okato = okato1.text.strip();

                            kladr1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}KLADR")
                            if kladr1 is not None:
                                kladr = kladr1.text.strip();

                            region1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Region")
                            if region1 is not None:
                                region = region1.text.strip();

                            district = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}District")
                            if district is not None:
                                district_name = district.attrib["Name"].strip();
                                district_type = district.attrib["Type"].strip();

                            city = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}City")
                            if city is not None:
                                city_name = city.attrib["Name"].strip();
                                city_type = city.attrib["Type"].strip();

                            locality = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Locality")
                            if locality is not None:
                                locality_name = locality.attrib["Name"].strip();
                                locality_type = locality.attrib["Type"].strip();

                            street = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Street")
                            if street is not None:
                                street_name = street.attrib["Name"].strip();
                                street_type = street.attrib["Type"].strip();

                            level1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Level1")
                            if level1 is not None:
                                level1_type = level1.attrib["Type"].strip();
                                level1_value = level1.attrib["Value"].strip();

                            note1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Note")
                            if note1 is not None:
                                note = note1.text.strip();

                    category1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Category")
                    if category1 is not None:
                        category = category1.text.strip();

                    utilization1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Utilization")
                    if utilization1 is not None:
                        if hasattr(utilization1, "Utilization"):
                            utilization_utilization = utilization1.attrib["Utilization"].strip();
                        if hasattr(utilization1, "ByDoc"):
                            utilization_by_doc = utilization1.attrib["ByDoc"].strip();

                    cadastral_cost1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralCost")
                    if cadastral_cost1 is not None:
                        cadastral_cost_value = cadastral_cost1.attrib["Value"].strip();
                        cadastral_cost_unit = cadastral_cost1.attrib["Unit"].strip();

                    entity_spatial = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}EntitySpatial")
                    poly = []
                    if entity_spatial is not None:
                        ent_sys = entity_spatial.attrib["EntSys"].strip();
                        for spatial_element in entity_spatial.iter(
                                "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpatialElement"):
                            part = []
                            # poly.append(part)
                            for spelement_unit in spatial_element.iter(
                                    "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpelementUnit"):
                                for ordinate in spelement_unit.iter(
                                        "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}Ordinate"):
                                    # specify coordinates in X,Y order (longitude, latitude)
                                    ord_nmb = ordinate.attrib["OrdNmb"]
                                    poly.append([float(ordinate.attrib['X']), float(ordinate.attrib['Y'])])
                                    if hasattr(ordinate, "DeltaGeopoint"):
                                        delta_geopoint = ordinate.attrib["DeltaGeopoint"]
                            coordinates_found = True
                            # copy attributes

                    if coordinates_found:
                        # print(poly)
                        w.poly(parts=[poly])
                        note1 = None
                        note2 = None
                        if note is not None and str.count(note, note) > 250:
                            note1 = note[:250]
                            note2 = note[250:]
                        # if note1 is None:
                        #     note1 = None
                        # if note2 is None:
                        #     note2 = None
                        w.record(
                            cadastral_number,
                            state,
                            date_created,
                            area_area,
                            area_unit,
                            name,
                            location_in_bounds,
                            okato,
                            kladr,
                            region,
                            district_name,
                            district_type,
                            city_name,
                            city_type,
                            locality_name,
                            locality_type,
                            street_name,
                            street_type,
                            level1_type,
                            level1_value,
                            note1,
                            note2,
                            category,
                            utilization_utilization,
                            utilization_by_doc,
                            cadastral_cost_value,
                            cadastral_cost_unit
                        )
                        # print("coord:  ", cadastral_number)
                        print("record: ", cadastral_number)

    w.save(shape_file)  # create the PRJ file
    with open(os.path.splitext(shape_file)[0] + os.extsep + 'prj', 'w') as prj:
        prj.write(projection)


def extract_zip(dir_name, file_name):
    fh = open(dir_name + file_name, 'rb')
    z = zipfile.ZipFile(fh)
    for name in z.namelist():
        print("Extracting: ", name)
        outpath = dir_name
        z.extract(name, outpath)
    fh.close()


dir_name = sys.argv[1]
if not os.path.exists(dir_name + "shp"):
    os.makedirs(dir_name + "shp")

if len(sys.argv) > 2:
    for i in range(2, len(sys.argv)):
        file_name = sys.argv[i]
        if file_name.endswith(".zip"):
            extract_zip(dir_name, file_name)
            # convertKPTXmlToShape(dirName, fileName)
        else:
            if file_name.endswith(".xml"):
                convert_kpt_xml_to_shape(dir_name, file_name)
else:
    for file_name in os.listdir(dir_name):
        if file_name.endswith(".zip"):
            extract_zip(dir_name, file_name)

    for file_name in os.listdir(dir_name):
        if file_name.endswith(".xml"):
            convert_kpt_xml_to_shape(dir_name, file_name)
