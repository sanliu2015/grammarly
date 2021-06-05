## 环境要求
* jdk11
* mongodb4.4

## 监测心跳使用api
curl 'https://institution.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Invited' \
-H 'authority: institution.grammarly.com' \
-H 'sec-ch-ua: " Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"' \
-H 'x-csrf-token: AABJXqR66XFcbWOg1USHKLtYmWIyNJ2W0Xus7g' \
-H 'x-client-version: 0.3.0-master.4784' \
-H 'sec-ch-ua-mobile: ?0' \
-H 'user-agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36' \
-H 'accept: application/json' \
-H 'x-client-type: account' \
-H 'x-container-id: ital6785ub8r0k82' \
-H 'origin: https://account.grammarly.com' \
-H 'sec-fetch-site: same-site' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-dest: empty' \
-H 'referer: https://account.grammarly.com/admin/members' \
-H 'accept-language: zh-CN,zh;q=0.9' \
-H 'cookie: gnar_containerId=ital6785ub8r0k82; _gcl_au=1.1.1068810783.1622431294; ga_clientId=315429443.1622431294; tdi=smglb1pcyr6czptdt; isGrammarlyUser=true; _fbp=fb.1.1622431616583.1843971571; drift_aid=89753372-2dbb-42a9-9d89-33c503c0176c; driftt_aid=89753372-2dbb-42a9-9d89-33c503c0176c; cookieNotificationAck=true; funnelType=free; _gid=GA1.2.1506079028.1622796923; browser_info=CHROME:91:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_7:WINDOWS; _uetsid=9a4f3d30c51211ebbd9499968df3f3a0; _uetvid=4d399160c1bf11ebaaa5f77a99cbec52; _ga=GA1.2.315429443.1622431294; grauth=AABJXqbrNjNLvtq9pQkv5Qh7cClOSqJzJr6g9cb9bJ5OYU2_obG_INK-_cPKiqz7NLQyFh2X5B6GIHIO; csrf-token=AABJXqR66XFcbWOg1USHKLtYmWIyNJ2W0Xus7g; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tLyJ9; _ga_CBK9K2ZWWE=GS1.1.1622809894.8.1.1622809951.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled' \
--compressed


##邀请用户api
curl 'https://institution.grammarly.com/api/institution/admin/users/add' \
-H 'authority: institution.grammarly.com' \
-H 'sec-ch-ua: " Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"' \
-H 'x-csrf-token: AABJXqR66XFcbWOg1USHKLtYmWIyNJ2W0Xus7g' \
-H 'x-client-version: 0.3.0-master.4784' \
-H 'sec-ch-ua-mobile: ?0' \
-H 'user-agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36' \
-H 'content-type: application/json' \
-H 'accept: */*' \
-H 'x-client-type: account' \
-H 'x-container-id: ital6785ub8r0k82' \
-H 'origin: https://account.grammarly.com' \
-H 'sec-fetch-site: same-site' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-dest: empty' \
-H 'referer: https://account.grammarly.com/admin/members' \
-H 'accept-language: zh-CN,zh;q=0.9' \
-H 'cookie: gnar_containerId=ital6785ub8r0k82; _gcl_au=1.1.1068810783.1622431294; ga_clientId=315429443.1622431294; tdi=smglb1pcyr6czptdt; isGrammarlyUser=true; _fbp=fb.1.1622431616583.1843971571; drift_aid=89753372-2dbb-42a9-9d89-33c503c0176c; driftt_aid=89753372-2dbb-42a9-9d89-33c503c0176c; cookieNotificationAck=true; funnelType=free; _gid=GA1.2.1506079028.1622796923; browser_info=CHROME:91:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_7:WINDOWS; _uetsid=9a4f3d30c51211ebbd9499968df3f3a0; _uetvid=4d399160c1bf11ebaaa5f77a99cbec52; _ga=GA1.2.315429443.1622431294; grauth=AABJXqbrNjNLvtq9pQkv5Qh7cClOSqJzJr6g9cb9bJ5OYU2_obG_INK-_cPKiqz7NLQyFh2X5B6GIHIO; csrf-token=AABJXqR66XFcbWOg1USHKLtYmWIyNJ2W0Xus7g; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL2FwcC5ncmFtbWFybHkuY29tLyJ9; _ga_CBK9K2ZWWE=GS1.1.1622809894.8.1.1622809951.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled' \
--data-raw '[{"email":"717208317@qq.com","firstName":"","secondName":"","ineligibleEmail":false,"assignedToOtherInstitutionEmail":false}]' \
--compressed

## 删除(active)
curl 'https://institution.grammarly.com/api/institution/admin/users/delete' \
-H 'authority: institution.grammarly.com' \
-H 'sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90"' \
-H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \
-H 'x-client-version: 0.3.0-master.4784' \
-H 'sec-ch-ua-mobile: ?0' \
-H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \
-H 'content-type: application/json' \
-H 'accept: */*' \
-H 'x-client-type: account' \
-H 'x-container-id: ylnm67btmejh09g2' \
-H 'origin: https://account.grammarly.com' \
-H 'sec-fetch-site: same-site' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-dest: empty' \
-H 'referer: https://account.grammarly.com/admin/members' \
-H 'accept-language: zh-CN,zh;q=0.9' \
-H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; browser_info=CHROME:90:COMPUTER:SUPPORTED:FREEMIUM:WINDOWS_10:WINDOWS; _gid=GA1.2.1659282940.1622859203; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken="eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ=="; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; funnel_firstTouchUtmSource=GrammarlyBusiness; redirect_location=eyJ0eXBlIjoiIiwibG9jYXRpb24iOiJodHRwczovL3d3dy5ncmFtbWFybHkuY29tL2J1c2luZXNzL2pvaW4/dXRtX2NhbXBhaWduPUFkbWluSW52aXRlX0VudGVycHJpc2UmdXRtX21lZGl1bT1lbWFpbCZ1dG1fc291cmNlPUdyYW1tYXJseUJ1c2luZXNzJnV0bV9jb250ZW50PTFBIn0=; _uetsid=9b720940c5a311ebaf8d4dd12f17a0f9; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0' \
--data-raw '{"type":"Active","emails":["13761336189@sina.cn"],"reverse":false}' \
--compressed



curl 'https://institution.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Invited' \
-H 'authority: institution.grammarly.com' \
-H 'sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90"' \
-H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \
-H 'x-client-version: 0.3.0-master.4784' \
-H 'sec-ch-ua-mobile: ?0' \
-H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \
-H 'accept: application/json' \
-H 'x-client-type: account' \
-H 'x-container-id: ylnm67btmejh09g2' \
-H 'origin: https://account.grammarly.com' \
-H 'sec-fetch-site: same-site' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-dest: empty' \
-H 'referer: https://account.grammarly.com/admin/members' \
-H 'accept-language: zh-CN,zh;q=0.9' \
-H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; _gid=GA1.2.1659282940.1622859203; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; funnel_firstTouchUtmSource=GrammarlyBusiness; _uetsid=9b720940c5a311ebaf8d4dd12f17a0f9; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken="eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ=="' \
--compressed

curl 'https://institution.grammarly.com/api/institution/admin/users/find_all?offset=0&limit=100&order=email&order_type=asc&type=Invited' \
-H 'authority: institution.grammarly.com' \
-H 'sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90"' \
-H 'x-csrf-token: AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg' \
-H 'x-client-version: 0.3.0-master.4784' \
-H 'sec-ch-ua-mobile: ?0' \
-H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36' \
-H 'accept: application/json' \
-H 'x-client-type: account' \
-H 'x-container-id: ylnm67btmejh09g2' \
-H 'origin: https://account.grammarly.com' \
-H 'sec-fetch-site: same-site' \
-H 'sec-fetch-mode: cors' \
-H 'sec-fetch-dest: empty' \
-H 'referer: https://account.grammarly.com/admin/members' \
-H 'accept-language: zh-CN,zh;q=0.9' \
-H 'cookie: gnar_containerId=ylnm67btmejh09g2; ga_clientId=446403749.1622556928; _gcl_au=1.1.2134693897.1622556929; tdi=tyyxq1pe9ha08d4ht; funnelType=free; _gid=GA1.2.1659282940.1622859203; grauth=AABJX7-m2WVxpLtflONOygxX_JdAKiiNlRoR7M4T-gkEZyMVn3cw_oDdTrNs16kN4c48EIyIvKiRDbkI; csrf-token=AABJXztZJjQSn6WLWIZZPbNDKA/VDutA/myaXg; invite_key=b4f1f17ec108f68bb4712d5360be8e0ed69ee7a55282b982fbc814a264461ce7; funnel_firstTouchUtmSource=GrammarlyBusiness; _uetsid=9b720940c5a311ebaf8d4dd12f17a0f9; _uetvid=d6ead010c2e311eb97beff739decef91; _ga=GA1.1.446403749.1622556928; _ga_CBK9K2ZWWE=GS1.1.1622859203.2.1.1622860913.0; experiment_groups=fsrw_in_sidebar_allusers_enabled|denali_capi_all_enabled|truecaser_enabled|extension_new_rich_text_fields_enabled|gdocs_for_chrome_enabled|officeaddin_outcomes_ui_exp5_enabled1|gb_tone_detector_onboarding_flow_enabled|completions_beta_enabled|premium_ungating_renewal_notification_enabled|quarantine_messages_enabled|small_hover_menus_existing_enabled|officeaddin_upgrade_state_exp2_enabled1|gb_in_editor_premium_Test1|gb_analytics_mvp_phase_one_enabled|gb_rbac_new_members_ui_enabled|ipm_extension_release_test_1|gb_analytics_group_filters_enabled|extension_assistant_bundles_all_enabled|ios_keyboard_multitouch_handling_v0_control2|officeaddin_proofit_exp3_enabled|gdocs_sidebar_allusers_enabled|ios_uphook_upgrade_page_congruency_v0_experiment2|gdocs_for_all_firefox_enabled|gb_analytics_mvp_phase_one_30_day_enabled|auto_complete_correct_safari_enabled|fluid_gdocs_rollout_enabled|officeaddin_ue_exp3_enabled|officeaddin_upgrade_state_exp1_enabled1|disable_extension_installation_disabled|safari_migration_inline_disabled_enabled|completions_release_enabled1|optimized_gdocs_gate_2_enabled|fsrw_in_assistant_all_enabled|autocorrect_new_ui_v3|emogenie_beta_enabled|apply_formatting_all_enabled|shadow_dom_chrome_enabled|denali_link_to_kaza_enabled|extension_assistant_experiment_all_enabled|gdocs_for_all_safari_enabled|extension_assistant_all_enabled|gb_kaza_support_chat_test_2|latency_slow_down_delay_500|safari_migration_backup_notif1_enabled|auto_complete_correct_edge_enabled|safari_migration_popup_editor_disabled_enabled|gb_kaza_presales_chat_control_2|extension_check_manakin_v2_experiment_enabled|safari_migration_inline_warning_enabled|gdocs_new_mapping_enabled|officeaddin_muted_alerts_exp2_enabled1|officeaddin_perf_exp3_enabled; profileToken="eyJlbWFpbCI6InFrOTk0ODY1NjN6aGVuZ3NAMTYzLmNvbSIsInNpZ25hdHVyZSI6ImJlYTBhMWI0NmZiYjA1NDE3OWI5ZTQyMDk3MDFiYjNlYzRkMjM5YzUifQ=="' \
--compressed