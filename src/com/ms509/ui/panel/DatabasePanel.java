package com.ms509.ui.panel;

import com.ms509.model.ResultSetTableModel;
import com.ms509.ui.MainFrame;
import com.ms509.ui.MessageDialog;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private JTextPane commandlist;
	private JScrollPane dblistpane;
	private JScrollPane datalistpane;
	private JScrollPane commandpane;
	private JComboBox choosedb;
	private JComboBox commonsql;
	private JLabel status;
	private JLabel selectdb;
	private JLabel sql_list;

	private String tmp_sql_str = null;
	private int t_locker = 0;
	// 传递参数
	private String[] tmp;
	private String url;
	private String code;
	private String config;
	private String pass;
	private int type;
	private String id;
	private String dbn;

	public DatabasePanel() {
		// TODO Auto-generated constructor stub

		this.initparams();
		this.setComponent();

		String param = Common.makeParams("1");
		Common.send(Safe.ASP_MAKE, param, Safe.CODE);

	}

	public void test() {
		this.initparams();
		this.setComponent();
	}

	private void setComponent() {

		// Label 显示
		status = new JLabel("状态栏");
		selectdb = new JLabel("数据库");
		sql_list = new JLabel("常用sql");

		// 数据库jtree
		dblist = new JTree();
		dblistpane = new JScrollPane(dblist);
		DefaultTreeModel model = (DefaultTreeModel) dblist.getModel();
		model.setRoot(new DefaultMutableTreeNode(""));// 先初始化根节点，不初始化会显示更多的组件自带内容
		dblist.setShowsRootHandles(true);
		dblist.setRootVisible(false);

		choosedb = new JComboBox<>();
		RePainDBList();
		// 数据库查询结果窗口
		datalist = new JTable();
		datalistpane = new JScrollPane(datalist);
		datalist.setAutoResizeMode(datalist.AUTO_RESIZE_OFF);
		// datalist.setAutoResizeMode(datalist.AUTO_RESIZE_ALL_COLUMNS);
		// jtree右键菜单
		DBPopMenu m = new DBPopMenu(this, dblist, datalist);

		// 命令执行窗口
		commandlist = new JTextPane();
		commandpane = new JScrollPane(commandlist);
		commandlist.setText("");
		EnterListener enteraction = new EnterListener();
		commandlist.addKeyListener(enteraction);

		// 加载常用sql语句
		String[] sql_example = DataBase.Load_SQL();
		commonsql = new JComboBox<>(sql_example);
		SelectItem aListener = new SelectItem();
		commonsql.addActionListener(aListener);

		// 数据库配置按钮
		dbset = new JButton("数据库配置");
		OpenDBDialog action = new OpenDBDialog();
		dbset.addActionListener(action);

		// sql命令执行
		exec = new JButton("执行SQL(ctrl+回车)");
		Exec execation = new Exec();
		exec.addActionListener(execation);

		// 初始化布局
		this.setLayout(new GridBagLayout());
		GBC gbcleft = new GBC(0, 0, 2, 4).setFill(GBC.BOTH).setWeight(100, 0).setIpad(180, 0);
		// GBC gbcright = new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setInsets(0, 0,
		// 0, 0);

		// x.= 1
		GBC gbcright1 = new GBC(2, 0, 5, 1).setFill(GBC.BOTH).setWeight(100, 100);

		// x.= 2
		GBC gbcright2_1 = new GBC(2, 1, 1, 1).setFill(GBC.NONE).setWeight(40, 0);
		GBC gbcright2_2 = new GBC(3, 1, 1, 1).setFill(GBC.BOTH).setWeight(100, 0);
		GBC gbcright2_3 = new GBC(4, 1, 1, 1).setFill(GBC.NONE).setWeight(40, 0);
		GBC gbcright2_4 = new GBC(5, 1, 1, 1).setFill(GBC.BOTH).setWeight(100, 0);
		GBC gbcright2_5 = new GBC(6, 1, 1, 1).setFill(GBC.NONE).setWeight(0, 0);

		// x.= 3
		GBC gbcright3 = new GBC(2, 2, 8, 1).setFill(GBC.BOTH).setWeight(100, 50);

		// x. = 4
		GBC gbcright4_1 = new GBC(2, 3, 5, 1).setFill(GBC.BOTH).setWeight(100, 0);
		// GBC gbcright4_2 = new GBC(5, 3, 1, 1).setFill(GBC.NONE).setWeight(30,
		// 0);
		GBC gbcstatus = new GBC(0, 4, 9, 1).setFill(GBC.BOTH).setWeight(100, 0);

		// jtree
		this.add(dblistpane, gbcleft);

		// sql data output
		this.add(datalistpane, gbcright1);

		// select db
		this.add(selectdb, gbcright2_1);
		this.add(choosedb, gbcright2_2);
		// sql list
		this.add(sql_list, gbcright2_3);
		this.add(commonsql, gbcright2_4);

		// command pane
		this.add(commandpane, gbcright3);

		// button
		this.add(dbset, gbcright2_5);

		// exec
		this.add(exec, gbcright4_1);
		this.add(status, gbcstatus);

		// 初始化读取数据库

	}

	// 传递参数
	private void initparams() {
		String[] tmp = MainFrame.tab.getUrl().split("\t");
		id = tmp[0];
		url = tmp[1];
		pass = tmp[2];
		config = tmp[3];
		System.out.println("newdb=" + config);
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

	class OpenDBDialog implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetDBDialog dialog = new SetDBDialog(MainFrame.tab.getUrl().split("\t"));
			String k = dialog.getStr();
			System.out.println("回传" + k);
			config = k;

			RePainDBList();
		}

	}

	// sql text框 命令执行
	class Exec implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			tmp_sql_str = commandlist.getText();

			String dataname = GetSelectDB();
			System.out.println("选择数据" + dataname);

			// String result = DataBase.exec_sql(url, pass, config, type, code,
			// sql_query, dataname);
			// // System.out.println(result);
			// UpdateData(result);

			Thread_exec();

		}

	}

	// 数据库jtree点击事件
	class DoAction implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode ddd = new DefaultMutableTreeNode();
			ddd = (DefaultMutableTreeNode) dblist.getLastSelectedPathComponent();
			int pathcount = 0;
			try {
				pathcount = dblist.getSelectionPath().getPathCount();

				for (int m = 0; m < choosedb.getItemCount(); m++) // 选中jtree，设置下拉框中对应项
				{
					if (choosedb.getItemAt(m).toString().equals(ddd.toString())) {
						choosedb.setSelectedIndex(m);
					}
				}

				// System.out.println(pathcount);
			} catch (Exception k) {
				System.out.println("点击事件，未获取到count");
				pathcount = 0;
			}
			if (pathcount > 2) {

				if (e.getClickCount() == 2 && ddd.getChildCount() == 0) // 判断双击
				{
					System.out.println("执行总数查询");
					status.setText("正在查询");
					dbn = GetSelectDB();
					tmp_sql_str = "select * from " + dblist.getLastSelectedPathComponent().toString().replace("\t", "");
					Thread_exec();

					// tmp_sql_str = "";
				}
			} else {
				// 首次加载读表名
				if (e.getClickCount() == 2 && ddd.getChildCount() == 0) // 判断双击
				{
					Thread showtb = new Thread(new Runnable() {
						public void run() {
							try {
								DBPopMenu.showtable();
								dblist.expandRow(dblist.getLeadSelectionRow());
								t_locker = 0;
								status.setText("执行完毕");
							} catch (Exception e) {
								System.out.println(e);
								t_locker = 0;
								status.setText("error");
							}

						}
					});

					if (t_locker == 0) {
						t_locker = 1;
						if (config.equals("")) {
							status.setText("请先配置数据库");
							t_locker = 0;
						} else {
							showtb.start();
						}
					} else {
						status.setText("上次操作正在执行中...");
					}

				}
			}
			// ddd.getChildCount()>0

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

	class SelectItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String tmp = commonsql.getSelectedItem().toString();
			commandlist.setText(tmp);
		}

	}

	class EnterListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

			if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
				System.out.println("get");
				tmp_sql_str = commandlist.getText();
				System.out.println(tmp_sql_str);
				dbn = GetSelectDB();
				Thread_exec();
				commandlist.setText("");
			} else {
				tmp_sql_str = tmp_sql_str + e.getKeyChar();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private void Thread_exec() {
		Thread thread_exec = new Thread(new Runnable() {
			public void run() {
				status.setText("正在执行");
				String re = "";
				try {
					re = DataBase.exec_sql(url, pass, config, type, code, tmp_sql_str, dbn);
					UpdateData(re);
					status.setText("执行完毕");
					t_locker = 0;
				} catch (Exception e) {
					new MessageDialog(re,5);
					status.setText("error");
					t_locker = 0;
				}

			}
		});

		if (t_locker == 0) {
			t_locker = 1;
			if (config.equals("")) {
				status.setText("请先配置数据库");
				t_locker = 0;
			} else {
				thread_exec.start();
			}
		} else {
			status.setText("上次操作正在执行中...");
		}

	}

	private void RePainDBList() // 初始化读取数据库库名
	{
		Thread thread_Repaindblist = new Thread(new Runnable() {
			public void run() {// normal
				try {
					status.setText("正在执行...");
					String[] dbl = DataBase.getDBs(url, pass, config, type, code);

					for (int i = 0; i < dbl.length; i++) {
						choosedb.addItem(dbl[i]);
					}
					// 添加tree显示
					root = new DefaultMutableTreeNode("/");
					for (int d = 0; d < dbl.length; d++) // dbl[i]="database" 略去
					{
						root.add(new DefaultMutableTreeNode(dbl[d]));
					}
					DefaultTreeModel model = new DefaultTreeModel(root);
					dblist.setModel(model);

					// 添加tree点击事件
					DoAction caction = new DoAction();
					dblist.addMouseListener(caction);
					
					//给menu加载参数
					DBPopMenu.init_menu(url, pass, config, type, code);
					status.setText("完成");
				} catch (Exception e1) {
					System.out.println("异常，清空jtree");
					status.setText("连接数据库失败");
					root = new DefaultMutableTreeNode("/");
					root.removeAllChildren();
					DefaultTreeModel model = new DefaultTreeModel(root);
					dblist.setModel(model);
					dblist.updateUI();
					t_locker = 0;
				}
				// SwingUtilities.invokeLater(new Runnable() {
				// @Override
				// public void run() {
				//
				// }
				// });
				t_locker = 0;
			}
		});
		if (t_locker == 0) {
			t_locker = 1;
			if (config.equals("")) {
				status.setText("请先配置数据库");
				t_locker = 0;
			} else {
				thread_Repaindblist.start();
			}

		} else {
			status.setText("上次操作正在执行中...");
		}

	}

	private String GetSelectDB() {
		String dataname = "";
		System.out.println(dblist.getSelectionCount());
		if (dblist.getSelectionCount() > 0) {
			dataname = dblist.getLastSelectedPathComponent().toString();

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) dblist.getLastSelectedPathComponent();
			node = (DefaultMutableTreeNode) node.getParent();
			if (node.isRoot()) {
			} else {
				dataname = node.toString();
			}
		} else {
			dataname = "mysql";
			dataname = dblist.getModel().getChild(dblist.getModel().getRoot(), 0).toString();
			dblist.setSelectionPath(dblist.getPathForRow(1));

			for (int m = 0; m < choosedb.getItemCount(); m++) // 选中jtree，设置下拉框中对应项
			{
				if (choosedb.getItemAt(m).toString().equals(dataname)) {
					choosedb.setSelectedIndex(m);
				}
			}

		}

		dataname = dataname.replace("\t", "");
		return dataname;
	}

	// 根据result结果更新table的显示内容
	private void UpdateData(String result) {
		DefaultTableModel dtm = new DefaultTableModel();
		Vector<Object> al = new Vector<Object>();
		String[] rows = result.split("\t\\|\t\r\n");
		// System.out.println(rows[0]);
		// System.out.println("count="+rows.length);
		datalist.removeAll();
		Vector<Object> vtitle = new Vector<Object>();
		String[] dtitle = rows[0].split("\t\\|\t");
		int columns = dtitle.length;
		// System.out.println("columns" + columns);
		for (int k = 0; k < dtitle.length; k++) {
			vtitle.add(dtitle[k].replace("\t\\|\t", ""));
		}
		if (rows.length > 1) {
			for (int i = 1; i < rows.length; i++) {
				// System.out.println(rows[i]);
				String[] cols = rows[i].split("\t\\|\t");
				// System.out.println("cols=" + cols.length);
				Vector<String> vector = new Vector<String>();
				for (int m = 0; m < cols.length; m++) {
					// System.out.println("cols" + m + "=" + cols[m]);
					vector.add(cols[m].replace("\t\\|\t", ""));
				}
				al.add(vector);
			}
		}

		dtm.setDataVector(al, vtitle);

		datalist.setModel(dtm);
		datalistpane.updateUI();
	}

}
