package tools.jenkins.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import tools.jenkins.entity.JenkinsJob;

public class JenkinsJobUpdater {
	
	
	private HttpClient client;
	private PostMethod method;



	private void createAuthenticatedClient() {
		client = new HttpClient();
		client.getParams().setParameter("http.connection.timeout",new Integer(5000));
		 client.getState().setCredentials(AuthScope.ANY, 
	  				new UsernamePasswordCredentials(
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminUser"),  
	  						MigrationProperties.getInstance().getTranslatedValue("jenkinsServerAdminPassword")));
		method  = new PostMethod();
		method.setDoAuthentication(true);
		client.getParams().setAuthenticationPreemptive(true);
	}
	


	public void updateJobs(List<JenkinsJob> jobs) {
		createAuthenticatedClient();
		
		try {
		for (JenkinsJob jenkinsJob : jobs) {
			System.out.println(jenkinsJob.getUrl());
			System.out.println(jenkinsJob);
				method.setURI(new URI(jenkinsJob.getUrl() + "/config.xml" , true));
				XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				outputter.output(jenkinsJob.getDocument(), baos);
				RequestEntity requestEntity = new InputStreamRequestEntity(new ByteArrayInputStream(baos.toByteArray()));
				method.setRequestEntity(requestEntity);
				int resp = client.executeMethod(method);
				System.out.println(resp);
				String response = method.getResponseBodyAsString( );
				System.out.println( response );
			}
		} catch (NullPointerException | IOException e) {
			throw new RuntimeException(e);
		} finally{
			method.releaseConnection();
		}
		
	}

}
