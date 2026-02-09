package com.example.boardv1.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class BoardRequest {

    // 책임 : 클라이언트(브라우저)의 요청 데이터를 저장하는 클래스
    @Data
    public static class SaveOrUpdateDTO {
        @NotBlank(message = "제목을 입력해주세요")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        private String content;

    }
}
