package com.academy.entity;

import com.academy.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board_img")
@ToString(exclude = {"board", "admboard"})
@Getter
@Setter
public class BoardImg extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ino;

  private String imgName; //중복을 제거하기 위해서 uuid를 사용하면
                         //새로운 파일명이 됨 경로포함

  private String imgUrl; //경로

  private String oriImgName; //이미지 기존 파일명을 알기 위해서 작성

  private String repimgYn; //저장되는 이미지중에 대표 이미지인지를 지정

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_bno")
  private Board board; //board테이블을 참조, 누구의 이미지인가

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admboard_abno")
  private AdmBoard admboard; //board테이블을 참조, 누구의 이미지인가




  public void updateBoardImg(String oriImgName, String imgName, String imgUrl) {
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }


}
