package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.model.RadES;
import com.example.naucnacentrala.repository.RadESRepository;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class RadESService {

    @Autowired
    private RadESRepository radESRepository;

    @Autowired
    private RadService radService;

    public RadES save (RadES radES){
        return radESRepository.save(radES);
    }

    public RadES findOneById(Integer id) {
        return radESRepository.findOneById(id);
    }

    public String parsePDF(Rad rad) throws UnsupportedEncodingException {
        String path = radService.getPath(rad.getId());
        File pdf = new File(path);
        String text = null;
        try {
            System.out.println("*******************************************");
            System.out.println("Parsiranje PDF-a");
            System.out.println("Path: " + path);
            System.out.println("*******************************************");
            PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
            parser.parse();
            text = getText(parser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(parser.getPDDocument());
            return text;
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }
}
