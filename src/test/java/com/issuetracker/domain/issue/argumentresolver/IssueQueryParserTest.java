package com.issuetracker.domain.issue.argumentresolver;

import static org.assertj.core.api.Assertions.*;

import com.issuetracker.domain.issue.util.IssueQueryParser;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueQueryParserTest {

    private static final String QUERY_STRING = "is:closed 검색 기능 label:bug label:\"bug fix\" author:yelly milestone:\"기능 개발\"";

    @Test
    @DisplayName("쿼리 스트링을 파싱하면 is:closed 와 같이 지정한 검색 키워드로 파싱할 수 있다")
    void parseQueryString() {
        // when
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // then
        assertThat(parsedQueryString).contains(
                "is:closed", "검색", "기능", "label:bug", "label:\"bug fix\"", "author:yelly"
        );
    }

    @Test
    @DisplayName("파싱된 쿼리 스트링에서 '검색 기능' 이라는 이슈 타이틀을 파싱할 수 있다")
    void parseIssueTitle() {
        // given
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // when
        String issueTitle = IssueQueryParser.parseKeyword(parsedQueryString);

        // then
        assertThat(issueTitle).isEqualTo("검색 기능");
    }

    @Test
    @DisplayName("파싱된 쿼리 스트링에서 작성자 'yelly' 를 파싱할 수 있다")
    void parseAuthor() {
        // given
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // when
        String author = IssueQueryParser.parseAuthor(parsedQueryString);

        // then
        assertThat(author).isEqualTo("yelly");
    }

    @Test
    @DisplayName("파싱된 쿼리 스트링에서 레이블 'bug', 'bug fix' 를 파싱할 수 있다")
    void parseLabels() {
        // given
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // when
        List<String> labels = IssueQueryParser.parseLabels(parsedQueryString);

        // then
        assertThat(labels).contains("bug", "bug fix");
    }

    @Test
    @DisplayName("파싱된 쿼리 스트링에서 '기능 개발'이라는 마일스톤 아이디를 파싱할 수 있다")
    void parseMilestone() {
        // given
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // when
        String milestone = IssueQueryParser.parseMilestone(parsedQueryString);

        // then
        assertThat(milestone).isEqualTo("기능 개발");
    }

    @Test
    @DisplayName("파싱된 쿼리 스트링에서 이슈의 상태인 'closed' 라는 이슈의 열린 상태를 파싱할 수 있다")
    void parseOpenStatus() {
        // given
        List<String> parsedQueryString = IssueQueryParser.parseQueryString(QUERY_STRING);

        // when
        boolean isOpen = IssueQueryParser.parseOpenStatus(parsedQueryString);

        // then
        assertThat(isOpen).isFalse();
    }
}