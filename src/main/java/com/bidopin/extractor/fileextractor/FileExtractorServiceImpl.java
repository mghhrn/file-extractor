package com.bidopin.extractor.fileextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FileExtractorServiceImpl implements FileExtractorService {

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Value("${images.root}")
    String imagesRoot;

    @Override
    public void extractFilesFromDatabase() {

        System.out.println("Starting Extraction...");
        Long totalRecords = fileSystemRepository.count();
        int pageSize = 10;

        System.out.println("Total number of files is: " + totalRecords);
        System.out.println("Images root is: " + imagesRoot);

        int numberOfPages = totalRecords.intValue() % pageSize == 0 ? totalRecords.intValue() / pageSize
                : (totalRecords.intValue() / pageSize) + 1;
        System.out.println("Total number of pages: " + numberOfPages);

        for (int page = 0; page < numberOfPages; page++) {
            Pageable pageable = PageRequest.of(page, pageSize, new Sort(Sort.Direction.ASC, "id"));
            Page<FileSystem> files = fileSystemRepository.findAll(pageable);

            files.forEach(fs -> {
                String filePath = createFilePathFromId(imagesRoot, fs.getId());
                try {
//                    System.out.println("Extracting file with id " + fs.getId());
                    File file = new File(filePath);
                    FileUtils.writeByteArrayToFile(file, fs.getFileBlob());
                    String md5sum = DigestUtils.md5Hex(new FileInputStream(file));
//                    System.out.println("md5sum for file " + fs.getId() + " is : " + md5sum);
                    fs.setChecksum(md5sum);
                    fs.setPath(filePath);
                    fileSystemRepository.save(fs);
                } catch (IOException e) {
                    System.out.println("Exception for file with id: " + fs.getId());
                    e.printStackTrace();
                }
            });
        }
    }

    private String createFilePathFromId(String imagesRoot, Long id) {
        String path = "";

        String stringId = id.toString();
        int length = stringId.length();

        if (length < 3)
            return imagesRoot + "/" + stringId + "/" + stringId + "/" + stringId;

        for (int i = 0; i <= length; i += 2) {
            if (i + 2 > length)
                path = path + "/" + stringId.substring(i);
            else
                path = path + "/" + stringId.substring(i, i + 2);
        }

        return imagesRoot + path + "/" + stringId + "/" + stringId;
    }
}
