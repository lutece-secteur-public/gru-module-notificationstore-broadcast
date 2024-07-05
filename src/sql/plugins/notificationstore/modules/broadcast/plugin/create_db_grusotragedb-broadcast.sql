
--
-- Structure for table notificationstore_broadcast_subscription
--

DROP TABLE IF EXISTS notificationstore_broadcast_subscription;
CREATE TABLE notificationstore_broadcast_subscription (
id_subscription int AUTO_INCREMENT,
demand_type_id int default '0' NOT NULL,
mail varchar(255) default '' NOT NULL,
frequency int default '0' NOT NULL,
PRIMARY KEY (id_subscription)
);
