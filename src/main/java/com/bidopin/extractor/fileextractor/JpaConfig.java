package com.bidopin.extractor.fileextractor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EntityScan(basePackageClasses = {FileExtractorApplication.class})
public class JpaConfig {

}