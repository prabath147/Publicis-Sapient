-- liquibase formatted sql

--changeset adheshreghu:1
CREATE TABLE user_notifications(
    id int primary key,
    message varchar(200) not null,
    description varchar(255),
    status varchar(50) not null,
    severity varchar(50) not null,
    created_on date not null,
    user_id int not null
);

--changeset adheshreghu:2
insert into user_notifications (id,message,description,status,severity,created_on,user_id)
values
(2, 'Error in template upload', 'Data mismatch error in line 24,25 of template', 'UNSEEN','ERROR','2022-08-14T11:10:18.000+0000',1),
(3, 'Data duplicates', 'Row repeated at line 31,34', 'UNSEEN','WARNING','2022-08-18T11:10:18.000+0000',1);

--changeset adheshreghu:3
insert into user_notifications (id,message,description,status,severity,created_on,user_id)
values
(5, 'Error in template upload', 'Data mismatch error in line 20,23 of template', 'SEEN','ERROR','2022-09-21T11:10:18.000+0000',1),
(6, 'Data duplicates', 'Row repeated at line 30,43', 'UNSEEN','WARNING','2022-08-20T11:10:18.000+0000',1),
(7, 'Upload Successful', 'Upload of catalog template successful', 'UNSEEN','MESSAGE','2022-08-19T11:10:18.000+0000',1),
(8, 'Upload Successful', 'Upload of catalog template successful', 'SEEN','MESSAGE','2022-10-19T11:10:20.000+0000',1),
(9, 'Data duplicates', 'Row repeated at line 31,53', 'UNSEEN','WARNING','2022-08-19T11:10:18.000+0000',1),
(10, 'Error in template upload', 'Data mismatch error in line 19,20 of template', 'SEEN','ERROR','2022-11-21T11:10:18.000+0000',1);

--changeset adheshreghu:4
alter table user_notifications alter column description type varchar(15000);