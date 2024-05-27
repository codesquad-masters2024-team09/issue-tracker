package com.issuetracker.domain.milestone.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MilestoneProcessResponse {

    private Long openedIssueCount;
    private Long closedIssueCount;
    private Integer achievementRate;

    public static int calculateAchievementRate(Long openedIssueCount, Long closedIssueCount) {
        return Math.round(((float) closedIssueCount / (openedIssueCount + closedIssueCount)) * 100);
    }

    public static MilestoneProcessResponse of(Long openedIssueCount, Long closedIssueCount) {
        return MilestoneProcessResponse.builder()
                .openedIssueCount(openedIssueCount)
                .closedIssueCount(closedIssueCount)
                .achievementRate(calculateAchievementRate(openedIssueCount, closedIssueCount))
                .build();
    }
}
