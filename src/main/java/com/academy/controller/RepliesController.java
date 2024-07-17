package com.academy.controller;

import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.dto.ReplyDTO;
import com.academy.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reply/")
public class RepliesController {

    private final ReplyService replyService;

    @PostMapping(value = "/new")
    public Map<String, String> register(@RequestBody ReplyDTO replyDTO , Principal principal) {

        if (principal == null) {
            Map<String, String> result = new HashMap<>();
            result.put("rno", "로그인해라");
            return result;
        }

        log.info(replyDTO);


        log.info(replyDTO);

        Long rno = replyService.register(replyDTO, principal);

        Map<String, String> result = new HashMap<>();
        result.put("rno", Long.toString(rno));

        return result;

    }

    //읽기       //게시물읽기페이지에서

    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){

        log.info("현재 본문은 : " + bno);
        log.info(pageRequestDTO);

        PageResponseDTO<ReplyDTO> pageResponseDTO =
                replyService.getListOfBoard(bno, pageRequestDTO);

        return pageResponseDTO;

    }

    @GetMapping("/get/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

        return replyService.read(rno);


    }

    @PutMapping("/modify/{rno}")
    public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                    @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String, Long> map = new HashMap<>();
        map.put("rno", rno);

        return map;

    }

    @DeleteMapping("/remove/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno){

        replyService.remove(rno);

        Map<String, Long> map = new HashMap<>();
        map.put("rno", rno);

        return map;

    }


}
