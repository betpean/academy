package com.academy.repository;


import com.academy.entity.Board;
import com.academy.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

  @Query("select r " +
          "from Reply r where r.board.bno = :bno")
  List<Reply> listReplyfrombno(Long bno);

  List<Reply> findByBoard(Board board);

  List<Reply> findByBoard_Bno(Long bno);

  @Query("select r from Reply r where r.board.bno = :bno")
  Page<Reply> listOfBoard(Long bno, Pageable pageable);

  //댓글수
  Long countByBoard_Bno(Long bno);

  @Query(value = "select * from Reply", nativeQuery = true)
  List<Reply> findaaa ();






}
