package com.example.file_uploade_download.Controller;

import com.example.file_uploade_download.Entity.FileBayt;
import com.example.file_uploade_download.Entity.FileFolder;
import com.example.file_uploade_download.Entity.FileInfo;
import com.example.file_uploade_download.Repository.FileBaytRepository;
import com.example.file_uploade_download.Repository.FileFolderRepository;
import com.example.file_uploade_download.Repository.FileInfoRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@RestController
public class FileController {
    @Autowired
    FileBaytRepository baytRepository;
    @Autowired
    FileInfoRepository infoRepository;
    @Autowired
    FileFolderRepository fileFolderRepository;

    @SneakyThrows
    @RequestMapping(value = "/fileyuklash",method = RequestMethod.POST)
    public String FileYuklash(MultipartHttpServletRequest request){
        Iterator<String> iterator=request.getFileNames();
        MultipartFile multipartFile=request.getFile(iterator.next());
        FileInfo info=new FileInfo();
        info.setNomi(multipartFile.getOriginalFilename());
        info.setTuri(multipartFile.getContentType());
        info.setHajmi(multipartFile.getSize());
        infoRepository.save(info);
        FileBayt bayt=new FileBayt();
        bayt.setFilebyte(multipartFile.getBytes());
        bayt.setInfo(info);
        baytRepository.save(bayt);
        return "Fayl saqlandi";
    }
    @SneakyThrows
    @RequestMapping(value = "/fileoqish/{id}",method = RequestMethod.GET)
    public void FileYuklash(@PathVariable Integer id, HttpServletResponse response){
        Optional<FileInfo> byId = infoRepository.findById(id);
        if (!byId.isPresent()) System.out.println("Bunday id li ma'lumot mavjud emas");
        System.out.println("Fayl ma'lumotlari mavjud");
        FileInfo info = byId.get();
        Optional<FileBayt> byId1 = baytRepository.findById(info.getId());
        if (!byId1.isPresent()) System.out.println("Bunday idli fayl mavjud emas");
        System.out.println("Fayl ma'lumotlari topildi");
        FileBayt bayt = byId1.get();
        response.setContentType(info.getTuri());
        response.setHeader("Content-Disposition","attachment; filename=\""+ info.getNomi()+"\"");
        FileCopyUtils.copy(bayt.getFilebyte(),response.getOutputStream());
    }
    public String manzil="src\\main\\Baza\\";
    @SneakyThrows
    @RequestMapping(value = "/faylgasaqlash",method = RequestMethod.POST)
    public void FaylgaSaqlash(MultipartHttpServletRequest request){
        try {
            Iterator<String> iterator=request.getFileNames();
            MultipartFile multipartFile= request.getFile(iterator.next());
            FileFolder fileFolder=new FileFolder();
            fileFolder.setFaylOrginalNomi(multipartFile.getOriginalFilename());
            fileFolder.setFaylTuri(multipartFile.getContentType());
            fileFolder.setFaylHajmi(multipartFile.getSize());
            String[] ajratish=multipartFile.getOriginalFilename().split("\\.");
            String yangiNom= UUID.randomUUID().toString()+"."+ajratish[ajratish.length-1];
            fileFolder.setFaylYangiNomi(yangiNom);
            Path path= Paths.get(manzil+yangiNom);
            Files.copy(multipartFile.getInputStream(),path);
            fileFolderRepository.save(fileFolder);
            System.out.println("Malumot bazaga saqlandi");
        }catch (Exception e){
            e.getStackTrace();
            System.out.println(e);
        }
    }
    @SneakyThrows
    @RequestMapping(value = "/filedownload/{id}",method = RequestMethod.GET)
    public void FayllarniYuklash(@PathVariable Integer id, HttpServletResponse response){
        Optional<FileFolder> byId = fileFolderRepository.findById(id);
        if (!byId.isPresent()){
            System.out.println("Bunday idda ma'lumot mavjud emas");
        }
        FileFolder fileFolder = byId.get();
        response.setContentType(fileFolder.getFaylTuri());
//        response.setHeader("Content-Disposition","attachment; filename=\""+ fileFolder.getFaylOrginalNomi()+"\"");
        response.setHeader("Content-Disposition","inline; filename=\""+ fileFolder.getFaylOrginalNomi()+"\"");
        FileInputStream inputStream=new FileInputStream(manzil+fileFolder.getFaylYangiNomi());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        System.out.println("Fayl yuklandi");
    }
}
