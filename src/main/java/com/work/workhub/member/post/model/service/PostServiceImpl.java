package com.work.workhub.member.post.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.work.workhub.member.post.model.dao.PostMapper;
import com.work.workhub.member.post.model.dto.CategoryDTO;
import com.work.workhub.member.post.model.dto.PostDTO;
import com.work.workhub.member.post.model.dto.ReplyDTO;

@Service("postService")
@Transactional
public class PostServiceImpl implements PostService{
	
	private final PostMapper postMapper;
	
	@Autowired
	public PostServiceImpl(PostMapper postMapper) {
		this.postMapper = postMapper;
	}

	
	@Override
	public List<PostDTO> findAllPost() {
		return postMapper.findAllPost();
	}
	
	
	@Override
	public List<PostDTO> findAllNotice() {
		return postMapper.findAllNotice();
	}

	
	@Override
	public List<CategoryDTO> findAllCategory() {
		return postMapper.findAllCategory();
	}

	
	@Override
	public PostDTO findPostByNo(Integer postNo) {
		return postMapper.findPostByNo(postNo);
	}
	
	@Override
	public List<PostDTO> showNotice() {
		return postMapper.showNotice();
	}
	
	@Override
	public boolean writePost(PostDTO post) throws Exception {
		
		int result;
		
		result = postMapper.writePost(post);

		if(result <= 0) {
			throw new Exception("게시글 등록에 실패하였습니다.");
		}
		
		return result > 0 ? true : false;
	}
	
	@Override
	public int updatePost(PostDTO post) {
		return postMapper.updatePost(post);
	}


	@Override
	public int deletePost(Integer postNo) {
		return postMapper.deletePost(postNo);
	}




}