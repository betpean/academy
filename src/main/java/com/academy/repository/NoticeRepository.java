package com.academy.repository;

import com.academy.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice ,Long> {
}
