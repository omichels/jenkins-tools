package tools.jenkins;


import static org.junit.Assert.*;

import org.junit.Test;

import tools.jenkins.control.SubversionPathGenerator;
import tools.jenkins.entity.CvsRepositoryItem;

public class TestSubversionPathGenerator {


	@Test
	public void testGetTranslatedPath_for_HEAD() {
		CvsRepositoryItem item = new CvsRepositoryItem("","remote", "HEAD", null);
		SubversionPathGenerator gen = new SubversionPathGenerator();
		String p = "svn://gi-d-rep02/projects/${branchesTagsTrunkFolders}/${projectName}";
		assertEquals("svn://gi-d-rep02/projects/trunk/remote", gen.transformPath(p, item));
	}
	
	@Test
	public void testGetTranslatedPath_for_BRANCHES() {
		CvsRepositoryItem item = new CvsRepositoryItem("","remote", "BRANCH", "BRANCH_2015");
		SubversionPathGenerator gen = new SubversionPathGenerator();
		String p = "svn://gi-d-rep02/projects/${branchesTagsTrunkFolders}/${projectName}";
		assertEquals("svn://gi-d-rep02/projects/branches/BRANCH_2015/remote", gen.transformPath(p, item));
		item = new CvsRepositoryItem("localName","remote", "BRANCH", "BRANCH_2015");
		assertEquals("svn://gi-d-rep02/projects/branches/BRANCH_2015/localName", gen.transformPath(p, item));
	}
	
	@Test
	public void testGetTranslatedPath_for_TAGS() {
		CvsRepositoryItem item = new CvsRepositoryItem("","remote", "TAG", "TRY_TAG_01-01-2015");
		SubversionPathGenerator gen = new SubversionPathGenerator();
		String p = "svn://gi-d-rep02/projects/${branchesTagsTrunkFolders}/${projectName}";
		assertEquals("svn://gi-d-rep02/projects/tags/TRY_TAG_01-01-2015/remote", gen.transformPath(p, item));
		
		item = new CvsRepositoryItem("","remote", "TAG", "TRY_TAG_01-01-2015");
		p = "svn://gi-d-rep02/projectsNames/${projectName}/${branchesTagsTrunkFolders}";
		assertEquals("svn://gi-d-rep02/projectsNames/remote/tags/TRY_TAG_01-01-2015", gen.transformPath(p, item));
		item = new CvsRepositoryItem("fooName","remote", "TAG", "TRY_TAG_01-01-2015");
		assertEquals("svn://gi-d-rep02/projectsNames/fooName/tags/TRY_TAG_01-01-2015", gen.transformPath(p, item));
	}
	
	@Test
	public void testGetTranslatedPath_for_EachProjectHasOwnBranchesTagsTrunk() {
		CvsRepositoryItem item = new CvsRepositoryItem("","fooProj/BarComponent/model/db", "BRANCH", "BRANCH_07_2015");
		SubversionPathGenerator gen = new SubversionPathGenerator();
		String p = "svn://gi-d-rep02/svn-root/${projectName}/${branchesTagsTrunkFolders}";
		assertEquals("svn://gi-d-rep02/svn-root/fooProj/branches/BRANCH_07_2015/BarComponent/model/db", gen.transformPath(p, item));
		
		item = new CvsRepositoryItem("","fooProj/BarComponent/model/db", "HEAD", null);
		p = "svn://gi-d-rep02/svn-root/${projectName}/${branchesTagsTrunkFolders}";
		assertEquals("svn://gi-d-rep02/svn-root/fooProj/trunk/BarComponent/model/db", gen.transformPath(p, item));
	}

	
	
	
}
