package com.ms509.ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.ms509.ui.MainFrame;
import com.ms509.ui.MessageDialog;
import com.ms509.ui.menu.ShellPopMenu;
import com.ms509.util.GBC;
import com.ms509.util.Safe;
import com.ms509.util.Shell;

public class ShellPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String pass;
	private int type;
	private String code;
	private int os;
	private String path = null;
	private String path_bak = null;
	private String path_show = null; // 用于[/usr/]$显示
	private int command_start;
	private int command_stop;
	private JToolBar bar;
	private JLabel status;
	private JTextPane console;
	private JScrollPane console_scroll;
	private Document shell_doc;// 文本控制
	private ArrayList<String> last_commands = new ArrayList();
	private int num = 1;
	private Shell core;
	private Font shell_font = null;
	private int num_t = 0;
	
	
	public ShellPanel() {
		// TODO Auto-generated constructor stub
		// 控件初始化

		bar = new JToolBar();
		status = new JLabel("完成");
		bar.setFloatable(false);
		console = new JTextPane();
		console_scroll = new JScrollPane(console);
		shell_doc = console.getDocument();
		
		ShellPopMenu a = new ShellPopMenu(this,console);
		
		// 初始化常量
		String[] tmp = MainFrame.tab.getUrl().split("\t");
		url = tmp[1];
		pass = tmp[2];
		// type = tmp[4];
		code = tmp[5];
		Safe.PASS = pass; // 初始化PASS常量
		// 初始化脚本类型
		switch (tmp[4]) {
		case "JSP(Eval)":
			type = 0;
			// this.jsp();
			break;
		case "PHP(Eval)":
			type = 1;
			// this.php();
			break;
		case "ASP(Eval)":
			type = 2;
			// this.asp();
			break;
		case "ASPX(Eval)":
			type = 3;
			break;
		case "Customize":  //添加自定义
			type = 4;
			break;
		}

		// core = new Shell(path,os,url,code,type1);
		core = new Shell(os, url, code, type);

		// /
		status.setText("正在连接...请稍等");
		Thread thread_getpath = new Thread(new Runnable() {
			public void run() {
				// 显示网站路径
				path = core.GetPath();
				check_path();
				final String tmp = path.substring(0, path.length() - 1);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (tmp.indexOf("HTTP/1.") > -1 || tmp.indexOf("/") < 0 && tmp.indexOf("\\") < 0) {
							new MessageDialog(tmp, 5);
							console.setEnabled(false);
						} else {
							try {
								shell_doc.insertString(shell_doc.getLength(), "\n" + path_show, null);
							} catch (BadLocationException e) {
							}
						}
						command_start = shell_doc.getLength();
						console.setCaretPosition(shell_doc.getLength());
						status.setText("完成");
					}
				});

			}
		});
		thread_getpath.start();

		// 初始化布局和控件
		this.setLayout(new GridBagLayout());
		GBC gbcinfo = new GBC(0, 0, 6, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0);
		GBC gbcconsole = new GBC(0, 1, 6, 1).setFill(GBC.BOTH).setWeight(0, 10);
		GBC gbcbar = new GBC(0, 2, 6, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0);

		// console.append("\n"+path);

		// text 焦点
		textareaFocus f_listener = new textareaFocus();
		this.addFocusListener(f_listener);

		// 监听text键盘事件
		textareaKey key_listener = new textareaKey();
		console.addKeyListener(key_listener);
		
		// 布局添加
		bar.add(status);
		this.add(bar, gbcinfo);
		this.add(console_scroll, gbcconsole);
		this.add(bar, gbcbar);
		// console.setVisible(true);
		// console.requestFocus();
		console.setCaretPosition(shell_doc.getLength());

		Color bgColor = Color.BLACK;
		UIDefaults defaults = new UIDefaults();
		defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
		console.putClientProperty("Nimbus.Overrides", defaults);
		console.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
		console.setBackground(bgColor);

		console.setForeground(Color.white);
		console.setBackground(Color.black);
		console.setCaretColor(Color.white);

		shell_font = new Font("幼圆", Font.BOLD, 12);
		console.setFont(new Font("幼圆", Font.BOLD, 12));
		command_start = shell_doc.getLength();
	}

	// 路径path
	public void check_path() {

		// 判断操作系统
		path = path.replace("\n", "");
		path = path.replace("\r", "");
		if (path.contains("pwd;echo")) {
			path = path_bak;
		}
		if (path.contains(":")) {

			Safe.SYSTEMSP = "\\";
			// System.out.println("win");
			if (!path.substring(path.length() - 1, path.length()).equals("\\")) {
				path = path + "\\";
			}
			path_show = path + ">";
		} else {
			Safe.SYSTEMSP = "/";
			if (!path.substring(path.length() - 1, path.length()).equals("/")) {
				path = path + "/";
			}
			path_show = "[" + path + "]$";
		}

	}

	// text焦点监听
	private class textareaFocus extends FocusAdapter {

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			console.requestFocus();
			console.setCaretPosition(shell_doc.getLength());
		}
	}


	// text键盘监听
	private class textareaKey extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if (shell_doc.getLength() <= command_start && !arg0.isControlDown()) {
				if (arg0.getKeyCode() == 8) {
					try {
						String t = shell_doc.getText(console.getCaretPosition() - 1, 1);
						shell_doc.insertString(console.getCaretPosition(), t, null);
					} catch (Exception e) {

					}
				}
			}

			if ((console.getCaretPosition() < command_start || console.getSelectionStart() < command_start
					|| console.getSelectionEnd() < command_start) && !arg0.isControlDown()) {
				console.setEditable(false);
				console.setCaretPosition(shell_doc.getLength());
			} else if(arg0.isControlDown()  && console.getCaretPosition() < command_start)
			{
				console.setEditable(false);
			}else
			{
				console.setEditable(true);

			}

			if (arg0.getKeyCode() == 10) {
				console.setCaretPosition(shell_doc.getLength());
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			command_stop = shell_doc.getLength();
			if (arg0.getKeyCode() == 10) { // 判断回车
				String tmp_cmd = null;
				try {
					tmp_cmd = shell_doc.getText(command_start, command_stop - command_start);
					/////////////////////////////////////////////////////////////
					//多次执行有可能会出错
					//中文输入法
					//////////////////////////////////////////////////////////////
					tmp_cmd = tmp_cmd.replace("\n", "").replace("\r", "");
					if (tmp_cmd.equals("cls") || tmp_cmd.equals("clear")) { // 清空文本显示区域
						shell_doc.remove(0, shell_doc.getLength());
						shell_doc.insertString(0, "\n" + path, null);
						command_start = shell_doc.getLength();
					
					}
					else if(tmp_cmd.indexOf("setp")==0)   //自定义路径
					{	
						if(tmp_cmd.substring(0,4).equals("setp"))
						{
							String k = tmp_cmd.substring(5,tmp_cmd.length());
							core.SetCMD(k);
							shell_doc.insertString(shell_doc.getLength(), "\n设置命令路径为:"+k, null);
							shell_doc.insertString(shell_doc.getLength(), "\n" + path+"", null);
							command_start = shell_doc.getLength();
						}
					}
					else { // 执行命令
						// shell_doc.insertString(shell_doc.getLength(), "\n",
						// null);
						Thread exe = new Thread(new Runnable() {
							// private Lock lock = new ReentrantLock();// 锁对象

							@Override
							public void run() {
								// TODO Auto-generated method stub
								num_t = 1;
								status.setText("正在执行...请稍等");
								try {
									// Thread.sleep(10000);
									execute(path, shell_doc.getText(command_start, command_stop - command_start), os,
											type);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									status.setText("执行失败");

									console.setEditable(true);
								} finally {
									num_t = 0;
								}
							}

						});
						if (num_t == 0) {
							exe.start();
						} else {
						}
					}
					last_commands.add(tmp_cmd);
					num = last_commands.size();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// 翻看命令记录(上)
			if (arg0.getKeyCode() == KeyEvent.VK_UP) {
				console.setCaretPosition(command_start);
				try {
					shell_doc.remove(command_start, shell_doc.getLength() - command_start);
					shell_doc.insertString(command_start, key_up_action(), null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			// 翻看命令记录（下）
			if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
				console.setCaretPosition(command_start);
				try {
					shell_doc.remove(command_start, shell_doc.getLength() - command_start);
					shell_doc.insertString(command_start, key_down_action(), null);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}

		}
	}

	// 命令执行调用
	public void execute(String pa, String command, int os, int type) {
		String result[] = new String[2];
		try {
			path_bak = path;
			core.SetPath(path);
			result = core.execute(command);
			shell_doc.insertString(shell_doc.getLength(), result[0], null);

			path = result[1];
			// check_path();
			
			check_path();
			shell_doc.insertString(shell_doc.getLength(), "\n" + path_show, null);
			command_start = shell_doc.getLength();
			console.setCaretPosition(shell_doc.getLength());
			status.setText("完成");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status.setText("执行失败");
			command_start = shell_doc.getLength();
			console.setCaretPosition(shell_doc.getLength());
		}

	}

	// 上翻历史命令
	public String key_up_action() {
		num = num - 1;
		String last_command = null;
		if (num >= 0 && !last_commands.isEmpty()) {
			last_command = last_commands.get(num);
			last_command = last_command.replace("\n", "").replace("\r", "");
			return last_command;
		} else {
			return "";
		}

	}

	// 下翻历史命令
	public String key_down_action() {
		num = num + 1;
		String last_command = null;
		if (num < last_commands.size() && num >= 0) {
			last_command = last_commands.get(num);
			last_command = last_command.replace("\n", "").replace("\r", "");
			return last_command;
		} else if (num < 0) {
			num = 0;
			return "";
		} else {
			num = last_commands.size();
			return "";
		}
	}

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

}
