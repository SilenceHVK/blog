// Code generated by protoc-gen-go-grpc. DO NOT EDIT.

package proto

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// StreamClient is the client API for Stream service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type StreamClient interface {
	GetStream(ctx context.Context, in *StreamReqData, opts ...grpc.CallOption) (Stream_GetStreamClient, error)
	PutStream(ctx context.Context, opts ...grpc.CallOption) (Stream_PutStreamClient, error)
	AllStream(ctx context.Context, opts ...grpc.CallOption) (Stream_AllStreamClient, error)
}

type streamClient struct {
	cc grpc.ClientConnInterface
}

func NewStreamClient(cc grpc.ClientConnInterface) StreamClient {
	return &streamClient{cc}
}

func (c *streamClient) GetStream(ctx context.Context, in *StreamReqData, opts ...grpc.CallOption) (Stream_GetStreamClient, error) {
	stream, err := c.cc.NewStream(ctx, &Stream_ServiceDesc.Streams[0], "/Stream/GetStream", opts...)
	if err != nil {
		return nil, err
	}
	x := &streamGetStreamClient{stream}
	if err := x.ClientStream.SendMsg(in); err != nil {
		return nil, err
	}
	if err := x.ClientStream.CloseSend(); err != nil {
		return nil, err
	}
	return x, nil
}

type Stream_GetStreamClient interface {
	Recv() (*StreamRespData, error)
	grpc.ClientStream
}

type streamGetStreamClient struct {
	grpc.ClientStream
}

func (x *streamGetStreamClient) Recv() (*StreamRespData, error) {
	m := new(StreamRespData)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

func (c *streamClient) PutStream(ctx context.Context, opts ...grpc.CallOption) (Stream_PutStreamClient, error) {
	stream, err := c.cc.NewStream(ctx, &Stream_ServiceDesc.Streams[1], "/Stream/PutStream", opts...)
	if err != nil {
		return nil, err
	}
	x := &streamPutStreamClient{stream}
	return x, nil
}

type Stream_PutStreamClient interface {
	Send(*StreamReqData) error
	CloseAndRecv() (*StreamRespData, error)
	grpc.ClientStream
}

type streamPutStreamClient struct {
	grpc.ClientStream
}

func (x *streamPutStreamClient) Send(m *StreamReqData) error {
	return x.ClientStream.SendMsg(m)
}

func (x *streamPutStreamClient) CloseAndRecv() (*StreamRespData, error) {
	if err := x.ClientStream.CloseSend(); err != nil {
		return nil, err
	}
	m := new(StreamRespData)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

func (c *streamClient) AllStream(ctx context.Context, opts ...grpc.CallOption) (Stream_AllStreamClient, error) {
	stream, err := c.cc.NewStream(ctx, &Stream_ServiceDesc.Streams[2], "/Stream/AllStream", opts...)
	if err != nil {
		return nil, err
	}
	x := &streamAllStreamClient{stream}
	return x, nil
}

type Stream_AllStreamClient interface {
	Send(*StreamReqData) error
	Recv() (*StreamRespData, error)
	grpc.ClientStream
}

type streamAllStreamClient struct {
	grpc.ClientStream
}

func (x *streamAllStreamClient) Send(m *StreamReqData) error {
	return x.ClientStream.SendMsg(m)
}

func (x *streamAllStreamClient) Recv() (*StreamRespData, error) {
	m := new(StreamRespData)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

// StreamServer is the server API for Stream service.
// All implementations must embed UnimplementedStreamServer
// for forward compatibility
type StreamServer interface {
	GetStream(*StreamReqData, Stream_GetStreamServer) error
	PutStream(Stream_PutStreamServer) error
	AllStream(Stream_AllStreamServer) error
	mustEmbedUnimplementedStreamServer()
}

// UnimplementedStreamServer must be embedded to have forward compatible implementations.
type UnimplementedStreamServer struct {
}

func (UnimplementedStreamServer) GetStream(*StreamReqData, Stream_GetStreamServer) error {
	return status.Errorf(codes.Unimplemented, "method GetStream not implemented")
}
func (UnimplementedStreamServer) PutStream(Stream_PutStreamServer) error {
	return status.Errorf(codes.Unimplemented, "method PutStream not implemented")
}
func (UnimplementedStreamServer) AllStream(Stream_AllStreamServer) error {
	return status.Errorf(codes.Unimplemented, "method AllStream not implemented")
}
func (UnimplementedStreamServer) mustEmbedUnimplementedStreamServer() {}

// UnsafeStreamServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to StreamServer will
// result in compilation errors.
type UnsafeStreamServer interface {
	mustEmbedUnimplementedStreamServer()
}

func RegisterStreamServer(s grpc.ServiceRegistrar, srv StreamServer) {
	s.RegisterService(&Stream_ServiceDesc, srv)
}

func _Stream_GetStream_Handler(srv interface{}, stream grpc.ServerStream) error {
	m := new(StreamReqData)
	if err := stream.RecvMsg(m); err != nil {
		return err
	}
	return srv.(StreamServer).GetStream(m, &streamGetStreamServer{stream})
}

type Stream_GetStreamServer interface {
	Send(*StreamRespData) error
	grpc.ServerStream
}

type streamGetStreamServer struct {
	grpc.ServerStream
}

func (x *streamGetStreamServer) Send(m *StreamRespData) error {
	return x.ServerStream.SendMsg(m)
}

func _Stream_PutStream_Handler(srv interface{}, stream grpc.ServerStream) error {
	return srv.(StreamServer).PutStream(&streamPutStreamServer{stream})
}

type Stream_PutStreamServer interface {
	SendAndClose(*StreamRespData) error
	Recv() (*StreamReqData, error)
	grpc.ServerStream
}

type streamPutStreamServer struct {
	grpc.ServerStream
}

func (x *streamPutStreamServer) SendAndClose(m *StreamRespData) error {
	return x.ServerStream.SendMsg(m)
}

func (x *streamPutStreamServer) Recv() (*StreamReqData, error) {
	m := new(StreamReqData)
	if err := x.ServerStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

func _Stream_AllStream_Handler(srv interface{}, stream grpc.ServerStream) error {
	return srv.(StreamServer).AllStream(&streamAllStreamServer{stream})
}

type Stream_AllStreamServer interface {
	Send(*StreamRespData) error
	Recv() (*StreamReqData, error)
	grpc.ServerStream
}

type streamAllStreamServer struct {
	grpc.ServerStream
}

func (x *streamAllStreamServer) Send(m *StreamRespData) error {
	return x.ServerStream.SendMsg(m)
}

func (x *streamAllStreamServer) Recv() (*StreamReqData, error) {
	m := new(StreamReqData)
	if err := x.ServerStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

// Stream_ServiceDesc is the grpc.ServiceDesc for Stream service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var Stream_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "Stream",
	HandlerType: (*StreamServer)(nil),
	Methods:     []grpc.MethodDesc{},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "GetStream",
			Handler:       _Stream_GetStream_Handler,
			ServerStreams: true,
		},
		{
			StreamName:    "PutStream",
			Handler:       _Stream_PutStream_Handler,
			ClientStreams: true,
		},
		{
			StreamName:    "AllStream",
			Handler:       _Stream_AllStream_Handler,
			ServerStreams: true,
			ClientStreams: true,
		},
	},
	Metadata: "stream.proto",
}