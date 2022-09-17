package com.work.workhub.member.post.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.work.workhub.member.member.dto.UserImpl;
import com.work.workhub.member.post.model.dto.ReplyDTO;
import com.work.workhub.member.post.model.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/reply")
public class ReplyController {
	
	private ReplyService replyService;
	private MessageSource messageSource;
	
	@Autowired
	public ReplyController(ReplyService replyService, MessageSource messageSource) {
		this.replyService = replyService;
		this.messageSource = messageSource;
	}
	
	
	
	//댓글 등록
	@PostMapping("write")
	public String writeReply(@ModelAttribute ReplyDTO reply, @AuthenticationPrincipal UserImpl user, RedirectAttributes rttr) throws Exception {
		
		reply.setNo(user.getNo());
		log.info("등록 요청 댓글 : {}", reply);
		
		replyService.writeReply(reply);
		
		return "redirect:/post/detail/no/" + reply.getPostNo();
	}
	
	
	//댓글 수정
//	@GetMapping("update/no/{postNo}")
//	public ModelAndView updatePost(ModelAndView mv, @PathVariable("postNo") Integer postNo) {
//
//		PostDTO postDTO = postService.findPostByNo(postNo);
//		
//		mv.addObject("postDTO", postDTO);
//		mv.setViewName("/post/update");
//		
//		return mv;
//	}
	
	@PostMapping("update/no/{postNo}")
	public String updateReply(@ModelAttribute ReplyDTO reply, @AuthenticationPrincipal UserImpl user, RedirectAttributes rttr) throws Exception {
		
		reply.setNo(user.getNo());
		log.info("수정 요청 replyNo : " , reply.getReplyNo());
		log.info("수정 요청 댓글 : {}", reply);
		
		replyService.writeReply(reply);
		
		return "redirect:/post/detail/no/" + reply.getPostNo();
	}
	
	
	//댓글 삭제
	@GetMapping("delete/{replyNo}")
	public String deletePost(@PathVariable("replyNo") String replyNo, RedirectAttributes rttr, Locale locale) {
		
		int delNo = Integer.parseInt(replyNo);
		
		log.info("삭제 요청 댓글 : {}", delNo);
		
		replyService.deleteReply(delNo);
		
		rttr.addFlashAttribute("successMessage", messageSource.getMessage("deletePost", null, locale));
		
		return "redirect:/post/list";

	}

	

}
