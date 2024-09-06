package com.makjan.sulgilddara.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makjan.sulgilddara.board.model.service.BoardService;
import com.makjan.sulgilddara.board.model.vo.Board;
import com.makjan.sulgilddara.board.model.vo.BoardFile;
import com.makjan.sulgilddara.board.model.vo.BoardTag;
import com.makjan.sulgilddara.common.utility.Util;
import com.makjan.sulgilddara.model.vo.Pagination;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Controller
public class BoardController {
	
	private BoardService bService;
	
	@Autowired
	public BoardController(BoardService bService) {
		this.bService = bService;
	}
	
	// 페이지 이동(폼) 메소드
	@PostMapping("/board/listCard")
	public String searchBoardListCard(@RequestParam(name="searchKeyword", required = false) String searchKeyword,
			@RequestParam(name="searchCondition", required = false,  defaultValue = "all") String searchCondition,
			@RequestParam(value = "cp", required = false, defaultValue = "1") Integer currentPage,
			Model model) {
		
		// 서비스에서 Pagination 객체, 조회된 bList 객체 매핑해서 반환
		Map<String, Object> map = bService.selectBoardList(currentPage, searchKeyword, searchCondition);
		// 태그 전체 리스트 조회
		List<BoardTag> bTagList = bService.selectBoardTagList();
		// 파일 전체 리스트 조회
		List<BoardFile> bFileList = bService.selectBoardFileList(); 
		
		model.addAttribute("bList", map.get("bList"));
		model.addAttribute("pn", map.get("pn"));
		model.addAttribute("bFileList", bFileList);
		model.addAttribute("bTagList", bTagList);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("searchCondition", searchCondition);
		return "board/boardList_card";
	}
	
	
	
	@GetMapping("/board/listCard")
	public String showBoardListCard(@RequestParam(name="searchKeyword", required = false ) String searchKeyword,
			@RequestParam(name="searchCondition", required = false, defaultValue = "all") String searchCondition,
			@RequestParam(value = "cp", required = false, defaultValue = "1") Integer currentPage,
			Model model) {
		
		// 서비스에서 Pagination 객체, 조회된 bList 객체 매핑해서 반환
		Map<String, Object> map = bService.selectBoardList(currentPage, searchKeyword, searchCondition);
		// 태그 전체 리스트 조회
		List<BoardTag> bTagList = bService.selectBoardTagList();
		// 파일 전체 리스트 조회
		List<BoardFile> bFileList = bService.selectBoardFileList(); 
		
		model.addAttribute("bList", map.get("bList"));
		model.addAttribute("pn", map.get("pn"));
		model.addAttribute("bFileList", bFileList);
		model.addAttribute("bTagList", bTagList);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("searchCondition", searchCondition);
		return "board/boardList_card";
	}
	
	
	
	
	@GetMapping("/board/listTable")
	public String showBoardListTable() {
		return "board/boardList_table";
	}
	
	@GetMapping("/board/detailPage")
	public String showBoardOne() {
		return "board/boardDetail";
	}
	
	@GetMapping("/board/writePage")
	public String showWriteBoardForm() {
		return "board/boardWrite";
	}
	
	@GetMapping("/board/modifyPage")
	public String showModifyForm() {
		return "board/boardModify";
	}
	
	
//	@ModelAttribute() 안에 들어가는 이름은 내가 정해주면 됨. 단, 지어준 이름으로 뷰에서 접근가능함!
	@PostMapping("/board/write")
	public String boardWrite(
			@ModelAttribute("Board") Board board,
			@ModelAttribute("BoardTag") BoardTag boardTag,
			@RequestParam(name="uploadFile", required = false) MultipartFile uploadFile,
			Model model) throws IllegalStateException, IOException {
		if(uploadFile != null && !"".equals(uploadFile.getOriginalFilename())) { // null체크시 실제 저장된 이름이 있는지도 체크해줘야 함.
			board.setBoardWriter("임시작성자이름");
			int boardResult = bService.insertBoard(board);
			
			String fileName = uploadFile.getOriginalFilename();
			String fileRename = Util.fileRename(fileName);
			
			// Web용 경로
			String filePath = "/board-images/"; // 이 경로가 fileConfig에 의해 실제 C드라이브 경로로 매핑됨.
			
			uploadFile.transferTo(new File("C:\\uploadFile\\board\\" + fileRename));
			
			BoardFile boardFile = new BoardFile();
			boardFile.setBoardFileName(fileName);
			boardFile.setBoardFileRename(fileRename);
			boardFile.setBoardFilePath(filePath); // 경로에 복사
			
			boardFile.setBoardNo(board.getBoardNo());
			
			int boardFileResult = bService.insertBoardFile(boardFile);	
		}
		
		// boardTagName엔 배열 문자열이 저장이 된다.
		// json형식의 문자열을 파싱해주고 배열에 저장
		
		if(!boardTag.getBoardTagName().equals("")) {
			List<String> tagNameArr = new ArrayList<String>();
			
			String tagNameJson = boardTag.getBoardTagName();
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				// JSON 문자열을 List<Map<String, String>> 형태로 변환
				List<Map<String, String>> list = objectMapper.readValue(tagNameJson, new TypeReference<List<Map<String, String>>>(){});
				
				// value 필드만 추출하여 List로 변환
				tagNameArr = list.stream().map(map -> map.get("value"))
						.toList();
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			// 각 배열의 값을 하나씩 table에 insert 시켜주기
			for(String tagName : tagNameArr) {
				boardTag.setBoardTagName(tagName);
				boardTag.setBoardNo(board.getBoardNo());
				int tagResult = bService.insertTag(boardTag);			
			}
		}
		
		return "redirect:/board/listCard";
	}
	
	
	
	@PostMapping("/board/modify")
	public String boardModify(){
		return "board/boardDetail";
	}
	
	@PostMapping("/board/delete")
	public String boardDelete() {
		return "board/boardList_card";
	}
	
	
	
	// 파일 업로드 메소드 (ajax 통신)
	@ResponseBody
	@PostMapping("/board/uploadImage")
	public ResponseEntity<?> boardUploadImage(@RequestParam("file") MultipartFile file){
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String fileRename = Util.fileRename(fileName);
			
			// Web용 경로
			String filePath = "/board-images/"; // 이 경로가 fileConfig에 의해 실제 C드라이브 경로로 매핑됨.
			
			
			try {
				//실제 경로 
				file.transferTo(new File("C:\\uploadFile\\board\\" + fileRename));
				
				// board객체 생성 -> db에 저장하기 위함. 사실 에디터 사용시 에디터가 정보 다갖고있음. -> 따라서 db에 저장할 필요 없음! 
//				BoardFile boardFile = new BoardFile();
//				boardFile.setFileName(fileName);
//				boardFile.setFileRename(fileRename);
//				boardFile.setFilePath(filePath); // 경로에 복사
				
//			boardFile에 boardNo 실제 게시글 업로드 시점에서 넣어주기 -> update로 boradNo만 수정해주면 됨.  실패!!!
//				int result = bService.insertBoardFile(boardFile);
				
				// ResponseEntity는 String과 크게 다른점은 없지만 다른 기능들을 사욜할 수 있는 객체형태
				return ResponseEntity.ok(filePath+fileRename); // 경로 전달
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("이미지 업로드 실패");
			}	
		}
		
		return null;
	}
	
	@PostMapping("/board/searchTag_listCard")
	public String searchTagListCard(
			@RequestParam("tagName") String tags,
			@RequestParam(value = "cp", required = false, defaultValue = "1") Integer currentPage,
			Model model) {
		
		
				System.out.println("tags : "+tags);
				String[] tagList = tags.split(",");
				
				for(String tag : tagList) {
					bService.selectBoardList(currentPage, tagList);
				}
				
				
				
				
				// 서비스에서 Pagination 객체, 조회된 bList 객체 매핑해서 반환
				Map<String, Object> map = bService.selectBoardList(currentPage, tagList);

				// 태그 전체 리스트 조회
				List<BoardTag> bTagList = bService.selectBoardTagList();
				// 파일 전체 리스트 조회
				List<BoardFile> bFileList = bService.selectBoardFileList(); 
				model.addAttribute("bFileList", bFileList);
				model.addAttribute("bTagList", bTagList);
				model.addAttribute("bList", map.get("bList"));
				model.addAttribute("pn", map.get("pn"));
				return "board/boardList_card";
	}
		
	
	
}








