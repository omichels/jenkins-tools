package tools.jenkins.control;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class JenkinsClient {

	protected HttpClient client;
	protected HttpMethodBase method;
	
	
	protected void createAuthenticatedClient() {
		client = new HttpClient();
		client.getParams().setParameter("http.connection.timeout",new Integer(5000));
		client.getState().setCredentials(AuthScope.ANY, 
	  				new UsernamePasswordCredentials(
	  						MigrationProperties.getInstance().getValue("jenkinsServerAdminUser"),  
	  						MigrationProperties.getInstance().getValue("jenkinsServerAdminPassword")));
		method.setDoAuthentication(true);
		client.getParams().setAuthenticationPreemptive(true);
	}


}
