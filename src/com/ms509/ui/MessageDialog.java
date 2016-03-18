package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.util.Timer;

public class MessageDialog extends JDialog {
	private int i;
	private java.util.TimerTask task;
	private java.util.Timer timer;

	public MessageDialog(String message,int time) {
		super(MainFrame.main, time+"秒后自动关闭窗口", true);
		this.i = time;
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

//		this.setModal(true); // 该方法可用于模态对话框，也可用于非模态对话框。
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				setTitle(--i + "秒后自动关闭窗口");
				if (i == 0) {
					timer.cancel(); // 终止此计时器
//					task.cancel(); // 取消此计时器任务
					setVisible(false);
				}
			}
		};
		timer.schedule(task, 1000, 1000);
		setVisible(true);

//		this.setModal(false); // 该方法只能用于非模态对话框
//		final javax.swing.Timer timer = new javax.swing.Timer(1000,
//				new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						setTitle(--i + "秒后自动关闭窗口");
//					}
//				});
//		timer.start();
//		setVisible(true);
//		Runnable run = new Runnable() {
//			public void run() {
////				while (timer.isRunning()) {
////					if (i == 0) {
////						SwingUtilities.invokeLater(new Runnable() {
////							@Override
////							public void run() {
////								timer.stop();
////								setVisible(false);
////							}
////						});
////						break;
////					}
////				}
//				// 在多线程编程中，主线程执行无线循环需要加入sleep或者yield让其他线程有机会执行，
//				// 不然其他线程没机会执行，则无线循环的条件也就满足不了，则会进入死循环。
//				while (true) {
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//					}
//					Thread.yield();
//					if (i == 0) {
//						// 必须通过EDT刷新组件
//						SwingUtilities.invokeLater(new Runnable() {
//							@Override
//							public void run() {
//								timer.stop();
//								setVisible(false);
//							}
//						});
//						break;
//					}
//				}
//			}
//		};
//		// 不能在EDT中执行其他耗时操作，应该在一个独立的工作器线程中（即普通线程中）做这件事情
//		new Thread(run).start();
	}
}
