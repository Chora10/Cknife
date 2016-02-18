package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

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
	public SetDBDialog() {
		// TODO Auto-generated constructor stub		
		super(MainFrame.main, "添加shell", true);
		this.setComponent();
		// 初始化布局和控件

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
		
		//数据库类型配置 先行支持aspx的各种类型接口
		String[] dbtypes = new String[]{"<T>ADO</T><C>Provider=Microsoft.Jet.OLEDB.4.0;Data Source=c:\111.mdb</C>",
				"<T>ADO</T><C>Driver={MySQL};Server=localhost;database=mysql;UID=root;PWD=</C>"};
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
			System.out.println("init db config,waiting for response");
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
}
