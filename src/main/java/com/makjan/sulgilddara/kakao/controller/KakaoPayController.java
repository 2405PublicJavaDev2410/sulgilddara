package com.makjan.sulgilddara.kakao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.makjan.sulgilddara.kakao.model.Service.KakaoPayService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
	public class KakaoPayController {


	    @Setter(onMethod_ = @Autowired)
	    private KakaoPayService kakaoPay;
		
//	    @Autowired
//	    private KakaoPayService kakaoPay;
	    
//	    public KakaoPayController(KakaoPayService kakaoPay) {
//	    	this.kakaoPay=kakaoPay;
//	    }

	    @GetMapping("/kakaoPay")
	    public void kakaoPayGet() {

	    }

	    @PostMapping("/kakaoPay")
	    public  String kakaoPay(){
	        log.info("kakaoPay post.....................");

	        return "redirect:" + kakaoPay.kakaoPayReady();
	    }

	    @GetMapping("/kakaoPaySuccess")
	    public void kakaoPaySuccess(@RequestParam("pg_token")String pg_token, Model model) {
	        log.info("kakaoPay Success get................");
	        log.info("kakaoPaySuccess pg_token : " + pg_token);
	        model.addAttribute("info", kakaoPay.kakaoPayInfo(pg_token));
	    }
}