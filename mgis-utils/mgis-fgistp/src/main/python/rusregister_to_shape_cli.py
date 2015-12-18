#! /usr/bin/env python
# -*- coding: utf-8 -*-

import os;
import sys;

import rusregister_converter;

dir_name = sys.argv[1]
shape_dir_name = dir_name + "/" + "shp"
if not os.path.exists(shape_dir_name):
    os.makedirs(shape_dir_name)

converter = rusregister_converter.RusRegisterConverter(None)

if len(sys.argv) > 2:
    for i in range(2, len(sys.argv)):
        source_file_name = sys.argv[i]
        if source_file_name.endswith(".zip"):
            converter.extract_zip(dir_name, source_file_name, shape_dir_name)
        else:
            if source_file_name.endswith(".xml"):
                converter.convert_kpt_xml_to_shape(dir_name, source_file_name, shape_dir_name)
else:
    converter.convert_dir(dir_name, shape_dir_name)
