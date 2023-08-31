package kr.ac.kopo.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")

public class Board extends BaseEntity{
    // primary-key, bno 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bno;
    private String title;
    private String content;

    @ManyToOne
    private Member writer;  // 실제 board 테이블에는 writer_email 컬럼이 생성되고 FK(Member 테이블의 email 컬럼값만 참조하기 위해) 설정됨

}
