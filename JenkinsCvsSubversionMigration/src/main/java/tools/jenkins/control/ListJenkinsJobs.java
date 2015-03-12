package tools.jenkins.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import tools.jenkins.entity.JenkinsJob;

public class ListJenkinsJobs {
	
	private HttpClient client;
	private GetMethod method;

	
	private void createAuthenticatedClient() {
		client = new HttpClient();
		client.getParams().setParameter("http.connection.timeout",new Integer(5000));
		 client.getState().setCredentials(AuthScope.ANY, 
	  				new UsernamePasswordCredentials(
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminUser"),  
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminPassword")));
		method  = new GetMethod();
		method.setDoAuthentication(true);
		client.getParams().setAuthenticationPreemptive(true);
	}
	

	public List<JenkinsJob> getMigratedJobs() {
		createAuthenticatedClient();
		List<JenkinsJob> jenkinsJobs = new ArrayList<JenkinsJob>();
	    try {
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
	    		  		JenkinsJob aJob = new JenkinsJob(jobUrl);
	    		  		method.setURI(new URI(jobUrl + "/config.xml" , true));
	    		  		client.executeMethod(method);
	    		  		JobMigrater migrater = new JobMigrater();
	    		  		Document doc = migrater.migrateJob(method.getResponseBodyAsStream());
	    		  		aJob.setDocument(doc);
	    		  		jenkinsJobs.add(aJob);
	    		  	}
	    	  }
			
			}
		} catch (NullPointerException | IOException | JDOMException e) {
			throw new RuntimeException(e);
		} finally {
			method.releaseConnection();
		}
		return jenkinsJobs;
	}

}
