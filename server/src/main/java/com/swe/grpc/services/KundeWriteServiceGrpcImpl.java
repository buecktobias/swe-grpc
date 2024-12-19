package com.swe.grpc.services;

import com.google.protobuf.Empty;
import com.swe.grpc.KundeProto;
import com.swe.grpc.KundeReadServiceGrpc;
import com.swe.grpc.entity.Kunde;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class KundeWriteServiceGrpcImpl extends KundeWriteServiceGrpc.KundeWriteServiceImplBase {

    private final KundeWriteService kundeWriteService;
    private final KundeMapperService kundeMapperService;

    public KundeWriteServiceGrpcImpl(KundeWriteService kundeWriteService, KundeMapperService kundeMapperService) {
        this.kundeWriteService = kundeWriteService;
        this.kundeMapperService = kundeMapperService;
    }

    @Override
    public void createKunde(CreateKundeRequest request, StreamObserver<KundeProto.KundeResponse> responseObserver) {
        try {
            Kunde kunde = kundeMapperService.fromCreateRequest(request);
            kundeWriteService.createKunde(kunde);

            responseObserver.onNext(KundeProto.KundeResponse.newBuilder()
                    .setMessage("Kunde created successfully with ID: " + kunde.id())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error creating Kunde: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteKunde(KundeProto.KundeByIdRequest request, StreamObserver<KundeProto.KundeResponse> responseObserver) {
        try {
            UUID kundeId = UUID.fromString(request.getId());
            kundeWriteService.deleteKunde(kundeId);

            responseObserver.onNext(KundeProto.KundeResponse.newBuilder()
                    .setMessage("Kunde deleted successfully with ID: " + request.getId())
                    .build());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error deleting Kunde: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
