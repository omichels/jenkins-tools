package tools.jenkins.control;

import tools.jenkins.entity.CvsRepositoryItem;

/**
 * for testing purpose only<p>
 * uses a fixed pattern instead of reading the pattern from configurations.properties
 * @author oliver
 *
 */
public class FixedPatternSubversionPathGenerator extends SubversionPathGenerator {
	
	@Override
	public String transformPath(String pattern, CvsRepositoryItem item) {
		return super.transformPath("svn://server/projects/${projectName}/${branchesTagsTrunkFolders}", item);
	}
	
}
