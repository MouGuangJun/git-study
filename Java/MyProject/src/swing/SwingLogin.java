package swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SwingLogin {
	public static void main(String[] args) {
		JFrame fream = new JFrame("Login");
		fream.setSize(350, 200);
		fream.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/**
		 * ������壬���������HTML��div��ǩ
		 * ���ǿ��Դ��������岢��JFrame��ָ��λ��
		 * ��������ǿ�������ı��ֶΣ���ť���������
		 */
		
		JPanel panel = new JPanel();
		//������
		fream.add(panel);
		
		/**
		 * �����û�����ķ����������������
		 */
		placeComponents(panel);
		
		//���ý���ɼ�
		fream.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {
		/**
		 * ���ò���Ϊnull
		 */
		panel.setLayout(null);
		
		JLabel userLabel = new JLabel("User:");
		/**
		 * ������������������λ�á�
		 * setBounds(x, y, width, height)
		 * x��yָ�����Ͻǵ���λ�ã���width��heightָ���µĴ�С
		 */
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);
		
		/**
		 * �����ı��������û�����
		 */
		JTextField userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);
		
		/**
		 * ����������ı���
		 */
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);
		
		/**
		 * �����������������ı���
		 * �����������Ϣ���Ե�Ŵ��棬���ڰ�������İ�ȫ��
		 */
		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 50, 165, 25);
		panel.add(passwordText);
		
		//������¼��ť
		JButton loginButton = new JButton("login");
		loginButton.setBounds(10, 80, 80, 25);
		panel.add(loginButton);
	}
}
