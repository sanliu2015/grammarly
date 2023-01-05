package com.plq.grammarly.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.plq.grammarly.model.entity.GrammarlyAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
public class BizUtil {

    private BizUtil() {}

    public static Map<String, String> convertFromCurl(String curlStr) {
        Map<String, String> headers = new HashMap<>(16);
        String[] strs = curlStr.split("\\\n");
        for (String header: strs) {
            int idx = header.indexOf("-H ");
            if (idx > -1) {
                String keyVal = header.substring(idx+4, header.length()-3).trim();
                int colonIdx = keyVal.indexOf(":");
                if (colonIdx > -1) {
                    String key = keyVal.substring(0, colonIdx);
                    headers.put(key, keyVal.substring(colonIdx + 2));
                }
            }
        }
        return headers;
    }

    /**
     * 构造邀请用户的httpRequest
     * @param httpRequestHeadMap
     * @return
     */
    public static HttpRequest buildInviteHttpRequest(Map<String, String> httpRequestHeadMap) {
        HttpRequest httpRequest = HttpUtil.createPost("https://goldengate.grammarly.com/institution/api/institution/admin/users/add")
                .header("user-agent", httpRequestHeadMap.get("user-agent"))
                .header("sec-fetch-site", httpRequestHeadMap.getOrDefault("sec-fetch-site", "same-site"))
                .header("sec-fetch-mode", httpRequestHeadMap.getOrDefault("sec-fetch-site", "cors"))
                .header("sec-fetch-dest", httpRequestHeadMap.getOrDefault("sec-fetch-dest", "empty"))
                .header("sec-ch-ua-platform", httpRequestHeadMap.getOrDefault("sec-ch-ua-platform", "\"Windows\""))
                .header("sec-ch-ua", httpRequestHeadMap.getOrDefault("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\""))
                .header("sec-ch-ua-mobile", httpRequestHeadMap.getOrDefault("sec-ch-ua-mobile", "?0"))
                .header("content-type", "application/json")
                .header("accept", "*/*")
                .header("authority", "goldengate.grammarly.com")
                .header("origin", "https://account.grammarly.com")
                .header("referer", "https://account.grammarly.com/admin/members")
                .header("accept-language", httpRequestHeadMap.getOrDefault("accept-language", "zh-CN,zh;q=0.9"))
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
        if (httpRequestHeadMap.containsKey("authorization")) {
            httpRequest.header("authorization", httpRequestHeadMap.get("authorization"));
        }
        return httpRequest;
    }

    public static HttpRequest buildRemoveHttpRequest(Map<String, String> httpRequestHeadMap) {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.DELETE, "https://goldengate.grammarly.com/institution/api/institution/members")
                .header("user-agent", httpRequestHeadMap.get("user-agent"))
                .header("sec-fetch-site", httpRequestHeadMap.getOrDefault("sec-fetch-site", "same-site"))
                .header("sec-fetch-mode", httpRequestHeadMap.getOrDefault("sec-fetch-site", "cors"))
                .header("sec-fetch-dest", httpRequestHeadMap.getOrDefault("sec-fetch-dest", "empty"))
                .header("sec-ch-ua-platform", httpRequestHeadMap.getOrDefault("sec-ch-ua-platform", "\"Windows\""))
                .header("sec-ch-ua", httpRequestHeadMap.getOrDefault("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\""))
                .header("sec-ch-ua-mobile", httpRequestHeadMap.getOrDefault("sec-ch-ua-mobile", "?0"))
                .header("content-type", "application/json")
                .header("accept", "*/*")
                .header("authority", "goldengate.grammarly.com")
                .header("origin", "https://account.grammarly.com")
                .header("referer", "https://account.grammarly.com/admin/members")
                .header("accept-language", httpRequestHeadMap.getOrDefault("accept-language", "zh-CN,zh;q=0.9"))
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
        if (httpRequestHeadMap.containsKey("authorization")) {
            httpRequest.header("authorization", httpRequestHeadMap.get("authorization"));
        }
        return httpRequest;
    }

    public static HttpRequest buildQueryHttpRequest(Map<String, String> httpRequestHeadMap) {
        HttpRequest httpRequest = HttpUtil.createGet("https://goldengate.grammarly.com/institution/api/institution/members?offset=0&limit=10&order=email&order_type=asc&search=_&memberStatus=ACTIVE&memberStatus=INVITED&memberStatus=INVITE_EXPIRED")
                .header("user-agent", httpRequestHeadMap.get("user-agent"))
                .header("sec-fetch-site", httpRequestHeadMap.getOrDefault("sec-fetch-site", "same-site"))
                .header("sec-fetch-mode", httpRequestHeadMap.getOrDefault("sec-fetch-site", "cors"))
                .header("sec-fetch-dest", httpRequestHeadMap.getOrDefault("sec-fetch-dest", "empty"))
                .header("sec-ch-ua-platform", httpRequestHeadMap.getOrDefault("sec-ch-ua-platform", "\"Windows\""))
                .header("sec-ch-ua", httpRequestHeadMap.getOrDefault("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\""))
                .header("sec-ch-ua-mobile", httpRequestHeadMap.getOrDefault("sec-ch-ua-mobile", "?0"))
                .header("accept", "application/json")
                .header("authority", "goldengate.grammarly.com")
                .header("origin", "https://account.grammarly.com")
                .header("referer", "https://account.grammarly.com/admin/members")
                .header("accept-language", httpRequestHeadMap.getOrDefault("accept-language", "zh-CN,zh;q=0.9"))
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
        if (httpRequestHeadMap.containsKey("authorization")) {
            httpRequest.header("authorization", httpRequestHeadMap.get("authorization"));
        }
        return httpRequest;
    }

    public static void main(String[] args) {
        String s = "curl 'https://subscription.grammarly.com/api/v1/subscription' \\\n" +
                "  -H 'authority: subscription.grammarly.com' \\\n" +
                "  -H 'sec-ch-ua: \" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"' \\\n" +
                "  -H 'x-csrf-token: AABJXijtlrtGWxe1LP6CP3KC6jKxhH5W2tfxCg' \\\n" +
                "  -H 'x-client-version: 1.5.43-3548+master' \\\n" +
                "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                "  -H 'user-agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36' \\\n" +
                "  -H 'accept: application/json' \\\n" +
                "  -H 'x-client-type: webeditor_chrome' \\\n" +
                "  -H 'x-container-id: ital6785ub8r0k82' \\\n" +
                "  -H 'origin: https://app.grammarly.com' \\\n" +
                "  -H 'sec-fetch-site: same-site' \\\n" +
                "  -H 'sec-fetch-mode: cors' \\\n" +
                "  -H 'sec-fetch-dest: empty' \\\n" +
                "  -H 'referer: https://app.grammarly.com/' \\\n" +
                "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                "  -H 'cookie: gnar_containerId=ital6785ub8r0k82; _gcl_au=1.1.1068810783.1622431294; ga_clientId=315429443.1622431294; tdi=smglb1pcyr6czptdt; isGrammarlyUser=true; _fbp=fb.1.1622431616583.1843971571; drift_aid=89753372-2dbb-42a9-9d89-33c503c0176c; driftt_aid=89753372-2dbb-42a9-9d89-33c503c0176c; cookieNotificationAck=true; experiment_groups=fsrw_in_sidebar_allusers_enabled|ipm_gb_member_activation_v2_control_1|extension_assistant_bundles_all_consumers_enabled|denali_capi_all_enabled|fsrw_in_assistant_all_consumers_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_analytics_mvp_phase_one_enabled|wonderpass_enabled|apply_formatting_all_consumers_enabled|ipm_extension_release_test_1|extension_assistant_experiment_all_consumers_enabled|extension_assistant_bundles_all_enabled|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|truecaser_control|gb_in_editor_free_Test1|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|extension_assistant_all_consumers_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|latency_slow_down_delay_1000|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|denali_capi_all_consumers_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; funnelType=free; browser_info=CHROME:91:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_7:WINDOWS; _gid=GA1.2.1506079028.1622796923; _gat=1; _ga=GA1.1.315429443.1622431294; _uetsid=9a4f3d30c51211ebbd9499968df3f3a0; _uetvid=4d399160c1bf11ebaaa5f77a99cbec52; grauth=AABJXg0YvtCFF9ABwyV1AYxfZgpxxRZJvlgXBdQaNYvk1KqKMJy5aWj6z-eA4yDzL3PPvRywJRmkQNki; csrf-token=AABJXijtlrtGWxe1LP6CP3KC6jKxhH5W2tfxCg; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tLyJ9; _ga_CBK9K2ZWWE=GS1.1.1622796922.7.1.1622796979.0' \\\n" +
                "  --compressed";
        System.out.println(JSONUtil.toJsonStr(convertFromCurl(s)));

        String b = "curl 'https://goldengate.grammarly.com/institution/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Invited' \\\n" +
                "-H 'authority: goldengate.grammarly.com' \\\n" +
                "-H 'sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"' \\\n" +
                "-H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \\\n" +
                "-H 'x-client-version: 0.3.0-master.4784' \\\n" +
                "-H 'sec-ch-ua-mobile: ?0' \\\n" +
                "-H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \\\n" +
                "-H 'accept: application/json' \\\n" +
                "-H 'x-client-type: account' \\\n" +
                "-H 'x-container-id: ylnm67btmejh09g2' \\\n" +
                "-H 'origin: https://account.grammarly.com' \\\n" +
                "-H 'sec-fetch-site: same-site' \\\n" +
                "-H 'sec-fetch-mode: cors' \\\n" +
                "-H 'sec-fetch-dest: empty' \\\n" +
                "-H 'referer: https://account.grammarly.com/admin/members' \\\n" +
                "-H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                "-H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; _gid=GA1.2.1659282940.1622859203; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; funnel_firstTouchUtmSource=GrammarlyBusiness; _uetsid=9b720940c5a311ebaf8d4dd12f17a0f9; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken=\"eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ==\"' \\\n" +
                "--compressed";
        System.out.println(JSONUtil.toJsonStr(convertFromCurl(b)));
    }


}
