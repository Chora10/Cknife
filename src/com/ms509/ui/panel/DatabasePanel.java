package com.ms509.ui.panel;

import com.ms509.model.ResultSetTableModel;
import com.ms509.ui.MainFrame;
import com.ms509.ui.SetDBDialog;
import com.ms509.ui.menu.DBPopMenu;
import com.ms509.util.Common;
import com.ms509.util.DataBase;
import com.ms509.util.DbDao;
import com.ms509.util.GBC;
import com.ms509.util.Safe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DatabasePanel extends JPanel {

	private JTextPane show_test; 
	private JPanel left;
	private JPanel right;
	private JButton exec;
	private JButton dbset;
	private ActionListener action;
	private JTree dblist;
	private DefaultMutableTreeNode root;
	private JTable datalist;
	private JTextPane sqllist;
	private JScrollPane dblistpane;
	private JScrollPane datalistpane;
	private JScrollPane sqllistpane;
	private JComboBox choosedb;
	private JComboBox commonsql;
	
	//传递参数
	private String[] tmp;
	private String url;
	private String code;
	private String config;
	private String pass;
	private int type;
	private String id;
	
	public DatabasePanel() {
		// TODO Auto-generated constructor stub
		
		this.initparams();
		this.setComponent();
	
		
		
		String param = Common.makeParams("1");
		Common.send(Safe.ASP_MAKE, param, Safe.CODE);

	
	}
	
	private void setComponent()
	{
		
		// 初始化布局和控件
		this.setLayout(new GridBagLayout());
		GBC gbcleft = new GBC(0, 0, 2, 4).setFill(GBC.BOTH).setWeight(
				100, 0);;
		//GBC gbcright = new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		
		GBC gbcright1 = new GBC(2, 0, 4, 1).setFill(GBC.BOTH).setWeight(
				100, 100);
		
		GBC gbcright2 = new GBC(2, 1, 2, 1).setFill(GBC.BOTH).setWeight(
						100, 0);
		GBC gbcright3 = new GBC(2, 2, 4, 1).setFill(GBC.BOTH).setWeight(
				100, 50);
		GBC gbcright4 = new GBC(4, 1, 2, 1).setFill(GBC.BOTH).setWeight(
				100, 0);
		GBC gbcright5 = new GBC(2, 3, 2, 1).setFill(GBC.BOTH).setWeight(
								100, 0);;
		GBC gbcright6 = new GBC(4, 3, 2, 1).setFill(GBC.BOTH).setWeight(
								100, 0);;

		dblist = new JTree();
		dblistpane = new JScrollPane(dblist);

		
		datalist = new JTable();
		datalistpane = new JScrollPane(datalist);
//		datalistpane.setHorizontalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
//		datalistpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		datalist.setAutoResizeMode(datalist.AUTO_RESIZE_OFF);
		
		DBPopMenu m = new DBPopMenu(this,dblist,datalist);


		
		sqllist = new JTextPane();
		sqllistpane = new JScrollPane(sqllist);
		sqllist.setText("Test");
		
	
		//初始化读取数据库
		choosedb = new JComboBox<>();
		try{
			String[] dbl = DataBase.getDBs(url,pass,config,type,code);
			choosedb = new JComboBox<>(dbl);
			//添加tree显示
			root = new DefaultMutableTreeNode("/");
			for(int d=0;d<dbl.length;d++) //dbl[i]="database" 略去
			{
				root.add(new DefaultMutableTreeNode(dbl[d]));
			}
			DefaultTreeModel model = new DefaultTreeModel(root);
			dblist.setModel(model);
			
			//添加tree双击事件
			DoAction caction = new DoAction();
			dblist.addMouseListener(caction);
		}catch(Exception e)
		{
			System.out.println("error");
		}

		
		//常用语句
		String[] sql_example = {"show tables from [dataname];","sql2","sql3"};
		commonsql = new JComboBox<>(sql_example);
		SelectItem aListener = new SelectItem();		
		commonsql.addActionListener(aListener);
		
		dbset = new JButton("SET");
		OpenDBDialog action = new OpenDBDialog();
		dbset.addActionListener(action);
		
		exec = new JButton("exec");
		Exec execation = new Exec();
		exec.addActionListener(execation);

		
		this.add(dblistpane,gbcleft);
		this.add(datalistpane, gbcright1);
		this.add(choosedb, gbcright2);
		this.add(sqllistpane, gbcright3);
		this.add(commonsql, gbcright4);
		this.add(dbset, gbcright5);
		this.add(exec, gbcright6);

	}
	
	
	//传递参数
	private void initparams()
	{
		String[] tmp = MainFrame.tab.getUrl().split("\t");
		id = tmp[0];
		url = tmp[1];
		pass = tmp[2];
		config = tmp[3];
		//System.out.println(config);
		code = tmp[5];
		Safe.PASS = pass; // 初始化PASS常量
		// System.out.println(type);
		// 初始化脚本类型
		switch (tmp[4]) {
		case "JSP(Eval)":
			// System.out.println("jsp");
			type = 0;
			// this.jsp();
			break;
		case "PHP(Eval)":
			// System.out.println("php");
			type = 1;
			// this.php();
			break;
		case "ASP(Eval)":
			// System.out.println("asp");
			type = 2;
			// this.asp();
			break;
		case "ASPX(Eval)":
			// System.out.println("aspx");
			type = 3;
			break;
		}
		

	}
	

	class OpenDBDialog implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetDBDialog dialog = new SetDBDialog(MainFrame.tab.getUrl().split("\t"));
		}
		
	}
	

	class Exec implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String sql_query = sqllist.getText();
			String dataname = choosedb.getSelectedItem().toString();
			System.out.println("索引"+choosedb.getSelectedIndex());
			if(choosedb.getSelectedIndex()==0)
			{
				JOptionPane
				.showMessageDialog(MainFrame.main,
						"未选择数据库", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
			String result = DataBase.exec_sql(url,pass,config,type,code, sql_query, dataname);
			System.out.println(result);
			DefaultTableModel dtm = new DefaultTableModel();
			Vector<Object> al = new Vector<Object>();
			String[] rows = result.split("\\|\t\r\n");
			//System.out.println(rows[0]);
			System.out.println("count="+rows.length);
			datalist.removeAll();
			Vector<Object> vtitle = new Vector<Object>();
			String[] dtitle = rows[0].split("\t\\|\t");
			int columns = dtitle.length;
			
			for(int k=0;k<dtitle.length;k++)
			{
				//System.out.println(dtitle[k]);
				vtitle.add(dtitle[k].replace("\t\\|\t", ""));
			}
			
			if(rows.length>1)
			{
				for(int i=1;i<rows.length;i++)
				{
					//System.out.println(list[i]);
					String[] cols = rows[i].split("\t\\|\t");
					Vector<String> vector = new Vector<String>();
					for(int m =0;m<columns;m++)
					{
						//System.out.println("cols"+m+"="+cols[m]);
						vector.add(cols[m].replace("\t\\|\t", ""));
					}
					al.add(vector);
				}
			}
			
			dtm.setDataVector(al, vtitle);
			
			datalist.setModel(dtm);
			datalistpane.updateUI();

			
			
			System.out.println(sql_query);
		}

	}
	
	class DoAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode ddd = new DefaultMutableTreeNode();
			ddd = (DefaultMutableTreeNode) dblist.getLastSelectedPathComponent();
			int pathcount = 0;
			try {
				pathcount = dblist.getSelectionPath().getPathCount();
				//System.out.println(pathcount);
			} catch (Exception k) {
				System.out.println("点击事件，未获取到count");
				pathcount = 0;
			}
			if(pathcount>2)
			{
				System.out.println("执行总数查询");
			}else
			{
				//首次加载读表名
				if(e.getClickCount()==2 && ddd.getChildCount()==0)
				{
					DBPopMenu.showtable();
					dblist.expandRow(dblist.getLeadSelectionRow());
				}
			}
			//ddd.getChildCount()>0

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		
	}

	class SelectItem implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String tmp = commonsql.getSelectedItem().toString();
			if(tmp.indexOf("[dataname]")>=0)
			{
				tmp = tmp.replace("[dataname]", choosedb.getSelectedItem().toString());
			}
			sqllist.setText(tmp);
		}

		
	}
}
