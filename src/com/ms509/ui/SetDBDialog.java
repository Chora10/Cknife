package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import com.ms509.ui.MainFrame;
import javax.swing.*;

import com.ms509.ui.panel.DatabasePanel;
import com.ms509.ui.panel.ListPanel;
import com.ms509.util.DataBase;
import com.ms509.util.DbDao;
import com.ms509.util.GBC;

public class SetDBDialog extends JDialog{

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
		type= t[4];
		this.setComponent();
		String getconfig_data = "select config from data where id="+id;
		System.out.println(getconfig_data);
		try {
			ResultSet rs = stmt.executeQuery(getconfig_data);
			while(rs.next())
			{
				//System.out.println(rs.getString(1));
				config = rs.getString(1);
				dbset.setText(config);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(t[3]);
		this.setVisible(true);
	}
	private void setComponent()
	{
		south = new JPanel();
		center = new JPanel();
		north = new JPanel();
		a = this;
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		this.setResizable(false);
		this.setSize(450, 240);
		this.setLocation((d.width - this.getWidth()) / 2,
				(d.height - this.getHeight()) / 2);
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
		
		
//		north.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 8));
//		center.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
//		south.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 8));
//		
		
		
		example = new JLabel("示例");
		setting = new JLabel("配置");
		dbset = new JTextArea();
		submit = new JButton("提交");
		// submit action
		InitDB action = new InitDB();
		dbset_scroll = new JScrollPane(dbset);
		dbset_scroll.setPreferredSize(new Dimension(400,100));
		dbset.setText(config);
		
		//数据库类型配置 先行支持aspx的各种类型接口
		String[] dbtypes = new String[]{};
		System.out.println(type);
		switch (type) {
		case "JSP(Eval)":
			// System.out.println("jsp");
			dbtypes = new String[]{"<T>MYSQL</T>\r\n<H>localhost</H>\r\n<U>root</U>\r\n<P>root</P>\r\n<L>utf8</L>",
					"<T>ORACLE</T>\r\n<H>localhost:1521</H>\r\n<U>root</U>\r\n<P>root</P>\r\n<M>database</M>\r\n<L>utf8</L>",
					"<T>MSSQL</T><H>localhost:1433</H>\r\n<U>root</U>\r\n<P>root</P>\r\n<M>database</M>\r\n<L>utf8</L>"};
			// this.jsp();
			break;
		case "PHP(Eval)":
			// System.out.println("php");
			dbtypes = new String[]{"<T>MYSQL</T>\r\n<H>localhost</H>\r\n<U>root</U>\r\n<P>root</P>\r\n<L>utf8</L>"};
			// this.php();
			break;
		case "ASP(Eval)":
			// System.out.println("asp");
			dbtypes = new String[]{"<T>MYSQL</T>\r\n<C>Driver=MySQL ODBC 5.3 Unicode Driver;Server=localhost;database=mysql;UID=root;PWD=root</C>",
					"<T>MSSQL</T>\r\n<C>Provider=SQLOLEDB.1;User ID=;Password=;Initial Catalog=master;Data Source=(local)</C>",
					"<T>MDB</T>\r\n<C>Provider=Microsoft.Jet.OLEDB.4.0;Data Source=C:\\111.mdb</C>"};
			// this.asp();
			break;
		case "ASPX(Eval)":
			// System.out.println("aspx");
			dbtypes = new String[]{"<T>MYSQL</T>\r\n<C>Driver=MySQL ODBC 5.3 Unicode Driver;Server=localhost;database=mysql;UID=root;PWD=root</C>",
					"<T>MSSQL</T>\r\n<C>Provider=SQLOLEDB.1;User ID=;Password=;Initial Catalog=master;Data Source=(local)</C>",
					"<T>MDB</T>\r\n<C>Provider=Microsoft.Jet.OLEDB.4.0;Data Source=C:\\111.mdb</C>"};
			break;
		}
		
		//dbtype
		dbtype = new JComboBox<>(dbtypes);
		dbtype.setPreferredSize(new Dimension(400, 30));
		SelectItem aListener = new SelectItem();
		
		dbtype.addActionListener(aListener);
		//
		submit.addActionListener(action);
		
		//布局
		north.add(example,gbcnorth1);
		north.add(dbtype,gbcnorth2);
		center.add(setting,gbccenter1);
		center.add(dbset_scroll,gbccenter2);
		south.add(submit);
		
		//		
		this.getContentPane().add(north, gbcnorth);
		this.getContentPane().add(center, gbccenter);
		this.getContentPane().add(south, gbcsouth);
	}
	class InitDB implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("init db config,waiting for response");
			
			//System.out.println(id);
			config = dbset.getText().replaceAll("'", "''");;
			String sql = "update data set config='"+config+"' where id="+id;
			//System.out.println(sql);
			try {
				stmt.execute(sql);
				Vector<String> vector = new Vector<String>();
				tmp = MainFrame.tab.getUrl().split("\t");
				//System.out.println(MainFrame.tab.getUrl());
				vector.add(tmp[0]);
				vector.add(tmp[1].replaceAll("''", "'"));
				vector.add(tmp[2].replaceAll("''", "'"));
				vector.add(config.replaceAll("''", "'"));
				vector.add(tmp[4]);
				vector.add(tmp[5]);
				vector.add(tmp[6]);
				vector.add(tmp[7]);
				//实例化向上转型，只修改原列表中row的config参数
				ListPanel list = (ListPanel)MainFrame.tab.addPanel("list");
				list.getModel().update(id, vector);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			a.setVisible(false);

		}
		
	}
	class SelectItem implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println(dbtype.getSelectedItem());
			dbset.append(dbtype.getSelectedItem()+"\n");
		}

		
	}
	
	public static String getStr()
	{
		config = config.replace("''","'");
		return config;
	}
}
