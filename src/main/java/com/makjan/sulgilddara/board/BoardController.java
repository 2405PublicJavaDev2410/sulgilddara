package com.makjan.sulgilddara.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
	
	@GetMapping("/board")
	public String showBoardList() {
		return "board/boardList";
	}
}