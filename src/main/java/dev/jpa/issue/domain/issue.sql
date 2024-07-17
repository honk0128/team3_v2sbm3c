SELECT issueno, title, content, cnt, rdate FROM issue WHERE rdate LIKE '%23/12/22%';

SELECT SUBSTR(rdate, 1, 10) FROM issue;

SELECT issueno, title, content, cnt, rdate 
FROM issue 
WHERE (SUBSTR(rdate, 1, 10) >= '2023-12-21') AND (SUBSTR(rdate, 1, 10) <= '2023-12-22');

SELECT issueno, title, content, cnt, rdate 
FROM issue 
ORDER BY rdate DESC;

SELECT issueno, title, content, cnt, rdate 
FROM issue 
WHERE (SUBSTR(rdate, 1, 10) >= '2023-12-23') AND (SUBSTR(rdate, 1, 10) <= '2023-12-24')
ORDER BY rdate DESC;

UPDATE issue SET title='서비스 오픈', content='사은품 증정' WHERE issueno=54;

UPDATE issue SET cnt = cnt + 1 WHERE issueno=54;

COMMIT;

-- 페이징: 최신글 500건
SELECT issueno, title, content, cnt, rdate, r
FROM (
      SELECT issueno, title, content, cnt, rdate, rownum as r
      FROM (
            SELECT issueno, title, content, cnt, rdate
            FROM issue     
            ORDER BY rdate DESC
      )
)
WHERE r <= 500;

SELECT issueno, title, content, cnt, rdate, r FROM ( SELECT issueno, title, content, cnt, rdate, rownum as r FROM ( SELECT issueno, title, content, cnt, rdate FROM issue ORDER BY rdate DESC )) WHERE r <= 500






