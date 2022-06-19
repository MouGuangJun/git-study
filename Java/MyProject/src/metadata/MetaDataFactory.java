package metadata;


public abstract class MetaDataFactory {

	private static MetaDataFactory factory;

	public void init() {
		initMetaData();
		System.out.println(this);
		factory = this;
	}

	public abstract void initMetaData();

	public static MetaDataFactory getFactory() throws Exception {
		AREServiceStub s = new AREServiceStub();
		s.loadService();
		return factory;
	}
	
	public abstract DataSourceMetaData getInstance(String s);
}
