package com.unionman.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class FileMove {

    public static void main(String[] args) throws Exception {
        File folder1 = new File("C:\\Users\\kim\\OneDrive\\문서\\2019_12_02");
        File folder2 = new File("C:\\Users\\kim\\OneDrive\\문서\\2019_12_02\\이동된폴더");
        FileMove.copy(folder1, folder2);
//        FileUtils.moveFile(srcFile, destFile);
//        FileMove.delete(folder1.toString());

    }

    public static void copy(File sourceF, File targetF) throws Exception {
        FileUtils.forceMkdir(targetF);
        File[] target_file = sourceF.listFiles();
        for (File file : target_file) {
            String monthStr = getFileCreateDate(file, "yyyyMM");
            File temp = new File(targetF.getAbsolutePath() + File.separator 
                    + monthStr + File.separator + file.getName());
            if (file.isDirectory()) {
//                temp.mkdir();
//                copy(file, temp);
            } else {
                getDescription(file);
                FileUtils.forceMkdir(new File(targetF + "/" + monthStr));
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    public static String getFileCreateDate(File file, String pattern) {
        
        BasicFileAttributes attrs;
        String formatted = null;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.lastModifiedTime();
            
            if(pattern == null && Objects.equals(pattern, "")) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            
            formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

            System.out.println( "파일 생성 날짜 및 시간은 다음과 같습니다.: " + formatted );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatted;
    }

    
    
    private static String getDescription(File file) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.format("[%s] - %s = %s",
//                    directory.getName(), tag.getTagName(), tag.getDescription());
//            }
//            if (directory.hasErrors()) {
//                for (String error : directory.getErrors()) {
//                    System.err.format("ERROR: %s", error);
//                }
//            }
//        }

        // 디렉토리 생성.
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
          
        // 디렉토리의 태그에 해당하는 값을 가져옴.
        // 이미지생성일시, 촬영기기모델명
        Date originalDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        String modelName = directory.getString(ExifSubIFDDirectory.TAG_LENS_MODEL);
        System.out.println(originalDate);
        System.out.println(modelName);

            return "";
    }


        
    public static void delete(String path) {

        File folder = new File(path);
        try {
            if (folder.exists()) {
                File[] folder_list = folder.listFiles();

                for (int i = 0; i < folder_list.length; i++) {
                    if (folder_list[i].isFile()) {
                        folder_list[i].delete();
                    } else {
                        delete(folder_list[i].getPath());
                    }
                    folder_list[i].delete();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
