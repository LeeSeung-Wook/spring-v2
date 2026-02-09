package com.example.boardv1.board;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.boardv1._core.errors.ex.Exception403;
import com.example.boardv1._core.errors.ex.Exception404;
import com.example.boardv1._core.errors.ex.Exception500;
import com.example.boardv1.reply.Reply;
import com.example.boardv1.user.User;

import lombok.RequiredArgsConstructor;

// 책임 : 트랜잭션관리, DTO만들기, 권한체크(DB정보가 필요하니까)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardResponse.ListDTO> 게시글목록() {
        return boardRepository.findAll().stream()
                .map(BoardResponse.ListDTO::new)
                .toList();
    }

    public BoardResponse.DetailDTO 상세보기(int id, Integer sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        return new BoardResponse.DetailDTO(board, sessionUserId);
    }

    public Board 수정폼게시글정보(int id, int sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        if (sessionUserId != board.getUser().getId())
            throw new Exception403("삭제할 권한이 없습니다.");

        return board;
    }

    @Transactional // update, delete, insert 할때 붙이세요!
    public void 게시글수정(int id, String title, String content, int sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        if (sessionUserId != board.getUser().getId())
            throw new Exception403("삭제할 권한이 없습니다.");
        board.setTitle(title);
        board.setContent(content);
    } // 자동 flush

    // 원자성(모든게 다되면 commit, 하나라도 실패하면 rollback)
    // 트랜잭션 종료시 flush 됨.
    @Transactional
    public void 게시글쓰기(String title, String content, User user) {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setUser(user);

        // 2. persist
        boardRepository.save(board);
    }

    @Transactional
    public void 게시글삭제(int id, int sessionUserId) {
        Board board = boardRepository.findById(id) // 영속화
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        if (sessionUserId != board.getUser().getId())
            throw new Exception403("삭제할 권한이 없습니다.");

        board.getReplies().forEach(r -> {
            r.setBoard(null);
        });

        boardRepository.delete(board);
    }

    private String extractYoutubeId(String html) {
        if (html == null)
            return null;

        Document doc = Jsoup.parse(html);
        String text = doc.text(); // ✅ 태그 제거하고 순수 텍스트만 얻는다

        // https://www.youtube.com/watch?v=MER9RT3EWUs
        Pattern pattern = Pattern.compile("v=([a-zA-Z0-9_-]{11})");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
