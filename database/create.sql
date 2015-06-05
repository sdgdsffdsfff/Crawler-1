create database crawler;
use crawler;

create table t_crawler_cookies(domian varchar(200) primary key,cookieName varchar(200),cookieValue varchar(300),isSecure bit,version int,path varchar(200),execDate timestamp);
create table t_crawler_task(crawlerId varchar(255) primary key,url varchar(255),host varchar(255),isVisited bit,isDownload bit,statusCode int,timeout int,lastModified varchar(100),requestMethod int,httpParams varchar(255),execDate timestamp);
create table t_crawler_web_page (url varchar(255) not null, contentMD5 varchar(255) not null, charset varchar(255), content varchar(255), createIndex bit not null, dataLength int not null, description varchar(255), execDate datetime, html varchar(255), keywords varchar(255), lastModified varchar(255), title varchar(255), PRIMARY KEY (url, contentMD5)) ENGINE=InnoDB DEFAULT CHARSET=utf8;