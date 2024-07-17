package com.academy.repository;

import com.academy.dto.BoardListReplyCountDTO;
import com.academy.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {



    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno group by b")
   Page<BoardListReplyCountDTO> searchWithReplyCount(Pageable pageable);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.title like %:keyword% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeyword(Pageable pageable,@Param("keyword") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.content like %:content% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypecontent(Pageable pageable, @Param("content") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.user.name like %:writer% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypewriter(Pageable pageable, @Param("writer") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.title like %:keyword% or b.content like %:keyword% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypect(Pageable pageable,@Param("keyword") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.title like %:keyword% or b.user.name like %:keyword% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypetw(Pageable pageable,@Param("keyword") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.user.name like %:keyword% or b.content like %:keyword% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypecw(Pageable pageable,@Param("keyword") String keyword);

    @Query("select new com.academy.dto.BoardListReplyCountDTO(b.bno,b.content,b.title,b.user.name,b.regDate,b.updateTime, count (r))  from Board b left join Reply r on b.bno = r.board.bno where b.title like %:keyword% or b.content like %:keyword% or b.user.name like %:keyword% group by b")
    Page<BoardListReplyCountDTO> searchWithReplyCountonlyKeywordtypectw(Pageable pageable,@Param("keyword") String keyword);
}
