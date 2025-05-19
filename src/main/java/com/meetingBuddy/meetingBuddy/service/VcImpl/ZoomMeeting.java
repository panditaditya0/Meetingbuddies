package com.meetingBuddy.meetingBuddy.service.VcImpl;

import com.meetingBuddy.meetingBuddy.entity.Meetings;
import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;
import com.meetingBuddy.meetingBuddy.repository.MeetingsRepo;
import com.meetingBuddy.meetingBuddy.service.MeetingJoiner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ZoomMeeting implements MeetingJoiner {
    private final String JOIN_MEETING_BASE_URL = "https://app.zoom.us/wc/@meeting-id/join?fromPWA=1";

    private static final By byJoinMeetingXpath = By.xpath("//*[@class='join-meetingId']");
    private static final By byJoinBtnXpath = By.xpath("//button[text()='Join']");
    private static final By byContWtMicAndCamXpath = By.xpath("//*[text()='Continue without microphone and camera']");
    private static final By byEnterMeetingPasscodeXpath = By.xpath("//*[@id='input-for-pwd']");

    //Enter meeting xpath
    private static final By byEnterNameXpath = By.xpath("//*[@id='input-for-name']");
    private static final By byIframeXpath = By.xpath("//iframe[@id='webclient']");
    private static final By byEnterMeetingScreenMicToggleXpath = By.xpath("//button[@class='preview-video__control-button']");
    private static final By byEnterMeetingScreenVideoToggleXpath = By.xpath("//button[@class='preview-video__control-button']");

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final DesiredCapabilities capabilities;
    private final URL gridUrl;
    private final MeetingsRepo meetingsRepo;

    ZoomMeeting(DesiredCapabilities capabilities, URL gridUrl, MeetingsRepo meetingsRepo) {
        this.capabilities = capabilities;
        this.gridUrl = gridUrl;
        this.meetingsRepo = meetingsRepo;
    }

    @Override
    public MeetingProvider getProviderType() {
        return MeetingProvider.ZoomMeeting;
    }

    @Async
    @Override
    public void joinMeeting(Meetings meetingInfo, String name) {
        try {
            WebDriver driver = new RemoteWebDriver(gridUrl, capabilities);
            String meetingId = meetingInfo.getMeetingId().replace(" ", "");
            String meetingUrl = JOIN_MEETING_BASE_URL.replaceAll("@meeting-id", meetingId);
            driver.get(meetingUrl);
            waitTillPresenceOfElement(driver, byIframeXpath);
            driver.switchTo().frame(driver.findElement(byIframeXpath));

//            waitTillPresenceOfElement(driver, byContWtMicAndCamXpath);
//            driver.findElement(byContWtMicAndCamXpath).click();
//            waitTillPresenceOfElement(driver, byContWtMicAndCamXpath);
//            driver.findElement(byContWtMicAndCamXpath).click();

            waitTillPresenceOfElement(driver, byEnterMeetingPasscodeXpath);
            driver.findElement(byEnterMeetingPasscodeXpath).sendKeys(meetingInfo.getPasscode());
            waitTillPresenceOfElement(driver, byEnterNameXpath);
            driver.findElement(byEnterNameXpath).sendKeys(name);
            waitTillPresenceOfElement(driver, byJoinBtnXpath);
            driver.findElement(byJoinBtnXpath).click();
            System.out.println(name + "JOINED");
            driver.manage().deleteAllCookies();
            updateBotStatus(meetingInfo, true);
            scheduleDriverShutdown(driver, meetingInfo.getTimeout());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            updateBotStatus(meetingInfo, false);
        }
    }

    private synchronized void updateBotStatus(Meetings meetings, boolean status) {
        Optional<Meetings> updatedMeetingOpt = meetingsRepo.findById(meetings.getId());
        if (updatedMeetingOpt.isPresent()) {
            Meetings updatedMeeting = updatedMeetingOpt.get();
            if (status) {
                updatedMeeting.setJoinedBots(updatedMeeting.getJoinedBots() + 1);
                meetingsRepo.save(updatedMeeting);
                return;
            }
            updatedMeeting.setJoinedBots(updatedMeeting.getFailedBots() + 1);
            meetingsRepo.save(updatedMeeting);
        }
    }

    private static void scheduleDriverShutdown(WebDriver driver, long minutes) {
        scheduler.schedule(() -> {
            try {
                driver.quit();
                System.out.println("Closed WebDriver after " + minutes + " minutes.");
            } catch (Exception e) {
                System.err.println("Error while closing WebDriver: " + e.getMessage());
            }
        }, minutes, TimeUnit.MINUTES);
    }

    public WebElement waitTillPresenceOfElement(WebDriver driver, By locator) {
        int waitDuration = 10;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
        return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}

