--procedure
create or replace function distribute_contribution(sponsorname text) 
returns void as $cont$
declare 
	s_contrib integer;
	s_sum integer;
	s_count integer;
	avg_rem_contrib integer;
	c1 cursor for select amount from sponsorships where sname = sponsorname;
	rec record;
begin
	select contrib into s_contrib from sponsor where sname=sponsorname;
	select sum(amount) into s_sum from sponsorships group by sname having sname=sponsorname;
	select count(amount) into s_count from sponsorships group by sname having sname=sponsorname;
	avg_rem_contrib := (s_contrib - s_sum)/s_count;
	
	open c1;
	loop
	fetch c1 into rec;
		exit when not found;
		update sponsorships set amount = amount + avg_rem_contrib where current of c1;
	end loop;
	close c1;
end;
$cont$ language plpgsql;
