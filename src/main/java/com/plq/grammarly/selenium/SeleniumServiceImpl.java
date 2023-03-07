package com.plq.grammarly.selenium;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static EdgeDriver driver;

    @Value("${downloadDir}")
    private String downloadDir;
    @Value("${fileSaveDir}")
    private String fileSaveDir;

    /**
     * 命令行cmd先启动这个,前提edge加入环境变量C:\Program Files (x86)\Microsoft\Edge\Application
     * msedge.exe --remote-debugging-port=9333 --user-data-dir="D:\selenium\msedge_data"
     */
    @PostConstruct
    @Override
    public void initEdgeSession() {
        try {
            WebDriverManager webDriverManager = WebDriverManager.edgedriver();
//            if ("dev".equals(SpringUtil.getActiveProfile())) {
//                webDriverManager.proxy("127.0.0.1:10809");
//            }
            webDriverManager.setup();
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9333");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setImplicitWaitTimeout(Duration.ofSeconds(10L));
            driver = new EdgeDriver(options);
            if (!"dev".equals(SpringUtil.getActiveProfile())) {
//                driver.get("edge://version/");
                driver.get("https://bot.sannysoft.com/");
//                driver.get("https://www.coursehero.com/");
            }
            log.info("初始化edge selenium驱动成功");
        } catch (Exception e) {
            log.error("初始化edge selenium驱动失败", e);
        }
    }

    @PreDestroy
    @Override
    public void destoryEdgeSession() {
        try {
            driver.quit();
            log.info("销毁edge selenium驱动成功");
        } catch (Exception e) {
            log.error("销毁edge selenium驱动失败", e);
        }
    }

    @Override
    public synchronized JSONObject unlockCourseHeroQuestion(QuestionExchangeCode questionExchangeCode) {
        try {
            if (!"dev".equals(SpringUtil.getActiveProfile())) {
                driver.get(questionExchangeCode.getQuestionUrl());
            }
            ThreadUtil.safeSleep(1000L);
            if (driver.getPageSource().contains("Incapsula")) {
                return new JSONObject().putOpt("result", false).putOpt("errmsg", "需要人机身份验证，请前往服务器进行操作！");
            }
            if (questionExchangeCode.getCode() == null) {
                return new JSONObject().putOpt("result", true).putOpt("errmsg", "");
            }
            // 先用文件解锁下载
            try {
                WebElement webElement = driver.findElement(By.cssSelector("a[class=\"d-flex\"]"));
                webElement.click();
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
//                File snapshot = elementSnapshot(driver,webElement);
//                File saveFile = new File(fileSaveDir + questionExchangeCode.getCode() + ".png");
//                FileUtil.copyFile(snapshot, saveFile);
                String pngBase64String = fullScreenCapture();
                File saveFile = new File(fileSaveDir + questionExchangeCode.getCode() + ".png");
                try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile)) {
                    fileOutputStream.write(pngBase64String.getBytes(StandardCharsets.UTF_8));
                }
                return new JSONObject().putOpt("result", true).putOpt("filePath", saveFile.getAbsolutePath());
            } catch (NoSuchElementException noSuchElementException) {
                log.info("answer-content 没找到页面答案");
            }
            FileUtil.writeUtf8String(driver.getPageSource(), fileSaveDir + questionExchangeCode.getCode() + ".html");
            return new JSONObject().putOpt("result", false).putOpt("errmsg", "没有找到期望操作的元素");
        } catch (Exception e) {
            log.error("解锁courseHero发生异常，questionExchangeCode：{}", JSONUtil.toJsonStr(questionExchangeCode), e);
            return new JSONObject().putOpt("result", false).putOpt("errmsg", e.getMessage());
        }
    }

    /**
     * 截全屏
     * https://www.zhihu.com/question/359095395/answer/1675694996
     * @return
     */
    public static String fullScreenCapture() {
        JavascriptExecutor driverJs= (JavascriptExecutor) driver;
        Object width = driverJs.executeScript("document.body.scrollWidth");
        Object height = driverJs.executeScript("document.body.scrollHeight");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", false);
        map.put("width", width);
        map.put("height", height);
        map.put("deviceScaleFactor", 1);
        driver.executeCdpCommand("Emulation.setDeviceMetricsOverride", map);
        // 然后再执行截图
        Map<String, Object> map2 = new HashMap<>();
        map2.put("fromSurface", true);
        String imageBase64 = driver.executeCdpCommand("Page.captureScreenshot", map2).get("data").toString();
        System.out.println(imageBase64.length());
        // 关闭设备模拟
        driver.executeCdpCommand("Emulation.clearDeviceMetricsOverride", new HashMap<>());
        // 返回的base64内容写入PNG文件
        return imageBase64;
    }

    /**
     * 根据Element截图指定区域方法
     *
     * @param driver
     * @param element  截图区域
     * @throws Exception
     */
    public static File elementSnapshot(WebDriver driver, WebElement element) throws Exception {
        //创建全屏截图
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(screen);

        //获取元素的高度、宽度
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        //创建一个矩形使用上面的高度，和宽度
        Rectangle rect = new Rectangle(width, height);
        //元素坐标
        Point p = element.getLocation();


        //对前面的矩形进行操作
        //TODO 使用可以截全图的方法（滚动条），暂未找到方式
        int w = rect.width; //指定矩形区域的宽度
        int h = rect.height;//指定矩形区域的高度
        int x = p.getX(); //指定矩形区域左上角的X坐标
        int y = p.getY(); //指定矩形区域左上角的Y坐标

        //driver的分辨率，这里设置1920*1080
        int w_driver = 1920;
        int h_driver = 1080;

        /**
         * 如果Element的Y坐标值加上高度超过driver的高度
         * 就会报错(y + height) is outside or not
         * 退而求其次，调整图片的宽度和高度, 调整到适合driver的分辨率
         * 此时会截图driver可见的元素区域快照
         * TODO 如果能找到跨滚动条截图的方式，可以不用裁剪
         */
        try{
            if ( y + h > h_driver){
                h = h- (y + h - h_driver);
            }
            //(x + width) is outside or not
            if (x + w > w_driver){
                w = x - (x + w - w_driver);
            }
            BufferedImage img = image.getSubimage(x, y, w, h);
            ImageIO.write(img, "png", screen);

        }catch (IOException e){
            e.printStackTrace();
        }
        return screen;
    }


    /**
     * 浏览器截屏
     */
    private void captureElementAndSaveToPng(String filePath, WebElement element) {
        try {
            File screenshot = new File(filePath);
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
