package com.ms509.ui.menu;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.Caret;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.ms509.model.RightTableModel;
import com.ms509.ui.MainFrame;
import com.ms509.ui.panel.FileManagerPanel;
import com.ms509.ui.panel.TextPanel;
import com.ms509.util.Common;
import com.ms509.util.Safe;
import com.ms509.util.TreeMethod;

public class FileManagerPopMenu extends JPopupMenu {
	private JMenuItem upfile, downfile, openfile, rename, delete, addfile,
			adddict,retime;

	public FileManagerPopMenu(String type) {
		// TODO Auto-generated constructor stub
		MenuAction menu = new MenuAction();
		if (type.equals("select")) {
			upfile = new JMenuItem("上传");
			downfile = new JMenuItem("下载");
			openfile = new JMenuItem("打开");
			rename = new JMenuItem("重命名");
			retime = new JMenuItem("修改时间");
			delete = new JMenuItem("删除");
			JMenu addfiledict = new JMenu("新建");
			addfile = new JMenuItem("文件");
			adddict = new JMenuItem("文件夹");
			this.add(upfile);
			this.add(downfile);
			this.addSeparator();
			this.add(openfile);
			this.add(delete);
			this.add(rename);
			this.add(retime);	
			addfiledict.add(addfile);
			addfiledict.add(adddict);
			this.add(addfiledict);
			upfile.addActionListener(menu);
			downfile.addActionListener(menu);
			openfile.addActionListener(menu);
			delete.addActionListener(menu);
			rename.addActionListener(menu);
			retime.addActionListener(menu);
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
		private byte[] bytes;

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
						filemanagerpanel.getStatus().setText("正在上传...请稍等");
						File select = upch.getSelectedFile();
						filename = select.getName();
						fis = new FileInputStream(select);
						byte[] b = new byte[fis.available()];
						fis.read(b);
						udata = b;
						abpath = path.getText() + filename;
						new uploadThread(filemanagerpanel, abpath, udata)
								.start();
					} catch (Exception e1) {

					}
				}
				break;
			case "打开":
				String type = list.getValueAt(list.getSelectedRow(), 0)
						.toString();
				if (type.indexOf("folder.png") > -1) {
					if (filemanagerpanel.isLstatus()
							&& filemanagerpanel.isRstatus()) {
						filemanagerpanel.setLstatus(false);
						filemanagerpanel.setRstatus(false);
						filemanagerpanel.getStatus().setText("正在读取...请稍等");
						String name = list.getValueAt(list.getSelectedRow(), 1)
								.toString();
						final String abpath = Common.autoPath(path.getText())
								+ name + Safe.SYSTEMSP;
						filemanagerpanel.showRight(abpath, list);
						DefaultMutableTreeNode tn = TreeMethod.searchNode(
								filemanagerpanel.getRoot(), name);
						if (tn != null) {
							TreePath tp = new TreePath(tn.getPath());
							DefaultTreeSelectionModel dsmodel = new DefaultTreeSelectionModel();
							dsmodel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
							dsmodel.setSelectionPath(tp);
							filemanagerpanel.getTree().setSelectionModel(dsmodel);
							filemanagerpanel.showLeft(tp);
						}
						Runnable run = new Runnable() {
							@Override
							public void run() {
								while (true) {
									Thread.yield();
									if (filemanagerpanel.isLstatus()
											&& filemanagerpanel.isRstatus()) {
										SwingUtilities
												.invokeLater(new Runnable() {
													@Override
													public void run() {
														path.setText(abpath);
														filemanagerpanel
																.getStatus()
																.setText("完成");
													}
												});
										break;
									}
								}
							}
						};
						new Thread(run).start();
					} else {
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
							final String data = filemanagerpanel.getFm()
									.doAction("readfile", abpath);
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
				break;
			case "文件":
				abpath = path.getText() + "newFile.txt";
				TextPanel text = (TextPanel) MainFrame.tab.addPanel("text");
				text.getPath().setText(abpath);
				break;
			case "删除":
				int button = JOptionPane.showConfirmDialog(MainFrame.main,
						"确认删除？", "提示", JOptionPane.YES_NO_OPTION);
				if(button == 0)
				{
					filemanagerpanel.getStatus().setText("正在删除...请稍等");
					abpath = path.getText()
							+ list.getValueAt(list.getSelectedRow(), 1);
					new deleteThread(filemanagerpanel, abpath).start();
				}
				break;
			case "重命名":
				model.setEdit(true);
				list.editCellAt(list.getSelectedRow(), 1);
				JTextField edit = (JTextField)list.getEditorComponent();
				Caret c = edit.getCaret();
				edit.requestFocusInWindow();
				// 在MAC皮肤下，如果设置的位置是末尾c.setDot(edit.getText().length())，或者如果没用使用setDot设置光标的位置则都会自动全选。其他皮肤则不会自动全选。
//				c.setDot(2);
				edit.selectAll();	// 全选，通用所有皮肤。
				c.setVisible(true);
				model.setEdit(false);
				break;
			case "修改时间":
				final String oldtime = model.getValueAt(list.getSelectedRow(),2).toString();
				model.setEdit(true);
				list.editCellAt(list.getSelectedRow(), 2);
				final JTextField edit2 = (JTextField)list.getEditorComponent();
				Caret c2 = edit2.getCaret();
				edit2.requestFocusInWindow();
				c2.setVisible(true);
				model.setEdit(false);
				model.addTableModelListener(new TableModelListener() {
					public void tableChanged(TableModelEvent e) {
						if(e.getType() == TableModelEvent.UPDATE)
						{
							final String newtime = model.getValueAt(list.getSelectedRow(),2).toString();
							final String abpath = path.getText() + list.getValueAt(list.getSelectedRow(), 1);
							if(!oldtime.equals(newtime))
							{
								Runnable run = new Runnable() {
									private String ret;
									public void run() {
										ret = "-1";
										filemanagerpanel.getStatus().setText("正在修改时间...请稍等");
										ret = filemanagerpanel.getFm().doAction("retime", abpath,newtime);
										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												if(ret.equals("1"))
												{
													filemanagerpanel.getStatus().setText("修改时间成功");
												} else 
												{
													model.setValueAt(oldtime, list.getSelectedRow(), 2);
													filemanagerpanel.getStatus().setText("修改时间失败");
												}
											}
										});		
									}
								};
								new Thread(run).start();
							}
						}
					}
				});
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
				model.setEdit(true);
				// 滚动条滑动到末尾
				int row = model.getRowCount()-1;
//				list.setRowSelectionInterval(row, row);
				Rectangle rect = list.getCellRect(row, 0, true);
				list.scrollRectToVisible(rect);
				
				list.editCellAt(model.getRowCount() - 1, 1);
				JTextField fedit = (JTextField)list.getEditorComponent();
				Caret fc = fedit.getCaret();
				fedit.requestFocusInWindow();
				fedit.selectAll();
				fc.setVisible(true);
				model.setEdit(false);
				break;
			case "下载":
				String name = list.getValueAt(list.getSelectedRow(), 1)
						.toString();
				abpath = path.getText() + name;
				final JFileChooser downch = new JFileChooser(".");
				downch.setDialogTitle("下载文件到本地");
				downch.setSelectedFile(new File(name));
				int select = downch.showSaveDialog(filemanagerpanel);
				if (select == JFileChooser.APPROVE_OPTION) {
					try {
						filemanagerpanel.getStatus().setText("正在下载...请稍等");
						bytes = null;
						Runnable dlrun = new Runnable() {
							public void run() {
								bytes = filemanagerpanel.getFm().Download(
										abpath);
						
									if (bytes != null) {
										try {
											File f = downch.getSelectedFile();
											FileOutputStream fos = new FileOutputStream(
													f);
											fos.write(
													bytes,
													Safe.SPL.length(),
													bytes.length
															- (Safe.SPL
																	.length() + Safe.SPR
																	.length()));
											fos.close();
											SwingUtilities
													.invokeLater(new Runnable() {
														public void run() {
															filemanagerpanel
																	.getStatus()
																	.setText(
																			"下载完成");
														}
													});

										} catch (Exception e2) {
											filemanagerpanel.getStatus().setText("下载失败");
										}
							
								}
							}
						};
						new Thread(dlrun).start();
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
					if (filemanagerpanel.isLstatus()
							&& filemanagerpanel.isRstatus()) {
						filemanagerpanel.setLstatus(false);
						filemanagerpanel.setRstatus(false);
						filemanagerpanel.getStatus().setText("正在读取...请稍等");
						String name = list.getValueAt(list.getSelectedRow(), 1)
								.toString();
						final String abpath = Common.autoPath(path.getText())
								+ name + Safe.SYSTEMSP;
						filemanagerpanel.showRight(abpath, list);
						DefaultMutableTreeNode tn = TreeMethod.searchNode(
								filemanagerpanel.getRoot(), name);
						if (tn != null) {
							TreePath tp = new TreePath(tn.getPath());
							DefaultTreeSelectionModel dsmodel = new DefaultTreeSelectionModel();
							dsmodel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
							dsmodel.setSelectionPath(tp);
							filemanagerpanel.getTree().setSelectionModel(dsmodel);
							filemanagerpanel.showLeft(tp);
						}
						Runnable run = new Runnable() {
							@Override
							public void run() {
								while (true) {
									Thread.yield();
									if (filemanagerpanel.isLstatus()
											&& filemanagerpanel.isRstatus()) {
										SwingUtilities
												.invokeLater(new Runnable() {
													@Override
													public void run() {
														path.setText(abpath);
														filemanagerpanel
																.getStatus()
																.setText("完成");
													}
												});
										break;
									}
								}
							}
						};
						new Thread(run).start();
					} else {
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
			final FileManagerPanel filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
			filemanagerpanel.getStatus().setText("读取中...请稍等");
			String name = Common.getName(path.getText());
			final DefaultMutableTreeNode tn = TreeMethod.searchNode(
					filemanagerpanel.getRoot(), name);
			if (tn != null) {
				TreePath tp = new TreePath(tn.getPath());
				DefaultTreeSelectionModel dsmodel = new DefaultTreeSelectionModel();
				dsmodel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				dsmodel.setSelectionPath(tp);
				filemanagerpanel.getTree().setSelectionModel(dsmodel);
				filemanagerpanel.showLeft(tp);
			}
			filemanagerpanel.showRight(path.getText(), list);
			new Status(filemanagerpanel, "完成").start();
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
				filemanagerpanel.getStatus().setText("读取中...请稍等");
				String name = Common.getName(path.getText());
				final DefaultMutableTreeNode tn = TreeMethod.searchNode(
						filemanagerpanel.getRoot(), name);
				if (tn != null) {
					TreePath tp = new TreePath(tn.getPath());
					DefaultTreeSelectionModel dsmodel = new DefaultTreeSelectionModel();
					dsmodel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
					dsmodel.setSelectionPath(tp);
					filemanagerpanel.getTree().setSelectionModel(dsmodel);
					filemanagerpanel.showLeft(tp);
				}
				filemanagerpanel.showRight(path.getText(), list);
				new Status(filemanagerpanel, "完成").start();
			}
		}
	}

	class Status extends Thread {
		private JLabel status;
		private FileManagerPanel fmp;
		private String info;

		public Status(FileManagerPanel fmp, String info) {
			this.fmp = fmp;
			this.status = fmp.getStatus();
			this.fmp.setLstatus(false);
			this.fmp.setRstatus(false);
			this.info = info;
		}

		public void run() {
			while (true) {
				Thread.yield();
				if (this.fmp.isLstatus() && this.fmp.isRstatus()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							status.setText(info);
						}
					});
					break;
				}
			}
		}
	}

	class deleteThread extends Thread {
		private FileManagerPanel fmp;
		private String abpath;
		private JTable list;

		public deleteThread(FileManagerPanel fmp, String abpath) {
			this.fmp = fmp;
			this.fmp.setRstatus(false);
			this.abpath = abpath;
			this.list = fmp.getList();
		}

		public void run() {
			String data = fmp.getFm().doAction("delete", abpath);
			FileManagerPanel filemanagerpanel = (FileManagerPanel) MainFrame.tab
					.getSelectedComponent();
				if (data.equals("1")) {
					fmp.setRstatus(false);
					try {
						String[] name = abpath.split("\\\\|/");
						String dname = name[name.length-1];
						DefaultMutableTreeNode dmt = TreeMethod.searchNode(filemanagerpanel.getRoot(), dname);
						filemanagerpanel.getModel().removeNodeFromParent(dmt);
					} catch (Exception e) {
						
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							fmp.showRight(Common.getAbsolutePath(abpath), list);
						}
					});
					while (true) {
						Thread.yield();
						if (fmp.isRstatus()) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									fmp.getStatus().setText("删除成功");
								}
							});
							break;
						}
					}
			} else
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fmp.getStatus().setText("删除失败");
					}
				});
			}
		}
	}

	class uploadThread extends Thread {
		private FileManagerPanel fmp;
		private String abpath;
		private byte[] udata;
		private JTable list;

		public uploadThread(FileManagerPanel fmp, String abpath, byte[] udata) {
			this.fmp = fmp;
			this.list = fmp.getList();
			this.abpath = abpath;
			this.udata = udata;
		}

		public void run() {
			String data = fmp.getFm().doAction("upload", abpath, Common.toHex(udata));
				if (data.equals("1")) {
					fmp.setRstatus(false);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							fmp.showRight(Common.getAbsolutePath(abpath), list);
						}
					});
					while (true) {
						Thread.yield();
						if (fmp.isRstatus()) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									fmp.getStatus().setText("上传成功");
								}
							});
							break;
						}
					}
				} else 
				{
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							fmp.getStatus().setText("上传失败");
						}
					});
				}
		}
	}
}
