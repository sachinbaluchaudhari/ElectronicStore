package com.electonic.store.services.implement;

import com.electonic.store.exception.BadApiRequestException;
import com.electonic.store.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

           String originalFilename = file.getOriginalFilename();
           String fileName = UUID.randomUUID().toString();
           String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
           String fileNameWithExtension = fileName + extension;
           String fullPathWithFileName = path + File.separator + fileNameWithExtension;
           System.out.println(fullPathWithFileName);

       if (extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")) {
           File folder = new File(path);
           if (!folder.exists()) {
               folder.mkdirs();
           }
           System.out.println(folder.exists());
           System.out.println(folder.getAbsolutePath());
           //upload
           long copy = Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
           folder.delete();
           System.out.println(copy);
            return fileNameWithExtension;

       }else
       {
           throw  new BadApiRequestException("file with "+extension+" not supported!1");
       }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
