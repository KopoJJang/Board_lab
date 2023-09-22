package kr.ac.kopo.board.service;

import kr.ac.kopo.board.dto.ReplyDTO;
import kr.ac.kopo.board.entity.Board;
import kr.ac.kopo.board.entity.Reply;

import java.util.List;

public interface ReplyService {
    // 댓글 등록
    Long register (ReplyDTO replyDTO);
    // 댓글 목록
    List<ReplyDTO> getList(Long bno);
    // 댓글 수정
    void modify(ReplyDTO replyDTO);
    // 댓글 삭제
    void remove(Long rno);

    // DTO => Entity로 변환
    default Reply dtoToEntity(ReplyDTO replyDTO) {
        Board board = Board.builder().build();
        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();
        return reply;
    }
    // Entity => DTO로 변환

    default ReplyDTO entityToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return dto;
    }
}
