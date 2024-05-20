CREATE TABLE AI_SEARCH(
		SEARCHNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		IMG_SEARCH                    		VARCHAR2(100)		 NULL ,
		IMG_SEARCH_SAVE               		VARCHAR2(100)		 NULL ,
		IMG_SEARCH_THUMB              		VARCHAR2(100)		 NULL ,
		IMG_SEARCH_SIZE               		NUMBER(10)		 NULL ,
		TEXT_SEARCH                   		CLOB		 NULL ,
		ACCOUNTNO                     		NUMBER(10)		 NULL ,
		ANSWERNO                      		NUMBER(10)		 NULL ,
  FOREIGN KEY (ACCOUNTNO) REFERENCES ACCOUNT (ACCOUNTNO)
);

COMMENT ON TABLE AI_SEARCH is 'AI �˻�';
COMMENT ON COLUMN AI_SEARCH.SEARCHNO is '�˻� ��ȣ';
COMMENT ON COLUMN AI_SEARCH.IMG_SEARCH is '�˻� �̹���';
COMMENT ON COLUMN AI_SEARCH.IMG_SEARCH_SAVE is '����� �˻� �̹���';
COMMENT ON COLUMN AI_SEARCH.IMG_SEARCH_THUMB is '�˻� �̹��� �����';
COMMENT ON COLUMN AI_SEARCH.IMG_SEARCH_SIZE is '�˻� �̹��� ������';
COMMENT ON COLUMN AI_SEARCH.TEXT_SEARCH is '�˻� ����';
COMMENT ON COLUMN AI_SEARCH.ACCOUNTNO is '���� ��ȣ';
COMMENT ON COLUMN AI_SEARCH.ANSWERNO is '�亯 ��ȣ';

DROP SEQUENCE AI_SEARCH_SEQ;

CREATE SEQUENCE AI_SEARCH_SEQ
    START WITH 1         -- ���� ��ȣ
    INCREMENT BY 1       -- ������
    MAXVALUE 9999999999  -- �ִ밪: 9999999999 --> NUMBER(10) ����
    CACHE 2              -- 2���� �޸𸮿����� ���
    NOCYCLE;             -- �ٽ� 1���� �����Ǵ� ���� ����
INSERT INTO AI_SEARCH (SEARCHNO, IMG_SEARCH, IMG_SEARCH_SAVE, IMG_SEARCH_THUMB, IMG_SEARCH_SIZE, TEXT_SEARCH, ACCOUNTNO, ANSWERNO)
VALUES (AI_SEARCH_SEQ.NEXTVAL, '�̹���1.jpg', '�̹���1_saved.jpg', '�̹���1_thumb.jpg', 1024, '�ؽ�Ʈ �˻� ���� 1', 1, 1);


/**********************************/
/* Table Name: AI �亯 */
/**********************************/

CREATE TABLE AI_ANSWER(
		ANSWERNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		TEXT_ANSWER                   		CLOB		 NULL ,
		SEARCHNO                      		NUMBER(10)		 NULL ,
  FOREIGN KEY (SEARCHNO) REFERENCES AI_SEARCH (SEARCHNO)
);

COMMENT ON TABLE AI_ANSWER is 'AI �亯';
COMMENT ON COLUMN AI_ANSWER.ANSWERNO is '�亯 ��ȣ';
COMMENT ON COLUMN AI_ANSWER.TEXT_ANSWER is '�亯 ����';
COMMENT ON COLUMN AI_ANSWER.SEARCHNO is '�˻� ��ȣ';
