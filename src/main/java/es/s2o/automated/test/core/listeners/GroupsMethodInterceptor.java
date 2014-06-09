package es.s2o.automated.test.core.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * <pre>
 * Filter to test only the methods interseccion of -DincludeGroups=xGroup,yGroup && -DincludeGroups=xGroup,yGroup
 * Formato también disponible para exclude groups: 
 * 			variableDeEntorno?valorVariableEntorno?grupoAExcluir (
 * 			Por ejemplo: tipoEntorno?pro:excludeUserPro 
 * 					donde se resolverá: System.getProperty(variableDeEntorno);
 * </pre>
 * 
 * @author s2o
 */
public class GroupsMethodInterceptor implements IMethodInterceptor {

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<IMethodInstance> result = new ArrayList<IMethodInstance>();
		for (IMethodInstance method : methods) {
			if (isMethodForTestNow(method)) {
				result.add(method);
			}
		}
		return result;
	}

	/**
	 * @return
	 */
	protected boolean isMethodForTestNow(IMethodInstance method) {
		boolean runTest = false;
		String groupIn = System.getProperty("includeGroups");
		String groupNotIn = System.getProperty("excludeGroups");

		if ((groupIn == null) || (groupIn == "")) {
			runTest = true;
		} else {
			StringTokenizer groupsTagList = new StringTokenizer(groupIn, ",");
			runTest = true;

			List<String> groups = getGroups(method);
			while (groupsTagList.hasMoreTokens() && runTest) {
				if (!(groups.contains(groupsTagList.nextToken())))
					runTest = false;
			}

			if (runTest && (groupNotIn != null) && (groupNotIn != "")) {

				StringTokenizer groupsNotInList = new StringTokenizer(groupNotIn, ",");
				while (groupsNotInList.hasMoreTokens() && runTest) {
					runTest = executeMethodForExcludedGroups(groups, groupsNotInList.nextToken());
				}
			}
		}
		return runTest;
	}

	/**
	 * @param runTest
	 * @param groups
	 * @param groupsNotInList
	 * @return
	 */
	protected boolean executeMethodForExcludedGroups(List<String> groups, String groupNoToExecute) {
		boolean runTest = true;

		String[] exclusionDefinition = groupNoToExecute.split("\\?");

		if (exclusionDefinition.length == 1 && groups.contains(exclusionDefinition[0])) {
			runTest = false;
		} else {
			String[] values = exclusionDefinition[1].split(":");
			String actual = System.getProperty(exclusionDefinition[0]);
			if (values[0].equals(actual) && groups.contains(values[1])) {
				runTest = false;
			}

		}

		return runTest;
	}

	/**
	 * @param method
	 * @return
	 */
	private List<String> getGroups(IMethodInstance method) {
		String[] groupsList = method.getMethod().getGroups();
		return Arrays.asList(groupsList);
	}
}
