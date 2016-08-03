package com.ms509.ui.config.panel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ms509.ui.ConfigDialog;
import com.ms509.util.Common;
import com.ms509.util.Configuration;
import com.ms509.util.GBC;
import com.ms509.util.Safe;

public class ProxyPanel extends JPanel{
	private JTextField host;
	private JTextField port;
	private JTextField user;
	private JTextField pass;
	private JComboBox<String> type;
	public ProxyPanel() {
		this.setLayout(new GridBagLayout());
		GBC gbclhost = new GBC(0, 0).setInsets(5, -40, 0, 0);
		GBC gbchost = new GBC(1,0,3,1).setInsets(5, 20, 0, 0);
		GBC gbclport = new GBC(0,1).setInsets(10, -40, 0, 0);
		GBC gbcport = new GBC(1,1,3,1).setInsets(10, 20, 0, 0);
		GBC gbcluser = new GBC(0,2).setInsets(10, -40, 0, 0);
		GBC gbcuser = new GBC(1,2,3,1).setInsets(10, 20, 0, 0);
		GBC gbclpass = new GBC(0,3).setInsets(10, -40, 0, 0);
		GBC gbcpass = new GBC(1,3,3,1).setInsets(10, 20, 0, 0);
		GBC gbcltype = new GBC(0,4).setInsets(10, -40, 0, 0);
		GBC gbctype = new GBC(1,4,1,1).setInsets(10, 20, 0, 0);
		GBC gbcok = new GBC(2,4,1,1).setInsets(10, 5, 0, 0);
		GBC gbccancle = new GBC(3,4,1,1).setInsets(10, 5, 0, 0);
		Dimension dim = new Dimension(200, 23);
		JLabel lhost = new JLabel("地址:");
		host = new JTextField();
		host.setPreferredSize(dim);
		JLabel lport = new JLabel("端口:");
		port = new JTextField();
		port.setPreferredSize(dim);
		JLabel luser = new JLabel("用户名:");
		user = new JTextField();
		user.setPreferredSize(dim);
		JLabel lpass = new JLabel("密码:");
		pass = new JTextField();
		pass.setPreferredSize(dim);
		JLabel ltype = new JLabel("类型:");
		type = new JComboBox<String>();
		type.addItem("SOCKS");
		type.addItem("HTTP");
		type.addItem("DIRECT");
		JButton ok = new JButton("确定");
		JButton cancle = new JButton("取消");
		cancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigDialog.cdialog.hide();
			}
		});
		ok.addActionListener(new ButtonAction());
		this.add(lhost,gbclhost);
		this.add(host,gbchost);
		this.add(lport,gbclport);
		this.add(port,gbcport);
		this.add(luser,gbcluser);
		this.add(user,gbcuser);
		this.add(lpass,gbclpass);
		this.add(pass,gbcpass);
		this.add(ltype,gbcltype);
		this.add(type,gbctype);
		this.add(ok,gbcok);
		this.add(cancle,gbccancle);
		Configuration config = new Configuration();
		String ihost = config.getValue("PROXY_HOST");
		String iport = config.getValue("PROXY_PORT");
		String iuser = config.getValue("PROXY_USER");
		String ipass = config.getValue("PROXY_PASS");
		String itype = config.getValue("PROXY_TYPE");
		host.setText(ihost);
		port.setText(iport);
		user.setText(iuser);
		pass.setText(ipass);
		if(!itype.equals(""))
		{
			type.setSelectedItem(itype);
		}
	}
	class ButtonAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			Configuration config = new Configuration();
			String shost = host.getText().trim();
			String sport = port.getText().trim();
			String suser = user.getText().trim();
			String spass = pass.getText().trim();
			String stype = type.getSelectedItem().toString();
			Safe.PROXY_HOST = shost;
			Safe.PROXY_PORT = sport;
			Safe.PROXY_USER = suser;
			Safe.PROXY_PASS = spass;
			Safe.PROXY_TYPE = stype;
			String sstatus = Common.getProxyStatus();
			Safe.PROXY_STATUS = sstatus;
			config.setValue("PROXY_HOST", shost);
			config.setValue("PROXY_PORT", sport);
			config.setValue("PROXY_USER", suser);
			config.setValue("PROXY_PASS", spass);
			config.setValue("PROXY_TYPE", stype);
			config.setValue("PROXY_STATUS", sstatus);
			JOptionPane.showMessageDialog(ConfigDialog.cdialog, "代理设置成功", "提示", JOptionPane.DEFAULT_OPTION);
		}
	}
}
