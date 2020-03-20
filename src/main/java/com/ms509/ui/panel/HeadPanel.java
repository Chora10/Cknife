package com.ms509.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.ms509.ui.MainFrame;

public class HeadPanel extends JPanel {
	private JLabel tabclose;
	private JPanel panel;
	public HeadPanel(JPanel panel) {
		// TODO Auto-generated constructor stub
		this.panel = panel;
		this.setLayout(new BorderLayout(0,0));
		JLabel tabtitle = new JLabel();
		String[] tmp = MainFrame.tab.getUrl().split("\t");
		tabtitle.setText(this.deal(tmp[1]));
		tabclose = new JLabel("  ");
		tabclose.addMouseListener(new CloseAction());
		this.addMouseListener(new MouseAction());
		this.setOpaque(false);
		this.add(tabtitle,BorderLayout.WEST,-1);
		this.add(tabclose,BorderLayout.EAST,-1);
	}
	private String deal(String url)
	{
		String domain="";
		Matcher m = Pattern.compile("(?:\\w+?://)?([\\w-\\.]+)").matcher(url);
		if(m.find())
		{
			domain = m.group(1);
		}
		return domain;
	}
	class MouseAction extends MouseAdapter
	{
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			tabclose.setText("x");
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			tabclose.setText("  ");
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			MainFrame.tab.setSelectedIndex(MainFrame.tab.indexOfComponent(panel));
		}
	}
	class CloseAction extends MouseAdapter
	{
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			tabclose.setText("x");
			tabclose.setForeground(Color.WHITE);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			tabclose.setText("  ");
			tabclose.setForeground(Color.BLACK);
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			MainFrame.tab.removeTabAt(MainFrame.tab.indexOfComponent(panel));
		}
	}
}
