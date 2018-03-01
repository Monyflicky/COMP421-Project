/* Mod 1*/
delete from activity 
where actname in (
select actname from panel 
where duration>'08:00'
union 
select actname from shows
where duration>'08:00'
)

/*Mod 2*/
insert into rentals
select A.eq_id,B.actname 
from(
    SELECT eq_id,row_number() over (order by eq_id) as row_num
    FROM equipment
    where eq_id not in (
		select eq_id from rentals) and eq_name='Speaker')A
join
    (SELECT actname,row_number() over (order by actname) as row_num
    FROM rentals
    where actname not in (
		select actname from rentals
		where eq_id like 'SP%') and eq_id like 'MC%')B
on  A.row_num=B.row_num
ORDER BY A.eq_id,B.actname


/*Mod 3*/
update sponsor
set contrib= (
select SUM(amount)
from sponsorships
group by sname
having sname='MicroHard'
) where sname='MicroHard'

/*Mod 4*/
delete from schedule 
where actname in (
	select schedule.actname from schedule
	inner join (
		select * from performers
		where actname not in (
			select actname
			from performers
			group by actname
			having count(actname) > 1
		) and gname='Barclay Fowler'
		union
		select * from speakers
		where actname not in (
			select actname
			from speakers
			group by actname
			having count(actname) > 1
		) and gname='Barclay Fowler'
	) as guestactivity
	on schedule.actname = guestactivity.actname
)