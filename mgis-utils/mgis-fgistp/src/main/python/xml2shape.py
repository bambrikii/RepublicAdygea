#! /usr/bin/env python
# -*- coding: utf-8 -*-

from tkinter import *;
from tkinter import filedialog
import logging
from tkinter import INSERT

import rusregister_converter;


class WidgetLogger(logging.Handler):
    def __init__(self, widget):
        logging.Handler.__init__(self)
        self.widget = widget

    def emit(self, record):
        # Append message (record) to the widget
        self.widget.insert(INSERT, record + '\n')


class XmlToShapeConverterGUI:
    def __init__(self):
        pass

    def create_coord_systems_window(self, root):
        coord_systems_win = Toplevel(root)
        row = 0
        button = Button(coord_systems_win, text="Do nothing button")
        button.grid(row=row, column=0)
        return coord_systems_win

    def main_window(self):
        def select_dir(dir_var):
            dirname = filedialog.askdirectory(parent=root, initialdir=dir_var.get(), title='Please select a directory')
            if dirname:
                dir_var.set(dirname)

            return dir_var

        def start_conversion(logging_handler):
            converter = rusregister_converter.RusRegisterConverter(logging_handler)
            converter.convert_dir(source_directory_var.get(), target_directory_var.get())
            return ""

        def create_menu(parent_window):
            menu_bar = Menu(parent_window)
            menu_1 = Menu(menu_bar, tearoff=0)
            menu_1.add_command(label="Coordinate Systems", command=lambda: self.create_coord_systems_window(root=parent_window))
            menu_1.add_command(label="Exit", command=parent_window.quit)
            menu_bar.add_cascade(label="Main", menu=menu_1)
            return menu_bar

        root = Tk()

        for x in range(3):
            Grid.columnconfigure(root, x, weight=1)

        menu_bar = create_menu(parent_window=root)

        root.config(menu=menu_bar)
        source_directory_var = StringVar()
        target_directory_var = StringVar()

        row = 0
        Label(root, text="Converter", font="16").grid(row=row, column=1)

        row += 1
        Label(root, text="Source Directory").grid(row=row)
        Entry(root, textvariable=source_directory_var, width=80).grid(row=row, column=1)

        def select_source_directory():
            select_dir(source_directory_var)
            if target_directory_var.get() == "":
                target_directory_var.set(source_directory_var.get() + "/shape_files")

        Button(root, text="Select Source Directory", command=select_source_directory).grid(row=row, column=2)

        row += 1
        Label(root, text="Target Directory").grid(row=row)
        Entry(root, textvariable=target_directory_var, width=80).grid(row=row, column=1)
        Button(root, text="Select Source Directory", command=lambda: select_dir(target_directory_var)).grid(row=row, column=2)

        row += 1

        Grid.rowconfigure(root, row, weight=1)
        Label(root, text="Start").grid(row=row)
        text_frame = Frame(root)
        text_frame.grid(row=row, column=1)

        text = Text(text_frame)
        text_scrollbar = Scrollbar(text, command=text.yview)

        text.grid(row=0, column=0, sticky="nsew")
        text['yscrollcommand'] = text_scrollbar.set
        logger = WidgetLogger(text);

        Button(root, text="Start Conversion", command=lambda: start_conversion(logger)).grid(row=row, column=2)

        root.mainloop()


XmlToShapeConverterGUI().main_window()
