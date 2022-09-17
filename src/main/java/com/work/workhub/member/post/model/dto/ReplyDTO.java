package com.work.workhub.member.post.model.dto;

import java.sql.Date;

import com.work.workhub.member.member.dto.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplyDTO {

	private int replyNo;
	private String replyContent;
	private Date replyDate;
	private Date replyUpdate;
	private String replyStatus;
	private int postNo;
	private int no;
	private int refReplyNo;
	
	private PostDTO post;
	private MemberDTO member;
	private ReplyDTO reply;
}
