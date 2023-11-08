CREATE database student;

create table Courses(
    CourseCode VARCHAR(10) constraint pk_course primary key,
    Name VARCHAR(255) Not NULL,
    DurationInDays int,
    Fee DECIMAL(10,2)

)

CREATE TABLE Batches(
    BatchId int identity (100,1) constraint pk_batch primary key,
    CourseCode VARCHAR(10),
    StartDate Date,
    EndDate Date,
    Timings VARCHAR(255),
    DurationInDays int,
    Fee DECIMAL(10,2),
    FOREIGN KEY (CourseCode) REFERENCES Courses(CourseCode) ON DELETE CASCADE
)

create table Students(
    StudentId int identity (100,1) constraint pk_student primary key,
    Name varchar(255) NOT NULL,
    Email VARCHAR(255),
    Mobile VARCHAR(15),
    BatchId INT,
    DateOfJoining date,
    FOREIGN KEY (BatchId) REFERENCES Batches(BatchId) ON DELETE CASCADE

)

create table Payments(
    PaymentId INT identity (100,1) constraint payment_pk primary key,
    StudentId int UNIQUE,
    Amount DECIMAL(10,2) NOT NULL,
    PayDate DATE Not NULL,
    PayMode CHAR(1)NOT NULL,
    FOREIGN KEY (StudentId) REFERENCES Students(StudentId) ON DELETE CASCADE,
    CONSTRAINT CHK_PayMode CHECK (PayMode IN ('U', 'N', 'C'))

)

INSERT into Courses VALUES
('JAVA123','Java',60,12000.00),
('PYTHON45','Python',50,10000.00),
('SPRING66','Spring Boot',40,20000.00),
('SQL89','SQL',20,7000.00),
('C55','C',15,6000.00)


insert into Batches values
('C55','2023-10-10','2023-10-25','Mon/Wed 1 pm - 3 pm',15,6000.00),
('JAVA123','2023-8-5','2023-10-5','Tues/Fri 9 am - 12 pm',60,12000.00),
('PYTHON45','2023-6-1','2023-7-20','Mon/Wed 10 am - 11 am',50,10000.00),
('SPRING66','2023-4-5','2023-5-15','Wed/Fri 10 am - 12 pm',40,20000.00),
('SQL89','2023-3-12','2023-4-2','Fri/Sat 5 pm - 6 pm',20,7000.00)


insert into students values
('Ramya','ramya@gmail.com','9989357637',108,'2023-04-05'),
('Bhuvana','bhuvana@gmail.com','9023456783',107,'2023-06-01'),
('Charitha','charitha@gmail.com','8092347891',105,'2023-10-10'),
('Lahari','lahari@gmail.com','9023782901',106,'2023-08-05'),
('Sai','sai@gmail.com','9390621363',110,'2023-03-12')



insert into Payments values
(100,20000.00,'2023-06-05','U'),
(101,10000.00,'2023-08-01','C'),
(104,7000.00,'2023-10-12','N'),
(102,'6000.00','2023-10-10','C'),
(103,12000.00,'2023-10-05','N')

select *from courses

select *from batches

select *from students

select *from payments

insert into batches VALUES
('C55','2023-10-11','2023-11-26','Tues/Thurs 9 am-11 am',15,6000.00),
('JAVA123','2023-10-10','2023-12-10','Wed/Fri 10 am - 1 pm',60,12000.00),
('SQL89','2023-4-05','2023-04-24','Mon/Tues 3 pm - 4 pm',20,7000.00),
('PYTHON45','2023-07-25','2023-09-15','Thurs/Sat 2 pm - 3 pm',50,10000.00),
('SPRING66','2023-05-20','2023-06-30','Mon/Wed 11 am -1 pm',40,20000.00)

insert into students VALUES
('Harini','harini@gmail.com','7980345672',115,'2023-07-25'),
('Vinay','vinay@gmail.com','7901533660',110,'2023-03-12'),
('Anu','anu@gmail.com','90142345487',114,'2023-04-05'),
('Sruthi','sruthi@gmail.com','9012873650',110,'2023-03-12'),
('Meghana','meghana@gmail.com','9982374959',116,'2023-05-20')

insert into payments VALUES
(107,7000.00,'2023-03-14','U'),
(106,7000.00,'2023-03-12','C'),
(109,20000.00,'2023-05-22','N'),
(105,10000.00,'2023-07-26','N'),
(108,7000.00,'2023-03-15','C')


SELECT * FROM Students WHERE BatchId IN (SELECT BatchId FROM Batches WHERE CourseCode = 'SQL89')

select *from courses
select * from batches
select *from students
select *from payments

insert into students values('cherry','cherry@gmail.com',8902369083,113,'2023-10-10')

insert into payments values(124,12000,'2023-10-10','C')


select *from students join batches b on b.CourseCode='java123' where StartDate='2023-08-05'

select * from batches where startdate='2023-01-12' and enddate ='2023-11-12'

