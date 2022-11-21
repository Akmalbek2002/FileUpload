package com.example.file_uploade_download.Repository;

import com.example.file_uploade_download.Entity.FileFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileFolderRepository extends JpaRepository<FileFolder,Integer> {
}
