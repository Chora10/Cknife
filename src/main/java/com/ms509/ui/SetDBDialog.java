package com.ms509.ui;
//数据库配置窗口
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ms509.ui.MainFrame;
import javax.swing.*;

import com.ms509.ui.panel.DatabasePanel;
import com.ms509.ui.panel.ListPanel;
import com.ms509.util.DataBase;
import com.ms509.util.DbDao;
import com.ms509.util.GBC;

public class SetDBDialog extends JDialog {

	private JDialog a;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JLabel example;
	private JLabel setting;
	private JTextArea dbset;
	private JButton submit;
	private JScrollPane dbset_scroll;
	private JComboBox dbtype;
	private String id;
	private static String config;
	private Statement stmt = DbDao.getInstance().getStmt();;
	private String[] tmp;
	private String type;

	public SetDBDialog(String[] t) {
		// TODO Auto-generated constructor stub
		super(MainFrame.main, "数据库配置", true);
		// 初始化布局和控件
		id = t[0];
		type = t[4];
		this.setComponent();
		
		//从数据库中读取配置信息
		String getconfig_data = "select config from data where id=" + id;
		//System.out.println(getconfig_data);
		try {
			ResultSet rs = stmt.executeQuery(getconfig_data);
			while (rs.next()) {
				config = rs.getString(1);
				dbset.setText(config);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(t[3]);
		this.setVisible(true);
	}

	//初始化界面
	private void setComponent() {
		south = new JPanel();
		center = new JPanel();
		north = new JPanel();
		a = this;
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		this.setResizable(false);
		this.setSize(450, 240);
		this.setLocation((d.width - this.getWidth()) / 2, (d.height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// 初始化布局和控件
		this.setLayout(new GridBagLayout());
		GBC gbcnorth = new GBC(0, 0, 3, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbccenter = new GBC(0, 1, 3, 2).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbcsouth = new GBC(0, 5, 3, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);

		GBC gbcnorth1 = new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbcnorth2 = new GBC(1, 0, 2, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbccenter1 = new GBC(0, 1, 1, 2).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbccenter2 = new GBC(1, 1, 2, 2).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);


		example = new JLabel("示例");
		setting = new JLabel("配置");
		dbset = new JTextArea();
		submit = new JButton("提交");
		// submit action
		InitDB action = new InitDB();
		dbset_scroll = new JScrollPane(dbset);
		dbset_scroll.setPreferredSize(new Dimension(400, 100));
		dbset.setText(config);

		// 数据库类型配置示例
		String[] dbtypes = new String[] {};
		//System.out.println(type);
		switch (type) {
		case "JSP(Eval)":
			dbtypes = new String[] { "<T>MYSQL</T><H>localhost</H><U>root</U><P>root</P><L>utf8</L>",
					"<T>ORACLE</T><H>localhost:1521</H><U>root</U><P>root</P><M>database</M><L>utf8</L>",
					"<T>MSSQL</T><H>localhost:1433</H><U>root</U><P>root</P><M>database</M><L>utf8</L>" };
			break;
		case "PHP(Eval)":
			dbtypes = new String[] { "<T>MYSQL</T><H>localhost</H><U>root</U><P>root</P><L>utf8</L>" };
			break;
		case "ASP(Eval)":
			dbtypes = new String[] {
					"<T>MYSQL</T><C>Driver=MySQL ODBC 5.3 Unicode Driver;Server=localhost;database=mysql;UID=root;PWD=root</C>",
					"<T>MSSQL</T><C>Provider=SQLOLEDB.1;User ID=;Password=;Initial Catalog=master;Data Source=(local)</C>",
					"<T>MSSQL</T><C>Driver={Sql Server};Server=(local);Database=master;Uid=sa;Pwd=</C>",
					"<T>MDB</T><C>Provider=Microsoft.Jet.OLEDB.4.0;Data Source=C:\\111.mdb</C>" };
			break;
		case "ASPX(Eval)":
			dbtypes = new String[] {
					"<T>MYSQL</T><C>Driver=MySQL ODBC 5.3 Unicode Driver;Server=localhost;database=mysql;UID=root;PWD=root</C>",
					"<T>MSSQL</T><C>Provider=SQLOLEDB.1;User ID=;Password=;Initial Catalog=master;Data Source=(local)</C>",
					"<T>MSSQL</T><C>Driver={Sql Server};Server=(local);Database=master;Uid=sa;Pwd=</C>",		
					"<T>MDB</T><C>Provider=Microsoft.Jet.OLEDB.4.0;Data Source=C:\\111.mdb</C>" };
			break;
		}
		// dbtype
		dbtype = new JComboBox<>(dbtypes);
		dbtype.setPreferredSize(new Dimension(400, 30));
		SelectItem aListener = new SelectItem();
		dbtype.addActionListener(aListener);

		submit.addActionListener(action);
		dbtype.setSelectedIndex(0);
		// 布局
		north.add(example, gbcnorth1);
		north.add(dbtype, gbcnorth2);
		center.add(setting, gbccenter1);
		center.add(dbset_scroll, gbccenter2);
		south.add(submit);

		// 添加布局到panel
		this.getContentPane().add(north, gbcnorth);
		this.getContentPane().add(center, gbccenter);
		this.getContentPane().add(south, gbcsouth);
	}

	class InitDB implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			config = dbset.getText().replaceAll("'", "''");
			String sql = "update data set config='" + config + "' where id=" + id;
			try {
				stmt.execute(sql);
				Vector<String> vector = new Vector<String>();
				tmp = MainFrame.tab.getUrl().split("\t");
				vector.add(tmp[0]);
				vector.add(tmp[1].replaceAll("''", "'"));
				vector.add(tmp[2].replaceAll("''", "'"));
				vector.add(config.replaceAll("''", "'"));
				vector.add(tmp[4]);
				vector.add(tmp[5]);
				vector.add(tmp[6]);
				vector.add(tmp[7]);
				// 实例化向上转型，只修改原列表中row的config参数
				ListPanel list = (ListPanel) MainFrame.tab.addPanel("list");
				list.getModel().update(id, vector);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			a.setVisible(false);

		}

	}

	
	//将示例添加至配置框中
	class SelectItem implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String tmp = dbtype.getSelectedItem().toString();
			tmp = tmp.replace("><", ">\r\n<");
			dbset.append(tmp + "\n");
		}

	}

	public static String getStr() {
		config = config.replace("''", "'");
		return config;
	}
}
