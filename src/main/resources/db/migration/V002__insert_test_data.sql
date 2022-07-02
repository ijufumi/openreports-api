--
-- test data
--

insert into t_group (group_name, created_at, updated_at) values ('admin', now(), now());
insert into t_group (group_name, created_at, updated_at) values ('assistant', now(), now());

insert into t_member (email_address, password, name, is_admin, created_at, updated_at) values ('ijufumi@gmail.com', 'a11efa8c78f2870fd3918ee6528fd8bcb2daa9edb9f7a57384ac7a40c98a8daf', 'administrator', '1', now(), now());
insert into r_member_group values (1, 1);
insert into r_member_group values (1, 2);

insert into t_function (function_id, function_name, created_at, updated_at) values (1, 'テスト機能1', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (2, 'テスト機能2', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (3, 'テスト機能3', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (4, 'テスト機能4', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (5, 'テスト機能5', now(), now());
insert into r_group_function values (1, 1);
insert into r_group_function values (1, 2);
insert into r_group_function values (2, 3);
insert into r_group_function values (2, 4);
insert into r_group_function values (2, 5);

insert into t_report_template(file_name, file_path, created_at, updated_at) values ('sample1.xlsx', 'sample1.xlsx', now(), now());
insert into t_report_template(file_name, file_path, created_at, updated_at) values ('sample2.xlsx', 'sample2.xlsx', now(), now());
insert into t_report_template(file_name, file_path, created_at, updated_at) values ('sample3.xlsx', 'sample3.xlsx', now(), now());
insert into t_report_template(file_name, file_path, created_at, updated_at) values ('sample4.xlsx', 'sample4.xlsx', now(), now());

insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート1', 'テストレポート1', 1, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート2', 'テストレポート2', 2, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート3', 'テストレポート3', 3, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート4', 'テストレポート4', 4, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート5', 'テストレポート5', 1, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート6', 'テストレポート6', 2, now(), now());
insert into t_report (report_name, description, template_id, created_at, updated_at) values ('テストレポート7', 'テストレポート7', 3, now(), now());

insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ1', now(), now());
insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ2', now(), now());
insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ3', now(), now());

insert into r_report_report_group values (1, 1);
insert into r_report_report_group values (1, 2);
insert into r_report_report_group values (1, 3);
insert into r_report_report_group values (2, 1);
insert into r_report_report_group values (2, 2);
insert into r_report_report_group values (2, 3);
insert into r_report_report_group values (3, 1);
insert into r_report_report_group values (3, 2);
insert into r_report_report_group values (3, 3);
insert into r_report_report_group values (4, 3);
insert into r_report_report_group values (5, 3);
insert into r_report_report_group values (6, 3);
insert into r_report_report_group values (7, 1);

insert into r_group_report_group values (1, 1);
insert into r_group_report_group values (1, 2);
insert into r_group_report_group values (1, 3);

insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param1', 'ぱらむ1', 'テスト用パラメメータ1', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param2', 'ぱらむ2', 'テスト用パラメメータ2', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param3', 'ぱらむ3', 'テスト用パラメメータ3', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param4', 'ぱらむ4', 'テスト用パラメメータ4', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param5', 'ぱらむ5', 'テスト用パラメメータ5', '1', '');

insert into t_report_param_config (report_id, param_id) values (1, 1);
insert into t_report_param_config (report_id, param_id) values (1, 2);
insert into t_report_param_config (report_id, param_id) values (1, 3);
insert into t_report_param_config (report_id, param_id) values (1, 4);
insert into t_report_param_config (report_id, param_id) values (1, 5);
