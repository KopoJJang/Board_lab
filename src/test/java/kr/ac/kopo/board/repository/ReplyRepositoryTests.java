package kr.ac.kopo.board.repository;

import kr.ac.kopo.board.entity.Board;
import kr.ac.kopo.board.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1, 300).forEach(i->{
            // 1부터 100까지의 정수중에 임의의 값 저장
            long bno = new Random().nextInt(100) + 1;
            Board board = Board.builder()
                    .bno(bno)
                    .build();

            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .board(board)
                    .replyer("guest" + i)
                    .build();

            replyRepository.save(reply);
        });
    }
}
