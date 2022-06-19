package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class FileHelper {

	// 写文件
	public static void write(String fileName, String msg) {
		write(fileName, msg, false, false);
	}

	// 写文件（追加输出）
	public static void append(String fileName, String msg) {
		write(fileName, msg, true, false);
	}

	public static void appendWithEnter(String fileName, String msg) {
		write(fileName, msg, true, true);
	}

	private static void write(String fileName, String msg, boolean flag, boolean enter) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file, flag);
			if (enter) {
				fos.write("\r\n".getBytes());
			}
			fos.write(msg.getBytes());
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 记录错误到文件系统
	 */
	public static void writeError(String fileName, Exception e) {
		write(fileName, getDetailMessage(e));
	}

	private static String getDetailMessage(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			return sw.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (sw != null) {
				try {
					sw.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return "";
	}

	/*
	 * 复制文件
	 */
	public static void copyFile(String source, String target) {
		File oldFile = new File(source);
		if (!oldFile.exists())
			return;

		copyFile(oldFile, new File(target));
	}

	public static void copyFile(File sourceFile, File targetFile) {
		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			FileOutputStream fos = new FileOutputStream(targetFile);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				fos.flush();
			}

			fos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 复制文件夹
	 */
	public static void copyFolder(String source, String target) {
		File newFolder = new File(target);
		if (!newFolder.exists()) {
			newFolder.mkdirs();
		}

		File oldFolder = new File(source);
		String[] files = oldFolder.list();
		File temp = null;

		for (String file : files) {
			if (source.endsWith(File.separator)) {
				temp = new File(source + file);
			} else {
				temp = new File(source + File.separator + file);
			}

			if (temp.isFile()) {
				copyFile(temp, new File(target + File.separator + temp.getName()));
			}

			if (temp.isDirectory()) {
				copyFolder(source + File.separator + file, target + File.separator + file);
			}

		}
	}
}
