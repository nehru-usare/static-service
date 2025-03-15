package com.smartjava.siteservice.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartjava.siteservice.service.SiteService;

@Controller
@RequestMapping("/sites")
public class SiteUploadController {

	private final SiteService siteService;

	public SiteUploadController(SiteService siteService) {
		this.siteService = siteService;
	}

	// Upload site page
	@GetMapping("/upload")
	public String uploadSitePage() {
		return "upload"; // This corresponds to the "upload.html" Thymeleaf template
	}

	// Handle site upload
	@PostMapping("/upload")
	public String uploadSite(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "startFile", required = false) String startFile) {
		siteService.uploadSite(file, startFile);
		return "redirect:/sites/list"; // Redirect to list of sites after upload
	}

	// List of uploaded sites
	@GetMapping("/list")
	public String listSites(Model model) {
		model.addAttribute("sites", siteService.newListSites()); // Pass sites list to the view
		return "list";
	}

	// View a specific site
	@GetMapping("/view/{siteName}")
	public String viewSite(@PathVariable String siteName, Model model) {
		String startFile = null;
		try {
			startFile = siteService.getStartFileForSite(siteName);
		} catch (IOException e) {
			model.addAttribute("errorMessage", "Error reading site info file");
			return "error"; // This corresponds to the "error.html" Thymeleaf template
		}
		if (startFile == null) {
			model.addAttribute("errorMessage", "Site not found or start file missing");
			return "error"; // This corresponds to the "error.html" Thymeleaf template
		}
		return "redirect:/sites/" + siteName + "/" + startFile; // Redirect to site start file
	}
}
