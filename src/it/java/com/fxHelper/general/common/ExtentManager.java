package com.fxHelper.general.common;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.relevantcodes.extentreports.ExtentReports;
import com.fxHelper.general.utils.PortFinder;

public class ExtentManager {
	static ExtentReports extent;

	public static String reportLocation = null;
	final public static String filePath = getReportLocation() + "/SummaryReport.html";
	public static String emailReports ="";
	public static String getReportLocation() {

		if (reportLocation == null) {
			if (System.getProperty("jenkinsExecution") != null) {

				if (System.getProperty("jenkinsExecution").equalsIgnoreCase("true")) {

					if (System.getProperty("jenkinsJobName") != null) {
						reportLocation = ReadProperties.projectLocation + "Reports/"
								+ System.getProperty("jenkinsJobName") + "_report";
					} else {
						reportLocation = ReadProperties.projectLocation + "Reports/" + "Default-Jenkins" + "_report";
					}
					System.out.println("Report is being generated for the Jenkins execution ");

					return reportLocation;

				} else {
					reportLocation = ReadProperties.projectLocation + "Reports/Professional_Automation_Report_"
							+ (new Date()).toString().replace(":", "_").replace(" ", "_");
					return reportLocation;

				}
			}

			else {
				reportLocation = ReadProperties.projectLocation + "Reports/Professional_Automation_Report_"
						+ (new Date()).toString().replace(":", "_").replace(" ", "_");
				return reportLocation;
			}
		}
		return reportLocation;

	}


	public synchronized static ExtentReports getReporter() {

		if (extent == null) {

			if (System.getProperty("jenkinsExecution") != null) {
				if (System.getProperty("jenkinsExecution").equalsIgnoreCase("true")) {
					Logs.info("Report is being generated for the Jenkins job " + System.getProperty("jenkinsJobName"));
					try {
						FileUtils.deleteDirectory(new File(getReportLocation()));
					}

					catch (Exception e) {
						Logs.warn(UtilityMethods.getException(e));
						// e.printStackTrace();
					}
					//emailReports = "http://reports.portal.fxHelper.com/"+System.getProperty("jenkinsJobName");
					
					//Appending jobname and build number in report link dynamically.
	
					emailReports = "http://reports.portal.fxHelper.com/"+System.getProperty("jenkinsJobName")+"/report-"+System.getProperty("buildNum")+"/SummaryReport.html";
					
					Logs.info("Reports Location :"+emailReports);
					String reportLocation = getReportLocation();
					String filePath = reportLocation + "/SummaryReport.html";
					extent = new ExtentReports(filePath, true);

				} else {
					extent = new ExtentReports(filePath, true);
					// $('#enableDashboard').click(); });");
					getReportLocation();
					emailReports =ExtentManager.filePath;
					Logs.info("Reports Location :"+emailReports);

				}
			} else {
				extent = new ExtentReports(filePath, true);
				getReportLocation();
				emailReports =ExtentManager.filePath;
				Logs.info("Reports Location :"+emailReports);
			}

			extent.loadConfig(PortFinder.class, "extent-config.xml");
			Logs.info("************************************************************************************\n");
				Logs.info("Extent Reports Location : " + System.getProperty("user.dir") + "/" + emailReports
						+ "\n");
			Logs.info("************************************************************************************\n");
		}

		return extent;
	}

}