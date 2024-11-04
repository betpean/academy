package com.academy.service;

import com.academy.dto.BoardDTO;
import com.academy.dto.NoticeDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Board;
import com.academy.entity.Notice;
import com.academy.entity.User;
import com.academy.repository.BoardRepository;
import com.academy.repository.NoticeRepository;
import com.academy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository ;

    @Override
    public Long upload(NoticeDTO noticeDTO, String email) {
        Notice notice = modelMapper.map(noticeDTO,Notice.class);

        notice.setUser(userRepository.findByEmail(email));
        Long nno = noticeRepository.save(notice).getNno();

        return  nno;
    }

    @Override
    public List<Notice> select() {
        return null;
    }

    @Override
    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO) {
        Page<Notice> noticePage = noticeRepository.findAll(pageRequestDTO.getPageable());

        List<NoticeDTO> noticedDTOList =
                noticePage.getContent().stream()
                        .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                        .collect(Collectors.toList());

        log.info(noticedDTOList); //boardDTOList에는 bno등은 있지만 userId는 없다. user.userId라서

        for( Notice n  : noticePage.getContent() ){
            for(NoticeDTO ndto :noticedDTOList){
                if(n.getNno().equals(ndto.getNno()) ){
                    Optional<User> user = userRepository.findById( n.getUser().getUno() );
                    User user1 = user.orElseThrow();
                    ndto.setUser(user1.getName()); //board객체에서 userid를 꺼내서
                    //userRepository에서 userid로 검색해서
                    // 나온 user.getname를 하면 이름나온다.
                }
            }
        }

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(noticedDTOList)
                .pageRequestDTO(pageRequestDTO)
                .total((int)noticePage.getTotalElements())
                .build();
    }

    @Override
    public NoticeDTO view(Long bno) {
        Optional<Notice> notice = noticeRepository.findById(bno);
        return notice.map(value -> modelMapper.map(value, NoticeDTO.class)).orElse(null);
    }

    @Override
    public void modify(NoticeDTO noticeDTO) {
        Notice notice = noticeRepository.findById(noticeDTO.getNno()).orElseThrow(() -> new IllegalArgumentException("잘못된 공지사항 ID:" + noticeDTO.getNno()));
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        noticeRepository.save(notice);
    }

    @Override
    public void delete(Long nno) {
        noticeRepository.deleteById(nno);
    }
}
