CREATE TABLE recipeadmin (
    recipeadminno NUMBER(10) NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
    id         VARCHAR(30)   NOT NULL UNIQUE, -- 이메일(아이디), 중복 안됨, 레코드를 구분 
    passwd     VARCHAR(60)   NOT NULL, -- 패스워드, 영숫자 조합
    mname      VARCHAR(30)   NOT NULL, -- 성명, 한글 10자 저장 가능
    tel            VARCHAR(14)   NOT NULL, -- 전화번호
    mdate       DATE             NOT NULL, -- 가입일    
    grade        NUMBER(2)     NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
    PRIMARY KEY (memberno)               -- 한번 등록된 값은 중복 안됨
)