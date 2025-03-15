package com.smartjava.siteservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.smartjava.siteservice.service.SiteService;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

	private final SiteService siteService;

	public SiteController(SiteService siteService) {
		this.siteService = siteService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadSite(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "startFile", required = false) String startFile) {
		return siteService.uploadSite(file, startFile);
	}

	@GetMapping("/list")
	public ResponseEntity<List<String>> listSites() {
		return siteService.listSites();
	}

	@GetMapping("/view/{siteName}")
	public RedirectView viewSite(@PathVariable String siteName) throws IOException {
		String startFile = siteService.getStartFileForSite(siteName);
		if (startFile == null) {
			return new RedirectView("/error?message=Site not found or start file missing");
		}
		return new RedirectView("/sites/" + siteName + "/" + startFile);
	}
}
