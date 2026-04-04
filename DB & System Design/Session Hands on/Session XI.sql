SELECT 
    f.title AS film_title,
    c.name AS category_name
FROM film AS f
JOIN film_category AS fc
    ON f.film_id = fc.film_id
JOIN category AS c
    ON fc.category_id = c.category_id
ORDER BY f.title, c.name;

SELECT
    s.first_name,
    s.last_name,
    a.address
FROM staff AS s
JOIN store AS st
    ON s.store_id = st.store_id
JOIN address AS a
    ON st.address_id = a.address_id;


SELECT DISTINCT
    f.title AS movie_title,
    CONCAT(c1.first_name, ' ', c1.last_name) AS customer_1,
    CONCAT(c2.first_name, ' ', c2.last_name) AS customer_2,
    ci.city
FROM customer c1
JOIN customer c2
    ON c1.customer_id < c2.customer_id
JOIN address a1 ON c1.address_id = a1.address_id
JOIN address a2 ON c2.address_id = a2.address_id
JOIN city ci
    ON a1.city_id = ci.city_id
   AND a2.city_id = ci.city_id
JOIN rental r1 ON c1.customer_id = r1.customer_id
JOIN rental r2 ON c2.customer_id = r2.customer_id
JOIN inventory i1 ON r1.inventory_id = i1.inventory_id
JOIN inventory i2 ON r2.inventory_id = i2.inventory_id
JOIN film f
    ON i1.film_id = f.film_id
   AND i2.film_id = f.film_id
ORDER BY f.title, ci.city;


-- Below is **brainstorming / thought-process style guidance** for this requirement — **not just the final SQL**, but *how to think about it in exams, interviews, or design*.

-- ---

-- ## Problem statement (rephrased)

-- > Find **movies** that were **rented by two different customers** who **live in the same city**, and display:

-- * Movie title
-- * Name of both customers

-- ---

-- ## 1. Identify the core entities involved

-- Ask yourself: *What nouns are in the question?*

-- * **Movie** → `film`
-- * **Rented** → `rental` + `inventory`
-- * **Customer** → `customer`
-- * **Same city** → `address` → `city`

-- So immediately you know:

-- > ❗ This is a **multi-table + self-join problem**

-- ---

-- ## 2. Understand the relationships (very important)

-- ### Movie rental flow in Sakila

-- ```
-- film → inventory → rental → customer → address → city
-- ```

-- ### Customer relationship

-- You need **two customers**, so:

-- * Same table
-- * Different rows
-- * Related by **city**

-- 👉 This screams **SELF JOIN on customer**

-- ---

-- ## 3. Why self-join is unavoidable here

-- You are comparing:

-- * Customer A
-- * Customer B

-- From the **same table**, under conditions:

-- * Same city
-- * Rented the same movie
-- * Not the same customer

-- That is the textbook definition of a self-join.

-- ---

-- ## 4. Break the problem into logical conditions

-- ### Condition 1: Two different customers

-- ```sql
-- c1.customer_id <> c2.customer_id
-- ```

-- Better (to avoid duplicates):

-- ```sql
-- c1.customer_id < c2.customer_id
-- ```

-- ---

-- ### Condition 2: Same city

-- Customers don’t store city directly:

-- ```
-- customer → address → city
-- ```

-- So:

-- * Join address twice
-- * Ensure both city IDs are equal

-- ```sql
-- a1.city_id = a2.city_id
-- ```

-- ---

-- ### Condition 3: Same movie rented

-- Customers don’t rent films directly:

-- ```
-- customer → rental → inventory → film
-- ```

-- So:

-- * Each customer has their own rental chain
-- * Inventory items differ
-- * Film ID must match

-- ```sql
-- i1.film_id = i2.film_id
-- ```

-- ---

-- ## 5. Why duplicates are a problem

-- Without care, you’ll get:

-- * A–B and B–A
-- * Same pair repeated due to multiple rentals

-- Solutions:

-- * `c1.customer_id < c2.customer_id`
-- * `DISTINCT`
-- * Or grouping (advanced)

-- ---

-- ## 6. Join map (mental diagram)

-- ```
-- customer c1 ─┐
--              ├─ address a1 ─┐
-- customer c2 ─┘               ├─ city (same)
--              ├─ address a2 ─┘

-- c1 ─ rental r1 ─ inventory i1 ─┐
--                                 ├─ film (same)
-- c2 ─ rental r2 ─ inventory i2 ─┘
-- ```

-- If you can draw this in your head → the query writes itself.

-- ---

-- ## 7. What this question is REALLY testing

-- This is not just SQL syntax.

-- It tests:

-- * ✅ Understanding of **normalization**
-- * ✅ Many-to-many relationships
-- * ✅ Self-joins
-- * ✅ De-duplication logic
-- * ✅ Real-world data modeling

-- This is a **mid-to-advanced SQL interview question**.

-- ---

-- ## 8. Common mistakes students make

-- ❌ Forgetting `inventory`
-- ❌ Joining `film` directly to `rental`
-- ❌ Not preventing self-pairing
-- ❌ Not handling duplicate customer pairs
-- ❌ Using GROUP BY incorrectly

-- ---

-- ## 9. Variations they might ask next

-- Be prepared for:

-- * Movies rented by **customers from the same country**
-- * Movies rented by **more than 2 people**
-- * Movies rented by **exactly 2 people**
-- * Show **count of customers per movie**
-- * Show only **currently active customers**

-- ---




SELECT
	c1.customer_id as customer_1_customer_id,
    c1.first_name AS customer_1_first_name,
    c2.customer_id as customer_2_customer_id,
    c2.first_name AS customer_2_first_name,
    c1.last_name
FROM customer c1
JOIN customer c2
    ON c1.last_name = c2.last_name
   AND c1.customer_id < c2.customer_id
ORDER BY c1.last_name;



