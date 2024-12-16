import grpc
import pytest
from google.protobuf import empty_pb2

import kunde_pb2
import kunde_pb2_grpc


def test_grpc():
    # 1. Create a channel to connect to the gRPC server
    with grpc.insecure_channel("localhost:9090") as channel:
        # 2. Create a stub (client) to interact with the service
        client = kunde_pb2_grpc.KundeReadServiceStub(channel)

        # 3. Call the service method
        kunde = client.findById(kunde_pb2.KundeByIdRequest(id=1))
        assert kunde.id == 1
        assert kunde.vorname == "Max"

        # 3. Call the service method
        with pytest.raises(grpc.RpcError) as exception_info:
            client.findById(kunde_pb2.KundeByIdRequest(id=99999))
        error: grpc.RpcError = exception_info.value
        # noinspection PyUnresolvedReferences
        assert error.code() == grpc.StatusCode.NOT_FOUND

        # noinspection PyUnresolvedReferences
        responseStream = client.findAll(empty_pb2.Empty())
        clients = list(responseStream)
        assert len(clients) == 2

        print("All tests passed successfully!")

