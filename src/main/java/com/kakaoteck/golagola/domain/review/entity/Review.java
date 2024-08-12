package com.kakaoteck.golagola.domain.review.entity;

import com.kakaoteck.golagola.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "review_table")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String reviewTitle;
    private Long reviewStar;
    private String reviewContent;
    private LocalDate reviewCreateTime;
    private LocalDate reviewUpdateTime;


}
