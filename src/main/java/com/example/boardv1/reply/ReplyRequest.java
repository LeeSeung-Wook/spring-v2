package com.example.boardv1.reply;

import lombok.Data;

public class ReplyRequest {

    // 책임 : 클라이언트(브라우저)의 요청 데이터를 저장하는 클래스
    @Data
    public static class ReplyDTO {
        private String comment;
        private Integer boardId;

    }

}
