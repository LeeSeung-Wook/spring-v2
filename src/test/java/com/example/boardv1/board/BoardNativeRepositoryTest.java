package com.example.boardv1.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(BoardNativeRepository.class)
@DataJpaTest // EntityManger가 ioc에 등록됨
public class BoardNativeRepositoryTest {

    @Autowired // 어노테이션 DI 기법
    private BoardNativeRepository boardNativeRepository;

    @Test
    public void findById_test() {
        // given
        int id = 1;

        // when
        Board board = boardNativeRepository.findById(id);

        // eye
        System.out.println(board);

    }

    @Test
    public void findAll_test() {
        List<Board> list = boardNativeRepository.findAll();

        for (Board board : list) {
            System.out.println(board);
        }
    }

    @Test
    public void save_test() {
        int result = boardNativeRepository.save("tst", "sulkne");
        System.out.println(result);

        List<Board> list = boardNativeRepository.findAll();

        for (Board board : list) {
            System.out.println(board);
        }
    }

    @Test
    public void deleteById_test() {

        int id = 1;

        int result = boardNativeRepository.deleteById(id);

        System.out.println(result);

        List<Board> list = boardNativeRepository.findAll();

        for (Board board : list) {
            System.out.println(board);
        }
    }

    @Test
    public void uqdateById_test() {

        int id = 1;
        String title = "ewef";
        String content = "eiewiwiw";

        int result = boardNativeRepository.updateById(id, title, content);

        System.out.println(result);

        List<Board> list = boardNativeRepository.findAll();

        for (Board board : list) {
            System.out.println(board);
        }
    }
}
