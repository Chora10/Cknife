package com.ms509.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * 通过重写方法实现直接读取数据并显示在表格
 * 继承AbstractTableModel没有add,remove等方法
 * 添加了add,remove方法
 * @author Chora
 */
public class ResultSetTableModel extends AbstractTableModel {
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	private Vector<String> title;
	private Vector<Vector<String>> datas;
	private int colcount = 0;

	public ResultSetTableModel(ResultSet rs) {
		try {
			this.rs = rs;
			this.rsmd = rs.getMetaData();
			this.colcount = this.rsmd.getColumnCount();
			title = new Vector<String>();		
			for (int j = 1; j <= this.colcount; j++) {
				title.add(this.rsmd.getColumnName(j));
			}
			title.set(1, "Url");
			title.set(6, "Ip");
			title.set(7, "Time");
			datas = new Vector<Vector<String>>();
			while (rs.next()) {
				Vector<String> data = new Vector<String>();
				for (int i = 1; i <= this.colcount; i++) {
					data.add(this.rs.getString(i));
				}
				datas.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return title.get(column);

	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return title.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		return datas.get(row).get(column);
	}

	public void addRow(Vector<String> vector) {
		this.datas.add(vector);
		this.fireTableRowsInserted(datas.size()-1,datas.size()-1);
	}

	public void update(String id, Vector<String> vector) {
		this.datas.set(this.getId(id), vector);
		this.fireTableDataChanged();
	}

	public void remove(String id) {
		try {
			int row = this.getId(id);
			this.datas.remove(row);
			this.fireTableRowsDeleted(row,row);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private int getId(String id) {
		int i = 0;
		for (Vector<String> data : this.datas) {
			if (data.get(0).equals(id)) {
				return i;
			}
			i++;
		}
		return i;
	}
}
