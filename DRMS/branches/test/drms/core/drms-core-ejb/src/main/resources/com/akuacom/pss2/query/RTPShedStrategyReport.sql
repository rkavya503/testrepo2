
select pclient.participantName as client, pp.programName as program, if (pp.state=1, 'Yes', 'No') as enroll,
pparent.participantName as participant, pparent.account, parent_part.participantName as parent, 
	prs.veryHostSummerWeekday,
	prs.moderateSummerWeekday,
	prs.mildSummerWeekday,
	prs.lowCostWinterWeekday,
	prs.lowCostWeekend,
	prs.hotSummerWeekday,
	prs.highCostWinterWeekday,
	prs.extremelyHotSummerWeekday,
	prs.highCostWeekend

from program_participant pp inner join participant pclient on 
  pp.participant_uuid=pclient.uuid and pclient.client=1 [and pp.programName=${programName}] 
inner join participant pparent
  on pclient.parent=pparent.participantName and pparent.client=0 
left join program_participant parent_pp on
  parent_pp.programName=pp.programName and parent_pp.participant_uuid =pparent.uuid
left join program_participant pp_parent on
  pp_parent.uuid=parent_pp.parent_uuid
left join participant parent_part on
  pp_parent.participant_uuid=parent_part.uuid
inner join (  
	SELECT 
		program_participant_uuid,
		MAX(v1) AS veryHostSummerWeekday,
		MAX( v2) AS moderateSummerWeekday,
		MAX(v3) AS mildSummerWeekday,
		MAX(v4) AS lowCostWinterWeekday,
		MAX(v5) AS lowCostWeekend,
		MAX(v6) AS hotSummerWeekday,
		MAX(v7) AS highCostWinterWeekday,
		MAX(v8) AS extremelyHotSummerWeekday,
		MAX(v9) AS highCostWeekend 
	 FROM 
		(SELECT  
			program_participant_uuid,
			GROUP_CONCAT(IF(name = 'VERY HOT SUMMER WEEKDAY', VALUE, NULL)) AS v1, 
			GROUP_CONCAT(IF(name = 'MODERATE SUMMER WEEKDAY', VALUE, NULL)) AS v2, 
			GROUP_CONCAT(IF(name = 'MILD SUMMER WEEKDAY', VALUE, NULL)) AS v3, 
			GROUP_CONCAT(IF(name = 'LOW COST WINTER WEEKDAY', VALUE, NULL)) AS v4, 
			GROUP_CONCAT(IF(name = 'LOW COST WEEKEND', VALUE, NULL)) AS v5, 
			GROUP_CONCAT(IF(name = 'HOT SUMMER WEEKDAY', VALUE, NULL)) AS v6, 
			GROUP_CONCAT(IF(name = 'HIGH COST WINTER WEEKDAY', VALUE, NULL)) AS v7, 
			GROUP_CONCAT(IF(name = 'EXTREMELY HOT SUMMER WEEKDAY', VALUE, NULL)) AS v8, 
			GROUP_CONCAT(IF(name = 'HIGH COST WEEKEND', VALUE, NULL)) AS v9 
		FROM (
			SELECT program_participant_uuid,name, CONCAT(VALUE0,',',VALUE1,',',VALUE2,',',VALUE3,',',VALUE4,',',VALUE5,',',VALUE6,',',VALUE7,',',VALUE8,',',VALUE9,',',VALUE10,',',VALUE11,',',VALUE12
					,',',VALUE13,',',VALUE14,',',VALUE15,',',VALUE16,',',VALUE17,',',VALUE18,',',VALUE19,',',VALUE20,',',VALUE21,',',VALUE22,',',VALUE23) AS VALUE
			FROM participant_rtp_strategy 
			) temp GROUP BY program_participant_uuid,name
		) AS result GROUP BY program_participant_uuid
	) as prs on prs.program_participant_uuid=pp.uuid 
order by client, program

