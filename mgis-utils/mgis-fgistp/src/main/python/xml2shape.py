#! /usr/bin/env python
# -*- coding: utf-8 -*-

from Tkinter import *;
import tkFileDialog;


def create_coord_systems_window(root):
    coord_systems_win = Toplevel(root)
    button = Button(coord_systems_win, text="Do nothing button")
    button.pack()
    return coord_systems_win


def main_window():
    def select_dir(dir_var):
        dirname = tkFileDialog.askdirectory(parent=root, initialdir=dir_var.get(), title='Please select a directory')
        if dirname:
            dir_var.set(dirname)

        return dir_var

    def start_conversion():
        return ""

    def create_menu(root):
        menu_bar = Menu(root)
        menu_1 = Menu(menu_bar, tearoff=0)
        menu_1.add_command(label="Coordinate Systems", command=lambda: create_coord_systems_window(root=root))
        menu_1.add_command(label="Exit", command=root.quit)
        menu_bar.add_cascade(label="Main", menu=menu_1)
        return menu_bar

    root = Tk()

    #
    menu_bar = create_menu(root=root)

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
    Label(root, text="Start").grid(row=row)
    Button(root, text="Start Conversion", command=start_conversion).grid(row=row, column=2)

    root.mainloop()


main_window()
