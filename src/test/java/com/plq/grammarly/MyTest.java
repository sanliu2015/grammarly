package com.plq.grammarly;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.plq.grammarly.util.BizUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
public class MyTest {

    private static String set_cookie = null;

    public static void main(String[] args) {
        testQuery();
//        testinvite();
//        testDelete();
//        String s = "啊啊JSESSIONID=node0wpfkk5g5ozf21mdxem697qmoo14325325.node0;哈哈";
//        s = s.replaceAll("JSESSIONID=.*node0", "aa");
//        System.out.println(s);
    }

    private static void testQuery() {
        try {
            String curl = "curl 'https://goldengate.grammarly.com/institution/api/institution/user' \\\n" +
                    "  -H 'authority: goldengate.grammarly.com' \\\n" +
                    "  -H 'accept: application/json' \\\n" +
                    "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                    "  -H 'authorization: Bearer eyJ2ZXIiOiJWMyIsInR5cCI6ImF0K2p3dCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiYmVBaDZRLTZEeS01eV9RWDFoVFhqTDhDSktFZ1FrSktWdDE0amhOaTNJbyJ9.eyJzdWIiOiIxOTcxNDU1MzY3IiwiYXVkIjoiKi5ncmFtbWFybHkuY29tIiwic2NwIjoiZ3JhbW1hcmx5LmNhcGkuYWxsIiwicnRpIjoiMjI4OWYyMWEtY2ZmZS00YjM5LTlhZjEtMWQ2MWNjODIyMjViIiwib3JpZ2luIjoiaHR0cHM6Ly9hY2NvdW50LmdyYW1tYXJseS5jb20iLCJpc3MiOiJ0b2tlbnMuZ3JhbW1hcmx5LmNvbSIsImV4cCI6MTY3ODg0ODcxMiwiaWF0IjoxNjc4ODQ4NDEyLCJqdGkiOiIzMzBhMDNjYS01NGEyLTRhOGEtYWU4ZC0yMzk4NGYxM2U1Y2IiLCJjaWQiOiJhY2NvdW50In0.pUwqCJ2GL6PBMmPjgJJSyuDiLIbfYJ2yX1YVSMbNRR90nIuHDPDm_08djyMWeSFcZt8AdancLqWqVISfkh9ljVvbHvs_xgc5kccaCR-IaOxAukDxVoqkW_D4MWxL0_HV5rlywXRwWIGYdeCBdB5cw1GoWHN3SCiuWa7YYjHyzXZ6AsybogquDyym3On8wTSewcDU3R_7Z79EHwRer4_cJq2BmjtaxlHccXDQh2zzJN7Xl7nHLBSAQdI4lYK7IDLfgvhagx9b8r6YkRAVKsH8hgptHZUUgcVleJX3vl6sIPPXDeHuwbCWrZ_buXXZjHUlNXutFZ1kN5S7BYgE20E5_g' \\\n" +
                    "  -H 'cookie: gnar_containerId=uqfm7dkc9g7s0502; __zlcmid=1C3lEw3hBprhz3b; drift_aid=77e92db8-d8b8-4a7b-bbaf-30e067c127f7; driftt_aid=77e92db8-d8b8-4a7b-bbaf-30e067c127f7; ga_clientId=950222213.1663642794; _ga_3X1EDE2ENQ=GS1.1.1663834194.2.0.1663834194.0.0.0; _clck=1l4kbbs|1|f5f|0; _seg_uid_12252=01GJFASX8MY7AC4FXB36MYVX1P; _seg_uid=01GJFASX8MY7AC4FXB36MYVX1P; _seg_visitor_12252={\"referrer\":null}; tdi=fkeyr22qwegdpsqgx; _gcl_au=1.1.6037293.1678178568; funnelType=free; browser_info=CHROME:110:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_10:WINDOWS; _gid=GA1.2.790131444.1678846717; _rdt_uuid=1678846717611.d8d83e14-e269-42a4-aa02-c69c061287fe; OptanonConsent=isGpcEnabled=0&datestamp=Wed+Mar+15+2023+10%3A19%3A02+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202211.2.0&isIABGlobal=false&hosts=&landingPath=https%3A%2F%2Fwww.grammarly.com%2Fsignin&groups=C0001%3A1%2CC0002%3A1%2CC0003%3A1%2CC0004%3A1; _uetsid=b1401ed0c2d711ed8a2937ecd0d2e458; _uetvid=7303a3a03a4711ed8ebaed5d83bfe4b4; _ga=GA1.2.950222213.1663642794; grauth=AABL5xSn1ePGrcqjJH-KkWs_zh0l2Et3Ez_DqjYMDwOAYBwgh7N5fTSjDItlq7Aq-urlbt_8kNmvDBze; csrf-token=AABL53MUzcPW3fbuv+VtxwFzpVbT9sy9E6X02w; isGrammarlyUser=true; JSESSIONID=node0yame0a6ruk35y7bzrmj9dw6618034968.node0; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tLyJ9; _ga_CBK9K2ZWWE=GS1.1.1678846717.5.1.1678848165.48.0.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|kaza_security_hub_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|gb_snippets_csv_upload_enabled|grammarly_web_ukraine_logo_dapi_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|snippets_in_ws_gate_enabled|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|officeaddin_proofit_exp3_enabled|gdocs_for_all_firefox_enabled|gb_snippets_cycle_one_enabled|shared_workspaces_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|officeaddin_upgrade_state_exp1_enabled1|completions_release_enabled1|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|takeaways_premium_enabled|realtime_proofit_external_rollout_enabled|safari_migration_popup_editor_disabled_enabled|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|llama_beta_managed_test_1|gdocs_new_mapping_enabled|gb_expanded_analytics_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled' \\\n" +
                    "  -H 'origin: https://account.grammarly.com' \\\n" +
                    "  -H 'referer: https://account.grammarly.com/admin' \\\n" +
                    "  -H 'sec-ch-ua: \"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"' \\\n" +
                    "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                    "  -H 'sec-ch-ua-platform: \"Windows\"' \\\n" +
                    "  -H 'sec-fetch-dest: empty' \\\n" +
                    "  -H 'sec-fetch-mode: cors' \\\n" +
                    "  -H 'sec-fetch-site: same-site' \\\n" +
                    "  -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' \\\n" +
                    "  -H 'x-client-type: account' \\\n" +
                    "  -H 'x-client-version: 0.3.0-master.17290' \\\n" +
                    "  -H 'x-container-id: uqfm7dkc9g7s0502' \\\n" +
                    "  -H 'x-csrf-token: AABL53MUzcPW3fbuv+VtxwFzpVbT9sy9E6X02w' \\\n" +
                    "  --compressed";
            Map<String, String> httpRequestHeadMap = BizUtil.convertFromCurl(curl);
            HttpRequest httpRequest = BizUtil.buildQueryHttpRequest(httpRequestHeadMap);
            HttpResponse httpResponse = httpRequest.execute();
            System.out.println("httpStatus:" + httpResponse.getStatus() + ",Set-Cookie:" + httpResponse.header("Set-Cookie") + ",resBody:" + httpResponse.body());
//            if (httpResponse.header("Set-Cookie") != null) {
//                set_cookie =  httpResponse.header("Set-Cookie");
//            }
            httpResponse.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testinvite() {
        try {
            String curl = "curl 'https://goldengate.grammarly.com/institution/api/institution/user' \\\n" +
                    "  -H 'authority: goldengate.grammarly.com' \\\n" +
                    "  -H 'accept: application/json' \\\n" +
                    "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                    "  -H 'authorization: Bearer eyJ2ZXIiOiJWMyIsInR5cCI6ImF0K2p3dCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiYmVBaDZRLTZEeS01eV9RWDFoVFhqTDhDSktFZ1FrSktWdDE0amhOaTNJbyJ9.eyJzdWIiOiIxOTcxNDU1MzY3IiwiYXVkIjoiKi5ncmFtbWFybHkuY29tIiwic2NwIjoiZ3JhbW1hcmx5LmNhcGkuYWxsIiwicnRpIjoiMjI4OWYyMWEtY2ZmZS00YjM5LTlhZjEtMWQ2MWNjODIyMjViIiwib3JpZ2luIjoiaHR0cHM6Ly9hY2NvdW50LmdyYW1tYXJseS5jb20iLCJpc3MiOiJ0b2tlbnMuZ3JhbW1hcmx5LmNvbSIsImV4cCI6MTY3ODg0ODcxMiwiaWF0IjoxNjc4ODQ4NDEyLCJqdGkiOiIzMzBhMDNjYS01NGEyLTRhOGEtYWU4ZC0yMzk4NGYxM2U1Y2IiLCJjaWQiOiJhY2NvdW50In0.pUwqCJ2GL6PBMmPjgJJSyuDiLIbfYJ2yX1YVSMbNRR90nIuHDPDm_08djyMWeSFcZt8AdancLqWqVISfkh9ljVvbHvs_xgc5kccaCR-IaOxAukDxVoqkW_D4MWxL0_HV5rlywXRwWIGYdeCBdB5cw1GoWHN3SCiuWa7YYjHyzXZ6AsybogquDyym3On8wTSewcDU3R_7Z79EHwRer4_cJq2BmjtaxlHccXDQh2zzJN7Xl7nHLBSAQdI4lYK7IDLfgvhagx9b8r6YkRAVKsH8hgptHZUUgcVleJX3vl6sIPPXDeHuwbCWrZ_buXXZjHUlNXutFZ1kN5S7BYgE20E5_g' \\\n" +
                    "  -H 'cookie: gnar_containerId=uqfm7dkc9g7s0502; __zlcmid=1C3lEw3hBprhz3b; drift_aid=77e92db8-d8b8-4a7b-bbaf-30e067c127f7; driftt_aid=77e92db8-d8b8-4a7b-bbaf-30e067c127f7; ga_clientId=950222213.1663642794; _ga_3X1EDE2ENQ=GS1.1.1663834194.2.0.1663834194.0.0.0; _clck=1l4kbbs|1|f5f|0; _seg_uid_12252=01GJFASX8MY7AC4FXB36MYVX1P; _seg_uid=01GJFASX8MY7AC4FXB36MYVX1P; _seg_visitor_12252={\"referrer\":null}; tdi=fkeyr22qwegdpsqgx; _gcl_au=1.1.6037293.1678178568; funnelType=free; browser_info=CHROME:110:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_10:WINDOWS; _gid=GA1.2.790131444.1678846717; _rdt_uuid=1678846717611.d8d83e14-e269-42a4-aa02-c69c061287fe; OptanonConsent=isGpcEnabled=0&datestamp=Wed+Mar+15+2023+10%3A19%3A02+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202211.2.0&isIABGlobal=false&hosts=&landingPath=https%3A%2F%2Fwww.grammarly.com%2Fsignin&groups=C0001%3A1%2CC0002%3A1%2CC0003%3A1%2CC0004%3A1; _uetsid=b1401ed0c2d711ed8a2937ecd0d2e458; _uetvid=7303a3a03a4711ed8ebaed5d83bfe4b4; _ga=GA1.2.950222213.1663642794; grauth=AABL5xSn1ePGrcqjJH-KkWs_zh0l2Et3Ez_DqjYMDwOAYBwgh7N5fTSjDItlq7Aq-urlbt_8kNmvDBze; csrf-token=AABL53MUzcPW3fbuv+VtxwFzpVbT9sy9E6X02w; isGrammarlyUser=true; JSESSIONID=node0yame0a6ruk35y7bzrmj9dw6618034968.node0; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tLyJ9; _ga_CBK9K2ZWWE=GS1.1.1678846717.5.1.1678848165.48.0.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|kaza_security_hub_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|gb_snippets_csv_upload_enabled|grammarly_web_ukraine_logo_dapi_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|snippets_in_ws_gate_enabled|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|officeaddin_proofit_exp3_enabled|gdocs_for_all_firefox_enabled|gb_snippets_cycle_one_enabled|shared_workspaces_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|officeaddin_upgrade_state_exp1_enabled1|completions_release_enabled1|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|takeaways_premium_enabled|realtime_proofit_external_rollout_enabled|safari_migration_popup_editor_disabled_enabled|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|llama_beta_managed_test_1|gdocs_new_mapping_enabled|gb_expanded_analytics_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled' \\\n" +
                    "  -H 'origin: https://account.grammarly.com' \\\n" +
                    "  -H 'referer: https://account.grammarly.com/admin' \\\n" +
                    "  -H 'sec-ch-ua: \"Chromium\";v=\"110\", \"Not A(Brand\";v=\"24\", \"Google Chrome\";v=\"110\"' \\\n" +
                    "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                    "  -H 'sec-ch-ua-platform: \"Windows\"' \\\n" +
                    "  -H 'sec-fetch-dest: empty' \\\n" +
                    "  -H 'sec-fetch-mode: cors' \\\n" +
                    "  -H 'sec-fetch-site: same-site' \\\n" +
                    "  -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36' \\\n" +
                    "  -H 'x-client-type: account' \\\n" +
                    "  -H 'x-client-version: 0.3.0-master.17290' \\\n" +
                    "  -H 'x-container-id: uqfm7dkc9g7s0502' \\\n" +
                    "  -H 'x-csrf-token: AABL53MUzcPW3fbuv+VtxwFzpVbT9sy9E6X02w' \\\n" +
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


}
