import java.security.MessageDigest;

public class MD5 {
	private static final char[] hexCode = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
	public static void main(String[] args) throws Exception {
		String src = "000000als";
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(src.getBytes("UTF-8"));
		byte[] ss = md5.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : ss) {
			//sb.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
			//sb.append(toHex(b));
			sb.append(hexCode[(b >>> 4) & 0x0f]);
			sb.append(hexCode[b & 0x0f]);
		}
		
		System.out.println(sb.toString());
	}
	
	public static String encry(String content) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(content.getBytes("UTF-8"));
			byte[] s = md5.digest();
			for (byte b : s) {
				sb.append(hexCode[(b >>> 4) & 0x0f]);
				sb.append(hexCode[b & 0x0f]);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	} 

	private static String toHex(byte b) {
		String result = "";
		result = Integer.toHexString(b & 0xFF);
		if(result.length() == 1) {
			result = '0' + result;
		}
		
		return result;
	}
}
