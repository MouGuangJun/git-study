import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BeansUtils {
	
	public static void main(String[] args) throws Exception {
		System.out.println(getBeans(Class.forName("Students")));
	}

	public static Object getBeans(Class<?> clazz) throws Exception {
		List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> !"id".equals(f.getName()) && !"serialVersionUID".equals(f.getName()))
				.collect(Collectors.toList());
		Object obj = clazz.newInstance();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Value.class)) {
				String value = field.getAnnotation(Value.class).value();
				PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
				Method method = descriptor.getWriteMethod();
				System.out.println(field.getGenericType().toString());
				invoke(method, field, obj, value);
				// method.invoke(obj, value);
			}
		}

		return obj;
	}

	private static void invoke(Method method, Field field, Object obj, String value) throws Exception {
		String fieldType = field.getType().getSimpleName();
		if ("String".equals(fieldType)) {
			method.invoke(obj, value);
		}

		if ("Interger".equals(fieldType) || "int".equals(fieldType)) {
			Integer intVal = Integer.parseInt(value);
			method.invoke(obj, intVal);
		}

		if ("Long".equalsIgnoreCase(fieldType)) {
			Long longVal = Long.parseLong(value);
			method.invoke(obj, longVal);
		}

		if ("Short".equalsIgnoreCase(fieldType)) {
			Short shortVal = Short.parseShort(value);
			method.invoke(obj, shortVal);
		}

		if ("Byte".equalsIgnoreCase(fieldType)) {
			Byte byteVal = Byte.parseByte(value);
			method.invoke(obj, byteVal);
		}

		if ("Float".equalsIgnoreCase(fieldType)) {
			Float floatVal = Float.parseFloat(value);
			method.invoke(obj, floatVal);
		}

		if ("Double".equalsIgnoreCase(fieldType)) {
			Double doubleVal = Double.parseDouble(value);
			method.invoke(obj, doubleVal);
		}

		if ("Boolean".equalsIgnoreCase(fieldType)) {
			Boolean booleanVal = Boolean.parseBoolean(value);
			method.invoke(obj, booleanVal);
		}

		if ("Character".equals(fieldType) || "char".equals(fieldType)) {
			if (value.length() > 1) {
				throw new Exception("field [" + field.getName() + "] value [" + value + "] is not a Character£¡");
			}
			char temp = value.toCharArray()[0];
			method.invoke(obj, temp);
		}

		if ("Date".equals(fieldType)) {
			Date temp = parseDate(value);
			method.invoke(obj, temp);
		}
	}

	private static Date parseDate(String value) {
		if (null == value || "".equals(value)) {
			return null;
		}

		try {
			String fmtStr = "";
			if (value.indexOf("-") > 0 && value.indexOf(":") > 0) {
				fmtStr = "yyyy-MM-dd HH:mm:ss";
			}

			if (value.indexOf("/") > 0 && value.indexOf(":") > 0) {
				fmtStr = "yyyy/MM/dd HH:mm:ss";
			}

			if (value.indexOf("-") > 0 && value.indexOf(":") < 0) {
				fmtStr = "yyyy-MM-dd";
			}

			if (value.indexOf("/") > 0 && value.indexOf(":") < 0) {
				fmtStr = "yyyy/MM/dd";
			}

			return new SimpleDateFormat(fmtStr).parse(value);

		} catch (Exception e) {
			return null;
		}
	}
}
