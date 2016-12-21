package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import utils.ClassNameTreeCellRenderer;
import utils.EditorUtils;
import utils.FrameFactory;
import utils.ReadLinkmanXMl;
import utils.ReceiveMailTable;

/**
 * ������
 * @author caesar
 * @version Copyright(C) SCU. 2016
 */
public class MainFrame extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static JDesktopPane desktopPane = null;// ���ڴ������ĵ�������������������
	public static MainFrame MAINFRAME;
	private JTree tree;// ����ͼ
	private JList jl;// ��ϵ���б�
	private JPanel panel, panelframe;// panelframe��벿����
	private JLabel labelbackground;
	private JScrollPane scrollPane;
	private JMenuItem exitMI = null, newMailMI = null, sendedMI = null,
			receiveMI = null, recycleMI = null, refreshMI = null, groupMailMI = null,
			helpMI = null,aboutMI = null;
	private JButton addLinkmanButton = null;// �����ϵ�˰�ť
	private JMenu fileMenu = null;
	private JMenu mailMenu = null;
	private JMenu aboutMenu = null;
	private ReadLinkmanXMl readLinkman = null;

	// ��ʼ����������
	public void jFrameValidate() {
		Toolkit tk = getToolkit();// �����Ļ�Ŀ�͸�
		Dimension dim = tk.getScreenSize();
		this.setBounds(dim.width / 2 - 420, dim.height / 2 - 350, 850, 678);
		validate();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public MainFrame() {
		super("SCU�ʼ��ͻ���");
		MAINFRAME = this;
		this.setIconImage(EditorUtils.createIcon("email.png").getImage());
		desktopPane = new JDesktopPane();
		jFrameValidate();// ��ʼ������
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		fileMenu = new JMenu("�ļ�(F)");
		mailMenu = new JMenu("�ʼ�(M)");
		aboutMenu = new JMenu("����(H)");
		menuBar.add(fileMenu);
		menuBar.add(mailMenu);
		menuBar.add(aboutMenu);

		exitMI = addMenuItem(fileMenu, "�˳�", "exit.gif");// �˳��˵���ĳ�ʼ��
		newMailMI = addMenuItem(mailMenu, "�½��ʼ�", "newMail.gif");// �½��ʼ��˵���ĳ�ʼ��
		
		groupMailMI = addMenuItem(mailMenu,"�½�Ⱥ�ʼ�",""); //����Ⱥ�ʼ��˵����ʼ��
		
		sendedMI = addMenuItem(mailMenu, "������", "sended.png");// �ѷ����ʼ��˵���ĳ�ʼ��
		receiveMI = addMenuItem(mailMenu, "�ռ���", "receive.png");// �ռ����ʼ��˵���ĳ�ʼ��
		recycleMI = addMenuItem(mailMenu, "����վ", "deleted.png");// ��ɾ���ʼ��˵���ĳ�ʼ��
		refreshMI = addMenuItem(mailMenu, "ˢ���ռ���", "refresh.jpg");// ��ɾ���ʼ��˵���ĳ�ʼ��
		helpMI = addMenuItem(aboutMenu,"�����ĵ�",""); //�����ĵ��˵����ʼ��
		aboutMI = addMenuItem(aboutMenu,"��������",""); // �������ǲ˵���ĳ�ʼ��		
		
		// �������νڵ�
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("SCU�ʼ��ͻ���");
		DefaultMutableTreeNode send = new DefaultMutableTreeNode("�½��ʼ�");
		DefaultMutableTreeNode sendGroup = new DefaultMutableTreeNode("�½�Ⱥ�ʼ�");
		DefaultMutableTreeNode addressee = new DefaultMutableTreeNode("�ռ���");
		DefaultMutableTreeNode AlreadySend = new DefaultMutableTreeNode("������");
		DefaultMutableTreeNode delete = new DefaultMutableTreeNode("����վ");
		root.add(send);
		root.add(sendGroup);
		root.add(addressee);
		root.add(AlreadySend);
		root.add(delete);

		tree = new JTree(root);
		tree.addMouseListener(this);// Ϊ���νڵ�ע������¼�
		tree.setPreferredSize(new Dimension(160, 650));
		//tree.setBackground(Color.GRAY);
		// ������Ⱦ���νڵ�
		ClassNameTreeCellRenderer render = new ClassNameTreeCellRenderer();
		tree.setCellRenderer(render);
		// ��ϵ�����
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(160, 300));
		// ������벿���
		panelframe = new JPanel();
		panelframe.setLayout(new BorderLayout());
		panelframe.add(panel, BorderLayout.CENTER);
		panelframe.add(tree, BorderLayout.NORTH);

		addLinkmanButton = new JButton();
		addLinkmanButton.setText("��ϵ��");
		addLinkmanButton.setIcon(EditorUtils.createIcon("linkman.gif"));
		panel.add(addLinkmanButton, BorderLayout.NORTH);
		addLinkmanButton.addActionListener(this);// ע�������ϵ���¼�
		readLinkman = new ReadLinkmanXMl();
		jl = readLinkman.makeList();// ������ϵ���б�
		jl.addMouseListener(this);// �����ϵ���б�˫���¼�
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(jl);// �ڹ�������������ϵ��
		validate();

		labelbackground = new JLabel();
		labelbackground.setIcon(null); // ���屳��
		desktopPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(final ComponentEvent e) {
				Dimension size = e.getComponent().getSize();
				labelbackground.setSize(e.getComponent().getSize());
				// ���ô��屳��ͼ
				labelbackground.setText("<html><img width=" + size.width
						+ " height=" + size.height + " src='"
						+ this.getClass().getResource("/main.gif")
						+ "'></html>");
			}
		});
		desktopPane.add(labelbackground, new Integer(Integer.MIN_VALUE));

		// ���һ���ָ��
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panelframe, desktopPane);
		splitPane.setOneTouchExpandable(true);// �ڷָ������ṩһ�� UI С����������չ��/�۵��ָ���
		splitPane.setDividerSize(10);// ���÷ָ����Ĵ�С
		getContentPane().add(splitPane, BorderLayout.CENTER);
	}

	// �����½��˵���
	private JMenuItem addMenuItem(JMenu menu, String name, String icon) {
		// �½��ʼ��˵���ĳ�ʼ��
		JMenuItem menuItem = new JMenuItem(name, EditorUtils.createIcon(icon));
		menuItem.addActionListener(this);// �����˳��˵����¼�
		menu.add(menuItem);
		return menuItem;
	}

	// ����Ӵ���ķ���
	public static void addIFame(JInternalFrame iframe) {
		JInternalFrame[] frames = desktopPane.getAllFrames();
		try {
			for (JInternalFrame ifm : frames) {
				if (ifm.getTitle().equals(iframe.getTitle())) {
					desktopPane.selectFrame(true);
					ifm.toFront();
					ifm.setSelected(true);
					return;
				}
			}
			desktopPane.add(iframe);
			iframe.setSelected(true);
			iframe.toFront();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	// action�¼��Ĵ���
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitMI) {
			System.exit(0);// �˳�ϵͳ
		} else if (e.getSource() == addLinkmanButton) {
			addIFame(FrameFactory.getFrameFactory().getAddLinkManFrame());// ��ϵ���б�
		} else if (e.getSource() == newMailMI) {// �½��ʼ�
			addIFame(FrameFactory.getFrameFactory().getSendFrame());// ������
		} else if (e.getSource() == groupMailMI) { // �½�Ⱥ�ʼ�
			addIFame(FrameFactory.getFrameFactory().getSendGroupMailFrame()); //�½�Ⱥ�ʼ�
		} else if (e.getSource() == itemPopupOne || e.getSource() == refreshMI) {// �Ҽ�ˢ���ռ��б�
			ReceiveMailTable.getMail2Table().startReceiveMail();// �Ҽ�ˢ���ռ��б�
		} else if (e.getSource() == sendedMI) {// �ѷ���
			addIFame(FrameFactory.getFrameFactory().getSendedFrame());// �ѷ���
		} else if (e.getSource() == receiveMI) {// ���ʼ�
			addIFame(FrameFactory.getFrameFactory().getReceiveFrame());// ���ʼ�
		} else if (e.getSource() == recycleMI) {// ��ɾ��
			addIFame(FrameFactory.getFrameFactory().getRecycleFrame());// ���ʼ�
		} else if (e.getSource() == helpMI) { //�����ĵ�
			addIFame(FrameFactory.getFrameFactory().getHelpContentsFrame()); // �����ĵ�
		} else if (e.getSource() == aboutMI) { //��������
			addIFame (FrameFactory.getFrameFactory().getAboutUsFrame()); //��������
		}

	}

	private SendFrame sendFrame = null;// �����ʼ�����
	public JMenuItem itemPopupOne = null;// ����Ҽ���һ��ѡ��

	@Override
	public void mouseClicked(MouseEvent e) {
		// ���νڵ��еĵ����¼�
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (e.getSource() == tree && e.getButton() != 3 && e.getButton() != 2) {
			if (selectedNode == null)
				return;
			else if (selectedNode.toString().equals("�½��ʼ�")) {
				sendFrame = FrameFactory.getFrameFactory().getSendFrame();
				addIFame(sendFrame);// ������
			} else if (selectedNode.toString().equals("�½�Ⱥ�ʼ�")) {
				addIFame(FrameFactory.getFrameFactory().getSendGroupMailFrame()); //�½�Ⱥ�ʼ�
			}
			else if (selectedNode.toString().equals("�ռ���")) {
				addIFame(FrameFactory.getFrameFactory().getReceiveFrame());// �ռ���
			} else if (selectedNode.toString().equals("������")) {
				addIFame(FrameFactory.getFrameFactory().getSendedFrame());// �ѷ����ʼ�
			} else if (selectedNode.toString().equals("����վ")) {
				addIFame(FrameFactory.getFrameFactory().getRecycleFrame());// ��ɾ���ʼ�
			}
		} else if (e.getSource() == jl && e.getClickCount() == 2) {// ˫����ϵ���¼�
			int index = jl.getSelectedIndex();
			if (sendFrame != null && sendFrame.isSelected()) {// ��������ʼ����汻��ʼ�����ұ�����
				sendFrame.addLinkman(readLinkman.findLinkman(index));
			}
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getSource() == tree) {// �ռ����Ҽ�ˢ��
			if (selectedNode == null)
				return;
			else if ("�ռ���".equals(selectedNode.toString())) {
				JPopupMenu popup = new JPopupMenu();
				itemPopupOne = new JMenuItem("ˢ���ռ���",
						EditorUtils.createIcon("refresh.jpg"));
				itemPopupOne.addActionListener(this);
				popup.add(itemPopupOne);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
