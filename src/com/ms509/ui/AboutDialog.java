package com.ms509.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.ms509.util.GBC;

public class AboutDialog extends JDialog {
	private JPanel about_pane;
	private JLabel img_label;
	private JEditorPane about_info;
	private Icon icon;

	public AboutDialog() {

		super(MainFrame.main, "添加shell", true);
		this.setComponent();
		// 初始化布局和控件

		this.setVisible(true);

	}

	private void setComponent() {
		about_pane = new JPanel();
		about_pane.setLayout(new GridBagLayout());
		about_pane.setOpaque(true);
		about_pane.setBackground(Color.white);

		GBC gbclogo = new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setInsets(0, 0, 0, 0);
		GBC gbccontent1 = new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(1, 1).setInsets(20, 0, 0, 0);

		// img
		img_label = new JLabel();

		try {
			icon = new ImageIcon(getClass().getResource("/com/ms509/images/logo.png"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		img_label.setSize(50, 50);
		img_label.setBackground(Color.white);
		img_label.setIcon(icon);
		img_label.setOpaque(true);

		// text
		JEditorPane about_info = new JEditorPane();
		about_info.setEditable(false);
		about_info.setContentType("text/html");
		String copy = "<html><body><div><h3 style=\"text-align:center;\">Copyright(c) 2015-2016 MS509 Team</h3></div>"
				+ "<div style=\"font-size:10px;text-align:center;\">主页:<a href=\"http://www.ms509.com\">http://www.ms509.com</a></div>"
				+ "<br \\><div style=\"font-size:10px;\">免责声明:本工具仅限于安全研究与教学使用,用户使用本工具所造成的所有后果,由用户承担全部法律及连带责任!作者不承担任何法律及连带责任。</div><div></div>"
				+ "<div style=\"text-align:right;font-size:9px;\">Powered by Chora & MelodyZX&nbsp;&nbsp;&nbsp;&nbsp;</div>"
				+ "</body></html>";
		about_info.setText(copy.toString());
		// 超链接事件
		about_info.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(new URI("http://www.ms509.com"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

		});
		about_info.setOpaque(false);
		about_info.setBackground(Color.white);

		about_pane.add(about_info, gbccontent1);
		about_pane.add(img_label, gbclogo);

		this.add(about_pane);
		this.setSize(400, 250);
		this.setResizable(false);
		this.setTitle("关于 Cknife");
		this.setLocationRelativeTo(MainFrame.main);
	}

}
