SELECT * FROM user_back;
DELETE FROM user_back;

SELECT * FROM batch_job_execution WHERE JOB_INSTANCE_ID='67';

SELECT * FROM batch_job_instance ORDER BY JOB_INSTANCE_ID DESC;

SELECT * FROM batch_step_execution WHERE JOB_EXECUTION_ID = '188';