package com.smartjava.siteservice.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SiteService {
	private static final String ZIP_DIR = "/apps/siteszips/";
	private static final String SITE_DIR = "/apps/sites/";
	private static final String DEFAULT_START_FILE = "index.html";
	private static final String SITE_INFO_FILE = "siteinfo.txt"; // File to store start file name

	public ResponseEntity<String> uploadSite(MultipartFile file, String startFileName) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".zip")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file. Please upload a ZIP file.");
		}

		try {
			String fileName = file.getOriginalFilename();
			assert fileName != null;
			String siteName = fileName.replace(".zip", "");
			Path zipDirPath = Paths.get(ZIP_DIR);
			Path siteDirPath = Paths.get(SITE_DIR);

			Files.createDirectories(zipDirPath);
			Files.createDirectories(siteDirPath);

			Path tempFilePath = Paths.get(ZIP_DIR, file.getOriginalFilename());
			file.transferTo(tempFilePath);

			unzip(tempFilePath.toFile(), SITE_DIR);
			String extractedSitePath = SITE_DIR + siteName;

			// Determine start file
			String startFile = (startFileName != null && !startFileName.isEmpty()) ? startFileName : DEFAULT_START_FILE;
			String startFilePath = extractedSitePath + "/" + startFile;
			File startFileCheck = new File(startFilePath);

			if (!startFileCheck.exists()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start file not found: " + startFileName);
			}

			// Create siteinfo.txt and store the start file name
			createSiteInfoFile(extractedSitePath, startFile);

			return ResponseEntity.ok("Site uploaded successfully: " + siteName + ". Start file: " + startFile);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading site: " + e.getMessage());
		}
	}

	public ResponseEntity<List<String>> listSites() {
		File siteDirectory = new File(SITE_DIR);
		List<String> sites = new ArrayList<>();

		if (siteDirectory.exists() && siteDirectory.isDirectory()) {
			for (File file : siteDirectory.listFiles()) {
				if (file.isDirectory()) {
					sites.add(file.getName());
				}
			}
		}
		return ResponseEntity.ok(sites);
	}

	public List<String> newListSites() {
		File siteDirectory = new File(SITE_DIR);
		List<String> sites = new ArrayList<>();

		if (siteDirectory.exists() && siteDirectory.isDirectory()) {
			for (File file : siteDirectory.listFiles()) {
				if (file.isDirectory()) {
					sites.add(file.getName());
				}
			}
		}
		return sites;
	}

	private void unzip(File zipFile, String outputFolder) throws IOException {
		byte[] buffer = new byte[1024];
		File destDir = new File(outputFolder);
		Files.createDirectories(destDir.toPath());

		try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				File newFile = new File(outputFolder, zipEntry.getName());
				if (zipEntry.isDirectory()) {
					Files.createDirectories(newFile.toPath());
				} else {
					Files.createDirectories(newFile.getParentFile().toPath());
					try (FileOutputStream fos = new FileOutputStream(newFile)) {
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
					}
				}
				zis.closeEntry();
			}
		}
	}

	private void createSiteInfoFile(String sitePath, String startFileName) throws IOException {
		Path siteInfoPath = Paths.get(sitePath, SITE_INFO_FILE);
		Files.write(siteInfoPath, startFileName.getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	public String getStartFileForSite(String siteName) throws IOException {
		Path siteInfoPath = Paths.get(SITE_DIR, siteName, SITE_INFO_FILE);
		if (Files.exists(siteInfoPath)) {
			return Files.readAllLines(siteInfoPath).get(0);
		}
		return null;
	}

}
