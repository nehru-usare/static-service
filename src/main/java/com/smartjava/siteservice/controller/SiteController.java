package com.smartjava.siteservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartjava.siteservice.service.SiteService;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

	private final SiteService siteService;

	public SiteController(SiteService siteService) {
		this.siteService = siteService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadSite(@RequestParam("file") MultipartFile file) {
		return siteService.uploadSite(file);
	}

	@GetMapping("/list")
	public ResponseEntity<List<String>> listSites() {
		return siteService.listSites();
	}
}
