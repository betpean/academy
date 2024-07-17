package com.academy.service;

import com.academy.dto.BoardDTO;
import com.academy.dto.BoardListReplyCountDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Board;
import com.academy.entity.User;
import com.academy.repository.BoardRepository;
import com.academy.repository.UserRepository;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository ;

    @Override
    public Long upload(BoardDTO boardDTO , String email) {

        Board board = modelMapper.map(boardDTO,Board.class);

        board.setUser(userRepository.findByEmail(email));
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }



    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {


        Page<Board> boardPage = boardRepository.findAll(pageRequestDTO.getPageable());

        List<BoardDTO> boardDTOList =
                boardPage.getContent().stream()
                        .map(board -> modelMapper.map(board, BoardDTO.class))
                        .collect(Collectors.toList());


        log.info(boardDTOList+"디티오리스트"); //boardDTOList에는 bno등은 있지만 userId는 없다. user.userId라서

        for( Board b  : boardPage.getContent() ){
            for(BoardDTO bdto :boardDTOList){
                if(b.getBno().equals(bdto.getBno()) ){
                    Optional<User> user = userRepository.findById( b.getUser().getUno() );
                    User user1 = user.orElseThrow();
                    bdto.setUserId(user1); //board객체에서 userid를 꺼내서
                                                        //userRepository에서 userid로 검색해서
                                                        // 나온 user.getname를 하면 이름나온다.
                }
            }
        }

        return PageResponseDTO.<BoardDTO>withAll()
                .dtoList(boardDTOList)
                .pageRequestDTO(pageRequestDTO)
                .total((int)boardPage.getTotalElements())
                .build();

    }
    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();

        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = pageRequestDTO.getPageable("bno");

//        Page<Board> boardPage = boardRepository.findAll(pageRequestDTO.getPageable());
        Page<BoardListReplyCountDTO> boardPage = boardRepository.searchWithReplyCount(pageable);

        if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("t") ){
            boardPage = boardRepository.searchWithReplyCountonlyKeyword(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("c")){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypecontent(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("w")){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypewriter(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("tw") ){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypetw(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("ct") ){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypect(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("cw") ){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypecw(pageable, pageRequestDTO.getKeyword());

        }else if(pageRequestDTO.getType() != null && pageRequestDTO.getType().equals("ctw") ){
            boardPage = boardRepository.searchWithReplyCountonlyKeywordtypectw(pageable, pageRequestDTO.getKeyword());

        }
//        Page<Board> boardPage = boardRepository.findAll(pageRequestDTO.getPageable());



        List<BoardListReplyCountDTO> dtoList =  boardPage.getContent();

//    PageResponseDTO<BoardDTO> aa =
//            new PageResponseDTO<BoardDTO>(pageRequestDTO, boardDTOList, (int) boardPage.getTotalElements());

//    return aa;

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) boardPage.getTotalElements())
                .build();



    }

    @Override
    public BoardDTO view(Long bno, PageRequestDTO pageRequestDTO) {
        Board board = boardRepository.findById(bno).orElse(null);

        if (board == null) {
            return null;
        }

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        User user = userRepository.findById(board.getUser().getUno()).orElse(null);
        if (user != null) {
            boardDTO.setUserId(user);
        } else {
            boardDTO.setUserId(null); // 또는 기본값 설정
        }

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 ID:" + boardDTO.getBno()));
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);
    }

    @Override
    public void delete(Long bno) {
        boardRepository.deleteById(bno);
    }
}
