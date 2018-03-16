--Trigger
create trigger check_staff_hours
before insert on schedule
for each row execute procedure check_hours();

create function check_hours()
returns trigger as
$func$
begin
	if(
		(SELECT SUM(temp.difference) AS total_hours
		FROM staff st, (SELECT sid, actend - actstart AS difference FROM schedule) temp
		WHERE st.sid = new.sid AND temp.sid = st.sid) + (new.actend - new.actstart) 
			   > ((select hours from staff where sid=new.sid)* interval '1 hour')
	) then
		raise exception 'The staff member has exceeded his total hours!';
	end if;
	return new;
end 
$func$ language plpgsql;