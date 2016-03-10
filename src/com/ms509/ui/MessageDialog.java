package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.TimerTask;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.util.Timer;

public class MessageDialog extends JDialog {
	private int i = 5;
	private java.util.TimerTask task;
	private java.util.Timer timer;

	public MessageDialog(String message) {
		super(MainFrame.main, "5秒后自动关闭窗口", true);
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		this.setResizable(false);
		this.setSize(450, 240);
		this.setLocation((d.width - this.getWidth()) / 2,
				(d.height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		final JTextPane text = new JTextPane();
		text.setText(message);
		JScrollPane scroll = new JScrollPane(text);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scroll, BorderLayout.CENTER);
		this.getContentPane().add(panel);
		
//		this.setModal(true);	// 该方法可用于模态对话框，也可用于非模态对话框。
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				setTitle(--i + "秒后自动关闭窗口");
				if (i == 0) {
					timer.cancel();	// 终止此计时器
					// task.cancel();	// 取消此计时器任务
					setVisible(false);
				}
			}
		};
		timer.schedule(task, 1000, 1000);
		setVisible(true);
		
//		this.setModal(false);	// 该方法只能用于非模态对话框
//		javax.swing.Timer timer = new javax.swing.Timer(1000,
//				new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						setTitle(--i + "秒后自动关闭窗口");
//					}
//				});
//		timer.start();
//		setVisible(true);
//		while (timer.isRunning()) {
//			if (i == 0) {
//				timer.stop();
//				setVisible(false);
//				break;
//			}
//		}
	}
}
