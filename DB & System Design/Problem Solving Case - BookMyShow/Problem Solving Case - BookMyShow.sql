/* =========================================================
   BOOKMYSHOW – PROBLEM SOLVING CASE
   Covers: Schema + Inserts + Select Query
   Normalization: 1NF, 2NF, 3NF, BCNF
   ========================================================= */

/* =========================
   RESET SCHEMA – DROP TABLES
   ========================= */

DROP TABLE IF EXISTS show_timing;
DROP TABLE IF EXISTS show_;
DROP TABLE IF EXISTS format;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS screen;
DROP TABLE IF EXISTS theatre;
DROP TABLE IF EXISTS city;


/* =========================
   1. SCHEMA CREATION
   ========================= */

CREATE TABLE city (
    city_id INT PRIMARY KEY,
    city_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE theatre (
    theatre_id INT PRIMARY KEY,
    theatre_name VARCHAR(100) NOT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city(city_id)
);

CREATE TABLE screen (
    screen_id INT PRIMARY KEY,
    theatre_id INT NOT NULL,
    screen_number INT NOT NULL,
    FOREIGN KEY (theatre_id) REFERENCES theatre(theatre_id),
    UNIQUE (theatre_id, screen_number)
);

CREATE TABLE movie (
    movie_id INT PRIMARY KEY,
    movie_name VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL,
    language VARCHAR(30),
    certificate VARCHAR(10)
);

CREATE TABLE format (
    format_id INT PRIMARY KEY,
    format_name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE show_ (
    show_id INT PRIMARY KEY,
    movie_id INT NOT NULL,
    screen_id INT NOT NULL,
    format_id INT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (screen_id) REFERENCES screen(screen_id),
    FOREIGN KEY (format_id) REFERENCES format(format_id),
    UNIQUE (movie_id, screen_id, format_id)
);

CREATE TABLE show_timing (
    show_timing_id INT PRIMARY KEY,
    show_id INT NOT NULL,
    show_date DATE NOT NULL,
    show_time TIME NOT NULL,
    FOREIGN KEY (show_id) REFERENCES show_(show_id),
    UNIQUE (show_id, show_date, show_time)
);


/* =========================
   2. MASTER DATA INSERTS
   ========================= */

INSERT INTO city VALUES
(1, 'Mumbai');

INSERT INTO theatre VALUES
(1, 'PVR Nexus Mall', 1),
(2, 'INOX R City Mall', 1),
(3, 'Cinepolis Andheri', 1);

INSERT INTO screen VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 2, 1),
(5, 2, 2),
(6, 3, 1),
(7, 3, 2);

INSERT INTO movie VALUES
(1, 'Dunki', 160, 'Hindi', 'UA'),
(2, 'Fighter', 165, 'Hindi', 'UA'),
(3, 'Animal', 201, 'Hindi', 'A'),
(4, 'Avatar: The Way of Water', 192, 'English', 'UA'),
(5, 'Salaar', 175, 'Telugu', 'A'),
(6, 'Tiger 3', 155, 'Hindi', 'UA'),
(7, 'Oppenheimer', 180, 'English', 'R'),
(8, 'Pushpa 2', 190, 'Telugu', 'UA');

INSERT INTO format VALUES
(1, '2D'),
(2, '3D'),
(3, 'IMAX');

INSERT INTO show_ VALUES
(1, 1, 1, 1),
(2, 2, 2, 1),
(3, 3, 3, 1),
(4, 4, 2, 2),
(5, 4, 1, 3),
(6, 5, 4, 1), -- Salaar, INOX Screen 1, 2D
(7, 6, 5, 1), -- Tiger 3, INOX Screen 2, 2D
(8, 7, 6, 2), -- Oppenheimer, Cinepolis Screen 1, 3D
(9, 8, 7, 1), -- Pushpa 2, Cinepolis Screen 2, 2D
(10, 7, 4, 3); -- Oppenheimer, INOX Screen 1, IMAX


/* =========================
   3. SHOW TIMINGS – 6 DAYS
   ========================= */

-- Day 1 : 2026-01-18
INSERT INTO show_timing VALUES
(1, 1, '2026-01-18', '10:00'),
(2, 1, '2026-01-18', '13:30'),
(3, 2, '2026-01-18', '11:00'),
(4, 2, '2026-01-18', '15:00'),
(5, 3, '2026-01-18', '18:00'),
(6, 4, '2026-01-18', '14:00'),
(7, 5, '2026-01-18', '21:00');

INSERT INTO show_timing VALUES
(46, 8, '2026-01-18', '12:00'),
(47, 8, '2026-01-18', '19:00'),
(48, 9, '2026-01-18', '15:00'),
(49, 9, '2026-01-18', '21:30');


INSERT INTO show_timing VALUES
(38, 6, '2026-01-18', '10:15'),
(39, 6, '2026-01-18', '14:00'),
(40, 7, '2026-01-18', '11:30'),
(41, 7, '2026-01-18', '18:30'),
(42, 10, '2026-01-18', '21:45');


-- Day 2 : 2026-01-19
INSERT INTO show_timing VALUES
(8, 1, '2026-01-19', '10:30'),
(9, 1, '2026-01-19', '14:00'),
(10, 2, '2026-01-19', '11:30'),
(11, 3, '2026-01-19', '18:30'),
(12, 4, '2026-01-19', '15:00'),
(13, 5, '2026-01-19', '21:30');

INSERT INTO show_timing VALUES
(43, 6, '2026-01-19', '10:00'),
(44, 7, '2026-01-19', '15:30'),
(45, 10, '2026-01-19', '21:00');


-- Day 3 : 2026-01-20
INSERT INTO show_timing VALUES
(14, 1, '2026-01-20', '10:00'),
(15, 2, '2026-01-20', '11:00'),
(16, 2, '2026-01-20', '16:00'),
(17, 3, '2026-01-20', '19:00'),
(18, 4, '2026-01-20', '14:30'),
(19, 5, '2026-01-20', '22:00');

INSERT INTO show_timing VALUES
(50, 8, '2026-01-20', '11:30'),
(51, 8, '2026-01-20', '18:45'),
(52, 9, '2026-01-20', '22:00');


-- Day 4 : 2026-01-21
INSERT INTO show_timing VALUES
(20, 1, '2026-01-21', '11:00'),
(21, 1, '2026-01-21', '14:30'),
(22, 2, '2026-01-21', '12:00'),
(23, 3, '2026-01-21', '19:30'),
(24, 4, '2026-01-21', '16:00'),
(25, 5, '2026-01-21', '21:45');

-- Day 5 : 2026-01-22
INSERT INTO show_timing VALUES
(26, 1, '2026-01-22', '10:15'),
(27, 2, '2026-01-22', '11:45'),
(28, 2, '2026-01-22', '17:00'),
(29, 3, '2026-01-22', '20:00'),
(30, 4, '2026-01-22', '15:30'),
(31, 5, '2026-01-22', '22:15');

-- Day 6 : 2026-01-23
INSERT INTO show_timing VALUES
(32, 1, '2026-01-23', '10:00'),
(33, 1, '2026-01-23', '13:45'),
(34, 2, '2026-01-23', '11:30'),
(35, 3, '2026-01-23', '18:45'),
(36, 4, '2026-01-23', '14:00'),
(37, 5, '2026-01-23', '21:30');


/* =========================
   4. P2 – REQUIRED QUERY
   ========================= */

-- List all shows for a given theatre on a given date

SELECT
    m.movie_name,
    f.format_name,
    sc.screen_number,
    st.show_date,
    st.show_time
FROM show_timing st
JOIN show_ sh ON st.show_id = sh.show_id
JOIN movie m ON sh.movie_id = m.movie_id
JOIN format f ON sh.format_id = f.format_id
JOIN screen sc ON sh.screen_id = sc.screen_id
JOIN theatre t ON sc.theatre_id = t.theatre_id
WHERE t.theatre_name = 'Cinepolis Andheri'
  AND st.show_date = '2026-01-18'
ORDER BY m.movie_name, st.show_time;
