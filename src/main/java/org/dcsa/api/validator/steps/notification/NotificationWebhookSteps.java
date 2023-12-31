package org.dcsa.api.validator.steps.notification;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.hook.TestSetup;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.model.TNTEventSubscriptionTO;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.util.FileUtility;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webhook.SparkWebHook;
import org.dcsa.api.validator.webservice.init.AppProperty;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class NotificationWebhookSteps {
    private CallbackContext callbackContext;
    private SparkWebHook webhook;
    private Scenario scenario;

    @Before(order = 2)
    public void setUp(Scenario s) {
        System.out.println("Before Each");
        callbackContext = new CallbackContext();
        webhook = TestSetup.sparkWebHook;
        webhook.setContext(callbackContext);
        this.scenario = s;
        if (TestSetup.TestContexts.get(scenario.getId()) == null)
            TestSetup.TestContexts.put(scenario.getId(), new TestContext());
    }


    @Then("Receive Head request for CallBack URL")
    public void receiveHeadRequestForCallBackURL() {
        try {
            System.out.println("Waiting for callback head request ");
            callbackContext.getHeadRequestLock().await(Configuration.CALLBACK_WAIT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
            testcontext.setReasonOfFailure("Head Request not received");
            Assert.fail("Head Request not received");
        }
        if (!callbackContext.isHeadRequestReceived()) {
            TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
            testcontext.setReasonOfFailure("Head Request not received");
            Assert.fail("Head Request not received");
        }
        callbackContext.setHeadRequestCountDown(1);
      //  SparkWebHook.setSubscriptionID();

    }

    @Then("Not receive Head request for invalid callBack URL")
    public void notReceiveHeadRequestForInvalidCallBackURL() {
        try {
            System.out.println("Waiting for callback head request ");
            callbackContext.getHeadRequestLock().await(Configuration.CALLBACK_WAIT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
            testcontext.setReasonOfFailure("Head Request not received");
        }
        callbackContext.setHeadRequestCountDown(1);
    }

    @And("Receive a valid notification")
    public void receiveAValidNotification() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        try {
            System.out.println("Waiting for Notification");
            callbackContext.getNotificationRequestLock().await(Configuration.CALLBACK_WAIT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail("Head Request not received");
        }
        if (!callbackContext.isNotificationReceived()) {
            testcontext.setReasonOfFailure("Notification not Received");
            Assert.fail("Notification not Received");
        }
        testcontext.getMessage().add("Notification Received \n");

/*      testcontext.getMessage().add(callbackContext.getHeaders().toString());
        String receivedSignature = callbackContext.getHeaders().get("Notification-Signature");
        if (receivedSignature == null) {
            testcontext.setReasonOfFailure("Notification-Signature missing in request header");
            Assert.fail("Notification-Signature missing in request header");
        }

        String schemaString= FileUtility.loadFileAsString(TestUtility.getResponseSchema("Event"));
        try {
            boolean isValid=JsonUtility.validateSchema(schemaString,callbackContext.getNotificationBody());
            if(!isValid)
                Assert.fail("Schema validation failed");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        testcontext.getMessage().add(callbackContext.getNotificationBody());
        String key = "MTIzNDU2Nzg5MGFiY2RlZjEyMzQ1Njc4OTBhYmNkZWY=";
        if (testcontext.getRequestChain().size() > 0)
            key = JsonUtility.extractAttributeValue(testcontext.getRequestChain().get(testcontext.getRequestChain().size() - 1).getBody(), "secret");
        String expectedSignature = (TestUtility.getSignature(key, callbackContext.getNotificationBody()));
        if (!expectedSignature.equals(receivedSignature)) {
            testcontext.setReasonOfFailure("Invalid Signature: Expected=>" + expectedSignature + "\n\n received=>" + receivedSignature);
        }
        Assert.assertEquals(receivedSignature,expectedSignature);*/
        callbackContext.setNotificationCountDown(1);
    }

    @And("A valid Callback Url")
    public void aValidCallbackUrl() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        TNTEventSubscriptionTO configTNTEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
        if(!Configuration.CALLBACK_URL.isBlank()){
            testcontext.setCallbackURL(TestUtility.getConfigCallbackUrl());
        }else{
            testcontext.setCallbackURL(configTNTEventSubscriptionTO.getCallbackUrl());
        }
    }

    @And("An invalid Callback Url")
    public void aInvalidCallbackUrl() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        testcontext.setCallbackURL(Configuration.CALLBACK_URI+ Configuration.CALLBACK_PATH+"/307deecf-e599-4ff2-bf5a-fd47c171b8c4");
    }

}
