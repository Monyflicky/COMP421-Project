alter table sponsorships add add constraint sponsorships_amount_check check (amount>0);

alter table guests add constraint speciality check 
(speciality = 'speaker' or 
speciality = 'vendor' or speciality = 'performer')