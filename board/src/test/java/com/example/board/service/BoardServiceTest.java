package com.example.board.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.post.dto.PageRequestDTO;
import com.example.board.post.entity.Board;
import com.example.board.post.repository.BoardRepository;
import com.example.board.post.service.BoardService;

@SpringBootTest
class BoardServiceTest {
	@Autowired
	private BoardService bS;
	@Autowired
	private BoardRepository bR;
	@Autowired
	private ModelMapper mapper;

	@Test
	@Transactional(readOnly = true)
	public void getListTest() {
		var dto = PageRequestDTO.builder().page(1).size(10).build();
		var result = bS.getList(dto);
		System.out.println(result.getContent());

		List<Board> boards = result.getContent();
		boards.forEach(board -> {
			System.out.println(board);
			System.out.println(board.getWriter());
		});
	}
}
