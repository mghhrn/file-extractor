package com.bidopin.extractor.fileextractor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSystemRepository extends JpaRepository<FileSystem, Long> {

}
