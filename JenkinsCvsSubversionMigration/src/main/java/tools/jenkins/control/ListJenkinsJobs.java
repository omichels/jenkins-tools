package tools.jenkins.control;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ListJenkinsJobs {

	public static void main(String[] args) throws NullPointerException, HttpException, IOException, JDOMException {
		
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.connection.timeout",new Integer(5000));
		 client.getState().setCredentials(AuthScope.ANY, 
	  				new UsernamePasswordCredentials(
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminUser"),  
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminPassword")));
		GetMethod method  = new GetMethod();
		method.setDoAuthentication(true);
		client.getParams().setAuthenticationPreemptive(true);
	    method.setURI(new URI(MigrationProperties.getInstance().getValueOrEmptyString("jenkinsServerUrl") + "/api/xml", true));
	      int returnCode = client.executeMethod(method);
	      if (returnCode == HttpStatus.SC_OK) {
	    	  SAXBuilder saxBuilder = new SAXBuilder();
	    	  Document document = saxBuilder.build(method.getResponseBodyAsStream());
	    	  Element rootElement = document.getRootElement();
	    	  for (Element jobElement : rootElement.getChildren("job")) {
	    		  String pattern = MigrationProperties.getInstance().getValueOrEmptyString("jobNamePattern");
	    		  	if (jobElement.getChildText("name").matches(pattern)) {
	    		  		String jobUrl = jobElement.getChildText("url");
	    	  
	    		  		method.setURI(new URI(jobUrl + "/config.xml" , true));
 
	    		  		client.executeMethod(method);
	    	  
	    		  		//System.out.println(method.getResponseBodyAsString());
	    		  		
	    		  		JobMigrater migrater = new JobMigrater();
	    		  		migrater.migrateJob(method.getResponseBodyAsStream());
	    		  	}
	    	  }
			
		}
	      method.releaseConnection();
	      
		// TODO Auto-generated method stub

	}

}
