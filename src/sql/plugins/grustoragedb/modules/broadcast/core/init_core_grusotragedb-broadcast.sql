
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GRUSTORAGEDB_BROADCAST_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRUSTORAGEDB_BROADCAST_MANAGEMENT','grustoragedb-broadcast.adminFeature.ManageSubscriptions.name',1,'jsp/admin/plugins/grustoragedb/modules/broadcast/ManageSubscriptions.jsp?plugin_name=grustoragedb-broadcast','grustoragedb-broadcast.adminFeature.ManageSubscriptions.description',0,'grustoragedb-broadcast','APPLICATIONS',NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRUSTOTRAGEDB_BROADCAST_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('GRUSTORAGEDB_BROADCAST_MANAGEMENT',1);

