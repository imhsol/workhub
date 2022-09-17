package com.work.workhub.member.post.controller;


import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.work.workhub.member.member.dto.UserImpl;
import com.work.workhub.member.post.model.dto.CategoryDTO;
import com.work.workhub.member.post.model.dto.PostDTO;
import com.work.workhub.member.post.model.dto.PostLikeDTO;
import com.work.workhub.member.post.model.dto.ReplyDTO;
import com.work.workhub.member.post.model.service.PostService;
import com.work.workhub.member.post.model.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostController {
	
	private PostService postService;
	private MessageSource messageSource;
	private ReplyService replyService;
	
	@Autowired
	public PostController(PostService postService, MessageSource messageSource, ReplyService replyService) {
		this.postService = postService;
		this.messageSource = messageSource;
		this.replyService = replyService;
	}

	
	
	//게시판 목록 조회
	@GetMapping("list")
	public ModelAndView findPostList(ModelAndView mv) {
		
		List<PostDTO> notice=postService.showNotice();
		
		List<PostDTO> postList = postService.findAllPost();
		
		mv.addObject("notice", notice);
		mv.addObject("postList", postList);
		mv.setViewName("/post/list");
		
		return mv;
	}
	
	
	//공지사항 목록 조회
	@GetMapping("notice")
	public ModelAndView findNoticeList(ModelAndView mv) {
		
		List<PostDTO> noticeList = postService.findAllNotice();
		
		mv.addObject("noticeList", noticeList);
		mv.setViewName("/post/notice");
		
		return mv;
	}
	
	
	//게시글 카테고리
	@GetMapping(value="category", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<CategoryDTO> findCategoryList(){
		return postService.findAllCategory();
	}
	
	
	//게시글 상세페이지
	@GetMapping("detail/no/{postNo}")
	public ModelAndView selectPostDetail(ModelAndView mv, @PathVariable("postNo") Integer postNo, @AuthenticationPrincipal UserImpl user) {

		PostDTO post = postService.findPostByNo(postNo);
		
		postService.updatePostCnt(postNo);
		
		log.info("글 작성자 id : {}", post.getMember().getId());
		log.info("글 조회자 id : {}", user.getId());
		
		mv.addObject("post", post);
		
		mv.setViewName("/post/detail");
		
		
		//댓글 조회
		List<ReplyDTO> reply = null;
		reply = replyService.findReplyListByPostNo(postNo);
		mv.addObject("reply", reply);
		
		return mv;
	}
	
	
	//게시글 추천
//	@GetMapping("like/{postNo}")
//	public void likePost(@ModelAttribute PostLikeDTO postLike, @PathVariable("postNo") String postNo, @AuthenticationPrincipal UserImpl user) {
//		
//		int likeNo = Integer.parseInt(postNo);
//		
//		postLike.setNo(user.getNo());
//		postLike.setPostNo(likeNo);
//
//		postService.likePost(postLike);
//		
//		log.info("추천수 : " , postLike.getPostLikeCnt());
//		log.info("추천글 : " , postLike.getPostNo());
//		log.info("추천인 : " , postLike.getNo());
//	}
	
	
	//게시글 작성
	@GetMapping("write")
	public void writePage() {}
	
	@PostMapping("write")
	public String writePost(@ModelAttribute PostDTO post, @AuthenticationPrincipal UserImpl user, RedirectAttributes rttr, Locale locale) throws Exception {
		
		post.setNo(user.getNo());
		
		log.info("등록 요청 글 : {}", post);
		log.info("등록글 작성자 번호 : {}", post.getNo());
		
		postService.writePost(post);
		
		rttr.addFlashAttribute("successMessage", messageSource.getMessage("writePost", null, locale));
		
		return "redirect:/post/list";
		
	}
	
	
	//게시글 수정
	@GetMapping("update/no/{postNo}")
	public ModelAndView updatePost(ModelAndView mv, @PathVariable("postNo") Integer postNo, @AuthenticationPrincipal UserImpl user) {

		PostDTO post = postService.findPostByNo(postNo);
		
		mv.addObject("post", post);
		mv.setViewName("/post/update");
		
		return mv;
	}
	
	@PostMapping("update/no/{postNo}")
	public String updatePost(@ModelAttribute PostDTO post, @AuthenticationPrincipal UserImpl user, RedirectAttributes rttr, Locale locale) throws Exception {
		
		log.info("수정 요청 postNo : " , post.getPostNo());
		log.info("수정 요청 글 : {}", post.getPostTitle());
		
		postService.updatePost(post);
		
		rttr.addFlashAttribute("successMessage", messageSource.getMessage("updatePost", null, locale));
		
		return "redirect:/post/detail/no/" + post.getPostNo();
		
	}
	
	
	 //게시글 삭제
	@GetMapping("delete/no/{postNo}")
	public String deletePost(@PathVariable("postNo") String postNo, RedirectAttributes rttr, Locale locale) {
		
		int delNo = Integer.parseInt(postNo);
		
		log.info("삭제 요청 글 : {}", delNo);
		
		postService.deletePost(delNo);
		
		rttr.addFlashAttribute("successMessage", messageSource.getMessage("deletePost", null, locale));
		
		return "redirect:/post/list";

	}

	
}