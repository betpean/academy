package com.academy.repository.search;

import com.academy.dto.BoardListReplyCountDTO;
import com.academy.entity.Board;
import com.academy.entity.QBoard;
import com.academy.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{


  public BoardSearchImpl() {
    super(Board.class);
  }

  @Override
  public Page<Board> search1(Pageable pageable) {

    QBoard board = QBoard.board; //q도메인 객체

    JPQLQuery<Board> query = from(board);

    System.out.println("from(board) : " + query);

    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(board.title.contains("q"));
    booleanBuilder.or(board.content.contains("q"));

//    query.where(board.title.contains("q"));
    query.where(booleanBuilder);
    System.out.println(query);
    query.where(board.bno.gt(0L));
    System.out.println(query);

    this.getQuerydsl().applyPagination(pageable, query);

    System.out.println("contains(제목) : " + query );

    List<Board> list = query.fetch();

    System.out.println(list.get(0).getTitle());

    long count = query.fetchCount();

    System.out.println(count);



    return null;
  }

  @Override
  public Page<Board> search2(Pageable pageable) {

    QBoard board = QBoard.board; //q도메인 객체
    JPQLQuery<Board> query = from(board);

    System.out.println("from(board) : " + query);
    query.where(board.title.contains("1").or(board.user.email.contains("2")));
    System.out.println("from(board) where문 추가 : " + query);
    query.where(board.bno.eq(3L));
    System.out.println("from(board) where 3 = 3 문 추가 : " + query);
    this.getQuerydsl().applyPagination(pageable, query);
    query.fetch();


//    BooleanBuilder booleanBuilder = new BooleanBuilder();

//    booleanBuilder.or(board.title.contains("1"));
//    booleanBuilder.or(board.content.contains("1"));
//
//    //    query.where(board.title.contains("q"));
//    query.where(booleanBuilder);
//    System.out.println(query);
//    query.where(board.bno.gt(0L));
//    System.out.println(query);
//
//    this.getQuerydsl().applyPagination(pageable, query);
//
//    System.out.println("contains(제목) : " + query );
//
//    List<Board> list = query.fetch();
//
//    System.out.println(list.get(0).getTitle());
//
//    long count = query.fetchCount();
//
//    System.out.println(count);
//

    return null;
  }

  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

    QBoard board = QBoard.board; //q 도메인 객체 entity를 q로 바꾼것
    JPQLQuery<Board> query = from(board);
    if ( (types != null && types.length > 0 && keyword != null) ){
      BooleanBuilder booleanBuilder =new BooleanBuilder();

      for (String string : types){

        switch (string){
          case "t":
            booleanBuilder.or(board.title.contains(keyword));
            break;
          case "c":
            booleanBuilder.or(board.content.contains(keyword));
            break;
          case "w":
            booleanBuilder.or(board.user.email.contains(keyword));
            break;

        } //swich

      }//for
      query.where(booleanBuilder);
      System.out.println("검색조건 추가 : " + query);
    }//if

    query.where(board.bno.gt(0L));
    System.out.println("0보다 큰 조건 bno가 " + query);

    //페이징
    this.getQuerydsl().applyPagination(pageable, query);

    List<Board> boardList = query.fetch(); //실행
//    boardList.forEach(board1 -> log.info(board1));
    long count = query.fetchCount(); //row 수
//    System.out.println(count);


    return new PageImpl<>(boardList, pageable, count);
  }

  @Override
  public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

    QBoard board = QBoard.board; //q 도메인 객체 entity를 q로 바꾼것
    QReply reply = QReply.reply;

    JPQLQuery<Board> query = from(board);
    //select board from board

    query.leftJoin(reply).on(reply.board.eq(board));

    query.groupBy(board);



    if ( (types != null && types.length > 0 && keyword != null) ){
      BooleanBuilder booleanBuilder =new BooleanBuilder();

      for (String string : types){

        switch (string){
          case "t":
            booleanBuilder.or(board.title.contains(keyword));
            break;
          case "c":
            booleanBuilder.or(board.content.contains(keyword));
            break;
          case "w":
            booleanBuilder.or(board.user.email.contains(keyword));
            break;

        } //swich

      }//for
      query.where(booleanBuilder);
      System.out.println("검색조건 추가 : " + query);
    }//if

    query.where(board.bno.gt(0L));
    System.out.println("0보다 큰 조건 bno가 " + query);

    JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
            board.bno,
            board.title,
            board.user,
            board.regDate,
            reply.count().as("replyCount")
    ));



    //페이징
    this.getQuerydsl().applyPagination(pageable, dtoQuery);

    List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch(); //실행
//    boardList.forEach(board1 -> log.info(board1));
    long count = dtoQuery.fetchCount(); //row 수
//    System.out.println(count);


    return new PageImpl<>(dtoList, pageable, count);
  }


}
