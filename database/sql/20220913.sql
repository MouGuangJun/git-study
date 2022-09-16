SELECT * FROM user_back;
DELETE FROM user_back;

SELECT * FROM batch_job_execution WHERE JOB_INSTANCE_ID='67';

SELECT * FROM batch_job_instance ORDER BY JOB_INSTANCE_ID DESC;

SELECT * FROM batch_step_execution WHERE JOB_EXECUTION_ID = '188';

SELECT * FROM scenario_model;

SELECT * FROM scenario_relative;

SELECT * FROM scenario_model;


SELECT
  sm.MODEL_ID AS modelId,
  sm.MODEL_NAME AS modelName,
  sm.SCRIPT_TYPE AS scriptType,
  sm.EXECUTE_SCRIPT AS executeScript,
  sm.RUN_CONDITION AS runCondition,
  sm.STATUS AS STATUS,
  sm.PASS_MESSAGE AS passMessage,
  sm.NO_PASS_MESSAGE AS noPassMessage,
  sm.NO_PASS_DEAL AS noPassDeal,
  sm.REMARK AS remark
FROM
  SCENARIO_MODEL sm,
  SCENARIO_RELATIVE sr
WHERE sr.MODEL_ID = sm.MODEL_ID
  AND sr.SCENARIO_ID = '001'
  AND sr.GROUP_ID = '001010'
  AND sm.STATUS = '1'
ORDER BY sr.SCENARIO_ID,
  sr.GROUP_ID,
  sr.MODEL_ID