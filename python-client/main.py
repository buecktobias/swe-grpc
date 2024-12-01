import grpc
import kunde_pb2
import kunde_pb2_grpc
def find_by_id(client, customer_id):
    try:
        request = Kunde
        response = client.FindById(request)
        print(f"Kunde Found: ID={response.id}, Name={response.name}")
    except grpc.RpcError as e:
        print(f"Error: {e.code().name}, {e.details()}")

def find_all(client):
    try:
        print("Kunden List:")
        for kunde in client.FindAll(kunde_pb2.google_dot_protobuf_dot_empty__pb2.Empty()):
            print(f"- ID={kunde.id}, Name={kunde.name}")
    except grpc.RpcError as e:
        print(f"Error: {e.code().name}, {e.details()}")

def main():
    # 1. Create a channel to connect to the gRPC server
    with grpc.insecure_channel("localhost:9090") as channel:
        # 2. Create a stub (client) to interact with the service
        client = kunde_pb2_grpc.KundeReadServiceStub(channel)

        # 3. Call methods on the stub
        print("Calling find_by_id...")
        find_by_id(client, 1)  # Valid ID
        find_by_id(client, 999999)  # Invalid ID

        print("\nCalling find_all...")
        find_all(client)

if __name__ == "__main__":
    main()
