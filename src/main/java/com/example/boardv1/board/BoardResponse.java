package com.example.boardv1.board;

import java.util.List;
import java.util.regex.*;
import java.util.Map;

import com.example.boardv1.reply.ReplyResponse;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.client.RestTemplate;

@Data
public class BoardResponse {

    @Data
    public static class DetailDTO {
        // 화면에 보이지 않는것
        private int id;
        private int userId;

        // 화면에 보이는것
        private String title;
        private String content;
        private String username;

        private String youtubeId;

        // 연산해서 만들어야 되는것
        private boolean isOwner;

        private List<ReplyResponse.DTO> replies;

        public DetailDTO(Board board, Integer sessionUserId) {
            this.id = board.getId();
            this.userId = board.getUser().getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.username = board.getUser().getUsername();
            this.isOwner = board.getUser().getId() == sessionUserId;

            this.replies = board.getReplies().stream()
                    .map(reply -> new ReplyResponse.DTO(reply, sessionUserId))
                    .toList();

            // ✅ 여기서 반드시 호출한다
            this.youtubeId = extractYoutubeId(this.content);
        }

        // ✅ 유튜브 ID 추출 — iframe + watch 링크 모두 처리한다
        private String extractYoutubeId(String html) {
            if (html == null)
                return null;

            Document doc = Jsoup.parse(html);
            String text = doc.text(); // 태그 제거

            // watch?v=MER9RT3EWUs 패턴
            Pattern pattern = Pattern.compile("v=([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return matcher.group(1);
            }

            return null;
        }
    }

    @Data
    public static class ListDTO {

        private int id;
        private String title;
        private String content;
        private String youtubeTitle;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();

            String youtubeId = extractYoutubeId(this.content);
            this.youtubeTitle = fetchYoutubeTitle(youtubeId);
        }

        // ✅ 유튜브 ID 추출
        private String extractYoutubeId(String html) {
            if (html == null)
                return null;

            Document doc = Jsoup.parse(html);
            String text = doc.text();

            Pattern pattern = Pattern.compile("v=([a-zA-Z0-9_-]{11})");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return matcher.group(1);
            }

            return null;
        }

        // ✅ 유튜브 제목 가져오기
        private String fetchYoutubeTitle(String videoId) {
            if (videoId == null)
                return null;

            try {
                String url = "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v="
                        + videoId + "&format=json";

                RestTemplate rt = new RestTemplate();
                Map<?, ?> result = rt.getForObject(url, Map.class);

                return (String) result.get("title");

            } catch (Exception e) {
                return null;
            }
        }
    }

}