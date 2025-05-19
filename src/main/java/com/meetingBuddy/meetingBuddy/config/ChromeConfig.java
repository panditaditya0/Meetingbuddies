package com.meetingBuddy.meetingBuddy.config;

import org.bouncycastle.oer.its.etsi102941.Url;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class ChromeConfig {

    @Value("${remote.webdriver.link}")
    private String webDriverUrl;

    @Bean
    public DesiredCapabilities getChromeOptions() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--headless");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        return capabilities;
    }

    @Bean
    public URL getWebDriverUrlObj() throws MalformedURLException {
        return new URL(webDriverUrl);
    }
}