package com.academy.service;

import com.academy.dto.*;
import com.academy.entity.AdmBoard;
import com.academy.entity.Board;
import com.academy.entity.User;
import com.academy.repository.AdmBoardRepository;
import com.academy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class AdmServiceImpl implements AdmService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository ;
    private final AdmBoardRepository admBoardRepository;


    @Override
    public Long upload(AdmBoardDTO admBoardDTO, String email) {
        AdmBoard admBoard = modelMapper.map(admBoardDTO, AdmBoard.class);

        admBoard.setUser(userRepository.findByEmail(email));
        Long abno = admBoardRepository.save(admBoard).getAbno();

        return abno;
    }

    @Override
    public PageResponseDTO<AdmBoardDTO> list(PageRequestDTO pageRequestDTO) {

        Page<AdmBoard> admboardPage = admBoardRepository.findAll(pageRequestDTO.getPageable());

        List<AdmBoardDTO> admboardDTOList =
                admboardPage.getContent().stream()
                        .map(admboard -> modelMapper.map(admboard, AdmBoardDTO.class))
                        .collect(Collectors.toList());


        log.info(admboardDTOList+"디티오리스트"); //admboardDTOList에는 bno등은 있지만 userId는 없다. user.userId라서

        for( AdmBoard a  : admboardPage.getContent() ){
            for(AdmBoardDTO adto :admboardDTOList){
                if(a.getAbno().equals(adto.getAbno()) ){
                    Optional<User> user = userRepository.findById( a.getUser().getUno() );
                    User user1 = user.orElseThrow();
                    adto.setAdu(user1); //admboard객체에서 userid를 꺼내서
                    //userRepository에서 userid로 검색해서
                    // 나온 user.getname를 하면 이름나온다.
                }
            }
        }

        return PageResponseDTO.<AdmBoardDTO>withAll()
                .dtoList(admboardDTOList)
                .pageRequestDTO(pageRequestDTO)
                .total((int)admboardPage.getTotalElements())
                .build();

    }
    @Override
    public AdmBoardDTO view(Long abno, PageRequestDTO pageRequestDTO, Principal principal) {
        AdmBoard admBoard = admBoardRepository.findById(abno).orElse(null);

        if (admBoard == null) {
            return null;
        }

        AdmBoardDTO admBoardDTO = modelMapper.map(admBoard, AdmBoardDTO.class);

        User user = userRepository.findById(admBoard.getUser().getUno()).orElse(null);
        if (user != null) {
            admBoardDTO.setAdu(user);
        } else {
            admBoardDTO.setAdu(null); // 또는 기본값 설정
        }

        return admBoardDTO;
    }
    @Override
    public void modify(AdmBoardDTO admBoardDTO) {
        AdmBoard admBoard = admBoardRepository.findById(admBoardDTO.getAbno()).orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 ID:" + admBoardDTO.getAbno()));
        admBoard.setTitle(admBoardDTO.getTitle());
        admBoard.setContent(admBoardDTO.getContent());
        admBoardRepository.save(admBoard);
    }


    @Override
    public void delete(Long abno) {
        admBoardRepository.deleteById(abno);
    }
}
