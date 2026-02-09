package com.example.boardv1.reply;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ReplyRequest {

    // 책임 : 클라이언트(브라우저)의 요청 데이터를 저장하는 클래스
    @Data
    public static class ReplyDTO {

        @NotBlank(message = "게시글 ID가 필요합니다")
        private String comment;

        @NotBlank(message = "댓글을 입력해주세요")
        private Integer boardId;

    }

}
