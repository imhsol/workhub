package com.work.workhub.member.post.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.work.workhub.member.post.model.dao.ReplyMapper;
import com.work.workhub.member.post.model.dto.ReplyDTO;

@Service("replyService")
@Transactional
public class ReplyServiceImpl implements ReplyService{
	
	private final ReplyMapper replyMapper;
	
	@Autowired
	public ReplyServiceImpl(ReplyMapper replyMapper) {
		this.replyMapper = replyMapper;
	}

	@Override
	public boolean writeReply(ReplyDTO reply) throws Exception {
		
		int result;
		
		if(reply.getReplyNo() != 0) {
			result = replyMapper.updateReply(reply);
		} else {
			result = replyMapper.writeReply(reply);
		}
		
		if(result <= 0) {
			throw new Exception("댓글 등록에 실패하였습니다.");
		}
		
		return result > 0 ? true : false;
	}

	@Override
	public List<ReplyDTO> findReplyListByPostNo(Integer postNo) {
		return replyMapper.findReplyListByPostNo(postNo);
	}

	@Override
	public int deleteReply(int delNo) {
		return replyMapper.deleteReply(delNo);
	}



}
