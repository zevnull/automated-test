package es.s2o.automated.test.core.listeners;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.listeners.GroupsMethodInterceptor;

public class GroupsMethodInterceptorTest {

	private static final String[] POSSIBLE_GROUPS = { "excludeUserPro", "userDemo" };

	@Test
	public void executeMethodForExcludedGroups() {
		GroupsMethodInterceptor groupsMethodInterceptor = new GroupsMethodInterceptor();
		String groupNoToExecute = "tipoEntorno?pro:excludeUserPro";
		System.setProperty("tipoEntorno", "pro");
		List<String> groups = Arrays.asList(POSSIBLE_GROUPS);
		boolean runTest = groupsMethodInterceptor.executeMethodForExcludedGroups(groups, groupNoToExecute);
		Assert.assertFalse(runTest);
	}

	@Test
	public void executeMethodForExcludedGroupsRun() {
		GroupsMethodInterceptor groupsMethodInterceptor = new GroupsMethodInterceptor();
		String groupNoToExecute = "tipoEntorno?pro:excludeUserPro";
		System.setProperty("tipoEntorno", "dsv");
		List<String> groups = Arrays.asList(POSSIBLE_GROUPS);
		boolean runTest = groupsMethodInterceptor.executeMethodForExcludedGroups(groups, groupNoToExecute);
		Assert.assertTrue(runTest);
	}

	@Test
	public void executeMethodForExcludedGroupsNotRun() {
		GroupsMethodInterceptor groupsMethodInterceptor = new GroupsMethodInterceptor();
		String groupNoToExecute = "excludeUserPro";
		List<String> groups = Arrays.asList(POSSIBLE_GROUPS);
		boolean runTest = groupsMethodInterceptor.executeMethodForExcludedGroups(groups, groupNoToExecute);
		Assert.assertFalse(runTest);
	}
}
