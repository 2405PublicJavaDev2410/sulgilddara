package com.makjan.sulgilddara.Reservation.model.Service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.makjan.sulgilddara.Reservation.model.Mapper.ReservationMapper;
import com.makjan.sulgilddara.Reservation.model.Service.ReservationService;
import com.makjan.sulgilddara.Reservation.model.VO.Reservation;
import com.makjan.sulgilddara.model.vo.Pagination;
import com.makjan.sulgilddara.user.model.vo.User;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService{
@Autowired
	ReservationMapper rmapper;
@Autowired
	User user;
	@Override
	public int RegisterInfo(Reservation reservation) {
		System.out.println(reservation);
		int result = rmapper.RegisterInfo(reservation); 
		return result;
	}
//
//	@Override
//	public int RegisterInfo(Reservation reservation, String userId) {
//		System.out.println(reservation);
//		int result = rmapper.RegisterInfo(reservation,userId); 
//		return result;
//	}

	@Override
	public List<Reservation> SearchAllInfo(String userId, String breweryName,RowBounds rowbounds) {
		
		Map<String,String>param = new HashMap<String,String>();
		param.put("userId",userId);
		param.put("breweryName",breweryName);
		List<Reservation> rList = rmapper.SearchAllInfo(param,rowbounds);
		return rList;
	}

	@Override
	public List<Reservation> SearchreserveNo(Reservation reservation) {
		List<Reservation> rList = rmapper.SearchreserveNo(reservation);
		return rList;
	}

	@Override
	public List<Reservation> SearchInfo(Map<String, String> param) {
		List<Reservation> rList = rmapper.SearchInfo(param);
		return rList;
	}

	@Override
	public int getTotalCount(String breweryName,String userId) {
		Map<String,String>param = new HashMap<String,String>();
		param.put("userId",userId);
		param.put("breweryName",breweryName);
		int result = rmapper.getTotalCount();
		return result;
	}


	@Override
	public int getTotalCountWithConditiion(String userId, String breweryName) {
		Map<String,String>param = new HashMap<String,String>();
		param.put("userId",userId);
		param.put("breweryName",breweryName);
		int result = rmapper.getTotalCountWithConditiion(param);
		return result;
	}

//
//	@Override
//	public List<Reservation> selectOne(String reserveNo) {
//		List<Reservation>rList = rmapper.selectOne(reserveNo);
//		return rList;
//	}

	@Override
	public List<User> selectInfo(User user) {
		List<User>uList = rmapper.selectInfo(user);
		return uList;
	}

	@Override
	public List<Reservation> selectOne(String userId) {
		List<Reservation>rList = rmapper.selectOne(userId);
		return rList;
	}



}