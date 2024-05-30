package com.issuetracker.domain.issue.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueQueryParser {

    QUERY_STRING_PATTERN(Pattern.compile("(no:milestone|no:assignee|is:open|is:closed|label:[^\"]\\S+|label:\".*?\"|assignee:\".*?\"|assignee:[^\"]\\S+|author:[^\"]\\S+|author:\".*?\"|milestone:[^\"]\\S+|milestone:\".*?\"|\\S+)")),
    KEYWORD_PATTERN(Pattern.compile("\\b(?!no:milestone\\b|no:assignee\\b|is:open\\b|is:closed\\b|label:.+\\b|milestone:.+\\b|author:.+\\b|assignee:.+\\b)(\\S+)")),
    AUTHOR_PATTERN(Pattern.compile("(?i)author:\"?([^\"]+)")),
    LABELS_PATTERN(Pattern.compile("(?i)label:\"?([^\"]+)")),
    MILESTONE_PATTERN(Pattern.compile("(?i)milestone:\"?([^\"]+)")),
    ASSIGNEE_PATTERN(Pattern.compile("(?i)assignee:\"?([^\"]+)")),
    OPEN_STATUS_PATTERN(Pattern.compile("(?i)is:(open|closed)")),
    NO_MILESTONE_PATTERN(Pattern.compile("(?i)no:milestone")),
    NO_ASSIGNEE_PATTERN(Pattern.compile("(?i)no:assignee"));

    private static final int MATCH_INDEX = 1;
    private static final String SPACE = " ";
    private static final BiFunction<Pattern, String, Matcher> MATCHER_CONVERTER = Pattern::matcher;
    private final Pattern pattern;

    public static List<String> parseQueryString(String requestQueryString) {
        List<String> queryString = new ArrayList<>();

        Matcher queryStringMatcher = QUERY_STRING_PATTERN.pattern.matcher(requestQueryString);

        while (queryStringMatcher.find()) {
            queryString.add(queryStringMatcher.group(MATCH_INDEX));
        }

        return queryString;
    }

    public static String parseKeyword(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(KEYWORD_PATTERN.pattern, str).matches())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(KEYWORD_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .collect(Collectors.joining(SPACE));
    }

    public static String parseAuthor(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(AUTHOR_PATTERN.pattern, str).find())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(AUTHOR_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .findAny()
                .orElse(null);
    }

    public static List<String> parseLabels(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(LABELS_PATTERN.pattern, str).find())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(LABELS_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .toList();
    }

    public static String parseMilestone(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(MILESTONE_PATTERN.pattern, str).find())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(MILESTONE_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .findAny()
                .orElse(null);
    }

    public static boolean parseNoXXX(List<String> queryString, Pattern pattern) {
        return queryString.stream()
                .anyMatch(str -> MATCHER_CONVERTER.apply(pattern, str).matches());
    }

    public static List<String> parseAssignees(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(ASSIGNEE_PATTERN.pattern, str).find())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(ASSIGNEE_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .toList();
    }

    public static boolean parseOpenStatus(List<String> queryString) {
        return queryString.stream()
                .filter(str -> MATCHER_CONVERTER.apply(OPEN_STATUS_PATTERN.pattern, str).matches())
                .map(str -> {
                    Matcher matcher = MATCHER_CONVERTER.apply(OPEN_STATUS_PATTERN.pattern, str);
                    return matcher.find() ? matcher.group(MATCH_INDEX) : "";
                })
                .map(status -> status.equalsIgnoreCase("open"))
                .findAny()
                .orElse(true);
    }
}
