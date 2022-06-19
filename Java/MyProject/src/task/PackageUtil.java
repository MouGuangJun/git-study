package task;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {
	/** jar中的文件路径分隔符 **/
	private static final char SLASH_CHAR = '/';
	/** 包名分隔符 **/
	private static final char DOT_CHAR = '.';

	public static void main(String[] args) throws Exception {
		List<Class<?>> classes = getClass("task", true);
		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Task.class)) {
				MainTask task = (MainTask) clazz.newInstance();
				task.run();
			}
		}
	}

	/**
	 * 在当前项目中寻找指定包下的所有类
	 * 
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws Exception
	 */
	public static List<Class<?>> getClass(String packageName, boolean recursive) throws Exception {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		// 获取当前线程的类装载器中相应包名对应的资源
		Enumeration<URL> iter = Thread.currentThread().getContextClassLoader()
				.getResources(packageName.replace(DOT_CHAR, File.separatorChar));
		while (iter.hasMoreElements()) {
			URL url = iter.nextElement();
			String protocol = url.getProtocol();
			List<Class<?>> childClassList = Collections.emptyList();
			switch (protocol) {
			case "file":
				childClassList = getClassInFile(url, packageName, recursive);
				break;
			case "jar":
				childClassList = getClassInJar(url, packageName, recursive);
				break;
			default:
				// 在某些Web服务器中运行WAR包时，他不会像TOMCAT一样将WAR包解压为目录的，如JBOSS7，它是使用了一种叫VFS的协议
				System.out.println("unknown protocol");
				break;
			}

			classList.addAll(childClassList);
		}

		return classList;
	}

	/**
	 * 在给定的文件或文件夹中寻找指定包下的所有类
	 * 
	 * @param url         包的统一资源定位符
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInFile(URL url, String packageName, boolean recursive) throws Exception {
		Path path = Paths.get(url.toURI());
		return getClassInFile(path, packageName, recursive);
	}

	/**
	 * 在给定的文件或文件夹中寻找指定包下的所有类
	 * 
	 * @param path        包的路径
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInFile(Path path, String packageName, boolean recursive) throws Exception {
		if (!Files.exists(path)) {
			return Collections.emptyList();
		}

		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		if (Files.isDirectory(path)) {
			if (!recursive) {
				return Collections.emptyList();
			}

			// 获取目录下的所有文件
			Iterator<Path> iter = Files.list(path).iterator();
			while (iter.hasNext()) {
				classList.addAll(getClassInFile(iter.next(), packageName, recursive));
			}
		} else {
			// 由于传入的文件可能是相对路径，这里要拿到文件的实际路径
			path = path.toRealPath();
			String pathStr = path.toString();
			// 这里拿到一般的"aa:\bb\cc...\cc.class"格式的文件名，要去除末尾的类型后缀(.class)
			int lastDotIndex = pathStr.lastIndexOf(DOT_CHAR);
			// Class.forName只允许使用'.'分隔类名的形式
			String className = pathStr.replace(File.separatorChar, DOT_CHAR);
			// 获取包名的起始位置
			int beginIndex = className.indexOf(packageName);
			if (beginIndex == -1) {
				return Collections.emptyList();
			}

			// 将包名从起始位置到结束位置切割下来
			className = lastDotIndex == -1 ? className.substring(beginIndex)
					: className.substring(beginIndex, lastDotIndex);
			classList.add(Class.forName(className));
		}

		return classList;
	}

	/**
	 * 在给定的jar包中寻找指定包下的所有类
	 * 
	 * @param url         jar包的统一资源定位符
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInJar(URL url, String packageName, boolean recursive) throws Exception {
		JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
		return getClassInJar(jar, packageName, recursive);
	}

	/**
	 * 在给定的jar包中寻找指定包下的所有类
	 * 
	 * @param jar         jar对象
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInJar(JarFile jar, String packageName, boolean recursive) throws Exception {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		//该迭代器会递归得到该jar底下所有目录和文件
		Enumeration<JarEntry> iter = jar.entries();
		while(iter.hasMoreElements()) {
			//这里拿到的一般是"aa/bb/.../cc.class"格式的Entry或者"包路径"
			JarEntry jarEntry = iter.nextElement();
			if(!jarEntry.isDirectory()) {
				String name = jarEntry.getName();
				//对于拿到的文件，要去除末尾的.class
				int lastDotClassIndex = name.lastIndexOf(".class");
				if(lastDotClassIndex != -1) {
					int lastSlashIndex = name.lastIndexOf(SLASH_CHAR);
					name = name.replace(SLASH_CHAR, DOT_CHAR);
					if(name.startsWith(packageName)) {
						if(recursive || packageName.length() == lastSlashIndex) {
							String className = name.substring(0, lastDotClassIndex);
							classList.add(Class.forName(className));
						}
					}
				}
			}
		}
		return classList;
	}

}
