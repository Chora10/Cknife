package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ms509.ui.panel.ListPanel;
import com.ms509.util.DbDao;

public class AddDialog extends JDialog {
	private String id,ip,time;
	private JButton button;
	private JTextField urltext, passtext;
	private JTextArea configtext;
	private JComboBox<String> atype, acode;

	public AddDialog() {
		super(MainFrame.main, "添加SHELL", true);
		this.setComponent();
		this.setEvent();
		this.setVisible(true); // 模态对话框必须在添加完组件后设置可见，不然会显示不了。
	}

	public AddDialog(String s) {
		super(MainFrame.main, "修改SHELL", true);
		String[] tmp = s.split("\t");
		this.setComponent();
		this.setEvent();
		this.id = tmp[0];
		urltext.setText(tmp[1]);
		passtext.setText(tmp[2]);
		configtext.setText(tmp[3]);
		button.setText("编辑");
		atype.setSelectedItem(tmp[4]);
		acode.setSelectedItem(tmp[5]);
		this.ip = tmp[6];
		this.time = tmp[7];
		this.setVisible(true); // 模态对话框必须在添加完组件后设置可见，不然会显示不了。
	}

	private void setComponent() {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		this.setResizable(false);
		this.setSize(450, 240);
		this.setLocation((d.width - this.getWidth()) / 2,
				(d.height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		north.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 8));
		center.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		south.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 8));
		JLabel urllabel = new JLabel("地址:");
		urllabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		JLabel configlabel = new JLabel("配置:");
		configlabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		JLabel actionlabel = new JLabel("");
		actionlabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		urltext = new JTextField("http://");
		passtext = new JTextField();
		configtext = new JTextArea();
		JScrollPane configscroll = new JScrollPane(configtext);
		button = new JButton("添加");
		String[] strtype = new String[] { "脚本类型", "ASP(Eval)", "ASPX(Eval)", "PHP(Eval)",
				"JSP(Eval)", "Customize" };
		String[] strcode = new String[] { "字符编码", "GB2312", "GBK", "UTF-8",
				"BIG5", "ISO-8859-1" };
		atype = new JComboBox<>(strtype);
		acode = new JComboBox<>(strcode);
		urltext.setPreferredSize(new Dimension(320,23));
		passtext.setPreferredSize(new Dimension(56,23));
		configtext.setLineWrap(true);
		configtext.setPreferredSize(new Dimension(369,128));
		configscroll.setBorder(urltext.getBorder());
		north.add(urllabel);
		north.add(urltext);
		north.add(passtext);
		center.add(configlabel);
		center.add(configscroll);	
		south.add(atype);
		south.add(acode);
		south.add(button);
		south.add(actionlabel);
		this.getContentPane().add(north, BorderLayout.NORTH);
		this.getContentPane().add(center, BorderLayout.CENTER);
		this.getContentPane().add(south, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(button);	
	}

	private void setEvent() {
		urltext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				String url = urltext.getText();
				int pos = 0;
				if((pos = url.lastIndexOf("."))>0)
				{
					String ext = url.substring(pos+1);
					switch (ext.toLowerCase()) {
					case "asp":
						atype.setSelectedItem("ASP(Eval)");
						break;
					case "aspx":
						atype.setSelectedItem("ASPX(Eval)");
						break;
					case "php":
						atype.setSelectedItem("PHP(Eval)");
						break;
					case "jsp":
						atype.setSelectedItem("JSP(Eval)");
						break;
					case "jspx":
						atype.setSelectedItem("JSP(Eval)");
						break;
					}
				}
			}
		});
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String url = urltext.getText().replaceAll("'", "''");
				String pass = passtext.getText().replaceAll("'", "''");
				String config = configtext.getText().replaceAll("'", "''");
				String type = atype.getSelectedItem().toString();
				String code = acode.getSelectedItem().toString();
				Statement stmt = DbDao.getInstance().getStmt();

				if (!type.equals("脚本类型")) {

					if (code.equals("字符编码")) {
						code = "UTF-8";
					}
					if (e.getActionCommand().equals("添加")) {
						String sql = "insert into data(url,pass,config,type,code) values('"
								+ url
								+ "','"
								+ pass
								+ "','"
								+ config
								+ "','"
								+ type + "','" + code + "')";
						Vector<String> vector = new Vector<String>();
						try {
							if (stmt.executeUpdate(sql) < 1) {
								JOptionPane
										.showMessageDialog(MainFrame.main,
												"添加失败", "错误",
												JOptionPane.ERROR_MESSAGE);
								return;
							}
							ResultSet rs = stmt
									.executeQuery("select last_insert_rowid()");
							String id = rs.getString(1);
							vector.add(id);
							vector.add(url.replaceAll("''", "'"));
							vector.add(pass.replaceAll("''", "'"));
							vector.add(config.replaceAll("''", "'"));
							vector.add(type);
							vector.add(code);
							vector.add(" ");
							vector.add(" ");
							ListPanel listpanel= (ListPanel)MainFrame.tab.getSelectedComponent();
							listpanel.getModel().addRow(vector);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						String sql = "update data set url='"+url+"',pass='"+pass+"',config='"+config+"',type='"+type+"',code='"+code+"' where id="+id;
						Vector<String> vector = new Vector<String>();
						try {
							if (stmt.executeUpdate(sql) < 1) {
								JOptionPane
										.showMessageDialog(MainFrame.main,
												"修改失败", "错误",
												JOptionPane.ERROR_MESSAGE);
								return;
							}
							vector.add(id);
							vector.add(url.replaceAll("''", "'"));
							vector.add(pass.replaceAll("''", "'"));
							vector.add(config.replaceAll("''", "'"));
							vector.add(type);
							vector.add(code);
							vector.add(ip);
							vector.add(time);
							ListPanel listpanel= (ListPanel)MainFrame.tab.getSelectedComponent();
							listpanel.getModel().update(id, vector);
						} catch (HeadlessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					setVisible(false);

				} else {
					JOptionPane.showMessageDialog(MainFrame.main, "请填写脚本类型",
							"错误", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
	}
}
