package com.plq.grammarly.selenium;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import com.plq.grammarly.util.DingTalkRobot;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static EdgeDriver driver;

    @Value("${fileSaveDir}")
    private String fileSaveDir;
    @Value("${apiKey}")
    private String apiKey;

    private static final String FIND_RECAPTCHA_PARAM_SCRIPT = "function findRecaptchaClients() {\n" +
            "  // eslint-disable-next-line camelcase\n" +
            "  if (typeof (___grecaptcha_cfg) !== 'undefined') {\n" +
            "    // eslint-disable-next-line camelcase, no-undef\n" +
            "    return Object.entries(___grecaptcha_cfg.clients).map(([cid, client]) => {\n" +
            "      const data = { id: cid, version: cid >= 10000 ? 'V3' : 'V2' };\n" +
            "      const objects = Object.entries(client).filter(([_, value]) => value && typeof value === 'object');\n" +
            "\n" +
            "      objects.forEach(([toplevelKey, toplevel]) => {\n" +
            "        const found = Object.entries(toplevel).find(([_, value]) => (\n" +
            "          value && typeof value === 'object' && 'sitekey' in value && 'size' in value\n" +
            "        ));\n" +
            "     \n" +
            "        if (typeof toplevel === 'object' && toplevel instanceof HTMLElement && toplevel['tagName'] === 'DIV'){\n" +
            "            data.pageurl = toplevel.baseURI;\n" +
            "        }\n" +
            "        \n" +
            "        if (found) {\n" +
            "          const [sublevelKey, sublevel] = found;\n" +
            "\n" +
            "          data.sitekey = sublevel.sitekey;\n" +
            "          const callbackKey = data.version === 'V2' ? 'callback' : 'promise-callback';\n" +
            "          const callback = sublevel[callbackKey];\n" +
            "          if (!callback) {\n" +
            "            data.callback = null;\n" +
            "            data.function = null;\n" +
            "          } else {\n" +
            "            data.function = callback;\n" +
            "            const keys = [cid, toplevelKey, sublevelKey, callbackKey].map((key) => `['${key}']`).join('');\n" +
            "            data.callback = `___grecaptcha_cfg.clients${keys}`;\n" +
            "          }\n" +
            "        }\n" +
            "      });\n" +
            "      return data;\n" +
            "    });\n" +
            "  }\n" +
            "  return [];\n" +
            "}";

    /**
     * 命令行cmd先启动这个,前提edge加入环境变量C:\Program Files (x86)\Microsoft\Edge\Application
     * msedge.exe --remote-debugging-port=9333 --user-data-dir="D:\selenium\msedge_data"
     */
    @PostConstruct
    @Override
    public void initEdgeSession() {
        try {
            WebDriverManager webDriverManager = WebDriverManager.edgedriver();
            webDriverManager.setup();
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9333");
            options.setExperimentalOption("download.prompt_for_download", false);
            options.setExperimentalOption("download.default_directory", fileSaveDir);
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setImplicitWaitTimeout(Duration.ofSeconds(10L));
            driver = new EdgeDriver(options);
            if (!"dev".equals(SpringUtil.getActiveProfile())) {
//                driver.get("edge://version/");
//                driver.get("https://bot.sannysoft.com/");
                driver.get("https://www.coursehero.com/");
            }
            log.info("初始化edge selenium驱动成功");
        } catch (Exception e) {
            log.error("初始化edge selenium驱动失败", e);
            System.exit(500);
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
            driver.get(questionExchangeCode.getQuestionUrl());
            ThreadUtil.safeSleep(1000L);
            if (driver.getPageSource().contains("Incapsula")) {
                boolean passFlag = this.crackEnterpriseRecaptcha();
                if (passFlag) {
                    // 人机验证成功的话睡眠5秒钟再操作
                    ThreadUtil.safeSleep(5000L);
                } else {
                    return new JSONObject().putOpt("result", false)
                            .putOpt("errorcode", "").putOpt("errmsg", "破解人机校验失败，请手工前往服务器进行操作！");
                }
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
                String downloadFile = getNewestFile(fileSaveDir);
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


    /**
     * 参考 https://2captcha.com/2captcha-api#solving_recaptchav2_new
     * https://2captcha.com/p/recaptcha_enterprise
     *
     * @return
     */
    @Override
    public boolean crackEnterpriseRecaptcha() {
        driver.executeScript(FIND_RECAPTCHA_PARAM_SCRIPT);
        Object result = driver.executeScript("return findRecaptchaClients()");
        System.out.println(result);

//        String token = null;
//        try {
//            // 包含g-recaptcha 显示的进行人机身份验证
//            String siteKey = driver.findElement(By.xpath("//*[@id=\"g-recaptcha\"]")).getAttribute("outerHTML");
//            siteKey = siteKey.split("&k=")[1].split("&")[0];
//            JSONObject obj1 = new JSONObject().putOpt("key", apiKey).putOpt("method", "userrecaptcha")
//                    .putOpt("googlekey", siteKey).putOpt("pageurl", url);
//            String body1 = HttpUtil.createPost("http://2captcha.com/in.php").body(JSONUtil.toJsonStr(obj1))
//                    .execute().body();
//            if (body1.startsWith("OK")) {
//                String request_id = body1.split("|")[1];
//                // 15-20秒后再发起另外1个请求
//                ThreadUtil.safeSleep(20000L);
//                HttpRequest resRequest = HttpUtil.createGet("http://2captcha.com/res.php?key=" + apiKey + "&action=get&id=" + request_id);
//                token = getTokenFromRequest(resRequest);
//                if ("false".equals(token)) {
//                    return false;
//                }
//
//            } else {
//                log.error("2captcha.com/in.php请求失败，响应正文:{}", body1);
//                DingTalkRobot.sendMsg("2captcha.com/in.php请求失败，响应正文:" + body1);
//            }
//
//        } catch (NoSuchElementException e) {
//
//        }
//        driver.executeScript("var element = document.getElementById(\"g-recaptcha-response\"); element.style.display=\"\"");
//        driver.executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + token +  "\"");
        return false;
    }

    private String getTokenFromRequest(HttpRequest resRequest) {
        String body = resRequest.execute().body();
        if ("CAPCHA_NOT_READY".equals(body)) {
            // 还未完成，5秒后再重试
            ThreadUtil.safeSleep(5000L);
            return getTokenFromRequest(resRequest);
        } else if (body.startsWith("ERROR")) {
            log.error("2captcha.com/res.php请求失败，响应正文:{}", body);
            DingTalkRobot.sendMsg("2captcha.com/res.php请求失败，响应正文:" + body);
            return "false";
        } else {
            return body;
        }
    }
}
