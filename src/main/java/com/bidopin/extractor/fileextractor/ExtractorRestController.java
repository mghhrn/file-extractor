package com.bidopin.extractor.fileextractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExtractorRestController {

    @Autowired
    FileExtractorService fileExtractorService;

    @GetMapping("/start/extraction")
    public ResponseEntity<String> startFileSystemExtraction() {

        long start = System.currentTimeMillis();

        fileExtractorService.extractFilesFromDatabase();

        long end = System.currentTimeMillis();
        System.out.println("Elapsed time for extraction: " + (end - start)/1000F + " seconds");

        return ResponseEntity.ok("Extraction Done");
    }
}
