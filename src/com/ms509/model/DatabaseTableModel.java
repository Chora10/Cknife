package com.ms509.model;

import javax.swing.table.DefaultTableModel;

public class DatabaseTableModel extends DefaultTableModel {

	public Class getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}

	public boolean isCellEditable(int row, int column) {
		if(column == 0)
		{
			return false;
		} else 
		{
			return true;
		}
	}
}
