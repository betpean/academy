package com.academy.repository;

import com.academy.dto.AdmDTO;
import com.academy.entity.Adm;
import com.academy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmRepository extends JpaRepository<Adm, Long> {

}
