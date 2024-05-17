-- mysql

-- test user insert
INSERT INTO MEMBER(MEMBER_ID, PASSWORD) VALUES ("testUser", "1234");

INSERT INTO MILESTONE(MILESTONE_ID) VALUES("testMilestone");

INSERT INTO ISSUE(MEMBER_ID, TITLE, CONTENT) VALUES ("testUser", "testTitle", "testContent");