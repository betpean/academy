package com.academy.service;

import com.academy.dto.BoardDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Board;
import com.academy.entity.User;
import com.academy.repository.BoardRepository;
import com.academy.repository.UserRepository;
import jakarta.persistence.Id;
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
    public List<Board> select() {
        return null;
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {


        Page<Board> boardPage = boardRepository.findAll(pageRequestDTO.getPageable());

        List<BoardDTO> boardDTOList =
                boardPage.getContent().stream()
                        .map(board -> modelMapper.map(board, BoardDTO.class))
                        .collect(Collectors.toList());

        log.info(boardDTOList); //boardDTOList에는 bno등은 있지만 userId는 없다. user.userId라서

        for( Board b  : boardPage.getContent() ){
            for(BoardDTO bdto :boardDTOList){
                if(b.getBno().equals(bdto.getBno()) ){
                    Optional<User> user = userRepository.findById( b.getUser().getUno() );
                    User user1 = user.orElseThrow();
                    bdto.setUserId(user1.getName()); //board객체에서 userid를 꺼내서
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
    public BoardDTO view(Long bno) {

        Optional<Board> board
                = boardRepository.findById(bno);

        //log.info("값확인 " + board.isEmpty());

        if(board.isEmpty()){
            return null;
        }

        BoardDTO boardDTO = modelMapper.map(board.get(), BoardDTO.class);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

    }

    @Override
    public void delete(Long bno) {

    }
}
