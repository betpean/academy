package com.academy.service;


import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.dto.ReplyDTO;
import com.academy.entity.Board;
import com.academy.entity.Reply;
import com.academy.entity.User;
import com.academy.repository.BoardRepository;
import com.academy.repository.ReplyRepository;
import com.academy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

  private final ReplyRepository replyRepository;
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public Long register(ReplyDTO replyDTO, Principal principal) {

    User user = userRepository.findByEmail(principal.getName());
    Board board = boardRepository.findById(replyDTO.getBno()).orElseThrow(EntityNotFoundException::new);
//            Board.builder().bno(replyDTO.getBno()).build();
    // replyDTO에서 게시물 번호를 가져와 Board 객체 생성

    Reply reply = modelMapper.map(replyDTO, Reply.class); // replyDTO를 Reply 객체로 변환

    reply.setBoard(board);     // Reply 객체에 Board 객체 설정

    reply.setUser(user);
    return replyRepository.save(reply).getRno();   // Reply 객체를 저장하고, 댓글 번호 반환

  }

  @Override
//  @Transactional
  public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

    Pageable pageable = PageRequest
            .of(pageRequestDTO.getPage() <= 0 ? 0: pageRequestDTO.getPage()-1,
                    pageRequestDTO.getSize(),
                    Sort.by("rno").descending());

    Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

    log.info("변환값 " +result);
     List<ReplyDTO> replyDTOList = result.getContent()
             .stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
             .collect(Collectors.toList());
     result.getTotalElements();
     List<Reply> replyList = result.getContent();
     for (ReplyDTO replyDTO :replyDTOList){
       for (Reply reply : replyList){
         if (replyDTO.getRno() == reply.getRno()){
           replyDTO.setReplyer(reply.getUser().getName());
         }
       }


     }

     log.info("결과값" + replyDTOList);

    return PageResponseDTO.<ReplyDTO>withAll()
            .pageRequestDTO(pageRequestDTO)
            .dtoList(replyDTOList)
            .total((int) result.getTotalElements())
            .build();
  }

  @Override
  public ReplyDTO read(Long rno) {


    return replyRepository.findById(rno)
            .map(reply -> modelMapper.map(reply, ReplyDTO.class))
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 Reply가 존재하지 않습니다: " + rno));
  }


  @Override
  @Transactional
  public void modify(ReplyDTO replyDTO) {

    Reply reply = replyRepository.findById(replyDTO.getRno()).get();
    log.info(reply);
    reply.changeText(replyDTO.getReplyText()); //댓글 수정

    replyRepository.save(reply);


  }

  @Override
  public void remove(Long rno) {

    replyRepository.deleteById(rno);

  }


}
