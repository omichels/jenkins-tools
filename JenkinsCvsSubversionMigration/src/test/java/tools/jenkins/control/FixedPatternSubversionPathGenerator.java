package tools.jenkins.control;

import tools.jenkins.entity.CvsRepositoryItem;

public class FixedPatternSubversionPathGenerator extends SubversionPathGenerator {
	
	@Override
	public String transformPath(String pattern, CvsRepositoryItem item) {
		return super.transformPath("svn://server/projects/${projectName}/${branchesTagsTrunkFolders}", item);
	}
	
}
