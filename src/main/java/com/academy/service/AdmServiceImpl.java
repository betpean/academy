package com.academy.service;

import com.academy.dto.AdmDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Adm;
import com.academy.entity.Board;
import com.academy.repository.AdmRepository;
import com.academy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class AdmServiceImpl implements AdmService{

    private final AdmRepository admRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository ;



    @Override
    public Long upload(AdmDTO admDTO, String email) {
        Adm adm = modelMapper.map(admDTO,Adm.class);
        adm.setUser(userRepository.findByEmail(email));
        Long ano = admRepository.save(adm).getAno();

        return ano;
    }

    @Override
    public List<Board> select() {
        return null;
    }

    @Override
    public PageResponseDTO<AdmDTO> list(PageRequestDTO pageRequestDTO) {
        return null;
    }

    @Override
    public AdmDTO view(Long ano) {
        return null;
    }

    @Override
    public void modify(AdmDTO admDTO) {

    }

    @Override
    public void delete(Long ano) {

    }
}
