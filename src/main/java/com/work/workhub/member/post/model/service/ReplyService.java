package com.work.workhub.member.post.model.service;

import java.util.List;

import com.work.workhub.member.post.model.dto.ReplyDTO;

public interface ReplyService {

	boolean writeReply(ReplyDTO reply) throws Exception;

	List<ReplyDTO> findReplyListByPostNo(Integer postNo);

	int deleteReply(int delNo);

//	int updateReply(ReplyDTO reply);


}
