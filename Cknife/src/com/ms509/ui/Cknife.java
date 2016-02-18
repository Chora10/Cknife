package com.ms509.ui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import com.ms509.util.Configuration;
import com.ms509.util.InitConfig;

public class Cknife {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new InitConfig();
				setLookFeel();
				new MainFrame();

			}
		});
	}

	public static void setLookFeel() {
		try {
			Configuration config = new Configuration();
			String skin = config.getValue("SKIN");

			if (skin != null) {
				UIManager.setLookAndFeel(skin);
			} else {
				// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

				UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel");

			}

			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
			} catch (Exception e1) {
			}

		}

	}
}
