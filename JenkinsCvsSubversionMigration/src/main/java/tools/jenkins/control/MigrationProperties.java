package tools.jenkins.control;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class MigrationProperties {

	private Properties properties = new Properties();
	
	private final static MigrationProperties instance = new MigrationProperties();
 
	public static MigrationProperties getInstance() {
		return instance;
	}
	
	private MigrationProperties() {
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream("configuration.properties"));
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	
	public String getValue(String pKey) {
		if (properties.containsKey(pKey))  {
			return properties.getProperty(pKey);
		} else {
			throw new RuntimeException("no such key :" + pKey);
		}
	}
	
	
	public String getValueOrEmptyString(String pKey) {
		if (properties.containsKey(pKey))  {
			return properties.getProperty(pKey);
		} else {
			return "";
		}
	}
	

}
