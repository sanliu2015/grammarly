package com.plq.grammarly;

import ch.qos.logback.classic.Logger;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.plq.grammarly.util.BizUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
public class MyTest {

    public static void main(String[] args) {
        testinvite();
//        testDelete();
    }

    private static void testQuery() {
        try {
            String curl = "curl 'https://gnar.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Removed' \\\n" +
                    "  -H 'authority: gnar.grammarly.com' \\\n" +
                    "  -H 'sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"' \\\n" +
                    "  -H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \\\n" +
                    "  -H 'x-client-version: 0.3.0-master.4784' \\\n" +
                    "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                    "  -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \\\n" +
                    "  -H 'accept: application/json' \\\n" +
                    "  -H 'x-client-type: account' \\\n" +
                    "  -H 'x-container-id: ylnm67btmejh09g2' \\\n" +
                    "  -H 'origin: https://account.grammarly.com' \\\n" +
                    "  -H 'sec-fetch-site: same-site' \\\n" +
                    "  -H 'sec-fetch-mode: cors' \\\n" +
                    "  -H 'sec-fetch-dest: empty' \\\n" +
                    "  -H 'referer: https://account.grammarly.com/admin/members' \\\n" +
                    "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                    "  -H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken=\"eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ==\"' \\\n" +
                    "  --compressed";
            Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(curl);
            HttpRequest httpRequest = BizUtil.buildQueryHttpRequest(httpRequestHeadMap);
            HttpResponse httpResponse = httpRequest.execute();
            System.out.println("httpStatus:" + httpResponse.getStatus() + ",resBody:" + httpResponse.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testinvite() {
        try {
            String curl = "curl 'https://gnar.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Removed' \\\n" +
                    "  -H 'authority: gnar.grammarly.com' \\\n" +
                    "  -H 'sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"' \\\n" +
                    "  -H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \\\n" +
                    "  -H 'x-client-version: 0.3.0-master.4784' \\\n" +
                    "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                    "  -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \\\n" +
                    "  -H 'accept: application/json' \\\n" +
                    "  -H 'x-client-type: account' \\\n" +
                    "  -H 'x-container-id: ylnm67btmejh09g2' \\\n" +
                    "  -H 'origin: https://account.grammarly.com' \\\n" +
                    "  -H 'sec-fetch-site: same-site' \\\n" +
                    "  -H 'sec-fetch-mode: cors' \\\n" +
                    "  -H 'sec-fetch-dest: empty' \\\n" +
                    "  -H 'referer: https://account.grammarly.com/admin/members' \\\n" +
                    "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                    "  -H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken=\"eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ==\"' \\\n" +
                    "  --compressed";
            Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(curl);
            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>(16);
            map.put("email", "717208317@qq.com");
            map.put("firstName", "");
            map.put("secondName", "");
            map.put("ineligibleEmail", false);
            map.put("assignedToOtherInstitutionEmail", false);
            dataList.add(map);
            String body = JSONUtil.toJsonStr(dataList);
            HttpRequest httpRequest = BizUtil.buildInviteHttpRequest(httpRequestHeadMap);
            httpRequest.body(body);
            HttpResponse httpResponse = httpRequest.execute();
            System.out.println("httpStatus:" + httpResponse.getStatus() + ",resBody:" + httpResponse.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDelete() {
        try {
            String curl = "curl 'https://gnar.grammarly.com/api/institution/admin/users/delete' \\\n" +
                    "  -H 'authority: gnar.grammarly.com' \\\n" +
                    "  -H 'sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"' \\\n" +
                    "  -H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \\\n" +
                    "  -H 'x-client-version: 0.3.0-master.4784' \\\n" +
                    "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                    "  -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \\\n" +
                    "  -H 'content-type: application/json' \\\n" +
                    "  -H 'accept: */*' \\\n" +
                    "  -H 'x-client-type: account' \\\n" +
                    "  -H 'x-container-id: ylnm67btmejh09g2' \\\n" +
                    "  -H 'origin: https://account.grammarly.com' \\\n" +
                    "  -H 'sec-fetch-site: same-site' \\\n" +
                    "  -H 'sec-fetch-mode: cors' \\\n" +
                    "  -H 'sec-fetch-dest: empty' \\\n" +
                    "  -H 'referer: https://account.grammarly.com/admin/members' \\\n" +
                    "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                    "  -H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken=\"eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ==\"' \\\n" +
                    "  --data-raw '{\"type\":\"Invited\",\"emails\":[\"qk99486563zhengs@163.com\"],\"reverse\":true}' \\\n" +
                    "  --compressed";
            Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(curl);
            HttpRequest httpRequest = BizUtil.buildRemoveHttpRequest(httpRequestHeadMap);
            Map<String, Object> map = new HashMap<>(16);
            map.put("type", "Active");
            map.put("reverse", false);
            map.put("emails", Lists.newArrayList("717208317@qq.com"));
            httpRequest.body(JSONUtil.toJsonStr(map));
            HttpResponse httpResponse = httpRequest.execute();
            System.out.println("httpStatus:" + httpResponse.getStatus() + ",resBody:" + httpResponse.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendMailTest() {
        String email = "penglq20200101@163.com";
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setPassword("YTEDNGRXSXKONTSS");
        sender.setUsername(email);
        sender.setHost("smtp.163.com");
        sender.setDefaultEncoding("UTF-8");
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl", true);
        properties.put("mail.smtp.from", email);
        sender.setJavaMailProperties(properties);
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            // 开启文件上传
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("请回复您的需求  私募管理人的『专』心管家元年金服为您服务");
            helper.setText("<html><body>" +
                    "<p>您好，<br />\n" +
                    "<br />\n" +
                    "感谢您百忙之中查看我的简历，期待与您有更多交流机会。<br />\n" +
                    "<br />\n" +
                    "您可以通过下列方式联系我：<br />\n" +
                    "<br />\n" +
                    "微信：yuannianjinfu / xiaocai157054<br />\n" +
                    "电话：18221390682 / 15901902443<br />\n" +
                    "<br />\n" +
                    "也可以发我您的联系方式，我将竭力为您解决您的问题！<br />\n" +
                    "<br />\n" +
                    "祝好！</p>" +
                    "<img src='cid:id1'></body></html>", true);
            helper.setTo("717208317@qq.com");
            helper.setFrom(email);
            FileSystemResource res = new FileSystemResource(new File("d:/Downloads/sm.jpg"));
            helper.addInline("id1", res);
            sender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
