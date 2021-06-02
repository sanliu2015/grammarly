package com.plq.grammarly.task;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/01
 */
@Component
@Slf4j
public class TestTask {

    @Scheduled(fixedRate = 1000L*60*20)
    public void test() {
        try {
            HttpRequest httpRequest = HttpUtil.createGet("https://data.grammarly.com/api/mimic/withProps")
                    .header("x-csrf-token", "AABJW5TMliLU4A0TtIWqI1h/9XdyWskrDGfcPA")
                    .header("x-client-version", "0.3.0-master.4740")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                    .header("x-client-type", "account")
                    .header("x-container-id", "ylnm67btmejh09g2")
                    .header("origin", "https://account.grammarly.com")
                    .header("sec-fetch-site", "same-site")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-dest", "empty")
                    .header("referer", "https://account.grammarly.com/customize/language")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("cookie", "gnar_containerId=ylnm67btmejh09g2; funnelType=free; browser_info=CHROME:90:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_10:WINDOWS; funnel_firstTouchUtmSource=google; _gid=GA1.2.270755335.1622556928; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; _uetsid=d6e9f480c2e311eb8515e78636129204; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; grauth=AABJWxQ4ljvL1W1gYPVGQKr0hzuVVUxKJPMc8QhzV4Oojllel3iMCRf4V55ESnUblg99u98NCGBjc3HH; csrf-token=AABJW5TMliLU4A0TtIWqI1h/9XdyWskrDGfcPA; tdi=tyyxq1pe9ha08d4ht; _ga_CBK9K2ZWWE=GS1.1.1622556928.1.1.1622557337.0; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tL2FwcHMifQ==; experiment_groups=fsrw_in_sidebar_allusers_enabled|homepage_load_test_2|ipm_gb_member_activation_v2_control_1|extension_assistant_bundles_all_consumers_enabled|fsrw_in_assistant_all_consumers_enabled|denali_capi_all_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_analytics_mvp_phase_one_enabled|wonderpass_enabled|apply_formatting_all_consumers_enabled|ipm_extension_release_test_1|extension_assistant_experiment_all_consumers_enabled|extension_assistant_bundles_all_enabled|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|truecaser_control|gb_in_editor_free_Test1|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|disable_extension_installation_disabled|officeaddin_upgrade_state_exp1_enabled1|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|extension_assistant_all_consumers_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|latency_slow_down_delay_1000|apply_formatting_all_enabled|shadow_dom_chrome_enabled|extension_assistant_experiment_all_enabled|denali_link_to_kaza_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|denali_capi_all_consumers_enabled|gdocs_new_mapping_enabled|officeaddin_perf_exp3_enabled|officeaddin_muted_alerts_exp2_enabled1; profileToken=eyJlbWFpbCI6InBlbmdscTEyMTBAZ21haWwuY29tIiwic2lnbmF0dXJlIjoiMWE1ZTI5ZDJkYzUyYWUwNmViN2E0ZTQyOGJlYmRhNGJhMGNlZjRhYSJ9")
                    .header("authority", "data.grammarly.com")
                    .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                    .header("sec-ch-ua-mobile", "?0")
                    ;
            HttpResponse httpResponse = httpRequest.execute();
            log.info("httpStatus:{},resBody:{}", httpResponse.getStatus(), httpResponse.body());
            ThreadUtil.safeSleep(RandomUtil.randomLong(1000L*10, 1000L*10*6));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
