package metadata;

public class AREServiceStub {

	public void loadService() throws Exception {
		Class<?> clazz = Class.forName("metadata.XMLMetaDataFactory");
		MetaDataFactory m = (MetaDataFactory) clazz.newInstance();
		System.out.println(m);
		m.init();
	}
}
