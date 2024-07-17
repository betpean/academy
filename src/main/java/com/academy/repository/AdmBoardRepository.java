package com.academy.repository;

import com.academy.dto.AdmBoardDTO;
import com.academy.dto.AdmDTO;
import com.academy.entity.AdmBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmBoardRepository extends JpaRepository<AdmBoard,Long> {
    Page<AdmBoard> findByTitleContainingOrderByAbnoDesc(String keyword, Pageable pageable);

}
