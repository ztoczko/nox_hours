-- CREATE DATABASE nox_hours CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci;

USE nox_hours;


INSERT INTO users VALUES(null, "2020-01-01 12:00:00", "zzzz@wp.pl", "aaa", false, false, "aaa", "$2y$10$Z8qBFhEayCt4PKqMTvezuezIgn0Zufw74QvIUXTCEEdIxJ.Q9qwcS", false, "aaa", "ARS", 1);
INSERT INTO clients VALUES(null, false, "2020-01-01 12:00:00", "aaa");
INSERT INTO clients VALUES(null, false, "2019-01-01 12:00:00", "bbb");
INSERT INTO clients VALUES(null, false, "2018-01-01 12:00:00", "ccc");
INSERT INTO clients VALUES(null, true, "2017-01-01 12:00:00", "ddd");
INSERT INTO clients VALUES(null, false, "2016-01-01 12:00:00", "eee");
INSERT INTO clients VALUES(null, false, "2020-01-01 12:00:00", "aaa");
INSERT INTO clients VALUES(null, false, "2019-01-01 12:00:00", "bbb");
INSERT INTO clients VALUES(null, false, "2018-01-01 12:00:00", "ccc");
INSERT INTO clients VALUES(null, true, "2017-01-01 12:00:00", "ddd");
INSERT INTO clients VALUES(null, false, "2016-01-01 12:00:00", "eee");
INSERT INTO clients VALUES(null, true, "2020-01-01 12:00:00", "aaa");
INSERT INTO clients VALUES(null, false, "2019-01-01 12:00:00", "bbb");
INSERT INTO clients VALUES(null, false, "2018-01-01 12:00:00", "ccc");
INSERT INTO clients VALUES(null, true, "2017-01-01 12:00:00", "ddd");
INSERT INTO clients VALUES(null, false, "2016-01-01 12:00:00", "eee");

INSERT INTO rates VALUES(null, 250.05, 360.60, "2020-01-01", "2020-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2019-01-01", "2019-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2018-01-01", "2018-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2017-01-01", "2017-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2016-01-01", "2016-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2015-01-01", "2015-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2014-01-01", "2014-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2013-01-01", "2013-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2012-01-01", "2012-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2011-01-01", "2011-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2010-01-01", "2010-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 250.05, 360.60, "2009-01-01", "2009-12-31", 500, 100.1, 1);
INSERT INTO rates VALUES(null, 350.05, 460.60, "2021-01-01", "2200-12-31", 600, 200.1, 1);