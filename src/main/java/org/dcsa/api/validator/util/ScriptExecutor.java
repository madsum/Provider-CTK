package org.dcsa.api.validator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptExecutor {

    public static boolean isWindows;
    public static void runNewman(){
        String osType = TestUtility.getOperatingSystem();
        String scriptPath = "";
        if(osType.contains("Win")){
            scriptPath = FileUtility.getScriptPath("win_newman.bat");
            isWindows = true;
        }else if(osType.contains("Linux")){
            scriptPath = FileUtility.getScriptPath("nix_newman.sh");
        }
        ReportUtil.setIsWindows(isWindows);
        executeScript(scriptPath);
    }

    private static void executeScript(String scriptPath) {
        try {
            // the working directory for the process is .\script
            File dir = new File(FileUtility.getScriptPath(File.separator));
            Process process = Runtime.getRuntime().exec(scriptPath, null, dir);
            String line;
            System.out.println("***** Script execution Starts *****");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                ReportUtil.fillHtmlReportModel(line);
            }
            System.out.println("***** Script executed successfully *****");
        } catch (IOException e) {
            System.out.println("Error during script execution "+e.getMessage());
        }
    }
}
