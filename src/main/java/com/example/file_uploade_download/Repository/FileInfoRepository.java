package com.example.file_uploade_download.Repository;

import com.example.file_uploade_download.Entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo,Integer> {
}
