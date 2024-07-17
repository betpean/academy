package com.academy.repository;

import com.academy.dto.BoardListReplyCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Transactional
    @Test
    public void setBoardRepositorytest(){
        Pageable pageable =  PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> boardtest = boardRepository.searchWithReplyCount(pageable);
        boardtest.getContent().forEach(boardListReplyCountDTO -> log.info(boardListReplyCountDTO));


    }

}