package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import utils.EditorUtils;

/**
 * ������Ϣ����
 * @author caesar
 * @version Copyright(C) SCU. 2016
 */
public class HelpContentsFrame extends JInternalFrame implements MouseListener,
ActionListener{
	private JLabel background;

	public HelpContentsFrame(){
		super("�����ĵ�");
		//���öԻ���ͼ��
		this.setFrameIcon(EditorUtils.createIcon("help.png"));
		this.setClosable(true);
		this.setMaximizable(true);// �����������
		this.setIconifiable(true);
		this.setBounds(10, 10, 640, 600);
		this.getContentPane().setLayout(new BorderLayout());
		this.setVisible(true);
		
		background = new JLabel();
		background.setIcon(new ImageIcon(this.getClass().getResource("/helpBg.png")));
		getContentPane().add(background);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
