package com.ms509.ui.menu;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.ms509.ui.MainFrame;

public class ShellPopMenu {

	private JPopupMenu shellmenu;
	private JMenuItem copy, paste;
	private JPanel p;
	private JTextPane c;
	Clipboard clipboard;// 获取系统剪贴板。
	Transferable contents, tText;
	DataFlavor flavor;
	private Document shell_doc;
	public ShellPopMenu(JPanel panel, JTextPane console) {
		// TODO Auto-generated constructor stub
		p = panel;
		c = console;
		shell_doc = console.getDocument();
		shellmenu = new JPopupMenu();
		copy = new JMenuItem("复制");
		paste = new JMenuItem("粘贴");
		shellmenu.add(copy);
		shellmenu.add(paste);
		p.add(shellmenu);
		MenuAction action = new MenuAction();
		copy.addActionListener(action);
		paste.addActionListener(action);
		console.addMouseListener(new MouseL());

	}

	class MenuAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == copy) {
				String k = c.getSelectedText();

				clipboard = MainFrame.main.getToolkit().getSystemClipboard();
				tText = new StringSelection(k);
				clipboard.setContents(tText, null);
			} else if (e.getSource() == paste) {
				
				
				// System.out.println("1");
				clipboard = MainFrame.main.getToolkit().getSystemClipboard();
				Transferable clipT = clipboard.getContents(null);   
				if (clipT != null) {   
				// 检查内容是否是文本类型   
				if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))   
				{
					try {
						String pastestr = (String)clipT.getTransferData(DataFlavor.stringFlavor);
						try {
							shell_doc.insertString(shell_doc.getLength(), pastestr, null);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (UnsupportedFlavorException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				}
			}

		}
	}

	class MouseL implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.isMetaDown()) // 判断右键
			{
				shellmenu.show(c, e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
