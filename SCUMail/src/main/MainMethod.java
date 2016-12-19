package main;

import javax.swing.UIManager;

import frame.LoginFrame;

/**
 * ������
 * @author caesar
 * @version Copyright(C) SCU. 2016
 */
public class MainMethod {
	public static void main(String[] args) {
		// ���ý���Ϊ����ģʽ
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginFrame().setVisible(true);
			}
		});

	}

}
