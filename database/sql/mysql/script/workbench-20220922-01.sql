delete from menu_hotspot_history;
select * from menu_hotspot_history;


insert into menu_hotspot select USER_ID, MENU_ID, RECORDED_DATE, HOTSPOT from menu_hotspot_history;
select * from menu_hotspot where RECORDED_DATE < '2022/10/19';