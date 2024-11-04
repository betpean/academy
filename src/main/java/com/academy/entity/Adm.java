package com.academy.entity;

import com.academy.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "adm")
public class Adm extends BaseEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long ano;      //학원 글 번호

   private String admname;     //학원명

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id")
   private User user;    //회원

   private String admKind;    //학원종류

   private String admaddress; //학원주소

   private String admnumber; //학원전화번호


}
