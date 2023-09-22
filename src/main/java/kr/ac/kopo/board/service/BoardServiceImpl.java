package kr.ac.kopo.board.service;

import jakarta.transaction.Transactional;
import kr.ac.kopo.board.dto.BoardDTO;
import kr.ac.kopo.board.dto.PageRequestDTO;
import kr.ac.kopo.board.dto.PageResultDTO;
import kr.ac.kopo.board.entity.Board;
import kr.ac.kopo.board.entity.Member;
import kr.ac.kopo.board.repository.BoardRepository;
import kr.ac.kopo.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Log4j2

public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository; // final 객체 참조값 자동 주입 받기 위해
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto){
        log.info(dto);
        // 새 글 등록할 때 전달 받은 dto를 Board entity객체로 변환하여 반환된 Board 객체의 참조값을 사용하여 DB의 Board 테이블에 insert
        Board board = dtoToEntity(dto);
        repository.save(board);

        return null;
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno){
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    // 댓글 삭제와 원글 삭제를 같이 하기 위해
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글 삭제
        replyRepository.deleteByBno(bno);
        // 원글 삭제
        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = repository.getReferenceById(boardDTO.getBno());
//        Board board = repository.getOne(boardDTO.getBno());
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        repository.save(board);
    }

}
