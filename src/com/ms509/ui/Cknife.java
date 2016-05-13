package com.ms509.ui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.fonts.DefaultGnomeFontPolicy;
import org.pushingpixels.substance.internal.fonts.DefaultKDEFontPolicy;
import org.pushingpixels.substance.internal.fonts.DefaultMacFontPolicy;

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
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			if (!skin.equals("")) {
				UIManager.setLookAndFeel(skin);
			} else {
				// substance皮肤带LookAndFeel结尾的其实与不带的是一样的，只是实现方式不同而已。
				// 即SubstanceGraphiteLookAndFeel与GraphiteSkin是同一款皮肤
	
				// 带LookAndFeel结尾的皮肤使用UIManager.setLookAndFeel
//				UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
				
				// 不带LookAndFeel结尾的皮肤使用SubstanceLookAndFeel.setSkin
//				SubstanceLookAndFeel.setSkin(new GraphiteSkin());
				SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.GraphiteSkin");
				String os = System.getProperty("os.name");
				if(os.startsWith("Mac"))
				{
					SubstanceLookAndFeel.setFontPolicy(new DefaultMacFontPolicy());
				} else if(os.startsWith("Linux"))
				{
					SubstanceLookAndFeel.setFontPolicy(new DefaultKDEFontPolicy());
				}
			}
		} catch (Exception e) {
			try {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (Exception e1) {
			}
		}

	}
}
