from google.protobuf import empty_pb2 as _empty_pb2
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class KundeByIdRequest(_message.Message):
    __slots__ = ("id",)
    ID_FIELD_NUMBER: _ClassVar[int]
    id: int
    def __init__(self, id: _Optional[int] = ...) -> None: ...

class Kunde(_message.Message):
    __slots__ = ("id", "vorname", "nachname", "email", "geburtsdatum")
    ID_FIELD_NUMBER: _ClassVar[int]
    VORNAME_FIELD_NUMBER: _ClassVar[int]
    NACHNAME_FIELD_NUMBER: _ClassVar[int]
    EMAIL_FIELD_NUMBER: _ClassVar[int]
    GEBURTSDATUM_FIELD_NUMBER: _ClassVar[int]
    id: int
    vorname: str
    nachname: str
    email: str
    geburtsdatum: str
    def __init__(self, id: _Optional[int] = ..., vorname: _Optional[str] = ..., nachname: _Optional[str] = ..., email: _Optional[str] = ..., geburtsdatum: _Optional[str] = ...) -> None: ...
