package com.ms509.ui.menu;
//数据库列表右键菜单

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.ms509.model.DatabaseTableModel;
import com.ms509.ui.MainFrame;
import com.ms509.ui.panel.DatabasePanel;
import com.ms509.ui.panel.FileManagerPanel;
import com.ms509.util.DataBase;
import com.ms509.util.NodeData;
import com.ms509.util.NodeData.DataType;
import com.ms509.util.Safe;
import com.ms509.util.TreeMethod;

public class DBPopMenu extends JPopupMenu {

	private JPopupMenu dbmenu, dbmenu2, dbmenu3;
	private JMenuItem createtable, deltable, countnum, showtable, copysingle, copyline, outfile;
	private static JTree tree;
	private static JTable table;
	private static String url;
	private static String pass;
	private static String config;
	private static String code;
	private static int type;
	private boolean status;
	private JLabel jlabel_status;


	public DBPopMenu(JPanel j, JTree tr, JTable ta, JLabel st) {
		// TODO Auto-generated constructor stub
		jlabel_status = st;
		status = true;
		
		//数据库名右键菜单:显示表信息
		dbmenu = new JPopupMenu();
		showtable = new JMenuItem("查看表信息");
		dbmenu.add(showtable);
		tree = tr;
		DoAction action = new DoAction();
		showtable.addActionListener(action);

		//数据库表名右键菜单
		dbmenu2 = new JPopupMenu();
		countnum = new JMenuItem("获取表行数");
		countnum.addActionListener(action);

		dbmenu2.add(countnum);

		j.add(dbmenu);
		j.add(dbmenu2);
		DBMenu l = new DBMenu();
		tree.addMouseListener(l);

		//数据库结果列表右键菜单
		dbmenu3 = new JPopupMenu();
		// System.out.println("t2");
		copysingle = new JMenuItem("复制");
		copysingle.addActionListener(action);
		copyline = new JMenuItem("复制整行");
		copyline.addActionListener(action);
		outfile = new JMenuItem("导出");
		outfile.addActionListener(action);

		dbmenu3.add(copysingle);
		dbmenu3.add(copyline);
		dbmenu3.add(outfile);
		table = ta;
		j.add(dbmenu3);
		TBmenu l2 = new TBmenu();
		table.addMouseListener(l2);

	}

	//设置传递数据库相关配置参数
	public static void init_menu(String u, String p, String conf, int t, String c) {
		url = u;
		pass = p;
		config = conf;
		type = t;
		code = c;
	}

	// 数据库列表菜单
	class DBMenu extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			TreePath index = tree.getPathForLocation(e.getX(), e.getY());
			tree.setSelectionPath(index);
			int pathcount = 0;
			try {
				pathcount = index.getPathCount();
			} catch (Exception k) {
				pathcount = 0;
			}
			//判断jtree路径，分别显示两个菜单
			if (e.isMetaDown() && pathcount > 2) {
				tree.setSelectionPath(index);
				dbmenu2.show(tree, e.getX(), e.getY());
			} else if (e.isMetaDown() && pathcount == 2) {
				tree.setSelectionPath(index);
				dbmenu.show(tree, e.getX(), e.getY());
			}
		}
	}

	// 结果列表菜单
	class TBmenu extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int row = table.rowAtPoint(e.getPoint());
			table.setRowSelectionInterval(row, row);
			if (e.isMetaDown() && table.getSelectedRow() >= 0) {
				dbmenu3.show(table, e.getX(), e.getY());
			}
		}
	}

	// 菜单点击事件
	class DoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (status) {   //状态锁
				if (e.getSource() == showtable) {   //显示表信息
					status = false;
					Thread run = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							showtable();
							
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									tree.expandRow(tree.getLeadSelectionRow());
								}
							});
							jlabel_status.setText("执行完毕");
							status = true;
						}
					});
					try {
						run.start();

					} catch (Exception e1) {
						status = true;
					}
				} else if (e.getSource() == outfile) {   //导出到文件
					status = false;
					Thread run = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									String name = "test.txt";
									FileManagerPanel filemanagerpanel = null;
									JFileChooser downch = new JFileChooser(".");
									downch.setDialogTitle("导出内容");
									downch.setSelectedFile(new File(name));
									int select = downch.showSaveDialog(filemanagerpanel);
									if (select == JFileChooser.APPROVE_OPTION) {
										try {

											TableModel model = table.getModel();
											File fw = downch.getSelectedFile();
											BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
											for (int i = 0; i < model.getColumnCount(); i++) {
												bw.write(model.getColumnName(i));
												bw.write("\t");
											}
											bw.newLine();
											for (int i = 0; i < model.getRowCount(); i++) {
												for (int j = 1; j < model.getColumnCount(); j++) {  //从1开始，0为图标文件
													bw.write(model.getValueAt(i, j).toString());
													bw.write("\t");
												}
												bw.newLine();
											}
											bw.close();
											jlabel_status.setText("导出成功");
											status = true;
										} catch (Exception e1) {
											filemanagerpanel.getStatus().setText("导出失败");
											status = true;
										}
									}
								}
							});

						}

					});
					try {
						run.start();

					} catch (Exception e1) {
						status = true;
					}
				} else if (e.getSource() == copysingle) {   //复制单个表格内容
					status = false;
					Thread run = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								TableModel model = table.getModel();
								int x = table.getSelectedRow();
								int y = table.getSelectedColumn();
								String k = model.getValueAt(x, y).toString();
								Clipboard clipboard;// 获取系统剪贴板。
								clipboard = MainFrame.main.getToolkit().getSystemClipboard();
								Transferable tText = new StringSelection(k);
								clipboard.setContents(tText, null);
								jlabel_status.setText("复制完毕");
								status = true;
							} catch (Exception e1) {
								// System.out.println("copy failed");
								status = true;
							}
						}

					});

					try {
						run.start();
					} catch (Exception e1) {
						status = true;
					}
				} else if (e.getSource() == copyline) {  //复制整行
					status = false;
					Thread run = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								TableModel model = table.getModel();
								int y = table.getSelectedRow();
								int x = table.getColumnCount();
								String k = "";
								for (int lx = 1; lx < x; lx++) {   //从1开始，0列为图标文件

									try {
										k = k + model.getValueAt(y, lx).toString() + "\t";
									} catch (Exception e1) {
										break;
									}

								}
								Clipboard clipboard;// 获取系统剪贴板。
								clipboard = MainFrame.main.getToolkit().getSystemClipboard();
								Transferable tText = new StringSelection(k);
								clipboard.setContents(tText, null);
								jlabel_status.setText("复制完毕");
								status = true;
							} catch (Exception e1) {
								status = true;
							}
						}

					});
					try {
						run.start();
					} catch (Exception e1) {
						status = true;
					}
				} else if (e.getSource() == countnum) {  //统计count信息
					Thread run = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							status = false;
							String sql = "select count(*) from ";
							TreePath test = tree.getLeadSelectionPath();
							String dbn = test.getPathComponent(1).toString().replace("\t", "");
							String table = tree.getLastSelectedPathComponent().toString();
							if (config.toLowerCase().indexOf("oracle") > 0) // oracle	由于cknife.jsp中未指定数据库名，所以查询需要带上数据库名
							{
								sql = sql + dbn + "." + table;
							} else {
								sql = sql + table;
							}
							final String re = DataBase.exec_sql(url, pass, config, type, code, sql, dbn);
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									UpdateData(re);
									jlabel_status.setText("执行完毕");
									status = true;
								}

							});
						}

					});

					try {
						run.start();
					} catch (Exception e1) {
						e1.printStackTrace();
						status = true;
					}

				}
			} else {
				jlabel_status.setText("上一操作尚未执行完毕");
			}
		}

	}

	// 根据result结果更新table的显示内容
	private void UpdateData(String result) {
		DatabaseTableModel dtm = new DatabaseTableModel();
		Vector<Object> al = new Vector<Object>();
		String[] rows = result.split("\t\\|\t\r\n");
		table.removeAll();
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
		table.setModel(dtm);

		int rowcount = table.getRowCount();
		int colcount = table.getColumnCount();
		DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
		if (rowcount == 0) {
			JTableHeader header = table.getTableHeader();
			TableColumnModel hmodel = header.getColumnModel();
			for (int k = 0; k < hmodel.getColumnCount(); k++) {
				TableColumn hcolumn = hmodel.getColumn(k);
				Object hvalue = hcolumn.getHeaderValue();
				TableCellRenderer hrend = header.getDefaultRenderer();
				Component hcomp = hrend.getTableCellRendererComponent(table, hvalue, false, false, 0, 0);
				int hwidth = (int) hcomp.getPreferredSize().getWidth();
				hcolumn.setPreferredWidth(hwidth);
			}
		}
		for (int i = 0; i < colcount; i++) {
			int maxwidth = 0;
			for (int j = 0; j < rowcount; j++) {
				Object value = table.getValueAt(j, i);
				Component comp = rend.getTableCellRendererComponent(table, value, false, false, 0, 0);
				int width = (int) comp.getPreferredSize().getWidth();
				TableColumnModel cmodel = table.getColumnModel();
				TableColumn column = cmodel.getColumn(i);
				maxwidth = Math.max(maxwidth, width);
				if (j == rowcount - 1) {
					Object hvalue = column.getHeaderValue();
					TableCellRenderer hrend = table.getTableHeader().getDefaultRenderer();
					Component hcomp = hrend.getTableCellRendererComponent(table, hvalue, false, false, 0, 0);
					int hwidth = (int) hcomp.getPreferredSize().getWidth();
					maxwidth = Math.max(maxwidth, hwidth);
				}
				column.setPreferredWidth(maxwidth + 1);
			}
		}
		TableColumn fcolumn = table.getColumnModel().getColumn(0);
		fcolumn.setMaxWidth(0);
	}

	//显示表信息
	public static void showtable() {
		Safe.PASS = pass; // 初始化PASS常量
		// 初始化脚本类型
		String dbn = tree.getLastSelectedPathComponent().toString();
		// tree显示向量
		DefaultTreeModel root = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		node.removeAllChildren(); // 清空当前节点下已有的节点
		String tables = DataBase.getTables(url, pass, config, type, code, dbn);
		//System.out.println(tables);
		// table 显示 向量
		final DatabaseTableModel dtm = new DatabaseTableModel();
		Vector<Object> al = new Vector<Object>();
		String[] rows = tables.split("\\|\t\r\n");
		table.removeAll();
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
				for (int m = 0; m < columns; m++) {
					if (m == 0) {
						vector.add(new ImageIcon("".getClass().getResource("/com/ms509/images/data.png")));
					}
					// 添加到向量vector中，后续加入到table里面显示
					vector.add(cols[m].replace("\t", ""));
					// 添加到tree parent节点中
					NodeData nd = new NodeData(DataType.TABLE, cols[m]);
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(nd);
					root.insertNodeInto(child, node, 0);
				}
				al.add(vector);
				dtm.setDataVector(al, vtitle);
			}
		} else // 没有读取到数据时执行。
		{
			dtm.setDataVector(null, vtitle);
		}
		table.setModel(dtm);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				int rowcount = table.getRowCount();
				int colcount = table.getColumnCount();
				DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
				if (rowcount == 0) {
					JTableHeader header = table.getTableHeader();
					TableColumnModel hmodel = header.getColumnModel();
					for (int k = 0; k < hmodel.getColumnCount(); k++) {
						TableColumn hcolumn = hmodel.getColumn(k);
						Object hvalue = hcolumn.getHeaderValue();
						TableCellRenderer hrend = header.getDefaultRenderer();
						Component hcomp = hrend.getTableCellRendererComponent(table, hvalue, false, false, 0, 0);
						int hwidth = (int) hcomp.getPreferredSize().getWidth();
						hcolumn.setPreferredWidth(hwidth);
					}
				}
				for (int i = 0; i < colcount; i++) {
					int maxwidth = 0;
					for (int j = 0; j < rowcount; j++) {
						Object value = table.getValueAt(j, i);
						Component comp = rend.getTableCellRendererComponent(table, value, false, false, 0, 0);
						int width = (int) comp.getPreferredSize().getWidth();
						TableColumnModel cmodel = table.getColumnModel();
						TableColumn column = cmodel.getColumn(i);
						maxwidth = Math.max(maxwidth, width);
						if (j == rowcount - 1) {
							Object hvalue = column.getHeaderValue();
							TableCellRenderer hrend = table.getTableHeader().getDefaultRenderer();
							Component hcomp = hrend.getTableCellRendererComponent(table, hvalue, false, false, 0, 0);
							int hwidth = (int) hcomp.getPreferredSize().getWidth();
							maxwidth = Math.max(maxwidth, hwidth);
						}
						column.setPreferredWidth(maxwidth + 1);
					}
				}
				TableColumn fcolumn = table.getColumnModel().getColumn(0);
				fcolumn.setMaxWidth(0);
			}
		});

	}
}
