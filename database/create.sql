--create database if not exists `crawler`;
--use crawler;
create table t_crawler_cookies(host varchar(200) primary key,domian varchar(200),cookieName varchar(200),cookieValue varchar(300),isSecure bit,version int,path varchar(200),execDate timestamp);
create table t_crawler_task(crawlerId varchar(255),url varchar(255) primary key,host varchar(255),isDownload bit,statusCode int,retryCount int,lastModified varchar(100),httpMethod varchar(100),params varchar(255),execDate timestamp);
create table t_crawler_pages (url varchar(255) not null, html text not null,content text not null, sign varchar(255) not null, charset varchar(10), title varchar(100), keywords varchar(255), description varchar(255), isCreateIndex bit, execDate timestamp,primary key (url, sign));