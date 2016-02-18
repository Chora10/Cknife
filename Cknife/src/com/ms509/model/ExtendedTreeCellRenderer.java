package com.ms509.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
/*
 * 继承JPanel类且实现TreeCellRenderer接口，
 * 通过重写getTreeCellRendererComponent、paintComponent、getPreferredSize方法
 * 判断节点内容是否为/
 * 如果是则更改图标为驱动图标，不是的显示文件夹图标
 * 未使用
 * @author Chora
 */
public class ExtendedTreeCellRenderer extends JPanel implements TreeCellRenderer
{
	private String name;
//	private Color back;
//	private Color fore;
//	private ImageIcon icon;
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// TODO Auto-generated method stub
		this.name = value.toString();
//		this.back = hasFocus ? Color.GRAY : Color.PINK;
//		this.fore = hasFocus ? Color.RED : Color.GREEN;
		return this;
	}
	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
//		g.setColor(back);
//		g.fillRect(0,0,getWidth(),getHeight());
//		g.setColor(fore);
		if(name.equals("/"))
		{
			g.drawImage(new ImageIcon(getClass().getResource("/com/ms509/images/drive.png")).getImage(),1,2,null);

		}else 
		{
			g.drawImage(new ImageIcon(getClass().getResource("/com/ms509/images/folder.png")).getImage(),1,2,null);
		}
		
		g.drawString(this.name,20,14);
	}
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(100,20);
	}
}
