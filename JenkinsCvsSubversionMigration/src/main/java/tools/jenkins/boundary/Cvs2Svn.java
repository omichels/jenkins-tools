package tools.jenkins.boundary;

import java.util.ArrayList;
import java.util.List;

import tools.jenkins.control.JenkinsJobUpdater;
import tools.jenkins.control.ListJenkinsJobs;
import tools.jenkins.entity.JenkinsJob;

public class Cvs2Svn {

	
	public static void main(String[] args) {
	
		List<JenkinsJob> jobs = new ArrayList<JenkinsJob>();
		
		ListJenkinsJobs lister = new ListJenkinsJobs();
		jobs = lister.getMigratedJobs();
		
		JenkinsJobUpdater updater = new JenkinsJobUpdater();
		updater.updateJobs(jobs);
		
	}

}
