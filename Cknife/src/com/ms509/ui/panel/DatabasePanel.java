package com.ms509.ui.panel;

import javax.swing.JPanel;

import com.ms509.ui.SetDBDialog;
import com.ms509.util.Common;
import com.ms509.util.GBC;
import com.ms509.util.Safe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class DatabasePanel extends JPanel {

	private JTextPane show_test; 
	private JPanel left;
	private JPanel right;
	private JButton exec;
	private JButton dbset;
	private ActionListener action;
	private JTree dblist;
	private JTable datalist;
	private JTextPane sqllist;
	private JScrollPane dblistpane;
	private JScrollPane datalistpane;
	private JScrollPane sqllistpane;

	public DatabasePanel() {
		// TODO Auto-generated constructor stub
		
		
		this.setComponent();
	
		String param = Common.makeParams("1");
		Common.send(Safe.ASP_MAKE, param, Safe.CODE);
		System.out.println("test");
		System.out.println("1"+param);

	
	}
	
	private void setComponent()
	{
		
		// 初始化布局和控件
		this.setLayout(new GridBagLayout());
		GBC gbcleft = new GBC(0, 0, 2, 3).setFill(GBC.BOTH).setWeight(
				100, 0);;
		//GBC gbcright = new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		
		GBC gbcright1 = new GBC(2, 0, 4, 1).setFill(GBC.BOTH).setWeight(
				100, 100);;
		GBC gbcright2 = new GBC(2, 1, 4, 1).setFill(GBC.BOTH).setWeight(
				100, 50);;
		GBC gbcright3 = new GBC(2, 2, 2, 1).setFill(GBC.BOTH).setWeight(
						100, 0);;
		GBC gbcright4 = new GBC(4, 2, 2, 1).setFill(GBC.BOTH).setWeight(
								100, 0);;

		dblist = new JTree();
		dblistpane = new JScrollPane(dblist);

		datalist = new JTable();
		datalistpane = new JScrollPane(datalist);
		//datalistpane.setPreferredSize(new Dimension(500,300));
		
		sqllist = new JTextPane();
		sqllistpane = new JScrollPane(sqllist);
		sqllist.setText("Test");
		
		dbset = new JButton("SET");
		OpenDBDialog action = new OpenDBDialog();
		dbset.addActionListener(action);
		
		exec = new JButton("exec");
		Exec execation = new Exec();
		exec.addActionListener(execation);

		
		this.add(dblistpane,gbcleft);
		this.add(datalistpane, gbcright1);
		this.add(sqllistpane, gbcright2);
		this.add(dbset, gbcright3);
		this.add(exec, gbcright4);

	}
	class OpenDBDialog implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetDBDialog dialog = new SetDBDialog();
		}
		
	}
	

	class Exec implements ActionListener
	{


		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String sql_query = sqllist.getText();
			System.out.println(sql_query);
		}

	}

}
