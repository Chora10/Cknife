package com.ms509.model;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
/*
 * 继承实现类，判断节点内容是否为/或者为A-Z的盘符
 * 如果是则更改图标为驱动图标，不是的显示文件夹图标
 * @author Chora
 */
public class ExtendedDefaultTreeCellRenderer extends DefaultTreeCellRenderer
{
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// TODO Auto-generated method stub
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode df = (DefaultMutableTreeNode)value;
		ArrayList al = new ArrayList<>();
		String drive = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < drive.length(); i++) {
			al.add(drive.charAt(i)+":");
		}
		if(df.getUserObject().equals("/") || al.contains(df.getUserObject()))
		{
			this.setIcon(new ImageIcon(getClass().getResource("/com/ms509/images/drive.png")));
		} else 
		{
			this.setIcon(new ImageIcon(getClass().getResource("/com/ms509/images/folder.png")));
		}
		return this;
	}
}
