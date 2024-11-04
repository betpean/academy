package com.academy.entity;

import com.academy.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "board")

public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void saveWithExceptionHandling(EntityManager entityManager) throws Exception {
        try {
            entityManager.persist(this);
        } catch (Exception e) {
            // 여기서 예외를 적절히 처리합니다. 예를 들어, 특정 로그를 남기거나 예외를 다시 throw할 수 있습니다.
            e.printStackTrace(); // 혹은 로깅 시스템을 이용하여 예외를 기록할 수 있습니다.
            throw new Exception("게시물 저장 중 오류가 발생하였습니다.");
        }
    }

}
