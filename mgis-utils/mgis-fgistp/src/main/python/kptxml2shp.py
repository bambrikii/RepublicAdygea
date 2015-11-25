import xml.etree.ElementTree as ET;
import os;
import sys;
import re;
import zipfile;
import datetime;
import shutil;

import shapefile;


def cvt(param):
    return param.encode("utf-8").strip()


def convert_kpt_xml_to_shape(source_dir_name, source_file_name, target_dir_name):
    print("convert:", source_dir_name, source_file_name, target_dir_name)
    xml_file = source_dir_name + "/" + source_file_name
    shape_file = target_dir_name + "/" + re.sub(".xml$", "", source_file_name) + '.shp'
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
                    cadastral_number = cvt(parcel.attrib["CadastralNumber"]);
                    state = cvt(parcel.attrib["State"]);
                    date_created = "" + datetime.datetime.strptime(cvt(parcel.attrib["DateCreated"]), "%Y-%m-%d").date().strftime("%Y-%m-%d");
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
                            area_area = cvt(area_area1.text)
                        area_unit1 = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Unit")
                        if area_unit1 is not None:
                            area_unit = cvt(area_unit1.text);
                    for location in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Location"):
                        for address in location.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Address"):
                            okato1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}OKATO")
                            if okato1 is not None:
                                okato = cvt(okato1.text);

                            kladr1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}KLADR")
                            if kladr1 is not None:
                                kladr = cvt(kladr1.text);

                            region1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Region")
                            if region1 is not None:
                                region = cvt(region1.text);

                            district = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}District")
                            if district is not None:
                                district_name = cvt(district.attrib["Name"]);
                                district_type = cvt(district.attrib["Type"]);

                            city = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}City")
                            if city is not None:
                                city_name = cvt(city.attrib["Name"]);
                                city_type = cvt(city.attrib["Type"]);

                            locality = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Locality")
                            if locality is not None:
                                locality_name = cvt(locality.attrib["Name"]);
                                locality_type = cvt(locality.attrib["Type"]);

                            street = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Street")
                            if street is not None:
                                street_name = cvt(street.attrib["Name"]);
                                street_type = cvt(street.attrib["Type"]);

                            level1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Level1")
                            if level1 is not None:
                                level1_type = cvt(level1.attrib["Type"]);
                                level1_value = cvt(level1.attrib["Value"]);

                            note1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Note")
                            if note1 is not None:
                                note = cvt(note1.text);

                    category1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Category")
                    if category1 is not None:
                        category = cvt(category1.text);

                    utilization1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Utilization")
                    if utilization1 is not None:
                        if hasattr(utilization1, "Utilization"):
                            utilization_utilization = cvt(utilization1.attrib["Utilization"]);
                        if hasattr(utilization1, "ByDoc"):
                            utilization_by_doc = cvt(utilization1.attrib["ByDoc"]);

                    cadastral_cost1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralCost")
                    if cadastral_cost1 is not None:
                        cadastral_cost_value = cvt(cadastral_cost1.attrib["Value"]);
                        cadastral_cost_unit = cvt(cadastral_cost1.attrib["Unit"]);

                    entity_spatial = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}EntitySpatial")
                    parts = []
                    partTypes = []
                    if entity_spatial is not None:
                        ent_sys = cvt(entity_spatial.attrib["EntSys"]);
                        for spatial_element in entity_spatial.iter(
                                "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpatialElement"):
                            elem = []
                            parts.append(elem);
                            partTypes2 = []
                            partTypes.append(partTypes2)
                            for spelement_unit in spatial_element.iter(
                                    "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpelementUnit"):
                                # unit = []
                                # parts.append(unit)
                                for ordinate in spelement_unit.iter(
                                        "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}Ordinate"):
                                    # specify coordinates in X,Y order (longitude, latitude)
                                    ord_nmb = cvt(ordinate.attrib["OrdNmb"])
                                    elem.append([float(cvt(ordinate.attrib['X'])), float(cvt(ordinate.attrib['Y']))])
                                    partTypes2.append(shapefile.POLYGON)
                                    if hasattr(ordinate, "DeltaGeopoint"):
                                        delta_geopoint = cvt(ordinate.attrib["DeltaGeopoint"])
                                    coordinates_found = True
                                    # copy attributes

                    if coordinates_found:
                        w.poly(parts=parts, partTypes=partTypes)
                        note1 = None
                        note2 = None
                        if note is not None and note.split(".") > 250:
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


def extract_zip(source_dir_name, source_file_name):
    fh = open(source_dir_name + "/" + source_file_name, 'rb')
    z = zipfile.ZipFile(fh)
    for name in z.namelist():
        out_path = source_dir_name + "/" + source_file_name + ".unpacked"
        print("Extracting: ", name, out_path)
        z.extract(name, out_path)
    fh.close()


def convert_dir(source_dir_name, target_dir_name):
    for file_name in os.listdir(source_dir_name):
        if file_name.endswith(".xml"):
            convert_kpt_xml_to_shape(source_dir_name, file_name, target_dir_name)


dir_name = sys.argv[1]
shape_dir_name = dir_name + "/" + "shp"
if not os.path.exists(shape_dir_name):
    os.makedirs(shape_dir_name)

if len(sys.argv) > 2:
    for i in range(2, len(sys.argv)):
        source_file_name = sys.argv[i]
        if source_file_name.endswith(".zip"):
            extract_zip(dir_name, source_file_name)
            unpacked_dir_name = dir_name + "/" + source_file_name + ".unpacked"
            convert_dir(unpacked_dir_name, shape_dir_name)
            shutil.rmtree(unpacked_dir_name)
        else:
            if source_file_name.endswith(".xml"):
                convert_kpt_xml_to_shape(dir_name, source_file_name, shape_dir_name)
else:
    for source_file_name in os.listdir(dir_name):
        if source_file_name.endswith(".zip"):
            extract_zip(dir_name, source_file_name)

    convert_dir(dir_name)
