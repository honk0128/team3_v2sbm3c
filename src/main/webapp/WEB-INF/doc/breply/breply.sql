DROP TABLE breply;
CREATE TABLE BREPLY(
		BREPLYNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		BREPLYCONT                    		CLOB		 NOT NULL,
		BREPLYIMG                     		VARCHAR2(100)		 NULL ,
		BREPLYSAVED                   		VARCHAR2(100)		 NULL ,
		BREPLYTHUMB                   		VARCHAR2(100)		 NULL ,
		BREPLYSIZE                    		NUMBER(10)		 NULL ,
		BREPLYDATE                    		DATE		 NOT NULL,
		BREPLYPASSWD                  		VARCHAR2(200)		 NOT NULL,
		ACCOUNTNO                     		NUMBER(10)		 NULL ,
        MANAGERNO                     		NUMBER(10)		 NULL ,
		BOARDNO                       		NUMBER(10)		 NULL ,
  FOREIGN KEY (BOARDNO) REFERENCES BOARD (BOARDNO)
);

COMMENT ON TABLE BREPLY is '게시판댓글';
COMMENT ON COLUMN BREPLY.BREPLYNO is '게시판댓글번호';
COMMENT ON COLUMN BREPLY.BREPLYCONT is '게시판댓글내용';
COMMENT ON COLUMN BREPLY.BREPLYIMG is '댓글 이미지';
COMMENT ON COLUMN BREPLY.BREPLYSAVED is '저장된 댓글 이미지';
COMMENT ON COLUMN BREPLY.BREPLYTHUMB is '댓글 이미지 썸네일';
COMMENT ON COLUMN BREPLY.BREPLYSIZE is '댓글 파일 사이즈';
COMMENT ON COLUMN BREPLY.BREPLYDATE is '게시판댓글등록일';
COMMENT ON COLUMN BREPLY.BREPLYPASSWD is '게시판댓글비밀번호';
COMMENT ON COLUMN BREPLY.ACCOUNTNO is '유저 번호';
COMMENT ON COLUMN BREPLY.MANAGERNO is '관리자 번호';
COMMENT ON COLUMN BREPLY.BOARDNO is '게시판번호';

DROP SEQUENCE breply_seq;
CREATE SEQUENCE breply_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
INSERT INTO breply(BREPLYNO, BREPLYCONT, BREPLYIMG, BREPLYSAVED, BREPLYTHUMB, BREPLYSIZE, BREPLYDATE, BREPLYPASSWD)
VALUES(breply_seq.nextval, '안녕하세요', NULL, NULL, NULL, NULL, sysdate, 1234);

INSERT INTO breply(BREPLYNO, BREPLYCONT, BREPLYIMG, BREPLYSAVED, BREPLYTHUMB, BREPLYSIZE, BREPLYDATE, BREPLYPASSWD)
VALUES(breply_seq.nextval, '반갑습니다', NULL, NULL, NULL, NULL, sysdate, 1234);

INSERT INTO breply(BREPLYNO, BREPLYCONT, BREPLYIMG, BREPLYSAVED, BREPLYTHUMB, BREPLYSIZE, BREPLYDATE, BREPLYPASSWD)
VALUES(breply_seq.nextval, 'ㅎㅇ', NULL, NULL, NULL, NULL, sysdate, 1234);

SELECT BREPLYNO, BREPLYCONT, BREPLYIMG, BREPLYSAVED, BREPLYTHUMB, BREPLYSIZE, BREPLYDATE, BREPLYPASSWD
FROM breply
ORDER BY BREPLYNO ASC;

SELECT * FROM breply;

delete breply;

commit; 

UPDATE BREPLY
SET BREPLYCONT = '수정', BREPLYIMG = null, BREPLYSAVED = null, BREPLYTHUMB = null, BREPLYSIZE = null, BREPLYDATE = sysdate, BREPLYPASSWD = 5678
WHERE BREPLYNO = 6;


delete breply;

select * from loginlog;

delete loginlog;

commit;

SELECT COUNT(*) as cnt 
FROM BREPLY
WHERE breplyno = 39 AND BREPLYPASSWD = 'fS/kjO+fuEKk06Zl7VYMhg==';


/**********************************/
/* Table Name: 게시판 대댓글 */
/**********************************/
DROP TABLE BREREPLY;
CREATE TABLE BREREPLY(
		BREREPLY                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		BREREPLYCONT                  		CLOB		 NOT NULL,
		BREREPLYDATE                  		DATE		 NOT NULL,
		BREPLYNO                      		NUMBER(10)		 NULL ,
		ACCOUNTNO                     		NUMBER(10)		 NULL ,
		BREREPLYIMG                   		VARCHAR2(100)		 NULL ,
		BREREPLYSAVE                  		VARCHAR2(100)		 NULL ,
		BREREPLYTHUMB                 		VARCHAR2(100)		 NULL ,
		BREREPLYSIZE                  		NUMBER(10)		 NULL ,
  FOREIGN KEY (BREPLYNO) REFERENCES BREPLY (BREPLYNO),
  FOREIGN KEY (ACCOUNTNO) REFERENCES ACCOUNT (ACCOUNTNO)
);

COMMENT ON TABLE BREREPLY is '게시판 대댓글';
COMMENT ON COLUMN BREREPLY.BREREPLY is '대댓글번호';
COMMENT ON COLUMN BREREPLY.BREREPLYCONT is '게시판대댓글내용';
COMMENT ON COLUMN BREREPLY.BREREPLYDATE is '게시판대댓글등록일';
COMMENT ON COLUMN BREREPLY.BREPLYNO is '게시판댓글번호';
COMMENT ON COLUMN BREREPLY.ACCOUNTNO is '유저 번호';
COMMENT ON COLUMN BREREPLY.BREREPLYIMG is '대댓글 이미지';
COMMENT ON COLUMN BREREPLY.BREREPLYSAVE is '저장된 대댓글 이미지';
COMMENT ON COLUMN BREREPLY.BREREPLYTHUMB is '대댓글 이미지 썸네일';
COMMENT ON COLUMN BREREPLY.BREREPLYSIZE is '대댓글 파일 사이즈';

DROP SEQUENCE brereply_seq;
CREATE SEQUENCE brereply_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
select * from recommend;