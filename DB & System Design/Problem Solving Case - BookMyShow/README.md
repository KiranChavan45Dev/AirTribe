# BookMyShow - Database Design Case Study

## Overview
This project implements a **relational database design** for a simplified version of the **BookMyShow ticket booking platform**.

The design is based on a UI where:
- A user selects a **theatre**
- Sees the **next available dates**
- Selects a date to view **movies and their show timings**

The solution focuses on **schema design, normalization, and SQL queries**, not UI or backend code.

---

## Problem Statement
As part of the assignment:

### P1
- Identify all required entities
- Define attributes and relationships
- Create SQL tables
- Insert sample data
- Ensure normalization up to **BCNF**

### P2
- Write a SQL query to list **all shows on a given date at a given theatre**, along with show timings

---

## Database Design

### Core Entities
- City
- Theatre
- Screen
- Movie
- Format (2D / 3D / IMAX)
- Show
- Show_Timing

### Key Design Highlights
- One theatre has multiple screens
- A movie can run on multiple screens and formats
- A show can have multiple timings on different dates
- Dates are handled via `show_timing` (frontend calculates next 7 days)

---

## Normalization
The schema follows:

- **1NF**: Atomic attributes, no repeating groups
- **2NF**: No partial dependency
- **3NF**: No transitive dependency
- **BCNF**: All determinants are candidate keys

---

## SQL File Contents

The single SQL file includes:

1. **Schema reset**
   - `DROP TABLE IF EXISTS` in correct dependency order
2. **Schema creation**
   - Tables with PKs, FKs, and unique constraints
3. **Sample data**
   - Multiple theatres
   - Multiple screens
   - Latest movies
   - 6 days of show timings
4. **Query**
   - Fetch shows for a given theatre and date

---

## Sample Query (P2 Requirement)

```sql
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
WHERE t.theatre_name = 'PVR Nexus Mall'
  AND st.show_date = '2026-01-18'
ORDER BY m.movie_name, st.show_time;
