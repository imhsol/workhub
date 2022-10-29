package com.work.workhub.member.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImgDTO {
	
	private int imgNo;
	private String orgName;
	private String svrName;
	private String imgPath;
	private String imgType;
	private int postNo;
}
