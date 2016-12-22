package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import utils.EditorUtils;
import utils.SendedMailTable;
import mailutil.SendAttachMail;
import frame.JProgressBarFrame;

/**
 * ����Ⱥ�ʼ�����
 * 
 * @author caesar
 * @version Copyright(C) SCU. 2016
 */
public class SendGroupMailFrame extends JInternalFrame implements MouseListener, ActionListener {
	private JPanel upperPanel = null;
	private JButton linkmanInfoButton = null;
	private JTable table = null;
	private JButton sendGroupButton = null;
	private JButton resetButton = null;
	private JProgressBarFrame progressBar = null;
	// ��÷���ʵ��
	private SendAttachMail groupMails = SendAttachMail.getSendMailInstantiate();
	private int sendedEmailCount = 0;
	private int totalEmailCount = 0;

	// ������ϵ����Ϣ
	Vector<Vector<String>> linkmanInfo = new Vector<Vector<String>>();

	public SendGroupMailFrame() {
		super("Ⱥ�ʼ�");
		// ���öԻ���ͼ��
		this.setFrameIcon(EditorUtils.createIcon("newGroup.jpg"));
		this.setClosable(true);
		this.setMaximizable(true);// �����������
		this.setIconifiable(true);
		this.setBounds(10, 10, 640, 600);// ���ý���Ĵ�С
		this.getContentPane().setLayout(new BorderLayout());
		this.setVisible(true);

		// �������
		upperPanel = new JPanel();
		getContentPane().add(upperPanel, BorderLayout.NORTH);
		upperPanel.setLayout(new BorderLayout(0, 0));
		// ������
		final JToolBar toolBar = new JToolBar();
		toolBar.setEnabled(false);
		upperPanel.add(toolBar);
		// ͨѶ¼���ܰ���
		linkmanInfoButton = new JButton("ͨѶ¼", EditorUtils.createIcon("addressBook.png"));
		linkmanInfoButton.addActionListener(this);
		toolBar.add(linkmanInfoButton, JPanel.LEFT_ALIGNMENT);
		// Ⱥ�����ܰ���
		sendGroupButton = new JButton("Ⱥ��", EditorUtils.createIcon("sendGroup.png"));
		sendGroupButton.addActionListener(this);
		toolBar.add(sendGroupButton, JPanel.LEFT_ALIGNMENT);
		// ���ù��ܰ���
		resetButton = new JButton("����", EditorUtils.createIcon("reset.png"));
		resetButton.addActionListener(this);
		toolBar.add(resetButton, JPanel.LEFT_ALIGNMENT);

		// �������
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		// ͨѶ¼��
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setEnabled(false);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "����", "��ϵ��ַ", "�ʼ�����", "�ʼ�����" }));
		table.setRowHeight(25);
		scrollPane.setViewportView(table);
	}

	// �����¼�����
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == linkmanInfoButton) {
			openLinkmanInfoEvent();
		} else if (e.getSource() == sendGroupButton) {
			if (!isEmpty())
				sendGroupMail(linkmanInfo);
			else
				JOptionPane.showMessageDialog(SendGroupMailFrame.this, "ͨѶ¼Ϊ�գ�������" + "ͨѶ¼���ٴγ���Ⱥ���ʼ���", "����",
						JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == resetButton) {
			resetButtonEvent();
		}
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return linkmanInfo.size() == 0;
	}

	public void resetButtonEvent() {
		// TODO Auto-generated method stub
		linkmanInfo.clear();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "����", "��ϵ��ַ", "�ʼ�����", "�ʼ�����" }));
	}

	public void openLinkmanInfoEvent() {
		// ����һ����ǰ·�����ļ�ѡ����
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setOpaque(false);
		// ֻ�����ļ�
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setAcceptAllFileFilterUsed(false);

		// xls�ı�������
		chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls"))
					// TODO Auto-generated method stub
					return true;
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "xls�ļ�(*.xls)";
			}

		});

		// ���������ļ�
		chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "�����ļ�(*.*)";
			}

		});

		if (chooser.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {// ���ѡ��ȷ����
			File file = chooser.getSelectedFile();
			Icon icon = chooser.getIcon(file);
			if (file.isFile()) {
				// System.out.println("�ļ�:" + file.getAbsolutePath());
				dispLinkmanInfo(file);
			}
			// System.out.println(file.getName());
			// ����Ⱥ�ʼ�
			validate();
			repaint();
		}
	}

	public void dispLinkmanInfo(File file) {
		// ������ϵ��
		Vector<String> linkman = null;
		// �������ģ��
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		try {
			FileInputStream fis = new FileInputStream(file);
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			Sheet[] sheet = rwb.getSheets();
			for (int i = 0; i < sheet.length; i++) {
				Sheet rs = rwb.getSheet(i);
				for (int j = 1; j < rs.getRows(); ++j) {
					linkman = new Vector<String>();
					for (int k = 0; k < rs.getColumns(); ++k) {
						Cell cell = rs.getCell(k, j);
						if (cell.getContents() == "")
							continue;
						else
							linkman.add(cell.getContents());
					}
					// �����ϵ����Ϣ��ȷ
					if (linkman.size() == 4) {
						linkmanInfo.add(linkman);
						// ����ģ�͸���
						model.addRow(linkman);
						// ������
						table.updateUI();
					}
				}
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMail(final String toMan, final String subject, final ArrayList<String> list, final String text,
			final String copy, final String sendMan) {

		groupMails.setContent(text);// �����ʼ�����
		groupMails.setFilename(list);// �����ʼ���������
		groupMails.setFrom(sendMan);// ���÷�����
		groupMails.setSubject(subject);// �����ʼ�����
		groupMails.setTo(toMan);// �����ռ���
		groupMails.setCopy_to(copy);// ���ó�����
		if ((getSendedEmailCount()) == 0 && (getTotalEmailCount() != 0)) {
			JOptionPane.showMessageDialog(SendGroupMailFrame.this, "Ⱥ�ʼ����ڷ�����...��ȴ����������ʾ", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		}
		String message = "";
		if ("".equals(message = groupMails.send())) {
			SendedMailTable.getSendedMailTable().setValues(toMan, subject, list, text, copy, sendMan);// ���ʼ���ӵ��ѷ���
			// message = "�ʼ��ѷ��ͳɹ���";
		} else {
			message = "<html><h4>�ʼ�����ʧ�ܣ� ʧ��ԭ��</h4></html>\n" + message;
			JOptionPane.showMessageDialog(SendGroupMailFrame.this, message, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public int getSendedEmailCount() {
		return this.sendedEmailCount;
	}

	public int getTotalEmailCount() {
		return this.totalEmailCount;
	}

	public void sendGroupMail(Vector<Vector<String>> linkmanInfo) {
		// ��ʼ��Ⱥ�ʼ�����״̬
		sendedEmailCount = 0;
		totalEmailCount = linkmanInfo.size();
		// ������Ⱥ�ʼ�����
		String toMan = ""; // �ռ���
		String subject = ""; // ����
		ArrayList<String> list = new ArrayList<String>(); // �����б�
		String content = ""; // �ʼ���������
		String copy_to = ""; // ������
		String sendMan = groupMails.getUser(); // ������

		try {
			// System.out.println(linkmanInfo.toString());
			for (int i = 0; i < linkmanInfo.size(); i++) {
				// �����ʼ�����
				subject = linkmanInfo.get(i).get(2);
				// �����ʼ��ռ���
				toMan = linkmanInfo.get(i).get(1);
				// �����ʼ�����
				content = linkmanInfo.get(i).get(3);
				// ���ó�����
				groupMails.setCopy_to("");
				// �����ʼ�����ʵӦ���ö��̸߳�д���Ժ���д
				sendMail(toMan, subject, list, content, copy_to, sendMan);// �����ʼ�
				// �ɹ������ʼ�������1
				sendedEmailCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JOptionPane.showMessageDialog(SendGroupMailFrame.this, "Ⱥ�ʼ�����" + sendedEmailCount + "�������!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			System.out.println("�ɹ�����" + sendedEmailCount + "��");
		}
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
