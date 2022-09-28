package com.work.workhub.member.post.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.work.workhub.member.post.model.dto.CategoryDTO;
import com.work.workhub.member.post.model.dto.PostDTO;
import com.work.workhub.member.post.model.dto.PostLikeDTO;
import com.work.workhub.member.post.model.dto.ReplyDTO;

@Mapper
public interface PostMapper {

	List<PostDTO> findAllPost();

	List<PostDTO> findAllNotice();
	
	List<CategoryDTO> findAllCategory();

	PostDTO findPostByNo(Integer postNo);

	int writePost(PostDTO post);

	int updatePost(PostDTO post);

	int deletePost(Integer delNo);

	List<PostDTO> showNotice();
	
	int updatePostCnt(Integer postNo);
	
	int updateReplyCnt(Integer postNo);

	Integer findLike(Integer postNo, String likeId);
	
	int insertLike(PostLikeDTO postLike);

	int updateLikeCnt(Integer postNo, String likeId);

	int deleteLike(Integer postNo, String likeId);

}
