package com.smartjava.siteservice.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	public ResponseEntity<String> uploadSite(MultipartFile file) {
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

			Path zipFilePath = Paths.get(ZIP_DIR, file.getOriginalFilename());
			file.transferTo(zipFilePath);

			unzip(zipFilePath.toFile(), SITE_DIR);

			return ResponseEntity.ok("Site uploaded successfully: " + siteName);
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
}
