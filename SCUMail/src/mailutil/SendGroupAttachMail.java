package mailutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 
 * @author caesar
 *
 */
public class SendGroupAttachMail {
	private String SMTPHost = ""; // SMTP������
	private String user = ""; // ��¼SMTP���������ʺ�
	private String password = ""; // ��¼SMTP������������

	private String from = ""; // ����������
	private String to = ""; // �ռ�������
	private String subject = ""; // �ʼ�����
	private String content = ""; // �ʼ�����
	private String priority = "3"; // �ʼ��������ȼ� 1������ 3����ͨ 5������
	private String copy_to = null;// �����ʼ���
	private ArrayList<String> filename = new ArrayList<String>(); // �����ļ���
	private Session mailSession = null;
	private Transport transport = null;

	public SendGroupAttachMail(String SMTPHost, String user, String password, String to, String subject, String content,
			String copy_to, ArrayList<String> filename) {
		this.user = user;
		this.SMTPHost = SMTPHost;
		this.password = password;
		this.from = user;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.copy_to = copy_to;
		this.filename = (ArrayList<String>) filename.clone();
	}

	public void connect() throws Exception {

		// ����һ�����Զ���
		Properties props = new Properties();
		// ָ��SMTP������
		props.put("mail.smtp.host", SMTPHost);
		// ָ���Ƿ���ҪSMTP��֤
		props.put("mail.smtp.auth", "true");
		// ����һ����Ȩ��֤����
		SmtpPop3Auth auth = new SmtpPop3Auth();
		auth.setAccount(user, password);
		// ����һ��Session����
		mailSession = Session.getDefaultInstance(props, auth);
		// �����Ƿ����
		mailSession.setDebug(false);
		if (transport != null)
			transport.close();// �ر�����
		// ����һ��Transport����
		transport = mailSession.getTransport("smtp");
		// ����SMTP������
		transport.connect(SMTPHost, user, password);
	}

	public String send() {
		String issend = "";
		try {// ����smtp������
			connect();
			// ����һ��MimeMessage ����
			MimeMessage message = new MimeMessage(mailSession);

			// ָ������������
			message.setFrom(new InternetAddress(from));
			// ָ���ռ�������
			message.addRecipients(Message.RecipientType.TO, to);
			if (!"".equals(copy_to))
				// ָ������������
				message.addRecipients(Message.RecipientType.CC, copy_to);
			// ָ���ʼ�����
			message.setSubject(subject);
			// ָ���ʼ���������
			message.setSentDate(new Date());
			// ָ���ʼ����ȼ� 1������ 3����ͨ 5������
			message.setHeader("X-Priority", this.priority);
			message.saveChanges();
			// �жϸ����Ƿ�Ϊ��
			if (!filename.isEmpty()) {
				// �½�һ��MimeMultipart����������Ŷ��BodyPart����
				Multipart container = new MimeMultipart();
				// �½�һ������ż����ݵ�BodyPart����
				BodyPart textBodyPart = new MimeBodyPart();
				// ��BodyPart�����������ݺ͸�ʽ/���뷽ʽ
				textBodyPart.setContent(content, "text/html;charset=gbk");
				// �������ż����ݵ�BodyPart���뵽MimeMultipart������
				container.addBodyPart(textBodyPart);
				Iterator<String> fileIterator = filename.iterator();
				while (fileIterator.hasNext()) {// �������и���
					String attachmentString = fileIterator.next();
					// �½�һ������ż�������BodyPart����
					BodyPart fileBodyPart = new MimeBodyPart();
					// �������ļ���Ϊ����
					FileDataSource fds = new FileDataSource(attachmentString);
					fileBodyPart.setDataHandler(new DataHandler(fds));
					// �����ʼ��и����ļ�������������
					String attachName = fds.getName();
					attachName = MimeUtility.encodeText(attachName);
					// �趨�����ļ���
					fileBodyPart.setFileName(attachName);
					// ��������BodyPart������뵽container��
					container.addBodyPart(fileBodyPart);
				}
				// ��container��Ϊ��Ϣ���������
				message.setContent(container);
			} else {// û�и��������
				message.setContent(content, "text/html;charset=gbk");
			}
			// �����ʼ�
			Transport.send(message, message.getAllRecipients());
			if (transport != null)
				transport.close();
		} catch (Exception ex) {
			issend = ex.getMessage();
		}
		return issend;
	}
}
