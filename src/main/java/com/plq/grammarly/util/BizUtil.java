package com.plq.grammarly.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
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
            if (header.startsWith("  -H ")) {
                String keyVal = header.substring(6, header.length()-3).trim();
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
        return HttpUtil.createPost("https://institution.grammarly.com/api/institution/admin/users/add")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .header("sec-fetch-site", "same-site")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("origin", "*/*")
                .header("content-type", "application/json")
                .header("authority", "institution.grammarly.com")
                .header("referer", "https://account.grammarly.com/admin/members")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
    }

    public static HttpRequest buildRemoveHttpRequest(Map<String, String> httpRequestHeadMap) {
        return HttpUtil.createPost("https://institution.grammarly.com/api/institution/admin/users/delete")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .header("sec-fetch-site", "same-site")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("origin", "*/*")
                .header("content-type", "application/json")
                .header("authority", "institution.grammarly.com")
                .header("referer", "https://account.grammarly.com/admin/members")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
    }

    public static HttpRequest buildQueryHttpRequest(Map<String, String> httpRequestHeadMap) {
        return HttpUtil.createGet("https://institution.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=10&order=email&order_type=asc&type=Invited")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .header("sec-fetch-site", "same-site")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("origin", "https://account.grammarly.com")
                .header("accept", "application/json")
                .header("authority", "institution.grammarly.com")
                .header("referer", "https://account.grammarly.com/customize/language")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("x-container-id", httpRequestHeadMap.get("x-container-id"))
                .header("x-csrf-token", httpRequestHeadMap.get("x-csrf-token"))
                .header("x-client-type", httpRequestHeadMap.get("x-client-type"))
                .header("x-client-version", httpRequestHeadMap.get("x-client-version"))
                .header("cookie", httpRequestHeadMap.get("cookie"))
                ;
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
        Map<String, String> t = convertFromCurl(s);
    }


}
