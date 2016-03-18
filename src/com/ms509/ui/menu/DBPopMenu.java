package com.ms509.ui.menu;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.ms509.ui.MainFrame;
import com.ms509.ui.panel.FileManagerPanel;
import com.ms509.util.DataBase;
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
	
	
	// jtree 菜单
	public DBPopMenu(JPanel j, JTree tr, JTable ta) {
		// TODO Auto-generated constructor stub

		dbmenu = new JPopupMenu();
		showtable = new JMenuItem("查看表信息");
		dbmenu.add(showtable);
		tree = tr;
		DoAction action = new DoAction();
		showtable.addActionListener(action);

		dbmenu2 = new JPopupMenu();
		countnum = new JMenuItem("获取表行数(暂无)");
		createtable = new JMenuItem("创建表(暂无)");
		deltable = new JMenuItem("删除表(暂无)");

		dbmenu2.add(countnum);
		dbmenu2.add(createtable);
		dbmenu2.add(deltable);

		j.add(dbmenu);
		j.add(dbmenu2);
		DBMenu l = new DBMenu();
		tree.addMouseListener(l);

		dbmenu3 = new JPopupMenu();
		System.out.println("t2");
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

	
	public static void init_menu(String u,String p,String conf,int t,String c)
	{
		url = u;
		pass = p;
		config = conf;
		type = t;
		code = c;
	}
	// 数据库列表菜单
	class DBMenu implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// System.out.println("11");
			TreePath index = tree.getPathForLocation(e.getX(), e.getY());
			tree.setSelectionPath(index);
			// System.out.println(tree.getSelectionCount());
			int pathcount = 0;
			try {
				pathcount = tree.getSelectionPath().getPathCount();
				// System.out.println(pathcount);
			} catch (Exception k) {
				System.out.println("点击事件，未获取到count");
				pathcount = 0;
			}
			if (e.isMetaDown() && tree.getSelectionPath() != null && pathcount > 2) {
				// System.out.println(tree.getSelectionPath());
				dbmenu2.show(tree, e.getX(), e.getY());
			} else if (e.isMetaDown() && tree.getSelectionPath() != null && pathcount == 2) {
				dbmenu.show(tree, e.getX(), e.getY());
			}

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

	// 列表菜单
	class TBmenu implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.isMetaDown() && table.getSelectedRow() >= 0) {
				dbmenu3.show(table, e.getX(), e.getY());
			}
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

	// 菜单点击事件
	class DoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == showtable) {
				showtable();
				tree.expandRow(tree.getLeadSelectionRow());
			} 
			else if (e.getSource() == outfile) {
				System.out.println("导出");
				String abpath = "test";
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
							for (int j = 0; j < model.getColumnCount(); j++) {
								bw.write(model.getValueAt(i, j).toString());
								bw.write("\t");
							}
							bw.newLine();
						}
						bw.close();

					} catch (Exception e1) {
						filemanagerpanel.getStatus().setText("导出失败");
					}
				}
			}
			else if(e.getSource()==copysingle)
			{
				try
				{
					TableModel model = table.getModel();
					int x = table.getSelectedRow();
					int y  =table.getSelectedColumn();
					String k = model.getValueAt(x, y).toString();
					System.out.println("select = "+k);
					
					Clipboard clipboard;//获取系统剪贴板。
					
					clipboard = MainFrame.main.getToolkit().getSystemClipboard();
					Transferable tText = new StringSelection(k);
					clipboard.setContents(tText, null);
				} catch (Exception e1) {
					System.out.println("copy failed");
				}
			}else if(e.getSource()==copyline)
			{
				try
				{
					TableModel model = table.getModel();
					//System.out.println(table.getSelectedColumn());
					int y = table.getSelectedRow();
					int x  =table.getColumnCount();
					System.out.println("x="+x+",y="+y);
					String k = "";
					for(int lx =0;lx<x;lx++)
					{
						
						try
						{
							k= k+ model.getValueAt(y,lx).toString()+"\t";
							System.out.println("select = "+k);
						}catch(Exception e1)
						{
							break;
						}
						
					}
					//System.out.println("select = "+k);
					
					Clipboard clipboard;//获取系统剪贴板。
					
					clipboard = MainFrame.main.getToolkit().getSystemClipboard();
					Transferable tText = new StringSelection(k);
					clipboard.setContents(tText, null);
				} catch (Exception e1) {
					System.out.println("copy failed");
				}
			}

		}

	}

	public static void showtable() {
		System.out.println("显示指定库表名");
		Safe.PASS = pass; // 初始化PASS常量

		// System.out.println(type);
		// 初始化脚本类型

		String dbn = tree.getLastSelectedPathComponent().toString();
		
		// tree显示向量
		DefaultTreeModel root = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		System.out.println("childcount=" + node.getChildCount());
		node.removeAllChildren(); // 清空当前节点下已有的节点
		
		
		//
		String tables = DataBase.getTables(url, pass, config, type, code, dbn);
		System.out.println(tables);



		// tree.expandPath(new TreePath(node));

		// System.out.println("nodecount="+node.remove(0);

		// table 显示 向量
		final DefaultTableModel dtm = new DefaultTableModel();
		Vector<Object> al = new Vector<Object>();
		String[] rows = tables.split("\\|\t\r\n");
		// System.out.println(rows[0]);
		System.out.println("count=" + rows.length);
		table.removeAll();
		Vector<Object> vtitle = new Vector<Object>();
		String[] dtitle = rows[0].split("\t\\|\t");
		int columns = dtitle.length;

		for (int k = 0; k < dtitle.length; k++) {
			// System.out.println(dtitle[k]);
			vtitle.add(dtitle[k].replace("\t\\|\t", ""));
		}

		if (rows.length > 1) {
			for (int i = 1; i < rows.length; i++) {
				// System.out.println(list[i]);
				String[] cols = rows[i].split("\t\\|\t");
				Vector<String> vector = new Vector<String>();
				for (int m = 0; m < columns; m++) {
					// System.out.println("cols"+m+"="+cols[m]);
					// 添加到向量vector中，后续加入到table里面显示
					vector.add(cols[m].replace("\t\\|\t", ""));
					// 添加到tree parent节点中
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(cols[m]);
					root.insertNodeInto(child, node, 0);
				}
				al.add(vector);
			}
		}

		dtm.setDataVector(al, vtitle);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				table.setModel(dtm);

				tree.updateUI();
			}
		});

	}
}
