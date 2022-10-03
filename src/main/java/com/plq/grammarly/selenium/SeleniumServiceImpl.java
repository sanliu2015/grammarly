package com.plq.grammarly.selenium;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static WebDriver driver;

    @Value("${downloadDir}")
    private String downloadDir;
    @Value("${fileSaveDir}")
    private String fileSaveDir;

    @PostConstruct
    @Override
    public void initEdgeSession() {
        try {
            WebDriverManager webDriverManager = WebDriverManager.edgedriver();
            if ("dev".equals(SpringUtil.getActiveProfile())) {
                webDriverManager.proxy("127.0.0.1:10809");
            }
            webDriverManager.setup();
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9333");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setImplicitWaitTimeout(Duration.ofSeconds(10L));
            driver = new EdgeDriver(options);
            driver.get("https://www.coursehero.com/");
        } catch (Exception e) {
            log.error("初始化edge selenium驱动失败", e);
        }
    }

    @Override
    public synchronized JSONObject unlockCourseHeroQuestion(QuestionExchangeCode questionExchangeCode) {
        try {
            driver.get(questionExchangeCode.getQuestionUrl());
            ThreadUtil.safeSleep(1000L);
            if (driver.getPageSource().contains("Incapsula")) {
                return new JSONObject().putOpt("result", false).putOpt("errmsg", "需要人机身份验证，请前往服务器进行操作！");
            }
            if (questionExchangeCode.getCode() == null) {
                return new JSONObject().putOpt("result", true).putOpt("errmsg", "");
            }
            // 先用文件解锁下载
            try {
                driver.findElement(By.cssSelector("a[class=\"d-flex\"]")).click();
                // 留10秒文件下载
                ThreadUtil.safeSleep(10000L);
                String downloadFile = getNewestFile(downloadDir);
                String destFilePath = fileSaveDir + questionExchangeCode.getCode() + "." + FileUtil.getSuffix(downloadFile);
                File srcFile = new File(downloadFile);
                File dstFile = new File(destFilePath);
                FileUtil.move(srcFile, dstFile, false);
                return new JSONObject().putOpt("result", true).putOpt("filePath", dstFile.getAbsolutePath());
            } catch (NoSuchElementException noSuchElementException) {
                log.info("a[class='d-flex'] 没找到文件解锁下载按钮");
            }
            // 再用网页答案解解锁
            try {
                driver.findElement(By.cssSelector("a[data-cha-target-name=\"unlock_answer_btn\"][data-cha-location=\"answer_content\"]")).click();
                ThreadUtil.safeSleep(5000L);
                // 关闭弹窗
                try {
                    driver.findElement(By.cssSelector("a[class=\"ch_popup_close\"]")).click();
                    ThreadUtil.safeSleep(1000L);
                } catch (NoSuchElementException noSuchElementException) {
                    log.info("a[class=\"ch_popup_close\"] 没找到关闭弹窗按钮");
                }
            } catch (NoSuchElementException noSuchElementException) {
                log.info("a[data-cha-target-name=\"unlock_answer_btn\"][data-cha-location=\"answer_content\"] 没找到页面答案解锁按钮");
            }
            // 最后页面截图
            try {
                WebElement webElement = driver.findElement(By.id("answer-content"));
                String fileName = fileSaveDir + questionExchangeCode.getCode() + ".png";
                File pngFile = new File(fileName);
                captureElementAndSaveToPng(pngFile, webElement);
                return new JSONObject().putOpt("result", true).putOpt("filePath", pngFile.getAbsolutePath());
            } catch (NoSuchElementException noSuchElementException) {
                log.info("answer-content 没找到页面答案");
            }
            return new JSONObject().putOpt("result", false).putOpt("errmsg", driver.getPageSource());
        } catch (Exception e) {
            log.error("解锁courseHero发生异常，questionExchangeCode：{}", JSONUtil.toJsonStr(questionExchangeCode), e);
            return new JSONObject().putOpt("result", false).putOpt("errmsg", e.getMessage());
        }
    }


    /**
     * 浏览器截屏
     * @param screenshot
     */
    private void captureElementAndSaveToPng(File screenshot, WebElement element) {
        try {
            BufferedImage img = ImageIO.read(screenshot);
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            //获取指定元素的坐标
            Point point = element.getLocation();
            //从元素左上角坐标开始，按照元素的高宽对img进行裁剪为符合需要的图片
            BufferedImage dest = img.getSubimage(point.getX(), point.getY(), width, height);
            ImageIO.write(dest, "png", screenshot);
        } catch (IOException e) {
            log.error("截屏出现异常", e);
        }
    }

    /**
     * 获取浏览器下载的文件
     * @return
     */
    private String getNewestFile(String rootPath) {
        File dir = new File(rootPath);
        File[] files = dir.listFiles(file -> file.isFile());
        if (files != null && files.length > 0) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            return files[0].getAbsolutePath();
        }
        return null;
    }

}