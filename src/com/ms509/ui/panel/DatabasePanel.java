package com.ms509.ui.panel;
//数据库界面
import com.ms509.model.DatabaseTableModel;
import com.ms509.model.DatabaseTreeCellRenderer;
import com.ms509.ui.MainFrame;
import com.ms509.ui.MessageDialog;
import com.ms509.ui.SetDBDialog;
import com.ms509.ui.menu.DBPopMenu;
import com.ms509.util.Common;
import com.ms509.util.DataBase;
import com.ms509.util.GBC;
import com.ms509.util.NodeData;
import com.ms509.util.NodeData.DataType;
import com.ms509.util.Safe;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

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

	//界面布局
	private void setComponent() {

		// Label 显示
		status = new JLabel("状态栏");
		selectdb = new JLabel("数据库");
		sql_list = new JLabel("常用SQL语句");

		// 数据库jtree
		dblist = new JTree();
		dblistpane = new JScrollPane(dblist);
		dblistpane.setPreferredSize(new Dimension(25, 0));
		DefaultTreeModel model = (DefaultTreeModel) dblist.getModel();
		model.setRoot(new DefaultMutableTreeNode(""));// 先初始化根节点，不初始化会显示更多的组件自带内容
		dblist.setShowsRootHandles(true);
		dblist.setRootVisible(false);
		dblist.setCellRenderer(new DatabaseTreeCellRenderer());
		choosedb = new JComboBox<>();
		RePainDBList();
		// 数据库查询结果窗口
		datalist = new JTable();
		datalistpane = new JScrollPane(datalist);
		datalist.setAutoCreateRowSorter(true);
		datalist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		datalistpane.setPreferredSize(new Dimension(0, 0));
		// jtree右键菜单
		DBPopMenu m = new DBPopMenu(this, dblist, datalist, status);

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
		exec = new JButton("执行SQL(Ctrl+回车)");
		Exec execation = new Exec();
		exec.addActionListener(execation);

		// 初始化布局
		this.setLayout(new GridBagLayout());
		GBC gbcleft = new GBC(0, 0, 2, 4).setFill(GBC.VERTICAL).setWeight(0, 1).setIpad(200, 0);

		// x.= 1
		GBC gbcright1 = new GBC(2, 0, 5, 1).setFill(GBC.BOTH).setWeight(1, 0.7).setInsets(0, 5, 0, 0);

		// x.= 2
		GBC gbcright2_1 = new GBC(2, 1, 1, 1).setFill(GBC.NONE).setInsets(0, 5, 0, 0);
		GBC gbcright2_2 = new GBC(3, 1, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
		GBC gbcright2_3 = new GBC(4, 1, 1, 1).setFill(GBC.NONE);
		GBC gbcright2_4 = new GBC(5, 1, 1, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);
		GBC gbcright2_5 = new GBC(6, 1, 1, 1).setFill(GBC.NONE);

		// x.= 3
		GBC gbcright3 = new GBC(2, 2, 8, 1).setFill(GBC.BOTH).setWeight(1, 0.3).setInsets(0, 5, 0, 0);

		// x. = 4
		GBC gbcright4_1 = new GBC(2, 3, 5, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0).setInsets(0, 5, 0, 0);

		GBC gbcstatus = new GBC(0, 4, 9, 1).setFill(GBC.HORIZONTAL).setWeight(1, 0);

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
		// System.out.println("newdb=" + config);
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

	//打开数据库配置窗口
	class OpenDBDialog implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetDBDialog dialog = new SetDBDialog(MainFrame.tab.getUrl().split("\t"));
			String k = dialog.getStr();
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
			dbn = GetSelectDB();
			Thread_exec();

		}

	}

	// 数据库jtree点击事件
	class DoAction extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode ddd = new DefaultMutableTreeNode();
			ddd = (DefaultMutableTreeNode) dblist.getLastSelectedPathComponent();
			int pathcount = 0;
			TreePath tp = dblist.getSelectionPath();
			try {
				pathcount = tp.getPathCount();

				for (int m = 0; m < choosedb.getItemCount(); m++) // 选中jtree，设置下拉框中对应项
				{
					if (choosedb.getItemAt(m).toString().equals(ddd.toString())) {
						choosedb.setSelectedIndex(m);
					}
					else if(choosedb.getItemAt(m).toString().equals(dblist.getSelectionPath().getPathComponent(1).toString()))
					{
						choosedb.setSelectedIndex(m);
					}
				}
			} catch (Exception k) {
				// System.out.println("点击事件，未获取到count");
				pathcount = 0;
			}
			if (ddd != null) {
				if (pathcount > 2) {

					if (e.getClickCount() == 2 && ddd.getChildCount() == 0) // 判断双击
					{
						status.setText("正在查询...请稍等");
						dbn = GetSelectDB();
						tmp_sql_str = "select * from "
								+ dblist.getLastSelectedPathComponent().toString().replace("\t", "");
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
								showtb.start();
							}
						} else {
							status.setText("上一操作尚未执行完毕");
						}

					}
				}
			}
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

	//jbutton 数据库执行按钮事件
	class EnterListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
				tmp_sql_str = commandlist.getText();
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

	//sql语句执行 线程
	private void Thread_exec() {
		Thread thread_exec = new Thread(new Runnable() {
			private String re = "";

			public void run() {
				status.setText("正在读取...请稍等");
				try {
					re = DataBase.exec_sql(url, pass, config, type, code, tmp_sql_str, dbn);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								UpdateData(re);
							} catch (Exception e) {
							}
							status.setText("完成");
							t_locker = 0;
						}
					});
				} catch (Exception e) {
					new MessageDialog(re, 5);
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
			status.setText("上一操作尚未执行完毕");
		}

	}

	// 初始化读取数据库库名
	private void RePainDBList() 
	{
		Thread thread_Repaindblist = new Thread(new Runnable() {
			public void run() {// normal
				try {
					status.setText("正在连接...请稍等");
					String[] dbl = DataBase.getDBs(url, pass, config, type, code);
					for (int i = 0; i < dbl.length; i++) {
						choosedb.addItem(dbl[i]);
					}
					// 添加tree显示
					NodeData n = new NodeData(DataType.DATABASE, "/");
					root = new DefaultMutableTreeNode(n);
					for (int d = 0; d < dbl.length; d++) // dbl[i]="database" 略去
					{
						NodeData nd = new NodeData(DataType.DATABASE, dbl[d]);
						DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(nd);
						root.add(dmtn);
					}
					DefaultTreeModel model = new DefaultTreeModel(root);
					dblist.setModel(model);
					// 添加tree点击事件
					DoAction caction = new DoAction();
					dblist.addMouseListener(caction);

					// 给menu加载参数
					DBPopMenu.init_menu(url, pass, config, type, code);
					status.setText("完成");
				} catch (Exception e1) {
					//System.out.println("异常，清空jtree");
					status.setText("连接数据库失败");
					root = new DefaultMutableTreeNode("/");
					root.removeAllChildren();
					//DefaultTreeModel model = new DefaultTreeModel(root);
					//dblist.setModel(model);
					// dblist.updateUI();
					t_locker = 0;
				}
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
			status.setText("上一次操作尚未执行完毕");
		}

	}

	//获取选择数据库库名，用于传递dbn参数执行sql语句
	private String GetSelectDB() {
		String dataname = "";
		String dataname2 = "";
		dataname2 = choosedb.getSelectedItem().toString();
		dataname2 = dataname2.replace("\t", "");
		return dataname2;
	}

	// 根据result结果更新table的显示内容
	private void UpdateData(String result) {
		DatabaseTableModel dtm = new DatabaseTableModel();
		Vector<Object> al = new Vector<Object>();
		String[] rows = result.split("\t\\|\t\r\n");
		datalist.removeAll();
		Vector<Object> vtitle = new Vector<Object>();
		vtitle.add("");
		String[] dtitle = rows[0].split("\t\\|\t");
		int columns = dtitle.length;
		for (int k = 0; k < dtitle.length; k++) {
			vtitle.add(dtitle[k].replace("\t\\|\t", ""));
		}
		if (rows.length > 1) {
			for (int i = 1; i < rows.length; i++) {
				String[] cols = rows[i].split("\t\\|");
				Vector<Object> vector = new Vector<Object>();
				for (int m = 0; m < cols.length; m++) {
					if (m == 0) {
						vector.add(new ImageIcon(getClass().getResource("/com/ms509/images/data.png")));
					}
					vector.add(cols[m].replace("\t", ""));

				}
				al.add(vector);
				dtm.setDataVector(al, vtitle);
			}
		} else // 没有读取到数据时执行。
		{
			dtm.setDataVector(null, vtitle);
		}
		datalist.setModel(dtm);

		int rowcount = datalist.getRowCount();
		int colcount = datalist.getColumnCount();
		DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
		if (rowcount == 0) {
			JTableHeader header = datalist.getTableHeader();
			TableColumnModel hmodel = header.getColumnModel();
			for (int k = 0; k < hmodel.getColumnCount(); k++) {
				TableColumn hcolumn = hmodel.getColumn(k);
				Object hvalue = hcolumn.getHeaderValue();
				TableCellRenderer hrend = header.getDefaultRenderer();
				Component hcomp = hrend.getTableCellRendererComponent(datalist, hvalue, false, false, 0, 0);
				int hwidth = (int) hcomp.getPreferredSize().getWidth();
				hcolumn.setPreferredWidth(hwidth);
			}
		}
		for (int i = 0; i < colcount; i++) {
			int maxwidth = 0;
			for (int j = 0; j < rowcount; j++) {
				Object value = datalist.getValueAt(j, i);
				Component comp = rend.getTableCellRendererComponent(datalist, value, false, false, 0, 0);
				int width = (int) comp.getPreferredSize().getWidth();
				TableColumnModel cmodel = datalist.getColumnModel();
				TableColumn column = cmodel.getColumn(i);
				maxwidth = Math.max(maxwidth, width);
				if (j == rowcount - 1) {
					Object hvalue = column.getHeaderValue();
					TableCellRenderer hrend = datalist.getTableHeader().getDefaultRenderer();
					Component hcomp = hrend.getTableCellRendererComponent(datalist, hvalue, false, false, 0, 0);
					int hwidth = (int) hcomp.getPreferredSize().getWidth();
					maxwidth = Math.max(maxwidth, hwidth);
				}
				column.setPreferredWidth(maxwidth + 1);
			}
		}
		TableColumn fcolumn = datalist.getColumnModel().getColumn(0);
		fcolumn.setMaxWidth(0);
	}
}
