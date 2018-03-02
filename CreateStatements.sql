create table room (
roomno varchar,
capacity int,
CONSTRAINT valid_room CHECK (roomno ~* '^(N|S|W|E).*'),
primary key(roomno)
);

create table equipment (
eq_name varchar, 
eq_id varchar,
primary key(eq_id)
);

create table attendee (
att_name varchar, 
age integer,
address varchar,
city varchar,
province varchar,
a_phone varchar,
email varchar, 
username varchar,
primary key(username)
);

create table rentals(
eq_id varchar,
actname varchar not null,
primary key(eq_id),
foreign key(eq_id) references equipment(eq_id),
foreign key(actname) references activity(actname) ON UPDATE CASCADE ON DELETE CASCADE
);

create table sponsorships (
actname varchar,
sname varchar,
amount integer not null,
primary key(actname),
foreign key(actname) references activity(actname) ON UPDATE CASCADE ON DELETE CASCADE,
foreign key(sname) references sponsor(sname),
);

create table sponsor(
	sname VARCHAR not null,
	contrib integer not null,
	primary key (sname)	
);

create table staff(
	sid integer not null,
	stname varchar not null,
	role varchar,
	hours integer not null,
	primary key(sid)
);

create table guests(
	gname varchar not null,
	g_phone varchar,
	country varchar,
	speciality varchar not null, 
	primary key(gname, speciality)
);

create table schedactivity(
	sdate date not null,
	actname varchar not null, 
	foreign key (actname) references activity(actname) ON UPDATE CASCADE ON DELETE CASCADE,
	primary key(sdate, actname)
);

create table schedule(
	sid integer not null,
	roomno varchar not null,
	sdate date not null,
	actname varchar not null,
	actstart time not null,
	foreign key (sid) references staff(sid),
	foreign key (roomno) references room(roomno),
	foreign key (sdate, actname) references schedactivity(sdate, actname) ON UPDATE CASCADE ON DELETE CASCADE,
	primary key(sid, roomno, sdate, actname)
);

create table attendance(
	username varchar,
	sdate date,
	actname varchar, 
	foreign key (username) references attendee(username),
	foreign key (sdate, actname) references schedactivity(sdate, actname) ON UPDATE CASCADE ON DELETE CASCADE,
	primary key(username, sdate, actname)
);
