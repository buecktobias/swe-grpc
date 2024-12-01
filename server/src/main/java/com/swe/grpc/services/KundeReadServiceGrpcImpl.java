package com.swe.grpc.services;

import com.google.protobuf.Empty;
import com.swe.grpc.KundeProto;
import com.swe.grpc.KundeReadServiceGrpc;
import com.swe.grpc.entity.Kunde;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
public class KundeReadServiceGrpcImpl extends KundeReadServiceGrpc.KundeReadServiceImplBase {
    private final KundeReadService kundeReadService;
    private final KundeMapperService kundeMapperService;

    public KundeReadServiceGrpcImpl(KundeReadService kundeReadService, KundeMapperService kundeMapperService) {
        super();
        this.kundeReadService = kundeReadService;
        this.kundeMapperService = kundeMapperService;
    }

    @Override
    public void findAll(Empty request, StreamObserver<KundeProto.Kunde> responseObserver) {
        kundeReadService.findAll().stream()
                .map(kundeMapperService::toProto)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(KundeProto.KundeByIdRequest request, StreamObserver<KundeProto.Kunde> responseObserver) {
        Optional<Kunde> kunde = kundeReadService.findById(request.getId());

        if (kunde.isEmpty()) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Kunde mit ID " + request.getId() + " nicht gefunden.")
                            .asRuntimeException()
            );
            return;
        }

        responseObserver.onNext(kundeMapperService.toProto(kunde.get()));
        responseObserver.onCompleted();
    }
}
