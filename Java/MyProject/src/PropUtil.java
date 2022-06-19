import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropUtil {

	/**
	 * 根据键获取值
	 */
	public static String getValue(String key, String path) {
		String retVal = "";
		FileInputStream fis = null;
		try {
			Properties prop = new Properties();
			fis = new FileInputStream(new File(path));
			prop.load(fis);
			retVal = prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return retVal;
	}

	/**
	 * 设值
	 */
	public static void setValue(String key, String value, String path) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			Properties prop = new Properties();
			fis = new FileInputStream(new File(path));
			prop.load(fis);
			prop.setProperty(key, value);
			fos = new FileOutputStream(new File(path));
			prop.store(fos, "saveRecord");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != fis) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据key删除相应的值
	 */

	public static void remove(String key, String path) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			Properties prop = new Properties();
			fis = new FileInputStream(new File(path));
			prop.load(fis);
			prop.remove(key);
			fos = new FileOutputStream(new File(path));
			prop.store(fos, "saveRecord");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != fis) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
