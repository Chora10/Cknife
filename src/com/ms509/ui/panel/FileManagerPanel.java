package com.ms509.ui.panel;

import com.ms509.model.ExtendedDefaultTreeCellRenderer;
import com.ms509.model.ResultSetTableModel;
import com.ms509.model.RightTableModel;
import com.ms509.util.FileManager;
import com.ms509.ui.MainFrame;
import com.ms509.ui.MessageDialog;
import com.ms509.ui.menu.FileManagerPopMenu;
import com.ms509.ui.menu.FileManagerPopMenu.MouseAction;
import com.ms509.ui.menu.FileManagerPopMenu.SelectedMouseAction;
import com.ms509.util.Common;
import com.ms509.util.DbDao;
import com.ms509.util.GBC;
import com.ms509.util.Safe;
import com.ms509.util.TreeMethod;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import javax.management.RuntimeErrorException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import sun.misc.BASE64Encoder;

public class FileManagerPanel extends JPanel {
	private FileManager fm = null;
	private String id;
	private String url;
	private String pass;
	private String config;
	private String type;
	private String code;
	private JTree tree;
	private DefaultMutableTreeNode root = null;
	private DefaultTreeModel model = null;
	private RightTableModel listmodel = null;
	private JTextField path;
	private JTable list = null;
	private JLabel status;
	private JToolBar bar;
	private JButton read;
	private JScrollPane listpane;
	private JScrollPane treepane;
	private String webroot;
	private String readdict = "";
	private String[] tmp1 = null;
	private String[] index_datas;
	private String[] trees;
	private String arrtmp;
	private boolean lstatus = true;
	private boolean rstatus = true;
	private boolean init = true;

	public JTree getTree()
	{
		return tree;
	}
	
	public boolean isLstatus() {
		return lstatus;
	}

	public void setLstatus(boolean lstatus) {
		this.lstatus = lstatus;
	}

	public boolean isRstatus() {
		return rstatus;
	}

	public void setRstatus(boolean rstatus) {
		this.rstatus = rstatus;
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}

	public JLabel getStatus() {
		return status;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}

	public FileManager getFm() {
		return fm;
	}

	public void setFm(FileManager fm) {
		this.fm = fm;
	}

	public JScrollPane getListpane() {
		return listpane;
	}

	public void setListpane(JScrollPane listpane) {
		this.listpane = listpane;
	}

	public JTable getList() {
		return list;
	}

	public void setList(JTable list) {
		this.list = list;
	}

	public JTextField getPath() {
		return path;
	}

	public void setPath(JTextField path) {
		this.path = path;
	}

	public FileManagerPanel() {
		// TODO Auto-generated constructor stub

		this.setLayout(new GridBagLayout());
		GBC gbcpath = new GBC(0, 0 ,2 ,1).setWeight(1, 0).setFill(GBC.HORIZONTAL);
		GBC gbcread = new GBC(2, 0);
		GBC gbctree = new GBC(0, 1).setWeight(0, 1).setFill(GBC.VERTICAL).setIpad(200, 0);
		GBC gbclist = new GBC(1, 1, 2, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0);
		GBC gbcbar = new GBC(0, 2, 3, 1).setFill(GBC.HORIZONTAL).setWeight(100,0);

		path = new JTextField();
		list = new JTable();
		tree = new JTree();
		list.setAutoCreateRowSorter(true);
		model = (DefaultTreeModel) tree.getModel();
		model.setRoot(new DefaultMutableTreeNode(""));// 先初始化根节点，不初始化会显示更多的组件自带内容
		tree.setVisible(false);// 先隐藏，再最后更新的时候再显示出来，就不会看到初始化的节点，就是完全空白的，美观。
		tree.setShowsRootHandles(true);
		read = new JButton("读取");
		bar = new JToolBar();
		status = new JLabel("完成");
		String[] tmp = MainFrame.tab.getUrl().split("\t");
		id = tmp[0];
		url = tmp[1];
		pass = tmp[2];
		config = tmp[3];
		type = tmp[4];
		code = tmp[5];
		ListPanel listpanel = (ListPanel) MainFrame.tab.getSelectedComponent();
		ResultSetTableModel model = listpanel.getModel();
		String time = Common.getTime();
		String ip = Common.getIp(url);
		try {
			DbDao.getInstance()
					.getStmt()
					.executeUpdate(
							"update data set ip='" + ip + "',time='" + time
									+ "' where id=" + id);
			Vector<String> vector = new Vector<>();
			vector.add(id);
			vector.add(url);
			vector.add(pass);
			vector.add(config);
			vector.add(type);
			vector.add(code);
			vector.add(ip);
			vector.add(time);
			model.update(id, vector);
		} catch (SQLException e) {
		}
		fm = new FileManager(url, pass, type, code);
		this.path.setText("正在连接...请稍等");
		this.status.setText("正在载入路径...请稍等");
		Runnable run = new Runnable() {
			public void run() {
				arrtmp = fm.doAction("readindex");
				// System.out.println(arrtmp);

				if (arrtmp.indexOf("HTTP/1.") > -1 || arrtmp.indexOf("/") < 0
						&& arrtmp.indexOf("\\") < 0) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							path.setText("连接失败");
							status.setText("载入路径失败");
							new MessageDialog(arrtmp,5);
						}
					});
				} else {
					try {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								filemanagerindex();
								filemanagersystem();
							}
						});

					} catch (Exception e) {
					}
				}
			}
		};
		new Thread(run).start();
		bar.add(status);
		bar.setFloatable(false);
		treepane = new JScrollPane(tree);
		treepane.setPreferredSize(new Dimension(25, 0));
		listpane = new JScrollPane(list);
		FileManagerPopMenu fpop = new FileManagerPopMenu("select");
		FileManagerPopMenu npop = new FileManagerPopMenu("none");
		SelectedMouseAction saction = fpop.new SelectedMouseAction();
		MouseAction naction = npop.new MouseAction();
		list.addMouseListener(saction);
		listpane.addMouseListener(naction);
		read.addActionListener(fpop.new ReadAction(list, path));
		path.addKeyListener(fpop.new KeyAction(list, path));
		this.add(path, gbcpath);
		this.add(read, gbcread);
		this.add(treepane, gbctree);
		this.add(listpane, gbclist);
		this.add(bar, gbcbar);
	}

	private void filemanagerindex() {
		index_datas = arrtmp.split("\t");
		webroot = index_datas[0];
		if (webroot.charAt(0) != '/') // Windows系统
		{
			Safe.SYSTEMSP = "\\";
			webroot = webroot.replaceAll("/", "\\\\");
			tmp1 = webroot.split("\\\\");
			root = new DefaultMutableTreeNode("");
			// 前面设置：windows下，在前面如果设置了setRootVisible(false)则整个都不会显示，可以使用expandAll显示并展开
			// model.setRoot(root);
			String drive = "";
			for (int i = 0; i < index_datas[1].length() - 1; i++) {
				drive = String.valueOf(index_datas[1].charAt(i));
				drive = drive + String.valueOf(index_datas[1].charAt(i + 1));
				i++;
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(drive);
				dmtn.setAllowsChildren(false);
				root.add(dmtn);
			}
			model.setRoot(root); // 后面设置：windows下即使设置了setRootVisible(false)也会显示，可以使用expandAll以外的方式展开
			tree.setRootVisible(false);
		} else // Linux系统
		{
			Safe.SYSTEMSP = "/";
			tmp1 = webroot.split("/");
			root = new DefaultMutableTreeNode("/");
			model.setRoot(root);
		}
		String cut = webroot.substring(webroot.length()-1);
		if(cut.equals(Safe.SYSTEMSP))
		{
			path.setText(webroot);
		} else 
		{
			path.setText(webroot + Safe.SYSTEMSP);
		}
	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

	private void filemanagersystem() {
		// ExtendedTreeCellRenderer trenderer = new ExtendedTreeCellRenderer();
		ExtendedDefaultTreeCellRenderer trenderer = new ExtendedDefaultTreeCellRenderer();
		tree.setCellRenderer(trenderer);
		tree.setVisible(true); // 设置之前再显示出来
		tree.setModel(model);
		tree.addTreeSelectionListener(new TreeAction());
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		Runnable run = new Runnable() {
			public void run() {
				status.setText("正在载入左边栏...请稍等");
				trees = fm.makeleft(webroot);
				final String search;
				final String[] tmp2;
				if (Safe.SYSTEMSP.equals("/")) {
					search = "/";
					tmp2 = Arrays.copyOfRange(tmp1, 1, tmp1.length);
				} else {
					search = tmp1[0];
					tmp2 = Arrays.copyOfRange(tmp1, 1, tmp1.length);
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						TreeMethod.makeIndexTree(tmp2, trees,
								TreeMethod.searchNode(root, search),tree);
						TreeMethod.expandAll(tree, new TreePath(root), true);
						status.setText("正在载入右边栏...请稍等");
						showRight(webroot, list);
					}
				});
			}
		};
		new Thread(run).start();
	}

	public void showRight(final String path, final JTable list) {
		Runnable run2 = new Runnable() {
			public void run() {
				final String[] filedicts = fm.makeright(path);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							listmodel = new RightTableModel(filedicts);
							list.setModel(listmodel);
						} catch (Exception e) {
						}
						TableColumnModel columnmodel = list.getColumnModel();
						TableColumn isfiledict = columnmodel.getColumn(0);
						isfiledict.setHeaderValue("");
						isfiledict.setMaxWidth(0);
						TableColumn name = columnmodel.getColumn(1);
						name.setMinWidth(300);
						TableColumn time = columnmodel.getColumn(2);
						time.setMinWidth(150);
						DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
						renderer.setHorizontalAlignment(JTextField.CENTER);
						list.getColumnModel().getColumn(3)
								.setCellRenderer(renderer);
						list.getColumnModel().getColumn(4)
								.setCellRenderer(renderer);
						JTableHeader header = list.getTableHeader();
						header.setDefaultRenderer(renderer);
						if (init) {
							status.setText("完成");
							init = false;
						}
						rstatus = true;
					}
				});
			}
		};
		new Thread(run2).start();
	}

	public void showLeft(final TreePath tp) {
		Runnable run3 = new Runnable() {
			@Override
			public void run() {
				final String[] trees = fm.makeleft(TreeMethod.makePath(tp));
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						DefaultMutableTreeNode select = (DefaultMutableTreeNode) tp
								.getLastPathComponent();
						select.setAllowsChildren(true);
						TreeMethod.addTree(trees, select, model);
						if (!tree.isExpanded(tp)) {
							tree.scrollPathToVisible(tp);
						}
						lstatus = true;
					}
				});
			}
		};
		new Thread(run3).start();
	}

	public RightTableModel getListmodel() {
		return listmodel;
	}

	public void setListmodel(RightTableModel listmodel) {
		this.listmodel = listmodel;
	}

	class TreeAction implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (lstatus && rstatus) {
				final TreePath tp = tree.getSelectionPath();
				if (tp != null) {
					lstatus = false;
					rstatus = false;
					status.setText("正在读取...请稍等");
					showLeft(tp);
					showRight(TreeMethod.makePath(tp), list);
					path.setText(TreeMethod.makePath(tp));
					Runnable run = new Runnable() {
						public void run() {
							while (true) {
								Thread.yield();
								if (lstatus && rstatus) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											status.setText("完成");
										}
									});
									break;
								}
							}
						}
					};
					new Thread(run).start();
				}
			} else {
//				 new MessageDialog("上一操作尚未执行完毕");		
				status.setText("上一操作尚未执行完毕");
				DefaultTreeSelectionModel dsmodel = new DefaultTreeSelectionModel();
				dsmodel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				dsmodel.setSelectionPath(e.getOldLeadSelectionPath());
				tree.setSelectionModel(dsmodel);
			}
		}

	}
}