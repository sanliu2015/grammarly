package com.plq.grammarly;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class InstallDriversTest {

    @Test
    public void edgeSession() {
//        WebDriverManager.chromedriver().proxy("127.0.0.1:10809").setup();
        WebDriverManager.edgedriver().proxy("127.0.0.1:10809").setup();
        EdgeOptions options = new EdgeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9333");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        WebDriver driver = new EdgeDriver(options);
//        driver.get("www.baidu.com");
        driver.get("https://www.baidu.com/");
//        driver.quit();
    }

//    @Test
//    public void chromeSession() {
//        WebDriverManager.chromedriver().setup();
//
//        WebDriver driver = new ChromeDriver();
//
//        driver.quit();
//    }
//
//    @Test
//    public void edgeSession() {
//        WebDriverManager.edgedriver().setup();
//
//        WebDriver driver = new EdgeDriver();
//
//        driver.quit();
//    }

//    @Disabled("Only runs on Windows")
//    @Test
//    public void ieSession() {
//        WebDriverManager.iedriver().setup();
//
//        WebDriver driver = new InternetExplorerDriver();
//
//        driver.quit();
//    }
}
