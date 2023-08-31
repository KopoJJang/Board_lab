package kr.ac.kopo.board.repository;

import kr.ac.kopo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
