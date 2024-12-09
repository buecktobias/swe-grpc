package com.swe.grpc.services;

import com.swe.grpc.HelloProto;
import com.swe.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void sayHello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        HelloProto.HelloResponse response = HelloProto.HelloResponse.newBuilder()
                .setMessage("Hello, " + name)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
