package com.makjan.sulgilddara.board.model.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.makjan.sulgilddara.board.model.mapper.BoardMapper;
import com.makjan.sulgilddara.board.model.service.BoardService;
import com.makjan.sulgilddara.board.model.vo.Board;
import com.makjan.sulgilddara.board.model.vo.BoardFile;
import com.makjan.sulgilddara.board.model.vo.BoardTag;
import com.makjan.sulgilddara.model.vo.Pagination;

@Service
public class BoardServiceImpl implements BoardService{
	private BoardMapper bMapper;
	
	@Autowired
	public BoardServiceImpl(BoardMapper bMapper) {
		this.bMapper = bMapper;
	}

	@Override
	public Board selectOne(Integer boardNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> selectBoardList(Integer currentPage) {
		int totalCount = bMapper.getTotalCount();
		Pagination pn = new Pagination(totalCount, currentPage);
		int limit = pn.getBoardLimit();
		int offset = (currentPage-1) * limit ;
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<Board> bList = bMapper.selectBoardList(rowBounds);
		Map<String, Object> map = new HashMap<>();
		map.put("bList", bList);
		map.put("pn", pn);
		return map;
	}

	@Override
	public int insertBoard(Board board) {
		int result = bMapper.insertBoard(board);
		return result;
	}

	@Override
	public int updateBoard(Board board, MultipartFile uploadFile) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteBoard(Integer boardNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertTag(BoardTag boardTag) {
		int result = bMapper.insertTag(boardTag);
		return result;
	}

	@Override
	public int updateTag(List<String> tags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteTag(List<String> tags) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int insertBoardFile(BoardFile boardFile) {
		int result = bMapper.insertBoardFile(boardFile);
		return result;
	}

	
	@Override
	public List<BoardTag> selectBoardTagList() {
		List<BoardTag> bTagList = bMapper.selectBoardTagList();
		return bTagList;
	}
	
	
	
}
