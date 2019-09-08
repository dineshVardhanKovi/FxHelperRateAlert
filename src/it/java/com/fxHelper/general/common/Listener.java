package com.fxHelper.general.common;

import java.util.Arrays;
import java.util.Hashtable;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.LogStatus;

public class Listener implements IInvokedMethodListener, ITestListener {

	// This belongs to IInvokedMethodListener and will execute before every
	// method including @Before @After @Test

	private static int totalMethodsExecuted = 0;
	private static int totalMethodsPassed = 0;
	private static int totalMethodsSkipped = 0;
	private static int totalMethodsFalied = 0;
	private static String testCasesNames = "";

	public static int getPassedMethodsCount() {
		return totalMethodsPassed;
	}

	public static String getFailTestCaseNames() {
		return testCasesNames;
	}

	public static int getFailedMethodsCount() {
		return totalMethodsFalied;
	}

	public static int getSkippedMethodsCount() {
		return totalMethodsSkipped;
	}

	public static int getTotalMethodsExecuted() {
		return totalMethodsExecuted;
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

		Object[] params = testResult.getParameters();

		String paramValues = "";
		for (Object object : params) {
			paramValues = paramValues + object + "<br>";
		}

		String paramsMethodAppend = "";

		paramsMethodAppend = extractParamValues(params, paramValues, paramsMethodAppend);

		// Logs.info(testResult.getParameters());

		String instanceName = "";
		String instance[];
		String packageName = "";

		String className = "";
		if (!method.isConfigurationMethod()) {
			instanceName = method.getTestMethod().getInstance().toString();
			instance = instanceName.split("\\.");
			className = instance[instance.length - 1].split("@")[0];
			packageName = instance[0];
			for (int i = 1; i < instance.length - 1; i++) {
				packageName = packageName + "." + instance[i];
			}
		} else {
			instanceName = method.getTestMethod().getQualifiedName();
			instance = instanceName.split("\\.");
			className = instance[instance.length - 2];
			packageName = instance[0];
			for (int i = 1; i < instance.length - 2; i++) {
				packageName = packageName + "." + instance[i];
			}

		}

		if (method.getTestMethod().getMethodName().contains("setProjectExecutionVariable")
				|| method.getTestMethod().getMethodName().contains("beforeSuite")
				|| method.getTestMethod().getMethodName().contains("afterSuite")) {
			return;
		}
		totalMethodsExecuted = totalMethodsExecuted + 1;
		ExtentTestManager
				.startTest(UtilityMethods.getBrowserName(testResult).toUpperCase() + " => "
						+ method.getTestMethod().getMethodName() + (paramsMethodAppend.replace("env.", Base.url)))
				.assignCategory(UtilityMethods.getBrowserName(testResult).toUpperCase() + "  > "
						+ testResult.getTestContext().getName() + "  > " + className);
		ExtentTestManager.getTest().log(LogStatus.PASS,
				"=========		Execution Started for " + method.getTestMethod().getMethodName()
						+ "		=========<br><b>Package Name:<b>" + packageName + "<br><b>Class Name:<b>" + className
						+ "<br><b>Method Name:<b>" + method.getTestMethod().getMethodName() + "<br><b>Environment:<b>"
						+ UtilityMethods.getEnvforReport());
		if (params.length > 0) {
			if(paramValues.contains("Ljava.lang.String")){
				ExtentTestManager.getTest().log(LogStatus.PASS,
						"This Test case used below parameter values<br>" + Arrays.deepToString(params));
			}else{
			
				ExtentTestManager.getTest().log(LogStatus.PASS,
						"This Test case used below parameter values<br>" + paramValues);
			}
			
		}
		Logs.info("=========	Execution Started for " + method.getTestMethod().getMethodName()
				+ "	=========\nPackage Name :" + packageName + "\nClass Name :" + className + "\nMethod Name :"
				+ method.getTestMethod().getMethodName() + "\n");
	}

	private String extractParamValues(Object[] params, String paramValues, String paramsMethodAppend) {
		if (paramValues.length() > 0) {
			if (paramValues.contains("Ljava.lang.String")) {
				
				try {
					Hashtable<String, String> columnData = getHashMapDataWithColumnnames(params);
					String triggerEvent = columnData.get("TriggerEvent$");
					String locator = columnData.get("Locator$");
					String url = columnData.get("TestURL$").replace("env.", Base.url);
					paramsMethodAppend = "("+url+","+triggerEvent+","+locator+")";
				} catch (Exception e) {
				}
			} else {
				paramsMethodAppend = "(";
				for (int i = 0; i < params.length; i++) {
					if (i == (params.length - 1)) {
						paramsMethodAppend = paramsMethodAppend + params[i] + ")";
					} else {
						paramsMethodAppend = paramsMethodAppend + params[i] + ", ";
					}
				}
			}
		}
		return paramsMethodAppend;
	}

	// This belongs to IInvokedMethodListener and will execute after every
	// method including @Before @After @Test
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.getTestMethod().getMethodName().contains("setProjectExecutionVariable")
				|| method.getTestMethod().getMethodName().contains("beforeSuite")
				|| method.getTestMethod().getMethodName().contains("afterSuite")) {
			return;
		}

		Object[] params = testResult.getParameters();

		String paramValues = "";
		for (Object object : params) {
			paramValues = paramValues + object + "<br>";
		}

		String paramsMethodAppend = "";

		paramsMethodAppend = extractParamValues(params, paramValues, paramsMethodAppend);

		// Logs.info(testResult.getParameters());

		String instanceName = "";
		String instance[];
		String packageName = "";

		String className = "";
		if (!method.isConfigurationMethod()) {
			instanceName = method.getTestMethod().getInstance().toString();
			instance = instanceName.split("\\.");
			className = instance[instance.length - 1].split("@")[0];
			packageName = instance[0];
			for (int i = 1; i < instance.length - 1; i++) {
				packageName = packageName + "." + instance[i];
			}
		} else {
			instanceName = method.getTestMethod().getQualifiedName();
			instance = instanceName.split("\\.");
			className = instance[instance.length - 2];
			packageName = instance[0];
			for (int i = 1; i < instance.length - 2; i++) {
				packageName = packageName + "." + instance[i];
			}

		}

		String moduleName = className;
		String testCaseName = method.getTestMethod().getMethodName() + paramsMethodAppend;

		
		if ((ExtentTestManager.getTest().getRunStatus().toString()).equals("fail")
				|| testResult.getStatus() == ITestResult.FAILURE) {

			testCasesNames = testCasesNames + "<tr><td><font color='#153e7e' size='1' face='Arial'><b>" + moduleName
					+ "</b></font></td><td><font color='#153e7e' size='1' face='Arial'><b>" + testCaseName
					+ "</b></font></td><td width='5%' bgcolor='#E7432B' align='center'><font color='white' size='1' face='Arial'>FAIL</font></td></tr>";

			totalMethodsFalied = totalMethodsFalied + 1;
			if (UtilityMethods.getException(testResult.getThrowable()).length() > 1) {
				ExtentTestManager.getTest().log(LogStatus.FAIL, UtilityMethods.getException(testResult.getThrowable()));
			}
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					"=========		Test case failed for " + method.getTestMethod().getMethodName() + "		=========");
			Logs.info("========= Test case failed for " + method.getTestMethod().getMethodName() + " =========\n");

		} else if (ExtentTestManager.getTest().getRunStatus().toString().equals("pass")) {
			totalMethodsPassed = totalMethodsPassed + 1;
			testCasesNames = testCasesNames + "<tr><td><font color='#153e7e' size='1' face='Arial'><b>" + moduleName
					+ "</b></font></td><td><font color='#153e7e' size='1' face='Arial'><b>" + testCaseName
					+ "</b></font></td><td width='5%' bgcolor='#60b963' align='center'><font color='white' size='1' face='Arial'>PASS</font></td></tr>";

			ExtentTestManager.getTest().log(LogStatus.PASS,
					"=========		Test case pass for " + method.getTestMethod().getMethodName() + "		=========");
			Logs.info("========= Test case pass for " + method.getTestMethod().getMethodName() + " =========\n");
		} else if (ExtentTestManager.getTest().getRunStatus().toString().equals("skip")
				|| testResult.getStatus() == ITestResult.SKIP) {
			totalMethodsSkipped = totalMethodsSkipped + 1;
			testCasesNames = testCasesNames + "<tr><td><font color='#153e7e' size='1' face='Arial'><b>" + moduleName
					+ "</b></font></td><td><font color='#153e7e' size='1' face='Arial'><b>" + testCaseName
					+ "</b></font></td><td width='5%' bgcolor='#2196f3' align='center'><font color='white' size='1' face='Arial'>SKIP</font></td></tr>";

			ExtentTestManager.getTest().log(LogStatus.SKIP, "=========		Test case skipped for "
					+ method.getTestMethod().getMethodName() + "		=========");
			Logs.info("========= Test case skipped for " + method.getTestMethod().getMethodName() + " =========\n");
		} else if (ExtentTestManager.getTest().getRunStatus().toString().equals("warning")) {
			totalMethodsFalied = totalMethodsFalied + 1;
			testCasesNames = testCasesNames + "<tr><td><font color='#153e7e' size='1' face='Arial'><b>" + moduleName
					+ "</b></font></td><td><font color='#153e7e' size='1' face='Arial'><b>" + testCaseName
					+ "</b></font></td><td width='5%' bgcolor='#d88519' align='center'><font color='white' size='1' face='Arial'>Warning</font></td></tr>";

			ExtentTestManager.getTest().log(LogStatus.WARNING, "=========		Test case completed with warnings for "
					+ method.getTestMethod().getMethodName() + "		=========");
			Logs.info("========= Test case completed with warnings for " + method.getTestMethod().getMethodName()
					+ " =========\n");
		} else {
			totalMethodsFalied = totalMethodsFalied + 1;
			testCasesNames = testCasesNames + "<tr><td><font color='#153e7e' size='1' face='Arial'><b>" + moduleName
					+ "</b></font></td><td><font color='#153e7e' size='1' face='Arial'><b>" + testCaseName
					+ "</b></font></td><td width='5%' bgcolor='rgb(139, 0, 0)' align='center'><font color='white' size='1' face='Arial'>Others</font></td></tr>";

			if (UtilityMethods.getException(testResult.getThrowable()).length() > 1) {
				ExtentTestManager.getTest().log(LogStatus.FAIL, UtilityMethods.getException(testResult.getThrowable()));
			}
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"=========		Test case completed with failures/erros for "
							+ method.getTestMethod().getMethodName() + "		=========");
			Logs.info("========= Test case failed for " + method.getTestMethod().getMethodName() + " =========\n");
		}
		ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
		ExtentManager.getReporter().flush();
		ExtentTestManager.endTest();
		Logs.info("So far completed tests " + getTotalMethodsExecuted());

	}

	public void onTestSkipped(ITestResult result) {
		totalMethodsExecuted = totalMethodsExecuted + 1;
		totalMethodsSkipped = totalMethodsSkipped + 1;

		Object[] params = result.getParameters();

		String paramValues = "";
		for (Object object : params) {
			paramValues = paramValues + object + "<br>";
		}

		String paramsMethodAppend = "";

		if (paramValues.length() > 0) {
			paramsMethodAppend = "(";
			for (int i = 0; i < params.length; i++) {
				if (i == (params.length - 1)) {
					paramsMethodAppend = paramsMethodAppend + params[i] + ")";
				} else {
					paramsMethodAppend = paramsMethodAppend + params[i] + ", ";
				}
			}
		}

		String instanceName = "";
		String instance[];
		String packageName = "";
		String className = "";
		try {
			instanceName = result.getInstanceName().toString();
			instance = instanceName.split("\\.");
			className = instance[instance.length - 1].split("@")[0];
			packageName = instance[0];
			for (int i = 1; i < instance.length - 1; i++) {
				packageName = packageName + "." + instance[i];
			}

		} catch (Exception e) {

		}

		ExtentTestManager
				.startTest(UtilityMethods.getBrowserName(result).toUpperCase() + " => "
						+ result.getMethod().getMethodName() + (paramsMethodAppend.replace("env.", Base.url)))
				.assignCategory(UtilityMethods.getBrowserName(result).toUpperCase() + " > "
						+ result.getTestContext().getName() + "  > " + className);

		ExtentTestManager.getTest().log(LogStatus.INFO,
				"=========		Execution Started for " + result.getMethod().getMethodName()
						+ "		=========<br><b>Package Name:<b>" + packageName + "<br><b>Class Name:<b>" + className
						+ "<br><b>Method Name:<b>" + result.getMethod().getMethodName() + "<br><b>Environment:<b>"
						+ UtilityMethods.getEnvforReport());

		ExtentTestManager.getTest().log(LogStatus.SKIP,
				"Test skipped : " + UtilityMethods.getException(result.getThrowable()));
		Logs.info("Test skipped : " + UtilityMethods.getException(result.getThrowable()));
		ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
		ExtentManager.getReporter().flush();
		ExtentTestManager.endTest();

	}

	@Override
	public void onTestStart(ITestResult result) {
		/*
		 * TestRunner runner = (TestRunner) result.getTestContext();
		 * runner.setOutputDirectory(ExtentManager.getReportLocation());
		 * 
		 * System.out.println(runner.getOutputDirectory());
		 */

		/*
		 * TestListenerAdapter tla = new TestListenerAdapter(); TestNG testng =
		 * new TestNG();
		 * testng.setOutputDirectory(ExtentManager.getReportLocation());
		 * //testng.setTestClasses(new Class[] { Run2.class });
		 * testng.addListener(tla);
		 */
		// testng.run();
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onTestFailure(ITestResult result) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}
	
	public Hashtable<String, String> getHashMapDataWithColumnnames(Object[] params) {

		Hashtable<String, String> ht = new Hashtable<>();
				
		try {
			Object[] arr =  (Object[]) params[0];
			
			String list[] = new String[arr.length];
			
			for (int i = 0; i < arr.length; i++) {
				list[i] = String.valueOf(arr[i]);
			}
			for (int i = 0; i < list.length; i++) {
				String columnData[] = list[i].split("\\|\\|");
				String key =columnData[0];
				String value= "";
				try {
					value =columnData[1];
				} catch (Exception e) {
					value ="";
				}
				
				ht.put(key, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return ht;

	}

}