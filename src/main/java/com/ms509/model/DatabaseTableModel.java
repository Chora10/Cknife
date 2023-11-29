package com.ms509.model;

import javax.swing.table.DefaultTableModel;

public class DatabaseTableModel extends DefaultTableModel {

	public Class getColumnClass(int columnIndex) {
		if (this.getRowCount() == 0) {
			return super.getColumnClass(columnIndex);
		} else {
			return getValueAt(0, columnIndex).getClass();
		}
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
