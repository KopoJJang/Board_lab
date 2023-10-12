package kr.ac.kopo.board.controller;

import com.sun.tools.jconsole.JConsoleContext;
import kr.ac.kopo.board.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${kr.ac.kopo.upload.path}")
    private String uploadPath;
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){
        List<UploadResultDTO> resultDTOList = new ArrayList<>();
        for(MultipartFile uploadFile: uploadFiles){
            // 이미지 파일만 업로드 가능하도록함
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("이미지 파일형식이 아닙니다.");
                // uploadFile 메소드를 빠져나감
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);
            log.info("파일명: "+ fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            //  업로드 파일의 이름이 중복되서 기존의 파일이 덮어쓰기 되는 것을 방지하기 위해 UUID값을 파일명 앞에 추가한다.
            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath+ File.separator+folderPath + File.separator + uuid + "_" + fileName);
            try {
                uploadFile.transferTo(savePath);
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            }catch (IOException e){

            }
        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    public String makeFolder(){
        String folderPath = "";
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);
        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName){
        ResponseEntity<byte[]> result = null;
        try {
            String srcFilename = URLDecoder.decode(fileName,"UTF-8");
            log.info("fileName:" + srcFilename);

            File file = new File((uploadPath + File.separator + srcFilename));
            log.info("file: " + file);

            HttpHeaders headers = new HttpHeaders();

            headers.add("ContentType", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
















