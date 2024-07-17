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
@Table(name = "Reply", indexes = {@Index(name = "idx_reply_board_bno", columnList = "board_bno")})
public class Reply extends BaseEntity {

    @Id
    @Column(name = "rno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno; //댓글리스트

    private String replyText; // 내용


    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;    //댓글작성자 (학원글)


    public void changeText(String text){
        this.replyText = text;

    }

}
