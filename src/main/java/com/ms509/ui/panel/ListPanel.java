package com.ms509.ui.panel;

import java.awt.GridBagLayout;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ms509.model.ResultSetTableModel;
import com.ms509.ui.menu.ListPopMenu;
import com.ms509.util.Configuration;
import com.ms509.util.DbDao;
import com.ms509.util.GBC;
import com.ms509.util.Safe;

public class ListPanel extends JPanel {
	private JTable list;
	private ResultSetTableModel model;
	private JLabel status;
	public JTable getList() {
		return list;
	}
	public void setList(JTable list) {
		this.list = list;
	}
	public ResultSetTableModel getModel() {
		return model;
	}
	public void setModel(ResultSetTableModel model) {
		this.model = model;
	}
	public JLabel getStatus() {
		return status;
	}
	public void setStatus(JLabel status) {
		this.status = status;
	}
	
	public ListPanel() {
		// TODO Auto-generated constructor stub
		this.setLayout(new GridBagLayout());
		GBC gbclist = new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100);
		GBC gbcbar = new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0);
		JTable list = new JTable();

		list.setAutoCreateRowSorter(true);
		ResultSet rs = null;
		try {
			rs = DbDao.getInstance().getStmt()
					.executeQuery("select * from data");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model = new ResultSetTableModel(rs);
		list.setModel(model);

		TableColumnModel cmodel = list.getColumnModel();
		TableColumn tableurl = cmodel.getColumn(1);
		tableurl.setMinWidth(500);
		int[] arr = { 0, 2, 3, 4, 5 };
		for (int i : arr) {
			TableColumn column = cmodel.getColumn(i);
			column.setMinWidth(0);
			column.setMaxWidth(0);
		}
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		status = new JLabel("完成");
		JScrollPane listPane = new JScrollPane(list);
		new ListPopMenu(this, listPane);
		new ListPopMenu(this, list);
		// listPane.setComponentPopupMenu(new ListPopMenu(this, listPane));
		// //swing弹出菜单,不需要awt监听事件。
		// list.setComponentPopupMenu(new ListPopMenu(this, list));
		this.add(listPane, gbclist);
		bar.add(status);
		this.add(bar, gbcbar);
	}
	
}
