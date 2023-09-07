package kr.ac.kopo.board.repository;

import kr.ac.kopo.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // Lazy 방식에서는 Join이 필요할 때 JPQL을 사용해서 Join에 필요한 sql문을 작성해야 함
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno") // =:bno 콜롬이랑 파라미터가 붙어있어야 됌
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 목록 화면에 사용될 데이터행의 결과 집합을 얻을 수 있는 JPQL
    @Query("select b, r from Board b left join Reply r ON r.board=b where b.bno=:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 목록 화면에 사용되 데이터행의 결과 + 댓글의 개수 : JPQL
    @Query(value = "select b, w, count(r) from Board b " +
            "left join b.writer w " +
            "left join Reply r ON r.board=b " +
            "group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("select b, w, count(r) from Board b left join b.writer w left outer join Reply r on r.board=b where b.bno=:bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
