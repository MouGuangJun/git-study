import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
@HlFan
public class UpperCase implements Father {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("请选择操作：1 -> 转大写；2 -> 转小写；3 -> 获取当前时间：");
			String choose = sc.nextLine();
			while(!"1".equals(choose) && !"2".equals(choose) && !"3".equals(choose)) {
				System.err.println("输入错误，请重新输入！请选择操作：1 -> 转大写；2 -> 转小写：");
				choose = sc.nextLine();
			}
			
			if("3".equals(choose)) {
				System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			}else {
				System.out.println("请输入需要执行操作的字符串：");
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
		System.out.println("This is uppercase！");
	}
}
