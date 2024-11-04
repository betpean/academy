package com.academy.entity;

import com.academy.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = "board")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reply")
public class Reply extends BaseEntity {

    @Id
    @Column(name = "rno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //댓글리스트

    private LocalDate regDate; //등록일자

    private LocalDateTime updateTime; //수정일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adm_id")
    private User user;    //댓글작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nno")
    private User user2;

    private String content2;    //댓글내용


}
