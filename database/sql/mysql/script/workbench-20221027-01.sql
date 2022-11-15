select
        USER_ID as userId,
        MENU_ID as menuId,
        SUM(HOTSPOT) as hotspot
        from
        MENU_HOTSPOT
        where
        RECORDED_DATE > '2022/10/26'
        group by
        USER_ID,
        MENU_ID;
        
select * from MENU_HOTSPOT;