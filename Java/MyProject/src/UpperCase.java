import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
@HlFan
public class UpperCase implements Father {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("��ѡ�������1 -> ת��д��2 -> תСд��3 -> ��ȡ��ǰʱ�䣺");
			String choose = sc.nextLine();
			while(!"1".equals(choose) && !"2".equals(choose) && !"3".equals(choose)) {
				System.err.println("����������������룡��ѡ�������1 -> ת��д��2 -> תСд��");
				choose = sc.nextLine();
			}
			
			if("3".equals(choose)) {
				System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			}else {
				System.out.println("��������Ҫִ�в������ַ�����");
				String str = sc.nextLine();
				if("1".equals(choose)) {
					System.out.println(str.toUpperCase());
				}else {
					System.out.println(str.toLowerCase());
				}
			}
		}
	}

	@Override
	public void run() {
		System.out.println("This is uppercase��");
	}
}
