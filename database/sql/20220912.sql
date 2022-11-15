SELECT
  *
FROM
  sp_permission_api AS api
  LEFT JOIN sp_permission AS main
    ON main.ps_id = api.ps_id
WHERE main.ps_id IS NOT NULL



SELECT * FROM sp_permission_api;

SELECT * FROM sp_permission;