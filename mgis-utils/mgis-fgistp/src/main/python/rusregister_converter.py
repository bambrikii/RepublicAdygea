#!/usr/bin/env python
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET;
import re;
import zipfile;
import os;
import json;

import shapefile;


class RusRegisterConverter:
    cs_definitions = {}
    cs_aliases = {}

    def __init__(self):
        logger = None
        pass

    def __init__(self, logging_handler):
        self.logger = logging_handler
        pass

    def log(self, message):
        if self.logger is not None:
            self.logger.emit(message)
        else:
            print(message)

    @staticmethod
    def cvt(param):
        return param.strip()

    @staticmethod
    def cvt_utf8(param):
        if param is not None:
            return param.encode("utf-8")
        return None

    @staticmethod
    def split_long_str(note):
        notes = [None] * 2
        if note is not None:
            if len(note) > 250:
                notes[0] = note[:250]
                notes[1] = note[250:]
            else:
                notes[0] = note
                notes[1] = None
        return notes

    def load_coord_system_defs(self):
        self.log("loading coord system... ")
        data = None
        with open('coordinate_system_defs.json') as coord_system_defs:
            data = json.load(coord_system_defs)

        for cs_definition in data[u'cs_definitions']:
            definition = data[u'cs_definitions'][cs_definition]
            self.cs_definitions[cs_definition] = definition
            self.log("  coord system: " + cs_definition + ", " + definition)

        for cs_alias in data[u'cs_aliases']:
            alias = data[u'cs_aliases'][cs_alias]
            cs_definition = data[u'cs_aliases'][cs_alias]
            self.cs_aliases[cs_alias] = self.cs_definitions[cs_definition]
            self.log("  coord system alias: " + alias + ", " + cs_definition)

    def find_coord_systems(self, source_dir_name, source_file_name):
        self.load_coord_system_defs()

        self.log("looking for coord system: " + source_dir_name + ", " + source_file_name)
        xml_file = source_dir_name + "/" + source_file_name

        result = {}

        tree = ET.parse(xml_file)
        root = tree.getroot()
        for kpt in root.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlocks"):
            for block in kpt.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralBlock"):
                for coord_systems in block.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CoordSystems"):
                    for coord_system in coord_systems.iter("{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}CoordSystem"):
                        if hasattr(result, coord_system.attrib["CsId"]) is False:
                            result[self.cvt(coord_system.attrib["CsId"])] = self.cvt(coord_system.attrib["Name"])
        return result

    def convert_kpt_xml_to_shape(self, source_dir_name, source_file_name, target_dir_name):
        coord_systems = self.find_coord_systems(source_dir_name, source_file_name)
        self.log("coord. systems: " + str(coord_systems));

        self.log("convert: " + source_dir_name + ", " + source_file_name + ", " + target_dir_name)
        xml_file = source_dir_name + "/" + source_file_name

        ww = {}
        for coord_system_id in coord_systems:
            w = shapefile.Writer(shapefile.POLYGON)
            ww[coord_system_id] = w

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
                        cadastral_number = self.cvt(parcel.attrib["CadastralNumber"]);
                        state = self.cvt(parcel.attrib["State"]);
                        date_created = None
                        if "DateCreated" in parcel.attrib:
                            date_created = self.cvt(parcel.attrib["DateCreated"])
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
                        ent_sys = None
                        for name1 in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Name"):
                            name = self.cvt(name1.text)

                        for area in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area"):
                            area_area1 = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Area")

                            if area_area1 is not None:
                                area_area = self.cvt(area_area1.text)
                            area_unit1 = area.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Unit")
                            if area_unit1 is not None:
                                area_unit = self.cvt(area_unit1.text);

                        for location in parcel.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Location"):

                            for in_bounds1 in location.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}inBounds"):
                                location_in_bounds = self.cvt(in_bounds1.text)

                            for address in location.iter("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Address"):
                                okato1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}OKATO")
                                if okato1 is not None:
                                    okato = self.cvt(okato1.text);

                                kladr1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}KLADR")
                                if kladr1 is not None:
                                    kladr = self.cvt(kladr1.text);

                                region1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Region")
                                if region1 is not None:
                                    region = self.cvt(region1.text);

                                district = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}District")
                                if district is not None:
                                    district_name = self.cvt(district.attrib["Name"]);
                                    district_type = self.cvt(district.attrib["Type"]);

                                city = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}City")
                                if city is not None:
                                    city_name = self.cvt(city.attrib["Name"]);
                                    city_type = self.cvt(city.attrib["Type"]);

                                locality = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Locality")
                                if locality is not None:
                                    locality_name = self.cvt(locality.attrib["Name"]);
                                    locality_type = self.cvt(locality.attrib["Type"]);

                                street = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Street")
                                if street is not None:
                                    street_name = self.cvt(street.attrib["Name"]);
                                    street_type = self.cvt(street.attrib["Type"]);

                                level1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Level1")
                                if level1 is not None:
                                    level1_type = self.cvt(level1.attrib["Type"]);
                                    level1_value = self.cvt(level1.attrib["Value"]);

                                note1 = address.find("{urn://x-artefacts-rosreestr-ru/commons/complex-types/address-output/3.0.1}Note")
                                if note1 is not None:
                                    note = self.cvt(note1.text);

                        category1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Category")
                        if category1 is not None:
                            category = self.cvt(category1.text);

                        utilization1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}Utilization")
                        if utilization1 is not None:
                            if "Utilization" in utilization1.attrib:
                                utilization_utilization = self.cvt(utilization1.attrib["Utilization"]);
                            if "ByDoc" in utilization1.attrib:
                                utilization_by_doc = self.cvt(utilization1.attrib["ByDoc"]);

                        cadastral_cost1 = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}CadastralCost")
                        if cadastral_cost1 is not None:
                            cadastral_cost_value = self.cvt(cadastral_cost1.attrib["Value"]);
                            cadastral_cost_unit = self.cvt(cadastral_cost1.attrib["Unit"]);

                        entity_spatial = parcel.find("{urn://x-artefacts-rosreestr-ru/outgoing/kpt/9.0.3}EntitySpatial")
                        parts = []
                        part_types = []
                        if entity_spatial is not None:
                            ent_sys = self.cvt(entity_spatial.attrib["EntSys"]);
                            for spatial_element in entity_spatial.iter(
                                    "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpatialElement"):
                                elem = []
                                parts.append(elem);
                                part_types2 = []
                                part_types.append(part_types2)
                                for spelement_unit in spatial_element.iter(
                                        "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}SpelementUnit"):
                                    # unit = []
                                    # parts.append(unit)
                                    for ordinate in spelement_unit.iter(
                                            "{urn://x-artefacts-rosreestr-ru/commons/complex-types/entity-spatial/2.0.1}Ordinate"):
                                        # specify coordinates in X,Y order (longitude, latitude)
                                        ord_nmb = self.cvt(ordinate.attrib["OrdNmb"])
                                        elem.append([float(self.cvt(ordinate.attrib['Y'])), float(self.cvt(ordinate.attrib['X']))])
                                        part_types2.append(shapefile.POLYGON)
                                        if hasattr(ordinate, "DeltaGeopoint"):
                                            delta_geopoint = self.cvt(ordinate.attrib["DeltaGeopoint"])
                                        coordinates_found = True
                                        # copy attributes

                        if ent_sys is not None and coordinates_found:
                            w = ww[ent_sys]
                            w.poly(parts=parts, partTypes=part_types)
                            notes = self.split_long_str(note)
                            utilization_by_docs = self.split_long_str(utilization_by_doc)
                            w.record(
                                self.cvt_utf8(cadastral_number),
                                self.cvt_utf8(state),
                                self.cvt_utf8(date_created),
                                self.cvt_utf8(area_area),
                                self.cvt_utf8(area_unit),
                                self.cvt_utf8(name),
                                self.cvt_utf8(location_in_bounds),
                                self.cvt_utf8(okato),
                                self.cvt_utf8(kladr),
                                self.cvt_utf8(region),
                                self.cvt_utf8(district_name),
                                self.cvt_utf8(district_type),
                                self.cvt_utf8(city_name),
                                self.cvt_utf8(city_type),
                                self.cvt_utf8(locality_name),
                                self.cvt_utf8(locality_type),
                                self.cvt_utf8(street_name),
                                self.cvt_utf8(street_type),
                                self.cvt_utf8(level1_type),
                                self.cvt_utf8(level1_value),
                                self.cvt_utf8(notes[0]),
                                self.cvt_utf8(notes[1]),
                                self.cvt_utf8(category),
                                self.cvt_utf8(utilization_utilization),
                                self.cvt_utf8(utilization_by_docs[0]),
                                self.cvt_utf8(cadastral_cost_value),
                                self.cvt_utf8(cadastral_cost_unit)
                            )
                            self.log("record: " + cadastral_number + ", " + coord_system_id)

        for coord_system_id in ww:
            w = ww[coord_system_id]
            if len(w.records) > 0:
                coordinate_system_code = coord_systems[coord_system_id]  # .decode()
                coord_system_projection = self.cs_aliases[coordinate_system_code]
                shape_file = target_dir_name + "/" + re.sub(".xml$", "", source_file_name) + "-" + coordinate_system_code + '.shp'
                w.save(shape_file)  # create the PRJ file
                with open(os.path.splitext(shape_file)[0] + os.extsep + 'prj', 'w') as prj:
                    self.log("Storing records for " + coord_system_id)
                    prj.write(coord_system_projection)
            else:
                self.log("No records found for " + coord_system_id)

    def extract_zip(self, source_dir_name, source_file_name):
        fh = open(source_dir_name + "/" + source_file_name, 'rb')
        z = zipfile.ZipFile(fh)
        for name in z.namelist():
            out_path = source_dir_name + "/" + source_file_name + ".unpacked"
            self.log("Extracting: " + name + ", " + out_path)
            z.extract(name, out_path)
        fh.close()

    def convert_dir(self, source_dir_name, target_dir_name):
        for file_name in os.listdir(source_dir_name):
            if file_name.endswith(".xml"):
                self.convert_kpt_xml_to_shape(source_dir_name, file_name, target_dir_name)
