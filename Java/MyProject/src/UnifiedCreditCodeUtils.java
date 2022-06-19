import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UnifiedCreditCodeUtils {
	public static String baseCode = "0123456789ABCDEFGHJKLMNPQRTUWXY";
	public static char[] baseCodeArray = baseCode.toCharArray();
	public static int[] wi = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
	
	public static HashMap<Character, Integer> generateCodes() {
		HashMap<Character, Integer> codes = new HashMap<Character, Integer>();
		for (int i = 0; i < baseCodeArray.length; i++) {
			codes.put(baseCodeArray[i], i);
		}
		
		return codes;
	}
	
	public static boolean validateUnifiedCreditCode(String unifiedCreditCode) {
		if("".equals(unifiedCreditCode) || unifiedCreditCode.length() != 18) {
			return false;
		}
		
		HashMap<Character, Integer> codes = generateCodes();
		int parityBit;
		try {
			parityBit = getParityBit(unifiedCreditCode, codes);
		}catch (Exception e) {
			return false;
		}
		
		return parityBit == codes.get(unifiedCreditCode.charAt(unifiedCreditCode.length() - 1));
	}
	
	public static int getParityBit(String unifiedCreditCode, HashMap<Character, Integer> codes) {
		char[] businessCodeArray = unifiedCreditCode.toCharArray();
		int sum = 0;
		
		for (int i = 0; i < 17; i++) {
			char key = businessCodeArray[i];
			if (baseCode.indexOf(key) == -1) {
				throw new RuntimeException("第" + String.valueOf(i + 1) + "位传入了非法字符：" + key);
			}
			
			sum += (codes.get(key) * wi[i]);
		}
		
		int result = 31 - sum % 31;
		
		return result == 31 ? 0 : result;
	}
	
	public static String generateOneUnifiedCreditCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 17; i++) {
			int num = random.nextInt(baseCode.length() - 1);
			sb.append(baseCode.charAt(num));
		}
		
		String code = sb.toString();
		String upperCode = code.toUpperCase();
		HashMap<Character, Integer> codes = generateCodes();
		int parityBit = getParityBit(upperCode, codes);
		
		if(null == codes.get((char) parityBit)) {
			System.err.println("生成社会统一信用代码不符合规则！");
			upperCode = generateOneUnifiedCreditCode();
		}else {
			upperCode = upperCode + codes.get(parityBit);
		}
		
		return upperCode;
	}
	
	public static void main(String[] args) {
		
	}
}
