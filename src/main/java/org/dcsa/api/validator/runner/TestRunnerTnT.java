package org.dcsa.api.validator.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = {"pretty",
        "html:reports/report.html",
}, features = {
        "src/main/resources/features/tnt/v2/EventSubscription-Group2.feature",
        "src/main/resources/features/tnt/v2/EventSubscription-Group1.feature",
        "src/main/resources/features/tnt/v2/Event.feature",
        "src/main/resources/features/tnt/v2/Subscriber.feature"
    },
    glue = {
        "org.dcsa.api.validator.steps.common","org.dcsa.api.validator.steps.notification"
    }
)
public class TestRunnerTnT extends AbstractTestNGCucumberTests {

}
