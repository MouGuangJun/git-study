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
	/** jar�е��ļ�·���ָ��� **/
	private static final char SLASH_CHAR = '/';
	/** �����ָ��� **/
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
	 * �ڵ�ǰ��Ŀ��Ѱ��ָ�����µ�������
	 * 
	 * @param packageName ��'.'�ָ��İ���
	 * @param recursive   �Ƿ�ݹ�����
	 * @return �ð����µ�������
	 * @throws Exception
	 */
	public static List<Class<?>> getClass(String packageName, boolean recursive) throws Exception {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		// ��ȡ��ǰ�̵߳���װ��������Ӧ������Ӧ����Դ
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
				// ��ĳЩWeb������������WAR��ʱ����������TOMCATһ����WAR����ѹΪĿ¼�ģ���JBOSS7������ʹ����һ�ֽ�VFS��Э��
				System.out.println("unknown protocol");
				break;
			}

			classList.addAll(childClassList);
		}

		return classList;
	}

	/**
	 * �ڸ������ļ����ļ�����Ѱ��ָ�����µ�������
	 * 
	 * @param url         ����ͳһ��Դ��λ��
	 * @param packageName ��'.'�ָ��İ���
	 * @param recursive   �Ƿ�ݹ�����
	 * @return �ð����µ�������
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInFile(URL url, String packageName, boolean recursive) throws Exception {
		Path path = Paths.get(url.toURI());
		return getClassInFile(path, packageName, recursive);
	}

	/**
	 * �ڸ������ļ����ļ�����Ѱ��ָ�����µ�������
	 * 
	 * @param path        ����·��
	 * @param packageName ��'.'�ָ��İ���
	 * @param recursive   �Ƿ�ݹ�����
	 * @return �ð����µ�������
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

			// ��ȡĿ¼�µ������ļ�
			Iterator<Path> iter = Files.list(path).iterator();
			while (iter.hasNext()) {
				classList.addAll(getClassInFile(iter.next(), packageName, recursive));
			}
		} else {
			// ���ڴ�����ļ����������·��������Ҫ�õ��ļ���ʵ��·��
			path = path.toRealPath();
			String pathStr = path.toString();
			// �����õ�һ���"aa:\bb\cc...\cc.class"��ʽ���ļ�����Ҫȥ��ĩβ�����ͺ�׺(.class)
			int lastDotIndex = pathStr.lastIndexOf(DOT_CHAR);
			// Class.forNameֻ����ʹ��'.'�ָ���������ʽ
			String className = pathStr.replace(File.separatorChar, DOT_CHAR);
			// ��ȡ��������ʼλ��
			int beginIndex = className.indexOf(packageName);
			if (beginIndex == -1) {
				return Collections.emptyList();
			}

			// ����������ʼλ�õ�����λ���и�����
			className = lastDotIndex == -1 ? className.substring(beginIndex)
					: className.substring(beginIndex, lastDotIndex);
			classList.add(Class.forName(className));
		}

		return classList;
	}

	/**
	 * �ڸ�����jar����Ѱ��ָ�����µ�������
	 * 
	 * @param url         jar����ͳһ��Դ��λ��
	 * @param packageName ��'.'�ָ��İ���
	 * @param recursive   �Ƿ�ݹ�����
	 * @return �ð����µ�������
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInJar(URL url, String packageName, boolean recursive) throws Exception {
		JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
		return getClassInJar(jar, packageName, recursive);
	}

	/**
	 * �ڸ�����jar����Ѱ��ָ�����µ�������
	 * 
	 * @param jar         jar����
	 * @param packageName ��'.'�ָ��İ���
	 * @param recursive   �Ƿ�ݹ�����
	 * @return �ð����µ�������
	 * @throws Exception
	 */
	private static List<Class<?>> getClassInJar(JarFile jar, String packageName, boolean recursive) throws Exception {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		//�õ�������ݹ�õ���jar��������Ŀ¼���ļ�
		Enumeration<JarEntry> iter = jar.entries();
		while(iter.hasMoreElements()) {
			//�����õ���һ����"aa/bb/.../cc.class"��ʽ��Entry����"��·��"
			JarEntry jarEntry = iter.nextElement();
			if(!jarEntry.isDirectory()) {
				String name = jarEntry.getName();
				//�����õ����ļ���Ҫȥ��ĩβ��.class
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
