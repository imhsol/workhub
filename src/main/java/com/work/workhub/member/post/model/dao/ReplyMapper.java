package com.work.workhub.member.post.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.work.workhub.member.post.model.dto.ReplyDTO;

@Mapper
public interface ReplyMapper {

	int writeReply(ReplyDTO reply);

	int updateReply(ReplyDTO reply);

	List<ReplyDTO> findReplyListByPostNo(Integer postNo);

	int deleteReply(int delNo);


}
