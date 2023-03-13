package com.plq.grammarly.selenium;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.QuestionExchangeCode;
import com.plq.grammarly.util.DingTalkRobot;
import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.ReCaptcha;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.checkerframework.checker.units.qual.C;
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
import org.springframework.util.StringUtils;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
            "};let res=findRecaptchaClients();return res";

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
//            // 没什么用
//            Map<String, Object> prefs = new HashMap<>();
//            prefs.put("debuggerAddress", "127.0.0.1:9333");
//            prefs.put("download.prompt_for_download", false);
//            prefs.put("download.default_directory", fileSaveDir);
//            options.setExperimentalOption("prefs", prefs);
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9333");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setImplicitWaitTimeout(Duration.ofSeconds(10L));
            driver = new EdgeDriver(options);
            if (driver.getCurrentUrl() == null || !driver.getCurrentUrl().contains("coursehero.com")) {
                driver.get("https://www.coursehero.com/dashboard/");
            }
//            if ("dev".equals(SpringUtil.getActiveProfile())) {
////                driver.get("edge://version/");
////                driver.get("https://bot.sannysoft.com/");
//                driver.get("https://www.coursehero.com/");
//            }
            log.info("初始化edge selenium驱动成功");
            String pngBase64String = fullScreenCapture();
            File saveFile = new File(fileSaveDir + System.currentTimeMillis() + ".png");
            Base64.decodeToFile(pngBase64String, saveFile);
        } catch (Exception e) {
            log.error("初始化edge selenium驱动失败", e);
            System.exit(0);
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
            ThreadUtil.safeSleep(5000L);
            String recaptchaType = "";
            try {
                WebElement webElement = driver.findElement(By.cssSelector("#g-recaptcha-response"));
                recaptchaType = "1";
            } catch (NoSuchElementException noSuchElementException) {
                if (driver.getPageSource().contains("Incapsula")) {
                    recaptchaType = "2";
                }
            }
            if (!StrUtil.isEmpty(recaptchaType)) {
                boolean passFlag = this.crackEnterpriseRecaptcha(recaptchaType);
                if (passFlag) {
                    // 人机验证成功的话睡眠5秒钟再操作
                    ThreadUtil.safeSleep(5000L);
                } else {
                    return new JSONObject().putOpt("result", false)
                            .putOpt("errorcode", "").putOpt("errmsg", "破解人机校验失败，请手工前往服务器进行操作！");
                }
            }
            if (questionExchangeCode.getCode() == null) {
                // 心跳使用的
                return new JSONObject().putOpt("result", true).putOpt("errmsg", "");
            }

            // 如果页面有遮罩的话
            try {
                log.debug("检查是否有弹出遮罩，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                driver.findElement(By.cssSelector(".modal-dialog button[class*='modal-close']")).click();
                log.debug("发现弹框遮罩并关闭成功，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            } catch (NoSuchElementException noSuchElementException) {
                log.debug("没有发现有弹出遮罩，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            }

            // 如果是文件已经解锁
            try {
                log.debug("开始尝试【已解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                WebElement webElement = driver.findElement(By.cssSelector("#floating-ui-8"));
                webElement.click();
                ThreadUtil.safeSleep(1000L);
                WebElement downloadButtonElement = driver.findElement(By.cssSelector("div[data-testid=\"TOOLBAR_DOWNLOAD_SSI_TEST_ID\"] a"));
                downloadButtonElement.click();
                // 留10秒文件下载
                ThreadUtil.safeSleep(10000L);
                log.info("成功尝试【已解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                return downloadAnswerFile(questionExchangeCode);
            }  catch (NoSuchElementException noSuchElementException) {
                log.debug("结束尝试【已解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            }

            // 如果是文件还未解锁
            try {
                log.debug("开始尝试【未解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                WebElement webElement = driver.findElement(By.cssSelector("a[class=\"d-flex\"]"));
                webElement.click();
                // 留10秒文件下载
                ThreadUtil.safeSleep(10000L);
                log.info("成功尝试【已解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                return downloadAnswerFile(questionExchangeCode);
            } catch (NoSuchElementException noSuchElementException) {
                log.debug("结束尝试【已解锁且为文件】策略进行下载，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            }

            // 如果是网页还未解锁
            try {
                log.debug("开始尝试【未解锁且为网页】策略进行解锁，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                driver.findElement(By.cssSelector("a[data-cha-target-name=\"unlock_answer_btn\"][data-cha-location=\"answer_content\"]")).click();
                ThreadUtil.safeSleep(5000L);
                // 关闭弹窗
                try {
                    driver.findElement(By.cssSelector("a[class=\"ch_popup_close\"]")).click();
                    log.debug("发现弹框遮罩并关闭成功，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                    ThreadUtil.safeSleep(1000L);
                } catch (NoSuchElementException noSuchElementException) {
                    log.debug("a[class=\"ch_popup_close\"] 没找到关闭弹窗按钮");
                }
                log.info("成功尝试【未解锁且为网页】策略解锁成功，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            } catch (NoSuchElementException noSuchElementException) {
                log.debug("结束尝试【未解锁且为网页】策略进行解锁，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            }

            // 最后页面截图
            try {
                WebElement webElement = driver.findElement(By.id("answer-content"));
//                File snapshot = elementSnapshot(driver,webElement);
//                File saveFile = new File(fileSaveDir + questionExchangeCode.getCode() + ".png");
//                FileUtil.copyFile(snapshot, saveFile);
                String pngBase64String = fullScreenCapture();
                File saveFile = new File(fileSaveDir + questionExchangeCode.getCode() + ".png");
                Base64.decodeToFile(pngBase64String, saveFile);
                log.info("最后尝试【页面截图】策略进行截图保存，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
                return new JSONObject().putOpt("result", true).putOpt("filePath", saveFile.getAbsolutePath());
            } catch (NoSuchElementException noSuchElementException) {
                log.info("最后尝试【页面截图】策略进行操作失败，code:{},url:{}", questionExchangeCode.getCode(), questionExchangeCode.getQuestionUrl());
            }
            FileUtil.writeUtf8String(driver.getPageSource(), fileSaveDir + questionExchangeCode.getCode() + ".html");
            return new JSONObject().putOpt("result", false).putOpt("errmsg", "没有找到期望操作的元素");
        } catch (Exception e) {
            log.error("解锁courseHero发生异常，questionExchangeCode：{}", JSONUtil.toJsonStr(questionExchangeCode), e);
            return new JSONObject().putOpt("result", false).putOpt("errmsg", e.getMessage());
        }
    }

    private JSONObject downloadAnswerFile(QuestionExchangeCode questionExchangeCode) {
        String downloadFile = getNewestFile(downloadDir);
        String destFilePath = fileSaveDir + questionExchangeCode.getCode() + "." + FileUtil.getSuffix(downloadFile);
        File srcFile = new File(downloadFile);
        File dstFile = new File(destFilePath);
        FileUtil.move(srcFile, dstFile, false);
        return new JSONObject().putOpt("result", true).putOpt("filePath", dstFile.getAbsolutePath());
    }

    /**
     * 截全屏
     * https://www.zhihu.com/question/359095395/answer/1675694996
     * @return
     */
    public static String fullScreenCapture() {
        Object width = driver.executeScript("return document.body.scrollWidth");
        Object height = driver.executeScript("return document.body.scrollHeight");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", false);
        map.put("width", width);
        map.put("height", height);
        map.put("deviceScaleFactor", 1);
        driver.executeCdpCommand("Emulation.setDeviceMetricsOverride", map);
        // 然后再执行截图
        Map<String, Object> map2 = new HashMap<>();
        map2.put("fromSurface", true);
        Map<String, Object> result = driver.executeCdpCommand("Page.captureScreenshot", map2);
        String imageBase64 = result.get("data").toString();
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
    public boolean crackEnterpriseRecaptcha(String recaptchaType) {
        String pageurl = null;
        String sitekey = null;
        String version = null;
        String callbackFunctionName = null;
        ArrayList result = new ArrayList();
        if ("2".equals(recaptchaType)) {
            // 进入某个iframe,否则下面的执行脚本会报错
            driver.switchTo().frame("main-iframe");
        }
        result = (ArrayList)driver.executeScript(FIND_RECAPTCHA_PARAM_SCRIPT);
        if (result.size() == 1) {
            Map<String, Object> res = (Map)result.get(0);
            pageurl = (String)res.getOrDefault("pageurl", driver.getCurrentUrl());
            sitekey = (String)res.get("sitekey");
            version = (String)res.get("version");
            if (res.get("function") instanceof String) {
                callbackFunctionName = (String)res.get("function");
            } else {
                String jsCode = "return " + res.get("callback") + "['name']";
                callbackFunctionName = (String)driver.executeScript(jsCode);
            }
        } else {
            log.error("未知的capture类型：recaptchaType:{}, result:{}", recaptchaType, JSONUtil.toJsonStr(result));
            return false;
        }
        TwoCaptcha solver = new TwoCaptcha(apiKey);
        ReCaptcha captcha = new ReCaptcha();
        captcha.setUrl(pageurl);
        captcha.setSiteKey(sitekey);
        captcha.setVersion(version);
        try {
            solver.solve(captcha);
        } catch (Exception e) {
            log.error("2captcha Error occurred: {}", e.getMessage(), e);
            return false;
        }
        String responseCode = captcha.getCode();
        log.info("2capcha return responsee code:{}", responseCode);
        if ("1".equals(recaptchaType)) {
            driver.executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + responseCode +  "\"");
        } else if ("2".equals(recaptchaType)) {
            String jsScriptCode = "window['" + callbackFunctionName + "']('" + responseCode + "')";
            driver.executeScript(jsScriptCode);
        }
        return true;
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
