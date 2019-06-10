package com.chess.chessapi.services;

import com.chess.chessapi.entities.Certificates;
import com.chess.chessapi.repositories.CertificatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CertificatesService {
    @Autowired
    private CertificatesRepository certificatesRepository;

    public List<Certificates> findAllByUserId(long userId){
        return this.certificatesRepository.findAllByUserId(userId);
    }

    public void updateCertifications(List<Certificates> oldCetificates, List<Certificates> newCetificates){
        if(oldCetificates.isEmpty()){
            //add all
            for (Certificates newCetificate:
                    newCetificates) {
                this.certificatesRepository.save(newCetificate);
            }
        }else if(newCetificates != null && !newCetificates.isEmpty()){
            //check if new cetificate has already or not, if it not yet c=> create

            for (Certificates newCetificate:
                    newCetificates) {
                boolean isExist = false;
                for (Certificates oldCetificate:
                        oldCetificates) {
                    if(newCetificate.getCertificateLink().equals(oldCetificate.getCertificateLink())){
                        isExist = true;
                        oldCetificates.remove(oldCetificate);
                        break;
                    }
                }
                if(!isExist){
                    this.certificatesRepository.save(newCetificate);
                }
            }
            //check old records should be deleted
            for (Certificates oldCetificate:
                    oldCetificates) {
                this.certificatesRepository.delete(oldCetificate);
            }
        }
        else{
            //delete all
            for (Certificates cetificate:
                    oldCetificates) {
                this.certificatesRepository.delete(cetificate);
            }
        }
    }
}
