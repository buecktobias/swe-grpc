package com.swe.grpc.client;


import com.swe.grpc.KundeProto;
import com.swe.grpc.KundeReadServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SweGrpcClientApplication {

    public static KundeProto.Kunde findById(int id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        KundeReadServiceGrpc.KundeReadServiceBlockingStub stub = KundeReadServiceGrpc.newBlockingStub(channel);

        try {
            KundeProto.KundeByIdRequest request = KundeProto.KundeByIdRequest.newBuilder().setId(id).build();
            return stub.findById(request);
        } finally {
            channel.shutdown();
        }
    }


    public static void main(String[] args) {
        System.out.println("Hello from gRPC client!");

        final var kunde = findById(1);
        System.out.println("Kunde: " + kunde);
    }

}
