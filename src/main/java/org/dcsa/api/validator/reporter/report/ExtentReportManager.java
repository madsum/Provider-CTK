package org.dcsa.api.validator.reporter.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.dcsa.api.validator.constant.TestStatusCode;
import org.dcsa.api.validator.model.HtmlReportModel;
import org.dcsa.api.validator.util.ReportUtil;
import org.dcsa.api.validator.webservice.init.AppProperty;

import java.util.Objects;
import java.util.Properties;

public class ExtentReportManager {
    private static ExtentReports extentReports;
    private static ExtentTest extentTest;

    private static final String COMPANY = "Company";
    private static final String AUTHOR = "Author";
    private static final String OS_NAME = "os.name";
    private static final String USER_LANGUAGE = "user.language";
    private static final String JAVA_RUNTIME = "java.runtime.name";
    private static final String JAVA_VERSION = "java.version";

    public static synchronized ExtentReports getExtentReports(String filePrefix) {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            extentReports.attachReporter(getExtentSparkReporter(filePrefix));
            setReportSystemInfo();
        }
        return extentReports;
    }

    private static void setReportSystemInfo() {
        Properties properties = System.getProperties();
        extentReports.setSystemInfo(COMPANY.toUpperCase(), AppProperty.REPORT_COMPANY);
        extentReports.setSystemInfo(AUTHOR.toUpperCase(), AppProperty.REPORT_AUTHOR);
        extentReports.setSystemInfo(OS_NAME.toUpperCase().replaceAll("\\.", " "),
                (String) properties.get(OS_NAME));
        extentReports.setSystemInfo(USER_LANGUAGE.toUpperCase().replaceAll("\\.", " "),
                ((String) properties.get(USER_LANGUAGE)).toUpperCase());
        extentReports.setSystemInfo(JAVA_RUNTIME.toUpperCase().replaceAll("\\.", " "),
                (String) properties.get(JAVA_RUNTIME));
        extentReports.setSystemInfo(JAVA_VERSION.toUpperCase().replaceAll("\\.", " "),
                (String) properties.get(JAVA_VERSION));
    }

    public static void flush() {
        getExtentReports(null).flush();
    }

    public static ExtentSparkReporter getExtentSparkReporter(String filePrefix) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(
                                        ReportUtil.getReportPath(filePrefix, ".html"));
        reporter.config().setReportName(AppProperty.REPORT_NAME);
        reporter.config().setDocumentTitle(AppProperty.REPORT_TITLE);
        reporter.config().setTheme(Objects.equals(AppProperty.REPORT_THEME, Theme.DARK.getName()) ?
                Theme.DARK : Theme.STANDARD);
        reporter.config().setTimelineEnabled(Boolean.parseBoolean(AppProperty.REPORT_TIMELINE));
        reporter.config().setTimeStampFormat(AppProperty.REPORT_TIME_FORMAT);
        return reporter;
    }


    public static ExtentTest getExtentTest(String name, String filePrefix) {
        if (name != null) {
            extentTest = ExtentReportManager.getExtentReports(filePrefix).createTest(name);
        }
        return extentTest;
    }

    public static void writeExtentTestReport(HtmlReportModel htmlReportModel, String filePrefix) {
        if (htmlReportModel != null) {
            ExtentTest extentTest = ExtentReportManager.getExtentTest(htmlReportModel.getRequirementId() + " " +
                    htmlReportModel.getRequirement(), filePrefix);
            extentTest.assignCategory(htmlReportModel.getRequirementId());
            extentTest.info(htmlReportModel.getTntValidationRequirementID().getDetails());
            if(htmlReportModel.getTestStatusCode() != null) {
                if (htmlReportModel.getTestStatusCode().equals(TestStatusCode.PASSED)) {
                        extentTest.pass(htmlReportModel.getTestName());
                    Markup markUp = MarkupHelper.createLabel(TestStatusCode.PASSED.name().toUpperCase(), ExtentColor.GREEN);
                    extentTest.log(Status.INFO, markUp);
                    if (htmlReportModel.getTestDetails() != null)
                        extentTest.info(ReportUtil.modifyTestDetails(htmlReportModel.getTestDetails().toString()));
                } else if (htmlReportModel.getTestStatusCode().equals(TestStatusCode.FAILED)) {
                    extentTest.fail(htmlReportModel.getTestName());
                    if (htmlReportModel.getFailureReason() != null) {
                        extentTest.fail("<b>Failure reason: </b></br>" + htmlReportModel.getFailureReason());
                    }
                    Markup markUp = MarkupHelper.createLabel(TestStatusCode.FAILED.name().toUpperCase(), ExtentColor.RED);
                    extentTest.log(Status.WARNING, markUp);
                    if (htmlReportModel.getTestDetails() != null) {
                        extentTest.info(ReportUtil.modifyTestDetails(htmlReportModel.getTestDetails().toString()));
                    }
                }
            }
            ExtentReportManager.flush();
        }
    }

    static public void resetExtentTestReport(){
        extentReports = null;
        extentTest = null;
    }
}
