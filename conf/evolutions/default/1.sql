# --- !Ups
create table page_analytics(
   path char(255) not null,
   views int not null,
   unique_views int not null,
   avg_time_on_page decimal(10,2) not null,
   entrances int not null,
   exits int not null,
   date DATE not null,
   PRIMARY KEY (path, date)
);

# --- !Downs
drop table if exists page_analytics


