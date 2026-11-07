-- Fills the transactions table with 50,000 sample rows (1000 accounts)
-- so we can see how queries behave with real amounts of data.
SET SESSION cte_max_recursion_depth = 100000;

INSERT INTO transactions (account_id, amount, currency, country, occurred_at)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 50000
)
SELECT CONCAT('acc-', n % 1000),
       10.00,
       'CAD',
       'CA',
       TIMESTAMP('2026-11-01 00:00:00') + INTERVAL n MINUTE
FROM seq;