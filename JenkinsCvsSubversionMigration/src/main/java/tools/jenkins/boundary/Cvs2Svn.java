package tools.jenkins.boundary;

import java.util.List;

import tools.jenkins.control.JenkinsJobLister;
import tools.jenkins.control.JenkinsJobUpdater;
import tools.jenkins.entity.JenkinsJob;

public class Cvs2Svn {
	
	public static void main(String[] args) {
	
		JenkinsJobLister lister = new JenkinsJobLister();
		List<JenkinsJob> jobs = lister.getMigratedJobs();
		
		JenkinsJobUpdater updater = new JenkinsJobUpdater();
		updater.updateJobs(jobs);
		
	}

}
