/*Query 1*/
SELECT gname 
FROM guests g
WHERE g.gname NOT IN (SELECT gname FROM speakers
					  UNION 
					  SELECT gname FROM vendors
					  UNION
					  SELECT gname FROM performers);

/*Query 2*/
SELECT actname FROM panel
EXCEPT
SELECT actname FROM speakers
UNION
SELECT actname FROM market
EXCEPT
SELECT actname FROM vendors
UNION
SELECT actname FROM shows
EXCEPT
SELECT actname FROM performers;

/*Query 5*/
SELECT temp.actname, temp.sdate, temp.actstart
FROM	(SELECT DISTINCT a.actname, a.sdate, s.actstart
		FROM attendance a, schedule s
		WHERE  a.actname = s.actname AND a.sdate = s.sdate AND username = 'jatei04'
		ORDER BY a.actname, a.sdate, s.actstart) temp
ORDER BY temp.sdate, temp.actstart, temp.actname;

/*Query 6*/
select distinct liasons.sid, liasons.sname, schedule.actname, schedule.sdate
from liasons
inner join sponsorships on liasons.sname=sponsorships.sname and liasons.sid=944
inner join schedule on schedule.actname=sponsorships.actname
where schedule.actname not in (
	select schedule.actname
	from schedule
	where sid=944
);

/*Query 7*/
SELECT SUM(temp.difference) AS total_hours
FROM  staff st, (SELECT sid, actend - actstart AS difference
				 FROM schedule) temp
WHERE st.sid = 297 AND temp.sid = st.sid;

/*Query 8*/
select att_name, age, activity.actname, agelim
from attendee
inner join attendance on attendee.username=attendance.username and age<18
inner join activity on attendance.actname=activity.actname and activity.agelim>=18;