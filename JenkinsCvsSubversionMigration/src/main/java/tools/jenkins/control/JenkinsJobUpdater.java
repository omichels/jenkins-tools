package tools.jenkins.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import tools.jenkins.entity.JenkinsJob;

public class JenkinsJobUpdater extends JenkinsClient {
	
	public JenkinsJobUpdater(){
		method = new PostMethod();
		createAuthenticatedClient();
	}
	
	public void updateJobs(List<JenkinsJob> jobs) {
		try {
			for (JenkinsJob jenkinsJob : jobs) {
				method.setURI(new URI(jenkinsJob.getUrl() + "/config.xml" , true));
				XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				outputter.output(jenkinsJob.getDocument(), baos);
				RequestEntity requestEntity = new InputStreamRequestEntity(new ByteArrayInputStream(baos.toByteArray()));
				((PostMethod) method).setRequestEntity(requestEntity);
				int resp = client.executeMethod(method);
				System.out.println(resp);
			}
		} catch (NullPointerException | IOException e) {
			throw new RuntimeException(e);
		} finally{
			method.releaseConnection();
		}
	}
}
