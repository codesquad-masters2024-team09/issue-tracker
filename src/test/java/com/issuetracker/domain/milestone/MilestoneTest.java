package com.issuetracker.domain.milestone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MilestoneTest {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Test
    @DisplayName("제목, 완료날짜, 설명이 들어있는 마일스톤은 저장에 성공한다")
    @Transactional
    void create_success() {
        // given
        Milestone milestone = Milestone.builder()
                .id("this is a test")
                .dueDate(LocalDate.of(2024, 5, 24))
                .description("testDescription")
                .build();

        // when
        Milestone savedMilestone = milestoneRepository.save(milestone);

        // then
        assertThat(savedMilestone).isEqualTo(milestone);
        assertThat(savedMilestone.isOpen()).isTrue();
        assertThat(savedMilestone.getCreatedAt()).isNotNull();
        assertThat(savedMilestone.getCreatedAt()).isEqualTo(savedMilestone.getModifiedAt());
    }

    @Test
    @DisplayName("제목만 들어있는 마일스톤은 저장에 성공한다")
    @Transactional
    void create_with_only_name_success() {
        // given
        Milestone milestone = Milestone.builder()
                .id("this is a test")
                .build();

        // when
        Milestone savedMilestone = milestoneRepository.save(milestone);

        // then
        assertThat(savedMilestone).isEqualTo(milestone);
        assertThat(savedMilestone.isOpen()).isTrue();
        assertThat(savedMilestone.getCreatedAt()).isNotNull();
        assertThat(savedMilestone.getCreatedAt()).isEqualTo(savedMilestone.getModifiedAt());
    }

    @Test
    @DisplayName("제목이 들어있지 않은 마일스톤은 저장에 실패한다")
    @Transactional
    void create_without_name_fail() {
        // given
        Milestone milestone = Milestone.builder()
                .dueDate(LocalDate.of(2024, 5, 24))
                .description("testDescription")
                .build();

        // when & then
        assertThatThrownBy(() -> milestoneRepository.save(milestone))
                .isInstanceOf(DbActionExecutionException.class);
    }
}
