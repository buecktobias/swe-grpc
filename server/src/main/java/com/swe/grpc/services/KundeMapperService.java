package com.swe.grpc.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.swe.grpc.entity.Kunde;
import com.swe.grpc.KundeProto;

@Service
public class KundeMapperService{

    public KundeProto.Kunde toProto(Kunde kunde) {
        return KundeProto.Kunde.newBuilder()
                .setId(kunde.id())
                .setVorname(kunde.vorname())
                .setNachname(kunde.nachname())
                .setEmail(kunde.email())
                .setGeburtsdatum(kunde.geburtsdatum().toString())
                .build();
    }

    public Kunde fromProto(KundeProto.Kunde protoKunde) {
        return new Kunde(
                protoKunde.getId(),
                protoKunde.getVorname(),
                protoKunde.getNachname(),
                protoKunde.getEmail(),
                LocalDate.parse(protoKunde.getGeburtsdatum())
        );
    }
}
