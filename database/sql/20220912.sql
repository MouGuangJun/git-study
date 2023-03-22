SELECT * FROM sp_user;


SELECT * FROM sp_manager;


select * from user_info;

select * from acct_transaction;


select fo.*
  from flow_object fo, acct_transaction at, acct_loan al
 where al.serialno = at.relativeobjectno(+)
   and at.serialno = fo.objectno
   and fo.objecttype = 'TransactionApply'
   and fo.objectno = at.serialno
   and fo.applytype = 'TransactionApply'
   and fo.phasetype = '1010'
   and at.transcode in ('',
                        '2001',
                        '2002',
                        '2004',
                        '3006',
                        '3007',
                        '2005',
                        '2006',
                        '9997',
                        '3010');

