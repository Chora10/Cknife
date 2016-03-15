package com.ms509.ui.menu;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.ms509.model.RightTableModel;
import com.ms509.ui.MainFrame;
import com.ms509.ui.panel.FileManagerPanel;
import com.ms509.ui.panel.TextPanel;
import com.ms509.util.Common;
import com.ms509.util.Request;
import com.ms509.util.Safe;
import com.ms509.util.TreeMethod;
import com.sun.corba.se.spi.ior.MakeImmutable;
import com.sun.imageio.plugins.common.InputStreamAdapter;

public class FileManagerPopMenu extends JPopupMenu {
	private JMenuItem upfile, downfile, openfile, rename, delete, addfile,
			adddict;

	public FileManagerPopMenu(String type) {
		// TODO Auto-generated constructor stub
		MenuAction menu = new MenuAction();
		if (type.equals("select")) {
			upfile = new JMenuItem("上传");
			downfile = new JMenuItem("下载");
			openfile = new JMenuItem("打开");
			rename = new JMenuItem("重命名");
			delete = new JMenuItem("删除");
			JMenu addfiledict = new JMenu("新建");
			addfile = new JMenuItem("文件");
			adddict = new JMenuItem("文件夹");
			this.add(upfile);
			this.add(downfile);
			this.addSeparator();
			this.add(openfile);
			this.add(rename);
			this.add(delete);
			addfiledict.add(addfile);
			addfiledict.add(adddict);
			this.add(addfiledict);
			upfile.addActionListener(menu);
			downfile.addActionListener(menu);
			openfile.addActionListener(menu);
			rename.addActionListener(menu);
			delete.addActionListener(menu);
			addfile.addActionListener(menu);
			adddict.addActionListener(menu);
		} else {
			upfile = new JMenuItem("上传");
			JMenu addfiledict = new JMenu("新建");
			addfile = new JMenuItem("文件");
			adddict = new JMenuItem("文件夹");
			this.add(upfile);
			this.addSeparator();
			addfiledict.add(addfile);
			addfiledict.add(adddict);
			this.add(addfiledict);
			upfile.addActionListener(menu);
			addfile.addActionListener(menu);
			adddict.addActionListener(menu);
		}
	}

	class MenuAction implements ActionListener {
		private FileManagerPanel filemanagerpanel;
		private JTable list;
		private JTextField path;
		private RightTableModel model;
		private String abpath = "";

		@Override
		public void actionPerformed(final ActionEvent e) {
			filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
			this.list = filemanagerpanel.getList();
			this.path = filemanagerpanel.getPath();
			this.model = filemanagerpanel.getListmodel();
			this.model.setFilemanagerpanel(filemanagerpanel);
			// TODO Auto-generated method stub

			switch (e.getActionCommand()) {
			case "上传":
				JFileChooser upch = new JFileChooser(".");
				upch.setApproveButtonText("上传");
				upch.setDialogTitle("上传文件到服务器");
				String filename = "";
				int act = upch.showOpenDialog(filemanagerpanel);
				byte[] udata = null;
				if (act == JFileChooser.APPROVE_OPTION) {
					FileInputStream fis;
					try {
						File select = upch.getSelectedFile();
						filename = select.getName();
						fis = new FileInputStream(select);
						byte[] b = new byte[fis.available()];
						fis.read(b);
						udata = b;
						abpath = path.getText() + filename;
						String data = filemanagerpanel.getFm().doAction(
								"upload", abpath, Common.toHex(udata));
						filemanagerpanel.showRight(
								Common.getAbsolutePath(abpath), list);
						if (data.equals("1")) {
							filemanagerpanel.getStatus().setText("上传成功");
							filemanagerpanel.showRight(
									Common.getAbsolutePath(abpath), list);
						} else {
							filemanagerpanel.getStatus().setText("上传失败");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
					}
				}
				break;
			case "打开":
				String type = list.getValueAt(list.getSelectedRow(), 0)
						.toString();
				if (type.indexOf("folder.png") > -1) {
					String name = list.getValueAt(list.getSelectedRow(), 1)
							.toString();
					abpath = Common.autoPath(path.getText()) + name
							+ Safe.SYSTEMSP;
					path.setText(abpath);
					filemanagerpanel.showRight(abpath, list);
					DefaultMutableTreeNode tn = TreeMethod.searchNode(
							filemanagerpanel.getRoot(), name);
					TreePath tp = new TreePath(tn.getPath());
					if (tp != null) {
						filemanagerpanel.showLeft(tp);
					}
				} else if (type.indexOf("file.png") > -1) {
					abpath = path.getText()
							+ list.getValueAt(list.getSelectedRow(), 1);
					TextPanel text = (TextPanel) MainFrame.tab.addPanel("text");
					String data = filemanagerpanel.getFm().doAction("readfile",
							abpath);
					text.getPath().setText(abpath);
					text.getText().setText(data);
				}
				break;
			case "文件":
				abpath = path.getText() + "newFile.txt";
				TextPanel text = (TextPanel) MainFrame.tab.addPanel("text");
				text.getPath().setText(abpath);
				text.getButton().setText("新建");
				break;
			case "删除":
				abpath = path.getText()
						+ list.getValueAt(list.getSelectedRow(), 1);
				String data = filemanagerpanel.getFm().doAction("delete",
						abpath);
				if (data.equals("1")) {
					filemanagerpanel.getStatus().setText("删除成功");
					filemanagerpanel.showRight(Common.getAbsolutePath(abpath),
							list);
				} else {
					filemanagerpanel.getStatus().setText("删除失败");
				}
				break;
			case "重命名":
				filemanagerpanel.getStatus().setText("正在重命名...");
				model.setEdit(true);
				list.editCellAt(list.getSelectedRow(), 1);
				model.setEdit(false);
				break;
			case "文件夹":
				Vector vector = new Vector<>();
				vector.add(new ImageIcon(getClass().getResource(
						"/com/ms509/images/folder.png")));
				vector.add("newFolder");
				vector.add(Common.getTime());
				vector.add("0");
				vector.add("0");
				model.addRow(vector);
				model.fireTableDataChanged();
				model.setEdit(true);
				list.editCellAt(model.getRowCount() - 1, 1);
				model.setEdit(false);
				break;
			case "下载":
				String name = list.getValueAt(list.getSelectedRow(), 1)
						.toString();
				abpath = path.getText() + name;
				JFileChooser downch = new JFileChooser(".");
				downch.setDialogTitle("下载文件到本地");
				downch.setSelectedFile(new File(name));
				int select = downch.showSaveDialog(filemanagerpanel);
				if (select == JFileChooser.APPROVE_OPTION) {
					try {
						byte[] bytes = filemanagerpanel.getFm()
								.Download(abpath);
						if (bytes != null) {
							File f = downch.getSelectedFile();
							FileOutputStream fos = new FileOutputStream(f);
							fos.write(bytes, Safe.SPL.length(), bytes.length
									- (Safe.SPL.length() + Safe.SPR.length()));
							filemanagerpanel.getStatus().setText("下载完成");
						}
					} catch (Exception e1) {
						filemanagerpanel.getStatus().setText("下载失败");
					}
				}
				break;
			}
		}
	}

	public class MouseAction extends MouseAdapter {
		private FileManagerPanel filemanagerpanel;
		private JTable list;
		private JTextField path;
		private JScrollPane listpane;

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
			this.list = filemanagerpanel.getList();
			this.path = filemanagerpanel.getPath();
			this.listpane = filemanagerpanel.getListpane();
			if (e.getButton() == MouseEvent.BUTTON3) {
				show(listpane, e.getX(), e.getY());
			}
		}
	}

	public class SelectedMouseAction extends MouseAdapter {
		private FileManagerPanel filemanagerpanel;
		private JTable list;
		private JTextField path;
		private JScrollPane listpane;
		private String data;

		@Override
		public void mousePressed(MouseEvent e) {
			filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
			this.list = filemanagerpanel.getList();
			this.path = filemanagerpanel.getPath();
			this.listpane = filemanagerpanel.getListpane();
			// TODO Auto-generated method stub

			if (e.getButton() == MouseEvent.BUTTON3) {
				int row = list.rowAtPoint(e.getPoint());
				list.setRowSelectionInterval(row, row);
				show(list, e.getX(), e.getY());
			} else if (e.getClickCount() == 2) {
				String type = list.getValueAt(list.getSelectedRow(), 0)
						.toString();

				if (type.indexOf("folder.png") > -1) {
					if(filemanagerpanel.isLstatus() && filemanagerpanel.isRstatus())
					{
					filemanagerpanel.setLstatus(false);
					filemanagerpanel.setRstatus(false);
					filemanagerpanel.getStatus().setText("正在读取...请稍等");
					String name = list.getValueAt(list.getSelectedRow(),
							1).toString();
					final String abpath = Common.autoPath(path.getText())
							+ name + Safe.SYSTEMSP;
					filemanagerpanel.showRight(abpath, list);
					DefaultMutableTreeNode tn = TreeMethod.searchNode(
							filemanagerpanel.getRoot(), name);
					if (tn != null) {
						TreePath tp = new TreePath(tn.getPath());
						filemanagerpanel.showLeft(tp);
					}
					Runnable run = new Runnable() {
						@Override
						public void run() {
							while (true) {
								Thread.yield();
								if (filemanagerpanel.isLstatus() && filemanagerpanel.isRstatus()) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											path.setText(abpath);
											filemanagerpanel.getStatus().setText("完成");
										}
									});
									break;
								}
							}
						}
					};
					new Thread(run).start();
					} else 
					{
						filemanagerpanel.getStatus().setText("上一操作尚未执行完毕");
					}
				} else if (type.indexOf("file.png") > -1) {
					final String abpath = path.getText()
							+ list.getValueAt(list.getSelectedRow(), 1);
					final TextPanel text = (TextPanel) MainFrame.tab
							.addPanel("text");
					text.getStatus().setText("正在读取...请稍等");
					text.getText().setText("读取中...");
					text.getPath().setText(abpath);
					Runnable run = new Runnable() {
						@Override
						public void run() {
							data = filemanagerpanel.getFm().doAction(
									"readfile", abpath);
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									text.getText().setText(data);
									text.getStatus().setText("完成");								
								}
							});
						}
					};
					new Thread(run).start();
				}

			}

		}
	}

	public class ReadAction implements ActionListener {
		public JTable list;
		public JTextField path;

		public ReadAction(JTable list, JTextField path) {
			this.list = list;
			this.path = path;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			final FileManagerPanel filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
			String name = Common.getName(path.getText());
			final DefaultMutableTreeNode tn = TreeMethod.searchNode(
					filemanagerpanel.getRoot(), name);
			Runnable run = new Runnable() {
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (tn != null) {
								TreePath tp = new TreePath(tn.getPath());
								filemanagerpanel.showLeft(tp);
							}
							filemanagerpanel.showRight(path.getText(), list);
							filemanagerpanel.getStatus().setText("完成");
						}
					});
				}
			};
			filemanagerpanel.getStatus().setText("正在读取...请稍后");
			new Thread(run).start();

		}

	}

	public class KeyAction extends KeyAdapter {
		public JTable list;
		public JTextField path;

		public KeyAction(JTable list, JTextField path) {
			this.list = list;
			this.path = path;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				final FileManagerPanel filemanagerpanel = (FileManagerPanel) MainFrame.tab
						.getSelectedComponent();
				String name = Common.getName(path.getText());
				final DefaultMutableTreeNode tn = TreeMethod.searchNode(
						filemanagerpanel.getRoot(), name);
				Runnable run = new Runnable() {
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if (tn != null) {
									TreePath tp = new TreePath(tn.getPath());
									filemanagerpanel.showLeft(tp);
								}
								filemanagerpanel.showRight(path.getText(), list);
								filemanagerpanel.getStatus().setText("完成");
							}
						});
					}
				};
				filemanagerpanel.getStatus().setText("正在读取...请稍后");
				new Thread(run).start();
			}
		}
	}
}
