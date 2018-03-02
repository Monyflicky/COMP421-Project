
----------------------------------- VIEW ONE ----------------------------------------

--This view is used to acess the query that returns the activities that attendies have signed up for and will
--be attending(continuation of the line before)

create view AttendeeSchedule (actname,sdate, actstart)
as select temp.actname, temp.sdate, temp.actstart
from (select distinct a.actname, a.sdate, s.actstart
		from attendance a, schedule s
		where  a.actname = s.actname and a.sdate = s.sdate and username = 'jatei04'
		order by a.actname, a.sdate, s.actstart) temp
order by temp.sdate, temp.actstart, temp.actname;


	
select actname, sdate, actstart
from AttendeeSchedule
where actstart in (select  actstart
                        from AttendeeSchedule
                        where  actstart > '09:00:00');

update AttendeeSchedule
set sdate = '2018-04-15'
where actame = 'Management in Analytics'

--This particular view is not updatable. The query execution of update to Tempta fails because views are not automatically updatable
--To enable updating the table it says we should provide and INSTEAD of update trigger or an unconditional on update do INSTEAD rule.
--The automatic update fails in PostgreSQL because you must tell PostgreSQL how you want the view to be updated





----------------------------------- VIEW TWO ----------------------------------------

--This view lists the attendees who are not old enough to comply with the age limit
-- of one or more activities which they are registered to as attending
CREATE VIEW UnderageAttendees (att_name, age, actname, agelim)
AS SELECT aee.att_name, aee.age, temp.actname, temp.agelim
FROM attendee aee, (SELECT aee.username, ance.actname, act.agelim, aee.age-act.agelim AS agediff
				   FROM attendee aee, attendance ance, activity act
				   WHERE aee.username = ance.username AND ance.actname = act.actname) temp
WHERE aee.username = temp.username AND temp.agediff < 0;


-- This query lists the attendees who are only a year too young to attend the activity they're registered for
SELECT att_name, age, agelim-age AS agediff
FROM UnderageAttendees
WHERE agelim-age = 1;


-- UDAPTE only works as is on the attendee table, in which case it DOES cascade to UnderageAttendees and then to the above query's result.
-- However, updating the view directly isn't straightforward in postgresql.
UPDATE attendee
SET att_name = 'Iliana Swanson', age = 20
WHERE username = 'wexiu39';
