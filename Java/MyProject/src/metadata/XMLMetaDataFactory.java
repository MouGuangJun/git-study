package metadata;

public class XMLMetaDataFactory extends MetaDataFactory {
	
	@Override
	public void initMetaData() {
		
	}

	@Override
	public DataSourceMetaData getInstance(String s) {
		return new DataSourceMetaData();
	}
}
